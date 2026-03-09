package com.example.management.service;

import com.example.management.db.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    @Query("""
        SELECT c.owner.login FROM CourseEntity c
        WHERE c.id = :id
        """)
    String getOwnersLoginCourseById(Long id);
}
