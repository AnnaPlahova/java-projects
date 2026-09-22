package com.example.ticTacToe.datasource.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "leaderboard")
public class LeaderboardEntryDto {

    @Id
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "win_ratio", nullable = false)
    private double winRatio;

    // Пустой конструктор для JPA
    protected LeaderboardEntryDto() {}

    public LeaderboardEntryDto(UUID userId, String login, double winRatio) {
        this.userId = userId;
        this.login = login;
        this.winRatio = winRatio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public double getWinRatio() { return winRatio; }
    public void setWinRatio(double winRatio) { this.winRatio = winRatio; }
}
