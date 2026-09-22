package com.example.ticTacToe.datasource.model;

import com.example.ticTacToe.domain.model.GameStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * JPA-сущность для хранения игры в базе данных.
 * Соответствует таблице `games`.
 */
@Entity
@Table(name = "games")
public class GameBaseDto {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    // Храним поле как строку из 9 символов: "000000000", "102010200" и т.д.
    @Column(name = "field", length = 9, nullable = false)
    private String field;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GameStatus status;

    // ID игрока за X (может быть null, пока не присоединился)
    @Column(name = "player_x_id")
    private UUID playerX;

    // ID игрока за O (может быть null)
    @Column(name = "player_o_id")
    private UUID playerO;

    // ID текущего игрока, который должен ходить (может быть null в статусе WAITING)
    @Column(name = "current_player_id")
    private UUID currentPlayer;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "vs_bot", nullable = false)
    private boolean vsBot;

    // JPA требует пустого конструктора
    protected GameBaseDto(){}

    // Конструктор для создания новой игры
    public GameBaseDto(UUID id, String field, boolean vsBot) {
        this.id = Objects.requireNonNull(id);
        this.field = Objects.requireNonNull(field);
        this.status = GameStatus.WAITING_FOR_PLAYER;
        this.playerX = null;
        this.playerO = null;
        this.currentPlayer = null;
        this.vsBot = vsBot;
    }

    // Конструктор для полной инициализации
    public GameBaseDto(UUID id, String field, GameStatus status, UUID playerX, UUID playerO,
                       UUID currentPlayer, LocalDateTime createdAt, boolean vsBot) {
        this.id = Objects.requireNonNull(id);
        this.field = Objects.requireNonNull(field);
        this.status = Objects.requireNonNull(status);
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentPlayer = currentPlayer;
        this.createdAt = createdAt;
        this.vsBot = vsBot;
    }

    // Геттеры

    public UUID getId() {
        return id;
    }

    public String getField() {
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

    public boolean isVsBot() { return vsBot; }

    // Сеттеры (для JPA)

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setField(String field) {
        this.field = field;
    }

    protected void setPlayerX(UUID playerX) {
        this.playerX = playerX;
    }

    protected void setPlayerO(UUID playerO) {
        this.playerO = playerO;
    }

    protected void setCurrentPlayer(UUID currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    protected void setVsBot(boolean vsBot) { this.vsBot = vsBot; }

    public static GameBaseDto newGame(boolean vsBot) {
        return new GameBaseDto(UUID.randomUUID(), "000000000", vsBot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameBaseDto)) return false;
        GameBaseDto that = (GameBaseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(field, that.field) &&
                status == that.status &&
                Objects.equals(playerX, that.playerX) &&
                Objects.equals(playerO, that.playerO) &&
                Objects.equals(currentPlayer, that.currentPlayer) &&
                vsBot == that.vsBot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, field, status, playerX, playerO, currentPlayer, vsBot);
    }

    @Override
    public String toString() {
        return "GameBaseDto{" +
                "id=" + id +
                ", field='" + field + '\'' +
                ", status=" + status +
                ", playerX=" + playerX +
                ", playerO=" + playerO +
                ", currentPlayer=" + currentPlayer +
                ", createdAt=" + createdAt +
                ", vsBot=" + vsBot +
                '}';
    }

}
