package com.example.management.domain;

import com.example.management.api.UserResponse;
import com.example.management.db.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements UserDetails {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private List<SimpleGrantedAuthority> roles;


    public UserResponse toResponse() {
        return UserResponse.builder()
                .id(id)
                .login(login)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    private String getRoleAsString(List<SimpleGrantedAuthority> roles) {
        if (roles == null || roles.isEmpty()) {
            return UserRole.STUDENT.name();
        }
        return roles.getFirst().getAuthority();
    }
}
