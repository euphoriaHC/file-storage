package com.alexlatkin.filestorage.controller;

import com.alexlatkin.filestorage.exception.file.FileIsEmptyException;
import com.alexlatkin.filestorage.exception.file.FileNotFoundException;
import com.alexlatkin.filestorage.exception.file.FilenameIsNotAvailableException;
import com.alexlatkin.filestorage.exception.tag.TagNotFoundException;
import com.alexlatkin.filestorage.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFileNotFound(FileNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(FilenameIsNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFilenameIsNotAvailable(FilenameIsNotAvailableException e) {
        return e.getMessage();
    }

    @ExceptionHandler(FileIsEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFileIsEmpty(FileIsEmptyException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(UserNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UsernameIsNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUsernameIsNotAvailable(UsernameIsNotAvailableException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmailIsNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmailIsNotAvailable(EmailIsNotAvailableException e) {
        return e.getMessage();
    }

    @ExceptionHandler(PasswordNotConfirmedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlePasswordNotConfirmed(PasswordNotConfirmedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAuthentication(AuthenticationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTagNotFound(TagNotFoundException e) {
        return e.getMessage();
    }

}
