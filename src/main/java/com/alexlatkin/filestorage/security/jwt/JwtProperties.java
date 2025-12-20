package com.alexlatkin.filestorage.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource("classpath:application.yml")
public class JwtProperties {
    @Value("${security.jwt.secret}")
    private String secret;
    @Value("${security.jwt.access}")
    private Long accessTokenTtl;
    @Value("${security.jwt.refresh}")
    private Long refreshTokenTtl;

}
