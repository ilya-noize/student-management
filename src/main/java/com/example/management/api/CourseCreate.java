package com.example.management.api;

import com.example.management.domain.CourseDto;
import jakarta.validation.constraints.NotBlank;

public record CourseCreate(
        @NotBlank
        String name,
        @NotBlank
        String description
) {

    public CourseDto mappedByDto() {
        return new CourseDto(null, name(), description());
    }
}
