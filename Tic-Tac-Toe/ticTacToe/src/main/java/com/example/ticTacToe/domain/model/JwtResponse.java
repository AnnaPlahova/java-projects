package com.example.ticTacToe.domain.model;

import java.util.Objects;

public class JwtResponse {
    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;

    public JwtResponse() {}

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = Objects.requireNonNull(accessToken, "Access token cannot be null");
        this.refreshToken = Objects.requireNonNull(refreshToken, "Refresh token cannot be null");
    }

    public String getType() {
        return type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
