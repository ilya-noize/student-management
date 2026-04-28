package com.example.management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @see SecurityConfiguration in .addFilterBefore
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String PREFIX = "Bearer ";
    private final JwtTokenManager jwtTokenManager;
    @Lazy
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader == null) {
            filterChain.doFilter(request,response);
            return;
        }
        try {
            String loginFromToken;
            String token = extractToken(authorizationHeader);
            if (!jwtTokenManager.isValid(token)) {
                log.warn("[JWT] Invalid or expired JWT");
                filterChain.doFilter(request, response);
                return;
            }
            loginFromToken = jwtTokenManager.getLoginFromToken(token);
            var user = userDetailsService.loadUserByUsername(loginFromToken);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            log.error("Error while reading token", e);
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    public static String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.replaceFirst("^" + PREFIX, "");
        }
        return null;
    }
}
