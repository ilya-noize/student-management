package com.example.management.controller;

import jakarta.validation.constraints.NotBlank;

public record CourseCreate(
        @NotBlank
        String name,
        @NotBlank
        String description
) {
}
