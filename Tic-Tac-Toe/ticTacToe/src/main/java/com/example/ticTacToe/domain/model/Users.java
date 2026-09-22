package com.example.ticTacToe.domain.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Users {

    private UUID id;
    private String login;
    private LocalDateTime createdAt;
    private final List<Role> roles;

    public Users(UUID id, String login, LocalDateTime createdAt, List<Role> roles) {
        this.id = Objects.requireNonNull(id);
        this.login = Objects.requireNonNull(login);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.roles = Objects.requireNonNull(roles);
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Role> getRoles() { return roles; }

    // метод для Spring Security
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;
        Users users = (Users) o;
        return id.equals(users.id) && login.equals(users.login);
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

