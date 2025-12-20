package com.alexlatkin.filestorage.controller;

import com.alexlatkin.filestorage.dto.file.FileDto;
import com.alexlatkin.filestorage.mappers.FileMapper;
import com.alexlatkin.filestorage.model.entity.File;
import com.alexlatkin.filestorage.model.entity.Tag;
import com.alexlatkin.filestorage.service.FileService;
import com.alexlatkin.filestorage.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final TagService tagService;
    private final FileMapper fileMapper;

    @PostMapping("/upload")
    public FileDto uploadFile(@ModelAttribute FileDto fileDto) {
        MultipartFile multipartFile = fileDto.getFile();
        File file = fileMapper.toEntity(fileDto);
        return fileMapper.toDto(fileService.addFile(file, multipartFile));
    }

    @PostMapping("/download/{id}")
    public String downloadFile(@PathVariable Long id, @RequestBody String path) {
        return fileService.download(id, path);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileService.delete(id);
    }

    @PutMapping("/update/{id}")
    public FileDto updateFile(@PathVariable Long id, @ModelAttribute FileDto fileDto) {
        fileService.update(fileMapper.toEntity(fileDto));
        return fileMapper.toDto(fileService.getFileById(id));
    }

    @GetMapping("/share/{id}")
    public String generateShareLink(@PathVariable Long id, @RequestHeader Integer duration) {
        return fileService.generateShareLink(id, duration);
    }

    @GetMapping("/id/{id}")
    public FileDto getById(@PathVariable Long id) {
        return fileMapper.toDto(fileService.getFileById(id));
    }

    @GetMapping("/tag/{tagname}")
    public List<FileDto> getByTagName(@PathVariable String tagname) {
        Tag tag = tagService.getTagByName(tagname);
        return fileMapper.toListDto(fileService.getFilesByTag(tag));
    }

    @GetMapping("/user/{userId}")
    public List<FileDto> getByUserId(@PathVariable Long userId,
                                     @RequestHeader(value = "Authorization", required = false) String token) {
        return fileMapper.toListDto(fileService.getFilesByUserId(userId, token));
    }

}
