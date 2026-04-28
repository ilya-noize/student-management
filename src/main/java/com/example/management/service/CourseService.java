package com.example.management.service;

import com.example.management.controller.Course;
import com.example.management.controller.CourseCreate;
import com.example.management.controller.CourseUpdate;
import com.example.management.db.CourseEntity;
import com.example.management.db.CourseMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    public List<Course> findAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toCourse)
                .toList();
    }

    public Course create(CourseCreate course) {
        CourseEntity entity = CourseEntity.builder()
                .name(course.name())
                .description(course.description())
                .build();
        return courseMapper.toCourse(courseRepository.save(entity));
    }

    public Course update(Long courseId, CourseUpdate update) {
        if (!courseId.equals(update.id())) {
            throw new IllegalArgumentException();
        }
        CourseEntity entity = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);
        entity.setName(update.name());
        entity.setDescription(update.description());

        return courseMapper.toCourse(courseRepository.save(entity));
    }

    public void delete(Long courseId) {
        if(!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException();
        }
        courseRepository.deleteById(courseId);
    }
}
