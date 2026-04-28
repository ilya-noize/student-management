package com.example.management.config;

import com.example.management.db.UserRole;
import com.example.management.domain.CourseDto;
import com.example.management.domain.CourseService;
import com.example.management.domain.UserDto;
import com.example.management.domain.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DefaultDataInitializer {
    private final UserService userService;
    private final CourseService courseService;
    private final Map<String, UserDto> users = Map.of(
            UserRole.ADMIN.name(), UserDto.builder()
                    .login("admin")
                    .password("admin")
                    .firstName("Admin")
                    .lastName("Super")
                    .roles(List.of(new SimpleGrantedAuthority(UserRole.ADMIN.name())))
                    .build(),
            UserRole.TEACHER.name(), UserDto.builder()
                    .login("teacher")
                    .password("teacher")
                    .firstName("Teach")
                    .lastName("Professor")
                    .roles(List.of(new SimpleGrantedAuthority(UserRole.TEACHER.name())))
                    .build(),
            UserRole.STUDENT.name(), UserDto.builder()
                    .login("student")
                    .password("student")
                    .firstName("Study")
                    .lastName("Learning")
                    .roles(List.of(new SimpleGrantedAuthority(UserRole.STUDENT.name())))
                    .build()
    );
    private Authentication previousAuth;

    @PostConstruct
    public void init() {
        users.forEach((k, v) -> {
            log.info("Creating user: {}", k);
            createUserIfNotExists(v);
        });

        doAuthorization();
        try {
            List<CourseDto> courses = createTwentyCoursesFromJson();
            courses.forEach(this::createCourseIfNotExists);
        } finally {
            undoAuthorization();
        }
    }

    private void createUserIfNotExists(UserDto dto) {
        if (userService.isUserExistsByLogin(dto.getUsername())) {
            return;
        }
        userService.registrationUser(dto);
    }

    private void doAuthorization() {
        previousAuth = SecurityContextHolder.getContext().getAuthentication();
        var user = userService.getUserByLogin("teacher");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private List<CourseDto> createTwentyCoursesFromJson() {
        String json = "courses.json";
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource(json);
            try (InputStream inputStream = resource.getInputStream()) {
                return mapper.readValue(inputStream,
                        mapper.getTypeFactory().constructCollectionType(List.class, CourseDto.class));
            }
        } catch (IOException e) {
            throw new RuntimeException("File " + json + " not found:" + e.getMessage(), e);
        }
    }

    private void undoAuthorization() {
        SecurityContextHolder.getContext().setAuthentication(previousAuth);
    }

    private void createCourseIfNotExists(CourseDto dto) {
        if (courseService.isExistsByName(dto.getName())) {
            return;
        }
        courseService.create(dto);
    }
}
