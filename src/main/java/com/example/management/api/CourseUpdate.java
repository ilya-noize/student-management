package com.example.management.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseUpdate(
        @NotNull
        Long id,
        @NotBlank
        String name,
        @NotBlank
        String description
) {
}
