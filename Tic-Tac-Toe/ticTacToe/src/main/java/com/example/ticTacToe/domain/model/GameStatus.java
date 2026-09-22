package com.example.ticTacToe.domain.model;

public enum GameStatus {
    WAITING_FOR_PLAYER, // Ждём второго игрока
    PLAYER_X_TURN,       // Ходит X
    PLAYER_O_TURN,       // Ходит O
    X_WON,
    O_WON,
    DRAW
}
