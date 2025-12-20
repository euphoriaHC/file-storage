package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.exception.file.FileNotFoundException;
import com.alexlatkin.filestorage.exception.file.FilenameIsNotAvailableException;
import com.alexlatkin.filestorage.exception.user.UserNotFoundException;
import com.alexlatkin.filestorage.model.entity.File;
import com.alexlatkin.filestorage.model.entity.Tag;
import com.alexlatkin.filestorage.model.entity.User;
import com.alexlatkin.filestorage.repository.FileRepository;
import com.alexlatkin.filestorage.security.jwt.JwtUtils;
import com.alexlatkin.filestorage.service.FileStorageService;
import com.alexlatkin.filestorage.service.TagService;
import com.alexlatkin.filestorage.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @InjectMocks
    FileServiceImpl fileService;
    @Mock
    FileRepository fileRepository;
    @Mock
    FileStorageService fileStorageService;
    @Mock
    TagService tagService;
    @Mock
    UserService userService;
    @Mock
    JwtUtils jwtUtils;

    @Test
    void addFile_ExistsTag() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        File file = mock(File.class);
        User user = mock(User.class);
        Tag tag = new Tag(1L, "book");
        List<Tag> tags = List.of(tag);

        when(file.getExtension()).thenReturn("extension");
        when(user.getBucketName()).thenReturn("bucket2133");
        when(file.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(userService.getUserById(1L)).thenReturn(user);
        when(file.getName()).thenReturn("filename");
        when(fileRepository.findFileByName(file.getName())).thenReturn(Optional.of(file));
        when(file.getTags()).thenReturn(tags);
        when(tagService.existsTagByTagName(tag.getName())).thenReturn(true);
        when(tagService.getTagByName(tag.getName())).thenReturn(tag);

        var result = fileService.addFile(file, multipartFile);

        assertEquals(file, result);
        verify(userService, times(1)).getUserById(1L);
        verify(fileStorageService, times(1)).upload(file, multipartFile);
        verify(fileRepository, times(1)).save(file);
        verify(fileRepository, times(1)).findFileByName(file.getName());
        verify(tagService, times(1)).existsTagByTagName(tag.getName());
        verify(tagService,times(1)).getTagByName(tag.getName());
        verify(tagService, times(1)).assignFile(tag.getId(), file.getId());
    }

    @Test
    void addFile_TagDoesNotExist() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        File file = mock(File.class);
        User user = mock(User.class);
        Tag tag = new Tag(1L, "book");
        List<Tag> tags = List.of(tag);

        when(file.getExtension()).thenReturn("extension");
        when(user.getBucketName()).thenReturn("bucket2133");
        when(file.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(userService.getUserById(1L)).thenReturn(user);
        when(file.getName()).thenReturn("filename");
        when(fileRepository.findFileByName(file.getName())).thenReturn(Optional.of(file));
        when(file.getTags()).thenReturn(tags);
        when(tagService.existsTagByTagName(tag.getName())).thenReturn(false);
        when(tagService.getTagByName(tag.getName())).thenReturn(tag);

        var result = fileService.addFile(file, multipartFile);

        assertEquals(file, result);
        verify(userService, times(1)).getUserById(1L);
        verify(fileStorageService, times(1)).upload(file, multipartFile);
        verify(fileRepository, times(1)).save(file);
        verify(fileRepository, times(1)).findFileByName(file.getName());
        verify(tagService, times(1)).existsTagByTagName(tag.getName());
        verify(tagService, times(1)).addTag(tag);
        verify(tagService,times(1)).getTagByName(tag.getName());
        verify(tagService, times(1)).assignFile(tag.getId(), file.getId());
    }

    @Test
    void delete() {
        File file = mock(File.class);
        Tag tag = mock(Tag.class);
        List<Tag> tagList = List.of(tag);
        List<File> files = new ArrayList<>();
        files.add(file);

        when(fileRepository.findById(1L)).thenReturn(Optional.of(file));
        when(file.getTags()).thenReturn(tagList);
        when(tag.getFiles()).thenReturn(files);

        fileService.delete(1L);

        verify(fileRepository, times(1)).findById(1L);
        verify(fileStorageService, times(1)).delete(file);
        verify(fileRepository, times(1)).delete(file);
    }

    @Test
    void update_updateFileName() {
        File file = mock(File.class);
        File originalFile = mock(File.class);
        String updateName = "updateName";

        when(file.getId()).thenReturn(1L);
        when(fileService.existsFileById(file.getId())).thenReturn(true);
        when(fileRepository.findById(file.getId())).thenReturn(Optional.of(originalFile));
        when(file.getName()).thenReturn(updateName);
        when(originalFile.getName()).thenReturn("originalName");
        when(fileService.existsFileByName(file.getName())).thenReturn(false);

        var result = fileService.update(file);

        assertEquals(originalFile, result);
        verify(fileRepository, times(1)).findById(1L);
        verify(fileRepository, times(1)).save(originalFile);
    }

    @Test
    void update_updateTagsAndFileName() {
        File file = mock(File.class);
        File originalFile = mock(File.class);
        String updateName = "updateName";
        Tag tag = new Tag(1L, "book");
        List<Tag> tags = List.of(tag);

        when(file.getId()).thenReturn(1L);
        when(fileService.existsFileById(file.getId())).thenReturn(true);
        when(fileRepository.findById(file.getId())).thenReturn(Optional.of(originalFile));
        when(file.getName()).thenReturn(updateName);
        when(originalFile.getName()).thenReturn("originalName");
        when(fileService.existsFileByName(file.getName())).thenReturn(false);
        when(file.getTags()).thenReturn(tags);
        when(originalFile.getTags()).thenReturn(new ArrayList<>());
        when(tagService.existsTagByTagName(tag.getName())).thenReturn(false);

        var result = fileService.update(file);

        assertEquals(originalFile, result);
        verify(fileRepository, times(1)).findById(1L);
        verify(tagService, times(1)).existsTagByTagName(tag.getName());
        verify(fileRepository, times(1)).save(originalFile);
    }

    @Test
    void update_FileNotFoundException() {
        File file = mock(File.class);

        when(file.getId()).thenReturn(1L);
        when(fileService.existsFileById(file.getId())).thenReturn(false);

        assertThrows(FileNotFoundException.class, () -> fileService.update(file));
    }

    @Test
    void update_FilenameIsNotAvailableException() {
        File file = mock(File.class);
        File originalFile = mock(File.class);
        String updateName = "updateName";

        when(file.getId()).thenReturn(1L);
        when(fileService.existsFileById(file.getId())).thenReturn(true);
        when(fileRepository.findById(file.getId())).thenReturn(Optional.of(originalFile));
        when(file.getName()).thenReturn(updateName);
        when(originalFile.getName()).thenReturn("originalName");
        when(fileService.existsFileByName(file.getName())).thenReturn(true);

        assertThrows(FilenameIsNotAvailableException.class, () -> fileService.update(file));
        verify(fileRepository, times(1)).findById(1L);
    }

    @Test
    void download() {
        File file = mock(File.class);
        String expected = "файл скачан";

        when(fileService.existsFileById(1L)).thenReturn(true);
        when(fileRepository.findById(1L)).thenReturn(Optional.of(file));
        when(fileStorageService.download(file, "path")).thenReturn(expected);

        var result = fileService.download(1L, "path");

        assertEquals(expected, result);
        verify(fileRepository, times(1)).findById(1L);
        verify(fileStorageService, times(1)).download(file, "path");
    }

    @Test
    void download_FileNotFoundException() {

        when(fileService.existsFileById(1L)).thenReturn(false);

        assertThrows(FileNotFoundException.class, () -> fileService.download(1L, "path"));
    }

    @Test
    void generateShareLink() {
        File file = mock(File.class);
        String expectedLink = "link";

        when(fileService.existsFileById(1L)).thenReturn(true);
        when(fileRepository.findById(1L)).thenReturn(Optional.of(file));
        when(fileStorageService.generateShareLink(file, 1)).thenReturn(expectedLink);

        var result = fileService.generateShareLink(1L, 1);

        assertEquals(expectedLink, result);
        verify(fileRepository, times(1)).findById(1L);
        verify(fileStorageService, times(1)).generateShareLink(file, 1);
    }

    @Test
    void generateShareLink_FileNotFoundException() {

        when(fileService.existsFileById(1L)).thenReturn(true);

        assertThrows(FileNotFoundException.class, () -> fileService.generateShareLink(1L, 1));
    }

    @Test
    void getFileById() {
        File expectFile = mock(File.class);

        when(fileRepository.findById(1L)).thenReturn(Optional.of(expectFile));

        var result = fileService.getFileById(1L);

        assertEquals(expectFile, result);
        verify(fileRepository, times(1)).findById(1L);
    }

    @Test
    void getFileByName() {
        String fileName = "file";
        File expectFile = mock(File.class);

        when(fileRepository.findFileByName(fileName)).thenReturn(Optional.of(expectFile));

        var result = fileService.getFileByName(fileName);

        assertEquals(expectFile, result);
        verify(fileRepository, times(1)).findFileByName(fileName);
    }

    @Test
    void getFilesByTag() {
        String tagName = "tagname";
        Tag mockTag = mock(Tag.class);
        List<File> expectedList = List.of(new File(), new File());

        when(mockTag.getName()).thenReturn(tagName);
        when(mockTag.getId()).thenReturn(1L);
        when(tagService.getTagByName(mockTag.getName())).thenReturn(mockTag);
        when(fileRepository.findPublicFilesByTagId(1L)).thenReturn(expectedList);

        var result = fileService.getFilesByTag(mockTag);

        assertEquals(expectedList, result);
        verify(tagService, times(1)).getTagByName(tagName);
        verify(fileRepository, times(1)).findPublicFilesByTagId(1L);
    }

    @Test
    void getFilesByUserId_Owner() {
        String token = "Bearer token";
        String tokenWithoutBearer = token.substring(7);
        List<File> expectedList = new ArrayList<>();

        when(userService.existsUserById(1L)).thenReturn(true);
        when(jwtUtils.isValid(tokenWithoutBearer)).thenReturn(true);
        when(jwtUtils.getUserIdFromToken(tokenWithoutBearer)).thenReturn(1L);
        when(fileRepository.findFilesByUserId(1L)).thenReturn(expectedList);

        var result = fileService.getFilesByUserId(1L, token);

        assertEquals(expectedList, result);
        verify(jwtUtils, times(1)).isValid(tokenWithoutBearer);
        verify(jwtUtils, times(1)).getUserIdFromToken(tokenWithoutBearer);
        verify(fileRepository, times(1)).findFilesByUserId(1L);
    }

    @Test
    void getFilesByUserId_NotOwner() {
        String token = "Bearer token";
        String tokenWithoutBearer = token.substring(7);
        List<File> expectedList = new ArrayList<>();

        when(userService.existsUserById(1L)).thenReturn(true);
        when(jwtUtils.isValid(tokenWithoutBearer)).thenReturn(true);
        when(jwtUtils.getUserIdFromToken(tokenWithoutBearer)).thenReturn(2L);
        when(fileRepository.findPublicFilesByUserId(1L)).thenReturn(expectedList);

        var result = fileService.getFilesByUserId(1L, token);

        assertEquals(expectedList, result);
        verify(jwtUtils, times(1)).isValid(tokenWithoutBearer);
        verify(jwtUtils, times(1)).getUserIdFromToken(tokenWithoutBearer);
        verify(fileRepository, times(1)).findPublicFilesByUserId(1L);
    }

    @Test
    void getFilesByUserId_UserNotFoundException() {

        when(userService.existsUserById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> fileService.getFilesByUserId(1L, "Bearer token"));
    }

    @Test
    void existsFileById() {
        when(fileRepository.existsById(1L)).thenReturn(true);

        var result = fileService.existsFileById(1L);

        assertTrue(result);
        verify(fileRepository, times(1)).existsById(1L);
    }

    @Test
    void existsFileByName() {
        String fileName = "file";

        when(fileRepository.existsFileByName(fileName)).thenReturn(true);

        var result = fileService.existsFileByName(fileName);

        assertTrue(result);
        verify(fileRepository, times(1)).existsFileByName(fileName);
    }
}