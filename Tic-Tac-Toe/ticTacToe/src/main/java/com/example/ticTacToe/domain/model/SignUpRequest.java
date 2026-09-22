package com.example.ticTacToe.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

// Модель запроса на регистрацию
public class SignUpRequest {
    @NotBlank(message = "Login cannot be blank")
    @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // Конструкторы
    public SignUpRequest() {}

    public SignUpRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Геттеры
    public String getLogin() { return login; }
    public String getPassword() { return password; }

    // equals/hashCode/toString — для отладки
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignUpRequest)) return false;
        SignUpRequest that = (SignUpRequest) o;
        return login.equals(that.login) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "SignUpRequest{login='" + login + "', password='***'}";
    }
}
