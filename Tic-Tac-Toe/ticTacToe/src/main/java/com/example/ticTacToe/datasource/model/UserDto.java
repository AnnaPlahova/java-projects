package com.example.ticTacToe.datasource.model;

import com.example.ticTacToe.domain.model.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

// Сущность пользователя
@Entity
@Table(name = "users")
public class UserDto {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Добавлено: поле для ролей
    @ElementCollection(fetch = FetchType.EAGER)  // Создает отдельную таблицу для ролей
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",  // Имя таблицы для ролей
            joinColumns = @JoinColumn(name = "user_id")  // Внешний ключ к users.id
    )
    @Column(name = "role")  // Имя колонки для роли
    private Set<Role> roles;  // Множество ролей (коллекция), т.к. у одного пользователя может быть несколько ролей

    public UserDto() {
        this.roles = new HashSet<>(Collections.singleton(Role.USER));
    }

    public UserDto(UUID id, String login, String passwordHash, Set<Role> roles) {
        this.id = Objects.requireNonNull(id);
        this.login = Objects.requireNonNull(login);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.roles = new HashSet<>(Objects.requireNonNull(roles));
        // Гарантируем наличие хотя бы одной роли
        if (this.roles.isEmpty()) {
            this.roles.add(Role.USER);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id) && login.equals(userDto.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

    @Override
    public String toString() {
        return "UserDto{id=" + id + ", login='" + login + "'}";
    }
}
