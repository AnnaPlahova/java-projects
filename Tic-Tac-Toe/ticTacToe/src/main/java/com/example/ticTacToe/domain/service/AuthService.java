package com.example.ticTacToe.domain.service;

import com.example.ticTacToe.domain.model.*;
import com.example.ticTacToe.domain.security.JwtProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

// Обёртка над UserService для работы с HTTP-заголовком
// - декодирование логина и пароля и передача в UserService
@Service
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Регистрация: принимает SignUpRequest, возвращает true, если успех
     */
    public boolean register(SignUpRequest request) {
        return userService.register(request);
    }

    /**
     * Авторизация пользователя по JwtRequest, возвращает JwtResponse с токенами
     */
    public JwtResponse authenticate(JwtRequest request) {
        // Аутентифицируем пользователя через UserService
        UUID userId = userService.authenticate(request.getLogin(), request.getPassword());
        if (userId == null) {
            throw new RuntimeException("Invalid credentials");
        }

        // Получаем пользователя
        Users user = userService.getUserById(userId);

        // Генерируем токены
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);
    }

    /**
     * Обновление accessToken по refreshToken
     */
    public JwtResponse refreshAccessToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Получаем UUID из refreshToken
        Claims claims = jwtProvider.getClaims(refreshToken);
        UUID userId = UUID.fromString(claims.get("uuid", String.class));

        // Получаем пользователя
        Users user = userService.getUserById(userId);

        // Генерируем новый accessToken (refreshToken остается тем же)
        String newAccessToken = jwtProvider.generateAccessToken(user);

        return new JwtResponse(newAccessToken, refreshToken);
    }

    /**
     * Обновление refreshToken по refreshToken
     */
    public JwtResponse refreshRefreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Получаем UUID из refreshToken
        Claims claims = jwtProvider.getClaims(refreshToken);
        UUID userId = UUID.fromString(claims.get("uuid", String.class));

        // Получаем пользователя
        Users user = userService.getUserById(userId);

        // Генерируем новые оба токена
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        return new JwtResponse(newAccessToken, newRefreshToken);
    }

    /**
     * Получение JwtAuthentication из SecurityContextHolder,
     * который хранит информацию о пользователе и его ролях
     */
    public JwtAuthentication getJwtAuthentication() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}