package com.alexlatkin.filestorage.exception.user;

public class EmailIsNotAvailableException extends RuntimeException {

    public EmailIsNotAvailableException(String message) {
        super(message);
    }
}
