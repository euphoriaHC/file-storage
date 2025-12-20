package com.alexlatkin.filestorage.controller;


import com.alexlatkin.filestorage.dto.user.UserDto;
import com.alexlatkin.filestorage.mappers.UserMapper;
import com.alexlatkin.filestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userMapper.toDto(userService.getUserById(id));
    }

    @PutMapping("/update/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        userService.update(userMapper.toEntity(userDto));
        return userMapper.toDto(userService.getUserById(id));
    }

}
