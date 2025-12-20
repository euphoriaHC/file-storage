package com.alexlatkin.filestorage.exception.file;

public class FileIsEmptyException extends RuntimeException {

    public FileIsEmptyException(String message) {
        super(message);
    }
}
