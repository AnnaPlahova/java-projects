package com.example.ticTacToe.datasource.service;

import com.example.ticTacToe.datasource.model.GameBaseDto;

import java.util.List;
import java.util.UUID;

/**
 * Сервис, который работает с репозиторием.
 */
public interface GameBaseService {
    /**
     * Сохраняет текущую игру.
     */
    void saveGame(GameBaseDto gameDto);

    /**
     * Получает игру по UUID.
     */
    GameBaseDto getGame(UUID id);

    /**
     * Получает все игры из репозитория.
     */
    List<GameBaseDto> getAllGames();

    /**
     * Получает все завершенные игры пользователя из репозитория.
     * Игра считается завершенной, если у нее одно из состояний:     *
     * Победа игрока с UUID;
     * Ничья.
     */
    List<GameBaseDto> getCompletedGamesByPlayer(UUID playerId);

}

