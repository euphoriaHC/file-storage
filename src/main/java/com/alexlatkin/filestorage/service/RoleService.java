package com.alexlatkin.filestorage.service;

import com.alexlatkin.filestorage.model.entity.Role;

public interface RoleService {
    void addRole(Role role);
    Role getRole (String roleName);
}
