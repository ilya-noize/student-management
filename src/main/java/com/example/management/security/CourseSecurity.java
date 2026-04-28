package com.example.management.security;

import com.example.management.db.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("courseSecurity")
@RequiredArgsConstructor
public class CourseSecurity {
    private final CourseRepository courseRepository;

    public boolean isCourseOwner(Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return false;
        }
        String login = authentication.getName();
        if (login == null) {
            return false;
        }

        return courseRepository.findById(courseId)
                .map(course -> course.getOwner().getLogin().equals(login))
                .orElse(false);
    }
}
