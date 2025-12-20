package com.alexlatkin.filestorage.config;

import com.alexlatkin.filestorage.security.authorizationManager.FileAuthManager;
import com.alexlatkin.filestorage.security.authorizationManager.UserAuthManager;
import com.alexlatkin.filestorage.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final UserAuthManager userAuthManager;
    private final FileAuthManager fileAuthManager;
    public SecurityConfig(JwtFilter jwtFilter, UserAuthManager userAuthManager, FileAuthManager fileAuthManager) {
        this.jwtFilter = jwtFilter;
        this.userAuthManager = userAuthManager;
        this.fileAuthManager = fileAuthManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/refresh").hasRole("USER")
                        .requestMatchers("/users/update/**").access(userAuthManager)
                        .requestMatchers("/files/upload").hasRole("USER")
                        .requestMatchers("/files/user/**").permitAll()
                        .requestMatchers("/files/tag/**").permitAll()
                        .requestMatchers("/files/**").access(fileAuthManager)
                        .anyRequest().permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Unauthorized");
                }).accessDeniedHandler((request, response, accessDeniedException) -> {

                    if (    accessDeniedException.getMessage().equals("Файл не найден") ||
                            accessDeniedException.getMessage().equals("Пользователь не найден")) {

                            response.setStatus(HttpStatus.NOT_FOUND.value());
                            response.getWriter().write("Not Found");
                    } else if(accessDeniedException.getMessage().equals("Id is empty")) {

                            response.setStatus(HttpStatus.BAD_REQUEST.value());
                            response.getWriter().write("Bad Request");
                    } else {

                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write("Forbidden");
                    }
                })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
