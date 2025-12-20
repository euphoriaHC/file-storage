package com.alexlatkin.filestorage.exception.user;

public class PasswordNotConfirmedException extends RuntimeException {

    public PasswordNotConfirmedException(String message) {
        super(message);
    }
}
