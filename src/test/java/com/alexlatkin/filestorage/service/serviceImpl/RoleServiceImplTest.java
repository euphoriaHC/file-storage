package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.model.entity.Role;
import com.alexlatkin.filestorage.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @InjectMocks
    RoleServiceImpl roleService;
    @Mock
    RoleRepository roleRepository;

    @Test
    void addRole() {
        Role mockRole = mock(Role.class);

        roleService.addRole(mockRole);

        verify(roleRepository, times(1)).save(mockRole);
    }

    @Test
    void getRole() {
        String roleName = "ROLE_USER";
        Role expectedRole = mock(Role.class);

        when(roleRepository.findRoleByName(roleName)).thenReturn(Optional.of(expectedRole));

        var result = roleService.getRole(roleName);

        assertEquals(expectedRole, result);
        verify(roleRepository, times(1)).findRoleByName(roleName);
    }
}