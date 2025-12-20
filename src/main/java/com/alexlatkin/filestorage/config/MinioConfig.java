package com.alexlatkin.filestorage.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class MinioConfig {
    @Value("${minio.url}")
    private final String url;
    @Value("${minio.accessKey}")
    private final String accessKey;
    @Value("${minio.secretKey}")
    private final String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
