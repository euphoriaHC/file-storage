package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.dto.auth.JwtRequest;
import com.alexlatkin.filestorage.dto.auth.JwtResponse;
import com.alexlatkin.filestorage.model.entity.User;
import com.alexlatkin.filestorage.security.jwt.JwtUtils;
import com.alexlatkin.filestorage.service.AuthService;
import com.alexlatkin.filestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse login(JwtRequest jwtRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new com.alexlatkin.filestorage.exception.user.AuthenticationException("Неверное имя пользователя или пароль");
        }

        User user = userService.getUserByUserName(jwtRequest.getUsername());

        return new JwtResponse(
                user.getId(),
                user.getUsername(),
                jwtUtils.createAccessToken(user),
                jwtUtils.createRefreshToken(user)
        );
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtUtils.refreshUserTokens(refreshToken.substring(7));
    }
}
