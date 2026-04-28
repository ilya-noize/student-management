package com.example.management.domain;

import com.example.management.db.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CourseMapper {
    CourseDto toDto(CourseEntity courseEntity);
}