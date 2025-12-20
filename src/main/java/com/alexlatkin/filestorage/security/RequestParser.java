package com.alexlatkin.filestorage.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RequestParser {

    public boolean existsJwt(HttpServletRequest request) {
        String headAuth = request.getHeader("Authorization");
        return StringUtils.hasText(headAuth) && headAuth.startsWith("Bearer ");
    }

    public String getJwt(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    public String getEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.substring(0, uri.lastIndexOf("/"));
    }

    public Long getObjectId(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (!existsId(request)) {
            throw new RuntimeException("Id is empty");
        }

        return Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
    }

    public boolean existsId(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return StringUtils.hasText(uri.substring(uri.lastIndexOf("/") + 1));
    }
}
