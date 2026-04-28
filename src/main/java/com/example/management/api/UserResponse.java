package com.example.management.api;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String login,
        String lastName,
        String firstName
) {
}
