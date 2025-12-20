package com.alexlatkin.filestorage.security.jwt;


import com.alexlatkin.filestorage.security.RequestParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RequestParser requestParser;

    @Override
    @SneakyThrows
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) {

        String token = parseJwt(request);

        try {

            if (jwtUtils.isValid(token)) {

                Authentication authentication = jwtUtils.getAuthentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception e) {
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {

        if (requestParser.existsJwt(request)) {
            return requestParser.getJwt(request);
        }

        return null;
    }

}
