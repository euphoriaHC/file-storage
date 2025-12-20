package com.alexlatkin.filestorage.service;

import com.alexlatkin.filestorage.dto.auth.JwtRequest;
import com.alexlatkin.filestorage.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest jwtRequest);
    JwtResponse refresh(String refreshToken);
}
