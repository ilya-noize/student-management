package com.example.management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

/**
 * {@code @EnableMethodSecurity} нужна для использования @PreAuthorized в @RestController
 * {@code @EnableWebSecurity} нужна для применения своих настроек безопасности
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/swagger-ui/**").permitAll()
                        // Публичный доступ
                        //.requestMatchers("/api/auth/**", "/api/public/**").permitAll()
                        // Студенческий доступ
                        //.requestMatchers("/api/courses/**", "/api/assignments/submit").hasRole("STUDENT")
                        // Преподавательский доступ
                        //.requestMatchers("/api/courses/create", "/api/assignment/grade").hasRole("TEACHER")
                        // Администраторский доступ
                        //.requestMatchers("/api/admin/**", "/api/users/**").hasRole("ADMIN")
                        //.requestMatchers("/api/teacher/**").hasAllRoles("TEACHER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
