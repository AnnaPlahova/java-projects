package com.example.ticTacToe.domain.security;

import com.example.ticTacToe.domain.model.JwtAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {
    public JwtAuthentication createAuthentication(Claims claims) {
        // 1. Извлекаем UUID из claims (поле "uuid")
        UUID userId = UUID.fromString(claims.get("uuid", String.class));

        // 2. Извлекаем роли как List<Map> (так как JWT сохраняет GrantedAuthority как объекты)
        @SuppressWarnings("unchecked")
        List<Map<String, String>> rolesList = claims.get("role", List.class);

        // 3. Преобразуем в Collection<GrantedAuthority>
        Collection<? extends GrantedAuthority> authorities = rolesList.stream()
                .map(roleMap -> new SimpleGrantedAuthority(roleMap.get("authority")))
                .toList();

        return new JwtAuthentication(userId, authorities);
    }
}
