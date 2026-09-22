package com.example.ticTacToe.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Представляет одну игру.
 * UUID – уникальный идентификатор, поле – текущий статус.
 */
public final class GameDto {
    private final UUID id;
    private final GameFieldDto field;
    private GameStatus status;
    private UUID playerX; // кто играет за X
    private UUID playerO; // кто играет за O
    private UUID currentPlayer; // кто сейчас ходит
    private LocalDateTime createdAt;
    private boolean vsBot;

    // Конструктор для новой игры (без игроков — ожидает присоединения)
    public GameDto(UUID id, GameFieldDto field, LocalDateTime createdAt, boolean vsBot) {
        this.id = Objects.requireNonNull(id);
        this.field = Objects.requireNonNull(field);
        this.status = GameStatus.WAITING_FOR_PLAYER;
        this.playerX = null;
        this.playerO = null;
        this.currentPlayer = null; // пока никто не ходит
        this.createdAt = createdAt;
        this.vsBot = vsBot;
    }

    // Конструктор с игроками
    public GameDto(UUID id, GameFieldDto field, GameStatus status, UUID playerX, UUID playerO,
                   UUID currentPlayer, LocalDateTime createdAt, boolean vsBot) {
        this.id = Objects.requireNonNull(id);
        this.field = Objects.requireNonNull(field);
        this.status = Objects.requireNonNull(status);
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = currentPlayer; // пока никто не ходит
        this.createdAt = createdAt;
        this.vsBot = vsBot;
    }

    // Геттеры
    public UUID getId() {
        return id;
    }

    public GameFieldDto getField() {
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

    public boolean isVsBot () {
        return vsBot;
    }

    // Сеттеры (для обновления)
    public void setStatus(GameStatus status) { this.status = status; }
    public void setPlayerX(UUID playerX) { this.playerX = playerX; }
    public void setPlayerO(UUID playerO) { this.playerO = playerO; }
    public void setCurrentPlayer(UUID currentPlayer) { this.currentPlayer = currentPlayer; }
    public void setVsBot(boolean vsBot) { this.vsBot = vsBot; }

    /**
     * Создаёт новую игру с пустым полем
     */
    public static GameDto newGame(boolean vsBot) {
        return new GameDto(UUID.randomUUID(), new GameFieldDto(), LocalDateTime.now(), vsBot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameDto)) return false;
        GameDto gameDto = (GameDto) o;
        return id.equals(gameDto.id) &&
                field.equals(gameDto.field) &&
                status == gameDto.status &&
                Objects.equals(playerX, gameDto.playerX) &&
                Objects.equals(playerO, gameDto.playerO) &&
                Objects.equals(currentPlayer, gameDto.currentPlayer) &&
                vsBot == gameDto.vsBot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, field, status, playerX, playerO, currentPlayer, vsBot);
    }

    @Override
    public String toString() {
        return "Game{id=" + id +
                ", field=" + field +
                ", status=" + status +
                ", playerX=" + playerX +
                ", playerO=" + playerO +
                ", currentPlayer=" + currentPlayer +
                ", vsBot=" + vsBot +
                '}';
    }
}
