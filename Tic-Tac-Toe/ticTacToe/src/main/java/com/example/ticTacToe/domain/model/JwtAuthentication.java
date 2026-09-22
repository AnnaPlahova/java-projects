package com.example.ticTacToe.domain.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class JwtAuthentication implements Authentication {
    private final UUID principal; // UUID пользователя
    private final Collection<? extends GrantedAuthority> authorities; // Роли пользователя
    private boolean authenticated = true; // Флаг авторизации true, т.к. валидация токена прошла ранее

    public JwtAuthentication(UUID principal, Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public UUID getPrincipal() {
        return principal; // Возвращает UUID пользователя
    }

    @Override
    public String getName() {
        return principal.toString(); // Возвращает UUID как строку
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }
}
