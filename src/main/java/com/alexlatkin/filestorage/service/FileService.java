package com.alexlatkin.filestorage.service;

import com.alexlatkin.filestorage.model.entity.File;
import com.alexlatkin.filestorage.model.entity.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileService {
    File addFile(File file, MultipartFile multipartFile);
    void delete(Long id);
    File update(File file);
    String download(Long id, String path);
    String generateShareLink(Long id, int duration);
    File getFileById(Long id);
    File getFileByName(String name);
    List<File> getFilesByTag(Tag tag);
    List<File> getFilesByUserId(Long userId, String token);
    boolean existsFileById(Long id);
    boolean existsFileByName(String name);
}
