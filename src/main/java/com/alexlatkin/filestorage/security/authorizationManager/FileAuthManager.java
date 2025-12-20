package com.alexlatkin.filestorage.security.authorizationManager;

import com.alexlatkin.filestorage.exception.file.FileNotFoundException;
import com.alexlatkin.filestorage.security.RequestParser;
import com.alexlatkin.filestorage.security.jwt.JwtUtils;
import com.alexlatkin.filestorage.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class FileAuthManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final JwtUtils jwtUtils;
    private final FileService fileService;
    private final RequestParser requestParser;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        if (!requestParser.existsId(object.getRequest())) {
            throw new AccessDeniedException("Id is empty");
        }

        HttpServletRequest request = object.getRequest();
        boolean jwtExists = requestParser.existsJwt(request);
        String endPoint = requestParser.getEndpoint(request);

        return switch (endPoint) {
            case "/files/download", "/files/id" -> jwtIsNotRequiredRequests(jwtExists, request);
            case "/files/delete", "/files/update", "/files/share" -> jwtRequiredRequests(jwtExists, request);
            default -> new AuthorizationDecision(false);
        };
    }

    private AuthorizationDecision jwtRequiredRequests(boolean hasJwt, HttpServletRequest request) {

        if (!hasJwt) {
            return new AuthorizationDecision(false);
        }

        AuthorizationDecision decision;

        try {
            decision = userNamesMatching(request);
        } catch (FileNotFoundException e) {
            throw new AccessDeniedException(e.getMessage());
        }

        return decision;
    }

    private AuthorizationDecision jwtIsNotRequiredRequests(boolean hasJwt, HttpServletRequest request) {

        Long fileId = requestParser.getObjectId(request);
        boolean fileAccessModifierIsPrivate;

        try {
            fileAccessModifierIsPrivate = fileService.getFileById(fileId).getAccessModifier().name().equals("PRIVATE");
        } catch (FileNotFoundException e) {
            throw new AccessDeniedException(e.getMessage());
        }

        if (!hasJwt && fileAccessModifierIsPrivate) {
            return new AuthorizationDecision(false);
        }

        if (hasJwt && fileAccessModifierIsPrivate) {
            return userNamesMatching(request);
        }

        return new AuthorizationDecision(true);
    }

    private AuthorizationDecision userNamesMatching(HttpServletRequest request) {

        String jwt = requestParser.getJwt(request);
        String requestUsername = jwtUtils.getUserNameFromToken(jwt);
        String fileUploaderUsername = fileService.getFileById(requestParser.getObjectId(request)).getUser().getUsername();

        return new AuthorizationDecision(requestUsername.equals(fileUploaderUsername));
    }

}
