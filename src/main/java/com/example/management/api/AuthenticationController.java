package com.example.management.api;

import com.example.management.domain.UserDto;
import com.example.management.domain.UserMapper;
import com.example.management.security.AuthenticationService;
import com.example.management.domain.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registrationUser(
            @RequestBody @Valid SignUpRequest request
    ) {
        log.info("Registration user LOGIN={}", request.login());
        UserDto domain = userMapper.toDomain(request);
        return userService.registrationUser(domain).toResponse();
    }

    @PostMapping("/auth")
    public JwtResponse authenticate(
            @RequestBody @Valid SignInRequest request
    ) {
        log.info("Authentication user LOGIN={}", request.login());

        return new JwtResponse(authenticationService.authenticateUser(request));
    }
}
