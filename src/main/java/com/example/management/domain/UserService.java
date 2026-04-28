package com.example.management.domain;

import com.example.management.db.UserEntity;
import com.example.management.db.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto registrationUser(UserDto dto) {
        if (userRepository.existsByLogin(dto.getLogin())) {
            throw new IllegalArgumentException();
        }

        UserEntity entity = userMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(entity);

        return userMapper.toDomain(entity);
    }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public UserDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found. ID=%s".formatted(id))
        );
        return userMapper.toDomain(user);
    }

    public UserDto getUserByLogin(String login) {
        UserEntity user = userRepository.findByLogin(login).orElseThrow(
                () -> new EntityNotFoundException("User not found. LOGIN=%s".formatted(login))
        );
        return userMapper.toDomain(user);
    }
}
