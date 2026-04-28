package com.example.management.api;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(

        @NotBlank
        String login,

        @NotBlank
        String password,

        @NotBlank
        String lastName,

        @NotBlank
        String firstName
) {
}
