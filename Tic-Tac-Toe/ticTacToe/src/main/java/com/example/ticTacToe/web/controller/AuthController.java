package com.example.ticTacToe.web.controller;

import com.example.ticTacToe.domain.model.*;
import com.example.ticTacToe.domain.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// HTTP-эндпойнты
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignUpRequest request) {
        boolean success = authService.register(request);
        if (success) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists");
        }
    }

    // Авторизация пользователя
    // Возвращает JwtResponse с accessToken и refreshToken
    // В случаях ошибок возвращает String (сообщение об ошибке)
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtRequest request) {
        try {
            JwtResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login or password");
        }
    }

    // Обновление accessToken
    @PostMapping("/refresh/access")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshJwtRequest request) {
        try {
            JwtResponse response = authService.refreshAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    // Обновление refreshToken
    @PostMapping("/refresh/refresh")
    public ResponseEntity<?> refreshRefreshToken(@Valid @RequestBody RefreshJwtRequest request) {
        try {
            JwtResponse response = authService.refreshRefreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    // Получение информации о пользователе по accessToken
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            JwtAuthentication auth = authService.getJwtAuthentication();
            return ResponseEntity.ok(auth);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: Invalid or missing access token");
        }
    }

}
