package com.example.management.domain;

import com.example.management.api.SignUpRequest;
import com.example.management.db.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserMapper {

    public UserEntity toEntity(UserDto dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(dto.getRoles().toString())
                .build();
    }

    public UserDto toDomain(SignUpRequest request) {
        return UserDto.builder()
                .id(null)
                .login(request.login())
                .password(request.password())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .roles(null)
                .build();
    }

    public UserDto toDomain(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .roles(grantedAuthorities(entity.getRole()))
                .build();
    }

    private List<SimpleGrantedAuthority> grantedAuthorities(String role) {
        if (role == null || role.isBlank()) {
            return Collections.emptyList();
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        return Collections.singletonList(authority);
    }
}
