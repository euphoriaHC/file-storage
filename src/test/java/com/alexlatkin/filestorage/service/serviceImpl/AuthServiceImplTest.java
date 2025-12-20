package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.dto.auth.JwtRequest;
import com.alexlatkin.filestorage.dto.auth.JwtResponse;
import com.alexlatkin.filestorage.exception.user.AuthenticationException;
import com.alexlatkin.filestorage.model.entity.User;
import com.alexlatkin.filestorage.security.jwt.JwtUtils;
import com.alexlatkin.filestorage.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    AuthServiceImpl authService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserService userService;
    @Mock
    JwtUtils jwtUtils;

    @Test
    void login() {
        User mockUser = mock(User.class);
        JwtRequest mockJwtRequest = mock(JwtRequest.class);
        String username = "username";
        String accessToken = "Bearer qw[peqwpeqwp[";
        String refreshToken = "Bearer ewkqopeqweop";
        JwtResponse expected = new JwtResponse(1L, username, accessToken, refreshToken);

        when(mockJwtRequest.getUsername()).thenReturn(username);
        when(mockJwtRequest.getPassword()).thenReturn("password");
        when(userService.getUserByUserName(mockJwtRequest.getUsername())).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn(username);
        when(jwtUtils.createAccessToken(mockUser)).thenReturn(accessToken);
        when(jwtUtils.createRefreshToken(mockUser)).thenReturn(refreshToken);

        var result = authService.login(mockJwtRequest);

        assertEquals(expected, result);
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(mockJwtRequest.getUsername(), mockJwtRequest.getPassword()));
        verify(userService, times(1)).getUserByUserName(mockJwtRequest.getUsername());
        verify(jwtUtils, times(1)).createAccessToken(mockUser);
        verify(jwtUtils, times(1)).createRefreshToken(mockUser);
    }

    @Test
    void login_AuthenticationException() {
        JwtRequest mockJwtRequest = mock(JwtRequest.class);

        when(mockJwtRequest.getUsername()).thenReturn("username");
        when(mockJwtRequest.getPassword()).thenReturn("password");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mockJwtRequest.getUsername(), mockJwtRequest.getPassword())))
                .thenThrow(com.alexlatkin.filestorage.exception.user.AuthenticationException.class);

        assertThrows(AuthenticationException.class, () -> authService.login(mockJwtRequest));
    }

    @Test
    void refresh() {
        String token = "Bearer 12312wqepoqwep";
        JwtResponse expected = mock(JwtResponse.class);

        when(jwtUtils.refreshUserTokens(token.substring(7))).thenReturn(expected);

        var result = authService.refresh(token);

        assertEquals(expected, result);
        verify(jwtUtils, times(1)).refreshUserTokens(token.substring(7));
    }
}