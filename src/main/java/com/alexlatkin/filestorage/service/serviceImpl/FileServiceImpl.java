package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.exception.file.FileNotFoundException;
import com.alexlatkin.filestorage.exception.file.FilenameIsNotAvailableException;
import com.alexlatkin.filestorage.exception.user.UserNotFoundException;
import com.alexlatkin.filestorage.model.entity.File;
import com.alexlatkin.filestorage.model.entity.Tag;
import com.alexlatkin.filestorage.repository.FileRepository;
import com.alexlatkin.filestorage.security.jwt.JwtUtils;
import com.alexlatkin.filestorage.service.FileService;
import com.alexlatkin.filestorage.service.FileStorageService;
import com.alexlatkin.filestorage.service.TagService;
import com.alexlatkin.filestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final TagService tagService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Transactional
    @Override
    public File addFile(File file, MultipartFile multipartFile) {
        file.setStorageFileName(UUID.randomUUID() + "." + file.getExtension());
        file.getUser().setBucketName(userService.getUserById(file.getUser().getId()).getBucketName());
        fileStorageService.upload(file, multipartFile);
        fileRepository.save(file);

        File dbFile = getFileByName(file.getName());

        file.getTags().forEach(tag -> {
                    if (tagService.existsTagByTagName(tag.getName().toLowerCase())) {
                        Tag dbTag = tagService.getTagByName(tag.getName());
                        tagService.assignFile(dbTag.getId(), dbFile.getId());
                    } else {
                        tag.setName(tag.getName().toLowerCase());
                        tagService.addTag(tag);
                        Tag dbTag = tagService.getTagByName(tag.getName());
                        tagService.assignFile(dbTag.getId(), dbFile.getId());
                    }
                }
                );
        return file;
    }

    @Transactional
    @Override
    public void delete(Long id) {

        File file = getFileById(id);

        fileStorageService.delete(file);

        file.getTags().forEach(tag -> tag.getFiles().remove(file));

        fileRepository.delete(file);
    }

    @Override
    public File update(File file) {

        if (!existsFileById(file.getId())) throw new FileNotFoundException("Файл не найден");

        File originalFile = getFileById(file.getId());

        if (!originalFile.getName().equals(file.getName()) && existsFileByName(file.getName())) {
            throw new FilenameIsNotAvailableException("Файл с таким именем уже существует");
        }

        if (file.getName() != null) originalFile.setName(file.getName());
        if (file.getDescription() != null) originalFile.setDescription(file.getDescription());
        if (file.getAccessModifier() != null) originalFile.setAccessModifier(file.getAccessModifier());
        if (file.getTags() != null) {

            file.getTags().forEach(tag -> {
                    if (!tagService.existsTagByTagName(tag.getName())) originalFile.getTags().add(tag);
            }
            );
        }

        fileRepository.save(originalFile);
        return originalFile;
    }

    @Override
    public String download(Long id, String path) {

        if (!existsFileById(id)) throw new FileNotFoundException("Файл не найден");

        File file = getFileById(id);

        String response = fileStorageService.download(file, path);
        file.setNumberOfDownloads(file.getNumberOfDownloads() + 1);
        fileRepository.save(file);
        return response;
    }

    @Override
    public String generateShareLink(Long id, int duration) {

        if (!existsFileById(id)) throw new FileNotFoundException("Файл не найден");

        return fileStorageService.generateShareLink(getFileById(id), duration);
    }

    @Override
    public File getFileById(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("Файл не найден"));
    }

    @Transactional
    @Override
    public File getFileByName(String name) {
        return fileRepository.findFileByName(name).orElseThrow(() -> new FileNotFoundException("Файл не найден"));
    }

    @Override
    public List<File> getFilesByTag(Tag tag) {
        Long tagId = tagService.getTagByName(tag.getName()).getId();
        return fileRepository.findPublicFilesByTagId(tagId);
    }

    @Override
    public List<File> getFilesByUserId(Long userId, String token) {

        if(!userService.existsUserById(userId)) throw new UserNotFoundException("Пользователь не существует");

        if (    StringUtils.hasText(token) &&
                jwtUtils.isValid(token.substring(7)) &&
                userId.equals(jwtUtils.getUserIdFromToken(token.substring(7)))) {

            return fileRepository.findFilesByUserId(userId);
        }

        return fileRepository.findPublicFilesByUserId(userId);
    }

    @Override
    public boolean existsFileById(Long id) {
        return fileRepository.existsById(id);
    }

    @Override
    public boolean existsFileByName(String name) {
        return fileRepository.existsFileByName(name);
    }
}
