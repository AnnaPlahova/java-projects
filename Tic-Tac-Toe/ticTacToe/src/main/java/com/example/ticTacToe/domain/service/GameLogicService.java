package com.example.ticTacToe.domain.service;

import com.example.ticTacToe.domain.model.GameDto;
import com.example.ticTacToe.domain.model.GameFieldDto;
import com.example.ticTacToe.domain.model.GameStatus;

import java.util.List;
import java.util.UUID;

/**
 * Описание бизнес‑логики для игры.
 */
public interface GameLogicService {

    /**
     * Создаёт новую игру, сохраняет её и возвращает доменную модель
     */
    GameDto startNewGame(UUID playerId, boolean vsBot);

    /**
     * Получает игру из базы данных и возвращает доменную модель
     */
    GameDto getGame(UUID gameId);

    /**
     * Присоединяет игрока к существующей игре в качестве игрока O
     */
    GameDto joinGame(UUID gameId, UUID playerId);

    /**
     * Обрабатывает ход пользователя, генерирует ход компьютера, сохраняет и возвращает результат
     */
    GameDto processUserMove(UUID gameId, GameFieldDto requestFieldDto, UUID playerId);

    /**
     * Вычисляет ход компьютера по алгоритму «Минимакс» и возвращает обновлённую игру.
     *
     * @param gameDto текущая игра
     * @return игра с обновлённым полем (компьютер сделал ход)
     */
    GameDto makeComputerMove(GameDto gameDto);

    /**
     * Проверяет, что пользовательский ход не нарушает историю (т.е. не изменяет старые клетки).
     *
     * @param previousGameDto игра до пользовательского хода
     * @param currentGameDto игра после пользовательского хода
     * @return true, если ход корректный
     */
    boolean validateUserMove(GameDto previousGameDto, GameDto currentGameDto);

    /**
     * Определяет, завершена ли игра (победа, ничья, или продолжается).
     *
     * @param gameFieldDto поле, которое нужно проверить
     * @return статус игры
     */
    GameStatus checkGameStatus(GameFieldDto gameFieldDto);

    /**
     * Возвращает список активных игр (в процессе, без победителя).
     */
    List<GameDto> getActiveGames();

    /**
     * Возвращает список завершенных игр (победа, ничья) пользователя.
     */
    List<GameDto> getCompletedGamesByPlayer(UUID playerId);
}
