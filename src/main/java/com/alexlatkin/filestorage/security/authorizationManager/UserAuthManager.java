package com.alexlatkin.filestorage.security.authorizationManager;

import com.alexlatkin.filestorage.exception.user.UserNotFoundException;
import com.alexlatkin.filestorage.security.RequestParser;
import com.alexlatkin.filestorage.security.jwt.JwtUtils;
import com.alexlatkin.filestorage.service.UserService;
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
public class UserAuthManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final RequestParser requestParser;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        if (!requestParser.existsId(object.getRequest())) {
            throw new AccessDeniedException("Id is empty");
        }

        if (!requestParser.existsJwt(object.getRequest())) {
            return new AuthorizationDecision(false);
        }

        String jwt = requestParser.getJwt(object.getRequest());
        String requestUserName = jwtUtils.getUserNameFromToken(jwt);
        String updateUserName;

        try {
            updateUserName = userService.getUserById(requestParser.getObjectId(object.getRequest())).getUsername();
        } catch (UserNotFoundException e) {
            throw new AccessDeniedException(e.getMessage());
        }

        return new AuthorizationDecision(requestUserName.equals(updateUserName));
    }
}
