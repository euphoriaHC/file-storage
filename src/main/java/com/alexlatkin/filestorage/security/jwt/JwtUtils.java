package com.alexlatkin.filestorage.security.jwt;

import com.alexlatkin.filestorage.dto.auth.JwtResponse;
import com.alexlatkin.filestorage.model.entity.Role;
import com.alexlatkin.filestorage.model.entity.User;
import com.alexlatkin.filestorage.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtProperties jwtProperties;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("id", user.getId())
                .claim("roles", user.getRoles().stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getAccessTokenTtl()))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("id", user.getId())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getRefreshTokenTtl()))
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {

        if (!isValid(refreshToken)) {
            throw new RuntimeException();
        }

        Long userId = getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);

        return new JwtResponse(userId, user.getUsername(), createAccessToken(user), createRefreshToken(user));
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserNameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserNameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).get("id", Long.class);
    }

    public List<String> getUserRolesFromToken(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    public boolean isValid(String token) {
        return getAllClaimsFromToken(token).getExpiration().after(new Date());
    }

}
