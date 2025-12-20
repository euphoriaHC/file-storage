package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.dto.user.RegistrationUserDto;
import com.alexlatkin.filestorage.exception.user.EmailIsNotAvailableException;
import com.alexlatkin.filestorage.exception.user.PasswordNotConfirmedException;
import com.alexlatkin.filestorage.exception.user.UserNotFoundException;
import com.alexlatkin.filestorage.exception.user.UsernameIsNotAvailableException;
import com.alexlatkin.filestorage.mappers.UserMapper;
import com.alexlatkin.filestorage.model.entity.Role;
import com.alexlatkin.filestorage.model.entity.User;
import com.alexlatkin.filestorage.repository.UserRepository;
import com.alexlatkin.filestorage.service.RoleService;
import com.alexlatkin.filestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @Override
    public void addUser(RegistrationUserDto registrationUserDto) {

        if (existsUserByEmail(registrationUserDto.getEmail())) {
            throw new EmailIsNotAvailableException("Почта уже зарегистрирована");
        }

        if(existsUserByUsername(registrationUserDto.getUsername())) {
            throw new UsernameIsNotAvailableException("Пользователь с таким именем уже существует");
        }

        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            throw new PasswordNotConfirmedException("Пароли не совпали");
        }

        User user = userMapper.toEntity(registrationUserDto);
        user.setBucketName(UUID.randomUUID().toString());
        user.setRoles(new HashSet<>());
        user.setFiles(new ArrayList<>());

        Role roleUser = roleService.getRole("ROLE_USER");
        user.getRoles().add(roleUser);

        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(User user) {

        User originalUser = getUserById(user.getId());

        if (!originalUser.getUsername().equals(user.getUsername()) && existsUserByUsername(user.getUsername())) {
            throw new UsernameIsNotAvailableException("Пользователь с таким именем уже существует");
        }

        if (!originalUser.getEmail().equals(user.getEmail()) && existsUserByEmail(user.getEmail())) {
            throw new EmailIsNotAvailableException("Пользователь с такой почтой уже существует");
        }

        if (user.getUsername() != null) originalUser.setUsername(user.getUsername());

        if (user.getEmail() != null) originalUser.setEmail(user.getEmail());

        userRepository.save(originalUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsUserById(Long id) {
        return userRepository.existsById(id);
    }
}
