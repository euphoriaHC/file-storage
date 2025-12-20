package com.alexlatkin.filestorage.service.serviceImpl;

import com.alexlatkin.filestorage.exception.file.FileIsEmptyException;
import com.alexlatkin.filestorage.model.entity.File;
import com.alexlatkin.filestorage.service.FileStorageService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;

    @SneakyThrows
    @Override
    public void upload(File file, MultipartFile multipartFile) {

        String bucketName = file.getUser().getBucketName();
        String fileName = file.getStorageFileName();

        createBucket(bucketName);

        if (multipartFile.isEmpty()) {
            throw new FileIsEmptyException("Файл пуст");
        }

        InputStream inputStream;

        try {
             inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    @SneakyThrows
    @Override
    public String download(File file,  String path) {

        String fileName = file.getStorageFileName();
        String bucketName = file.getUser().getBucketName();
        String savePath = path + "/" + file.getName();

        minioClient.downloadObject(DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .filename(savePath)
                .build());

        return file.getName() + " скачан";
    }

    @SneakyThrows
    @Override
    public void delete(File file) {

        String bucketName = file.getUser().getBucketName();
        String fileName = file.getStorageFileName();

        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());

    }

    @SneakyThrows
    @Override
    public String generateShareLink(File file, int duration) {

        String bucketName = file.getUser().getBucketName();
        String fileName = file.getStorageFileName();

        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .expiry(duration, TimeUnit.HOURS)
                .build());
    }

    @SneakyThrows
    private void createBucket(String bucketName)  {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if(!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}
