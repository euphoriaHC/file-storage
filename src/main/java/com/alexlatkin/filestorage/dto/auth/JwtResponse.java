package com.alexlatkin.filestorage.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtResponse {
    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;
}
