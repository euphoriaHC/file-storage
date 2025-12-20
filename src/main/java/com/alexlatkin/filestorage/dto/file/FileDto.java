package com.alexlatkin.filestorage.dto.file;

import com.alexlatkin.filestorage.model.FileAccessModifier;
import com.alexlatkin.filestorage.model.entity.Tag;
import com.alexlatkin.filestorage.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String name;
    private String description;
    private String uploadDate;
    private int numberOfDownloads;
    private String extension;
    private FileAccessModifier accessModifier;
    private User user;
    private List<Tag> tags;
    @JsonIgnore
    private MultipartFile file;
    public FileDto(Long id, String name, String description, String uploadDate, int numberOfDownloads, String extension, FileAccessModifier accessModifier, User user, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uploadDate = uploadDate;
        this.numberOfDownloads = numberOfDownloads;
        this.extension = extension;
        this.accessModifier = accessModifier;
        this.user = user;
        this.tags = tags;
    }
}
