package com.alexlatkin.filestorage.exception.user;

public class UsernameIsNotAvailableException extends RuntimeException {

    public UsernameIsNotAvailableException(String message) {
        super(message);
    }
}
