package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.dto.user.RegistrationUserDto;
import com.alexlatkin.filestorage.exception.user.EmailIsNotAvailableException;
import com.alexlatkin.filestorage.exception.user.PasswordNotConfirmedException;
import com.alexlatkin.filestorage.exception.user.UsernameIsNotAvailableException;
import com.alexlatkin.filestorage.mappers.UserMapper;
import com.alexlatkin.filestorage.model.entity.Role;
import com.alexlatkin.filestorage.model.entity.User;
import com.alexlatkin.filestorage.repository.UserRepository;
import com.alexlatkin.filestorage.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleService roleService;
    @Mock
    UserMapper userMapper;

    @Test
    void addUser() {
        RegistrationUserDto registrationUserDto = mock(RegistrationUserDto.class);
        String email = "email@gmail.com";
        String username = "username";
        String password = "password";
        String confirmPassword = "password";
        User user = mock(User.class);
        Role role = mock(Role.class);

        when(registrationUserDto.getEmail()).thenReturn(email);
        when(registrationUserDto.getUsername()).thenReturn(username);
        when(userService.existsUserByEmail(email)).thenReturn(false);
        when(userService.existsUserByUsername(username)).thenReturn(false);
        when(registrationUserDto.getPassword()).thenReturn(password);
        when(registrationUserDto.getConfirmPassword()).thenReturn(confirmPassword);
        when(userMapper.toEntity(registrationUserDto)).thenReturn(user);
        when(roleService.getRole("ROLE_USER")).thenReturn(role);
        when(user.getRoles()).thenReturn(new HashSet<>());

        userService.addUser(registrationUserDto);

        verify(userMapper, times(1)).toEntity(registrationUserDto);
        verify(roleService, times(1)).getRole("ROLE_USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addUser_EmailIsNotAvailableException() {
        RegistrationUserDto registrationUserDto = mock(RegistrationUserDto.class);
        String email = "email@gmail.com";

        when(registrationUserDto.getEmail()).thenReturn(email);
        when(userService.existsUserByEmail(email)).thenReturn(true);

        assertThrows(EmailIsNotAvailableException.class, () -> userService.addUser(registrationUserDto));
    }

    @Test
    void addUser_UsernameIsNotAvailableException() {
        RegistrationUserDto registrationUserDto = mock(RegistrationUserDto.class);
        String email = "email@gmail.com";
        String username = "username";

        when(registrationUserDto.getEmail()).thenReturn(email);
        when(registrationUserDto.getUsername()).thenReturn(username);
        when(userService.existsUserByEmail(email)).thenReturn(false);
        when(userService.existsUserByUsername(username)).thenReturn(true);

        assertThrows(UsernameIsNotAvailableException.class, () -> userService.addUser(registrationUserDto));
    }

    @Test
    void addUser_PasswordNotConfirmedException() {
        RegistrationUserDto registrationUserDto = mock(RegistrationUserDto.class);
        String email = "email@gmail.com";
        String username = "username";
        String password = "password";
        String confirmPassword = "confirm";

        when(registrationUserDto.getEmail()).thenReturn(email);
        when(registrationUserDto.getUsername()).thenReturn(username);
        when(userService.existsUserByEmail(email)).thenReturn(false);
        when(userService.existsUserByUsername(username)).thenReturn(false);
        when(registrationUserDto.getPassword()).thenReturn(password);
        when(registrationUserDto.getConfirmPassword()).thenReturn(confirmPassword);

        assertThrows(PasswordNotConfirmedException.class, () -> userService.addUser(registrationUserDto));
    }

    @Test
    void delete() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void update() {
        User user = mock(User.class);
        User originalUser = mock(User.class);
        String username = "username";
        String originalUsername = "originalUserName";
        String userEmail = "userEmail@gmail.com";
        String originalUserEmail = "originalUserEmail@gmail.com";

        when(user.getId()).thenReturn(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(originalUser));
        when(originalUser.getUsername()).thenReturn(originalUsername);
        when(user.getUsername()).thenReturn(username);
        when(userService.existsUserByUsername(user.getUsername())).thenReturn(false);
        when(user.getEmail()).thenReturn(userEmail);
        when(originalUser.getEmail()).thenReturn(originalUserEmail);
        when(userService.existsUserByEmail(user.getEmail())).thenReturn(false);

        userService.update(user);

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(originalUser);
    }

    @Test
    void update_UsernameIsNotAvailableException() {
        User user = mock(User.class);
        User originalUser = mock(User.class);
        String username = "username";
        String originalUsername = "originalUserName";

        when(user.getId()).thenReturn(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(originalUser));
        when(originalUser.getUsername()).thenReturn(originalUsername);
        when(user.getUsername()).thenReturn(username);
        when(userService.existsUserByUsername(user.getUsername())).thenReturn(true);

        assertThrows(UsernameIsNotAvailableException.class, () -> userService.update(user));
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void update_EmailIsNotAvailableException() {
        User user = mock(User.class);
        User originalUser = mock(User.class);
        String username = "username";
        String originalUsername = "originalUserName";
        String userEmail = "userEmail@gmail.com";
        String originalUserEmail = "originalUserEmail@gmail.com";

        when(user.getId()).thenReturn(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(originalUser));
        when(originalUser.getUsername()).thenReturn(originalUsername);
        when(user.getUsername()).thenReturn(username);
        when(userService.existsUserByUsername(user.getUsername())).thenReturn(false);
        when(user.getEmail()).thenReturn(userEmail);
        when(originalUser.getEmail()).thenReturn(originalUserEmail);
        when(userService.existsUserByEmail(user.getEmail())).thenReturn(true);


        assertThrows(EmailIsNotAvailableException.class, () -> userService.update(user));
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getUserById() {
        User expectedUser = mock(User.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        var result = userService.getUserById(1L);

        assertEquals(expectedUser, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserByUserName() {
        String username = "alex";
        User expectedUser = mock(User.class);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(expectedUser));

        var result = userService.getUserByUserName(username);

        assertEquals(expectedUser, result);
        verify(userRepository, times(1)).findUserByUsername(username);
    }

    @Test
    void existsUserByUsername() {
        String username = "alex";

        when(userRepository.existsUserByUsername(username)).thenReturn(true);

        var result = userService.existsUserByUsername(username);

        assertTrue(result);
        verify(userRepository, times(1)).existsUserByUsername(username);
    }

    @Test
    void existsUserByEmail() {
        String email = "email@gmail.com";

        when(userRepository.existsUserByEmail(email)).thenReturn(true);

        var result = userService.existsUserByEmail(email);

        assertTrue(result);
        verify(userRepository, times(1)).existsUserByEmail(email);
    }

    @Test
    void existsUserById() {
        when(userRepository.existsById(1L)).thenReturn(true);

        var result = userService.existsUserById(1L);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(1L);
    }
}