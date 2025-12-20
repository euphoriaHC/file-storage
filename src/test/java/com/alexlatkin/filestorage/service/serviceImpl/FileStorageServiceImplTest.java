package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.exception.file.FileIsEmptyException;
import com.alexlatkin.filestorage.model.entity.File;
import com.alexlatkin.filestorage.model.entity.User;
import io.minio.*;
import io.minio.http.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceImplTest {

    @InjectMocks
    FileStorageServiceImpl fileStorageService;
    @Mock
    MinioClient minioClient;

    @Test
    void upload() throws Exception {
        File mockFile = mock(File.class);
        User mockUser = mock(User.class);
        MultipartFile multipartFile = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);
        String bucketName = UUID.randomUUID().toString();
        String storageFileName = "storageFileName";

        when(mockFile.getUser()).thenReturn(mockUser);
        when(mockFile.getUser().getBucketName()).thenReturn(bucketName);
        when(mockFile.getStorageFileName()).thenReturn(storageFileName);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        fileStorageService.upload(mockFile, multipartFile);
    }

    @Test
    void upload_FileIsEmptyException() {
        File mockFile = mock(File.class);
        User mockUser = mock(User.class);
        MultipartFile multipartFile = mock(MultipartFile.class);
        String bucketName = UUID.randomUUID().toString();
        String storageFileName = "storageFileName";

        when(mockFile.getUser()).thenReturn(mockUser);
        when(mockFile.getUser().getBucketName()).thenReturn(bucketName);
        when(mockFile.getStorageFileName()).thenReturn(storageFileName);
        when(multipartFile.isEmpty()).thenReturn(true);

        assertThrows(FileIsEmptyException.class, () -> fileStorageService.upload(mockFile, multipartFile));
    }

    @Test
    void download() {
        File mockFile = mock(File.class);
        User mockUser = mock(User.class);
        String bucketName = UUID.randomUUID().toString();
        String storageFileName = "storageFileName";
        String path = "path";
        String fileName = "fileName";

        when(mockFile.getUser()).thenReturn(mockUser);
        when(mockFile.getUser().getBucketName()).thenReturn(bucketName);
        when(mockFile.getStorageFileName()).thenReturn(storageFileName);
        when(mockFile.getName()).thenReturn(fileName);

        var result = fileStorageService.download(mockFile, path);

        assertEquals(fileName + " скачан", result);
    }

    @Test
    void delete() throws Exception {
        File mockFile = mock(File.class);
        User mockUser = mock(User.class);
        String bucketName = UUID.randomUUID().toString();
        String storageFileName = "storageFileName";

        when(mockFile.getUser()).thenReturn(mockUser);
        when(mockFile.getUser().getBucketName()).thenReturn(bucketName);
        when(mockFile.getStorageFileName()).thenReturn(storageFileName);

        fileStorageService.delete(mockFile);

        verify(minioClient, times(1)).removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(storageFileName)
                .build());
    }

    @Test
    void generateShareLink() throws Exception {
        File mockFile = mock(File.class);
        User mockUser = mock(User.class);
        String bucketName = UUID.randomUUID().toString();
        String storageFileName = "storageFileName";
        String expectedLink = "expected";

        when(mockFile.getUser()).thenReturn(mockUser);
        when(mockFile.getUser().getBucketName()).thenReturn(bucketName);
        when(mockFile.getStorageFileName()).thenReturn(storageFileName);
        when(minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(storageFileName)
                .expiry(1, TimeUnit.HOURS)
                .build()))
                .thenReturn(expectedLink);

        var result = fileStorageService.generateShareLink(mockFile, 1);

        assertEquals(expectedLink, result);
        verify(minioClient, times(1)).getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(storageFileName)
                .expiry(1, TimeUnit.HOURS)
                .build());
    }
}