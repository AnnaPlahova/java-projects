package com.example.ticTacToe.web.controller;

import com.example.ticTacToe.domain.model.Users;
import com.example.ticTacToe.domain.service.UserService;
import com.example.ticTacToe.web.mapper.UserMapper;
import com.example.ticTacToe.web.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

// HTTP-эндпойнты
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper; // Внедряем маппер

    /**
     * Получить информацию о пользователе по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        Users user = userService.getUserById(id);
        UserResponse response = userMapper.toResponse(user);
        // ответ с кодом 200 OK
        return ResponseEntity.ok(response);
    }
}
