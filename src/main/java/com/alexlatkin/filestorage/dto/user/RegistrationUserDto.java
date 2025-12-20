package com.alexlatkin.filestorage.dto.user;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegistrationUserDto {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
