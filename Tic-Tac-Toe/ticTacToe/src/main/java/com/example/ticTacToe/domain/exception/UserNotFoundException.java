package com.example.ticTacToe.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("Пользователь с ID " + userId + " не найден");
    }
}