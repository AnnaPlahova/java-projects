package com.example.ticTacToe.web.model;

import com.example.ticTacToe.domain.model.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Информация о пользователе для клиента
public class UserResponse {
    private UUID id;
    private String login;
    private LocalDateTime createdAt;
    private final List<Role> roles;

    // Конструктор — для создания ответа
    public UserResponse(UUID id, String login, LocalDateTime createdAt, List<Role> roles) {
        this.id = id;
        this.login = login;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    // Геттеры — для JSON
    public UUID getId() { return id; }
    public String getLogin() { return login; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Role> getRoles() { return roles; }

}
