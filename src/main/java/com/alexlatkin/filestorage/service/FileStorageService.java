package com.alexlatkin.filestorage.service;

import com.alexlatkin.filestorage.model.entity.File;
import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {

    void upload(File file, MultipartFile multipartFile);
    String download(File file, String path);
    void delete(File file);
    String generateShareLink(File file, int duration);
}
