package com.example.ticTacToe.domain.security;

import com.example.ticTacToe.domain.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "your-secret-key-here-your-secret-key-here-your-secret-key-here-your-secret-key-here".getBytes(StandardCharsets.UTF_8)
    );
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15 минут
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 дней

    // Генерация accessToken с UUID и ролями
    public String generateAccessToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getId().toString());
        claims.put("role", user.getAuthorities()); // Роли в claims

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Генерация refreshToken с UUID
    public String generateRefreshToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getId().toString());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Валидация accessToken - проверяет подпись и срок действия
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Валидация refreshToken (проверяем наличие UUID) - проверяет подпись, срок действия и наличие uuid
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.containsKey("uuid");
        } catch (Exception e) {
            return false;
        }
    }

    // Получение claims из токена - если  неверная подпись, формат или срок истек, выбросит исключение
    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "Token expired");
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}
