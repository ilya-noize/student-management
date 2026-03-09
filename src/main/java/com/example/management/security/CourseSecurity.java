package com.example.management.security;

import com.example.management.service.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("courseSecurity")
@RequiredArgsConstructor
public class CourseSecurity {
    private final CourseRepository courseRepository;

    public boolean isCourseOwner(Long courseId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        if (login == null) return false;
//        return courseRepository.getOwnersLoginCourseById(courseId).equals(login);

        return courseRepository.findById(courseId)
                .map(course -> course.getOwner().getLogin().equals(login))
                .orElse(false);
    }
}
