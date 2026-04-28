package com.example.management.api;

import com.example.management.domain.CourseDto;
import com.example.management.domain.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public List<CourseDto> getAll() {
        return courseService.findAll();
    }

    @GetMapping("/{courseId}")
    public CourseDto getById(@PathVariable Long courseId) {
        return courseService.getById(courseId);
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public CourseDto create(
            @RequestBody @Valid CourseCreate create
    ) {
        log.info("Received request : POST /api/courses {}", create);
        return courseService.create(create.mappedByDto());
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("@courseSecurity.isCourseOwner(#courseId) or hasRole('ADMIN')")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public CourseDto update(@PathVariable Long courseId,
                            @RequestBody @Valid CourseUpdate update
    ) {
        log.info("Request received: PUT api/courses/{},{} ", courseId, update);
        return courseService.update(courseId, update);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public void delete(@PathVariable Long courseId) {
        log.warn("Request received: DELETE api/courses/{}", courseId);
        courseService.delete(courseId);
    }
}
