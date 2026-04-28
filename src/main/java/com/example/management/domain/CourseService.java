package com.example.management.domain;

import com.example.management.api.CourseUpdate;
import com.example.management.db.CourseEntity;
import com.example.management.db.CourseRepository;
import com.example.management.security.AuthorizationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("courseService")
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final AuthorizationService authorizationService;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDto)
                .toList();
    }

    public CourseDto create(CourseDto course) {
        UserDto user = authorizationService.authorizeUser();
        CourseEntity saved = courseRepository.save(
                CourseEntity.builder()
                        .name(course.getName())
                        .description(course.getDescription())
                        .owner(userMapper.toEntity(user))
                        .build()
        );
        log.info("Saved course: ID={}", saved.getId());
        return courseMapper.toDto(saved);
    }

    public CourseDto update(Long courseId, CourseUpdate update) {
        if (!courseId.equals(update.id())) {
            throw new IllegalArgumentException();
        }
        CourseEntity entity = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);
        entity.setName(update.name());
        entity.setDescription(update.description());

        CourseEntity saved = courseRepository.save(entity);
        log.info("Updated course: ID={}", saved.getId());
        return courseMapper.toDto(saved);
    }

    public void delete(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException();
        }
        courseRepository.deleteById(courseId);
    }

    public CourseDto getById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("No such course. ID=" + courseId));
    }

    public boolean isExistsByName(String name) {
        return courseRepository.existsByName(name);
    }
}
