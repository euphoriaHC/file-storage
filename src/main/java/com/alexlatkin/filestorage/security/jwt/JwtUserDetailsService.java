package com.alexlatkin.filestorage.security.jwt;

import com.alexlatkin.filestorage.model.entity.Role;
import com.alexlatkin.filestorage.model.entity.User;
import com.alexlatkin.filestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUserName(username);
        return new JwtEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>(user.getRoles().stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
        );
    }
}
