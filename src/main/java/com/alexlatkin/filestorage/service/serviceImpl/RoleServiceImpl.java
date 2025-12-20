package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.model.entity.Role;
import com.alexlatkin.filestorage.repository.RoleRepository;
import com.alexlatkin.filestorage.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role getRole(String roleName) {
        return roleRepository.findRoleByName(roleName).get();
    }
}
