package com.alexlatkin.filestorage.mappers;

import com.alexlatkin.filestorage.dto.user.RegistrationUserDto;
import com.alexlatkin.filestorage.dto.user.UserDto;
import com.alexlatkin.filestorage.model.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    @Lazy
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public User toEntity(RegistrationUserDto registrationUserDto) {
        return new User(
                registrationUserDto.getUsername(),
                registrationUserDto.getEmail(),
                passwordEncoder.encode(registrationUserDto.getPassword())
        );
    }

    public User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getUsername(), userDto.getEmail());
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
