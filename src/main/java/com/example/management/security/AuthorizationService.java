package com.example.management.security;

import com.example.management.domain.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    public UserDto authorizeUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication is not present");
        }
        try {
            return (UserDto) authentication.getPrincipal();
        } catch (ClassCastException e) {
            log.warn("Invalid user type", e);
            throw new AuthorizationDeniedException(e.getMessage());
        }
    }
}
