package com.alexlatkin.filestorage.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
}
