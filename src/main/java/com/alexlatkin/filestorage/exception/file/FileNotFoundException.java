package com.alexlatkin.filestorage.exception.file;


public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String message) {
        super(message);
    }
}
