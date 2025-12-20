package com.alexlatkin.filestorage.controller;


import com.alexlatkin.filestorage.dto.user.RegistrationUserDto;
import com.alexlatkin.filestorage.dto.auth.JwtRequest;
import com.alexlatkin.filestorage.dto.auth.JwtResponse;
import com.alexlatkin.filestorage.dto.user.UserDto;
import com.alexlatkin.filestorage.mappers.UserMapper;
import com.alexlatkin.filestorage.service.AuthService;
import com.alexlatkin.filestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody RegistrationUserDto registrationUserDto) {
        userService.addUser(registrationUserDto);
        return userMapper.toDto(userService.getUserByUserName(registrationUserDto.getUsername()));
    }

    @GetMapping("/refresh")
    public JwtResponse refresh(@RequestHeader("Authorization") String refreshToken) {
        return authService.refresh(refreshToken);
    }

}
