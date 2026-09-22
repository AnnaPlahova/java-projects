package com.example.ticTacToe.domain.model;

import java.util.Objects;

public class RefreshJwtRequest {
    private String refreshToken;

    public RefreshJwtRequest() {}

    public RefreshJwtRequest(String refreshToken) {
        this.refreshToken = Objects.requireNonNull(refreshToken, "Refresh token cannot be null");
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
