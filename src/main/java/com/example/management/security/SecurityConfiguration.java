package com.example.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Публичный доступ
                        .requestMatchers("/api/auth/**", "/api/public/**")
                        .permitAll()
                        // Студенческий доступ
                        .requestMatchers("/api/courses/**","/api/assignments/submit")
                        .hasRole("STUDENT")
                        // Преподавательский доступ
                        .requestMatchers("/api/courses/create", "/api/assignment/grade")
                        .hasRole("TEACHER")
                        // Администраторский доступ
                        .requestMatchers("/api/admin/**", "/api/users/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/teacher/**")
                        .hasAllRoles("TEACHER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
