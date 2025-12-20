package com.alexlatkin.filestorage.mappers;

import com.alexlatkin.filestorage.dto.file.FileDto;
import com.alexlatkin.filestorage.model.entity.File;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileMapper {

    public File toEntity(FileDto fileDto) {
        return new File(
                fileDto.getId(),
                fileDto.getName(),
                fileDto.getDescription(),
                fileDto.getUploadDate(),
                fileDto.getNumberOfDownloads(),
                fileDto.getExtension(),
                fileDto.getAccessModifier(),
                fileDto.getUser(),
                fileDto.getTags()
        );
    }

    public FileDto toDto(File file) {
        return new FileDto(
                file.getId(),
                file.getName(),
                file.getDescription(),
                file.getUploadDate(),
                file.getNumberOfDownloads(),
                file.getExtension(),
                file.getAccessModifier(),
                file.getUser(),
                file.getTags()
        );
    }

    public List<FileDto> toListDto(List<File> fileList) {
        return fileList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
