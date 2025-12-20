package com.alexlatkin.filestorage.service;

import com.alexlatkin.filestorage.dto.user.RegistrationUserDto;
import com.alexlatkin.filestorage.model.entity.User;


public interface UserService {
    void addUser (RegistrationUserDto registrationUserDto);
    void delete (Long id);
    void update(User user);
    User getUserById (Long id);
    User getUserByUserName(String username);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
    boolean existsUserById(Long id);
}
