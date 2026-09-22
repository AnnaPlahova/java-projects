package com.example.ticTacToe.web.model;

import com.example.ticTacToe.domain.model.GameStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO, который отправляется клиенту/получается от него.
 * Полностью соответствует доменной модели GameDto.
 */
public final class GameWebDto {
    private final UUID id;
    private final GameFieldWebDto field;
    private final GameStatus status;
    private final UUID playerX;
    private final UUID playerO;
    private final UUID currentPlayer;
    private final LocalDateTime createdAt;
    private final boolean vsBot;

    @JsonCreator // Указывает Jackson: использовать этот конструктор для десериализации
    public GameWebDto(@JsonProperty("id") UUID id,
                      @JsonProperty("field") GameFieldWebDto field,
                      @JsonProperty("status") GameStatus status,
                      @JsonProperty("playerX") UUID playerX,
                      @JsonProperty("playerO") UUID playerO,
                      @JsonProperty("currentPlayer") UUID currentPlayer,
                      @JsonProperty("createdAt") LocalDateTime createdAt,
                      @JsonProperty("vsBot") boolean vsBot) {
        this.id = Objects.requireNonNull(id);
        this.field = Objects.requireNonNull(field);
        this.status = Objects.requireNonNull(status);
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = currentPlayer;
        this.createdAt = createdAt;
        this.vsBot = vsBot;
    }

    public UUID getId() {
        return id;
    }

    public GameFieldWebDto getField() {
        return field;
    }

    public GameStatus getStatus() {
        return status;
    }

    public UUID getPlayerX() {
        return playerX;
    }

    public UUID getPlayerO() {
        return playerO;
    }

    public UUID getCurrentPlayer() {
        return currentPlayer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isVsBot() {
        return vsBot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameWebDto)) return false;
        GameWebDto game = (GameWebDto) o;
        return Objects.equals(id, game.id) &&
                Objects.equals(field, game.field) &&
                status == game.status &&
                Objects.equals(playerX, game.playerX) &&
                Objects.equals(playerO, game.playerO) &&
                Objects.equals(currentPlayer, game.currentPlayer) &&
                vsBot == game.vsBot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, field, status, playerX, playerO, currentPlayer, vsBot);
    }

    @Override
    public String toString() {
        return "GameWebDto{" +
                "id=" + id +
                ", field=" + field +
                ", status=" + status +
                ", playerX=" + playerX +
                ", playerO=" + playerO +
                ", currentPlayer=" + currentPlayer +
                ", vsBot=" + vsBot +
                '}';
    }
}
