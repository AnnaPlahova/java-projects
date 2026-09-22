package com.example.ticTacToe.domain.service;

import com.example.ticTacToe.datasource.mapper.GameBaseMapper;
import com.example.ticTacToe.datasource.model.GameBaseDto;
import com.example.ticTacToe.datasource.service.GameBaseService;
import com.example.ticTacToe.domain.exception.InvalidGameException;
import com.example.ticTacToe.domain.model.GameDto;
import com.example.ticTacToe.domain.model.GameFieldDto;
import com.example.ticTacToe.domain.model.GameStatus;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация бизнес‑логики.
 * В качестве репозитория используется простая ConcurrentHashMap
 */
public class GameLogicServiceImpl implements GameLogicService {
    private final GameBaseService gameBaseService;

    public GameLogicServiceImpl(GameBaseService gameBaseService) {
        this.gameBaseService = gameBaseService;
    }

    /**
     * Создаёт новую игру, сохраняет её и возвращает доменную модель
     */
    @Override
    public GameDto startNewGame(UUID playerId, boolean vsBot) {
        GameDto newGameDto = GameDto.newGame(vsBot);  // статус = WAITING_FOR_PLAYER, PlayerO = null
        newGameDto.setPlayerX(playerId);
        newGameDto.setCurrentPlayer(playerId); // X ходит первым
        if (vsBot) {
            newGameDto.setPlayerO(null); // бот — не пользователь
            newGameDto.setStatus(GameStatus.PLAYER_X_TURN); // X ходит первым
        } else {
            newGameDto.setPlayerO(null); // ждём второго игрока
            newGameDto.setStatus(GameStatus.WAITING_FOR_PLAYER); // ожидание игрока O
        }
        // конвертируем в модель для репозитория
        GameBaseDto gameBaseDto = GameBaseMapper.toBaseDto(newGameDto);
        // сохраняем в репозитории
        gameBaseService.saveGame(gameBaseDto);
        return newGameDto;
    }

    /**
     * Получает игру из базы данных и возвращает доменную модель
     */
    @Override
    public GameDto getGame(UUID gameId) {
        GameBaseDto gameBaseDto = gameBaseService.getGame(gameId);
        if (gameBaseDto == null) {
            throw new InvalidGameException("Game not found");
        }
        return GameBaseMapper.toDomain(gameBaseDto);
    }

    /**
     * Присоединяет игрока к существующей игре в качестве игрока O.
     * Метод проверяет, что игра существует, находится в статусе ожидания второго игрока
     * (WAITING_FOR_PLAYER), и что запрашивающий игрок еще не является игроком X.
     * Если все проверки пройдены — игрок O присоединяется, статус игры меняется на PLAYER_X_TURN
     * (так как X всегда ходит первым), и текущий игрок устанавливается как X.
     * После успешного присоединения игра сохраняется в хранилище и возвращается обновленная версия.
     *
     * @param gameId   UUID идентификатор игры, к которой присоединяются.
     * @param playerId UUID идентификатор игрока, присоединяющегося как O.
     * @return GameDto — обновленная сущность игры с присоединенным игроком O и изменённым статусом.
     * @throws InvalidGameException если игра не найдена, уже началась, или игрок уже является X.
     */
    @Override
    public GameDto joinGame(UUID gameId, UUID playerId) {
        GameDto game = getGame(gameId); // Получаем игру из базы данных

        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYER) {
            throw new InvalidGameException("Game is not waiting for player");
        }

        if (Objects.equals(game.getPlayerX(), playerId)) {
            throw new InvalidGameException("Player already joined as X");
        }

        game.setPlayerO(playerId);
        game.setStatus(GameStatus.PLAYER_X_TURN); // X ходит первым
        game.setCurrentPlayer(game.getPlayerX());

        gameBaseService.saveGame(GameBaseMapper.toBaseDto(game));
        return game;
    }

    /**
     * Обрабатывает ход пользователя, генерирует ход компьютера, сохраняет и возвращает результат
     */
    @Override
    public GameDto processUserMove(UUID gameId, GameFieldDto requestFieldDto, UUID playerId) {
        // Получаем игру из базы данных
        GameDto game = getGame(gameId);

        // Проверка: ходит ли этот игрок?
        if (!Objects.equals(game.getCurrentPlayer(), playerId)) {
            throw new InvalidGameException("It's not your turn");
        }
        // Проверка: игра уже завершена?
        if (game.getStatus() != GameStatus.PLAYER_X_TURN &&
                game.getStatus() != GameStatus.PLAYER_O_TURN) {
            throw new InvalidGameException("Game is not ongoing");
        }

        // Проверяем корректность хода
        if (!validateUserMove(game, new GameDto(gameId, requestFieldDto, game.getStatus(),
                game.getPlayerX(), game.getPlayerO(), game.getCurrentPlayer(), game.getCreatedAt(), game.isVsBot()))) {
            throw new InvalidGameException("Invalid game state: move or game state is not allowed");
        }

        // Обновляем поле
        GameDto updatedGame = new GameDto(gameId, requestFieldDto, game.getStatus(),
                game.getPlayerX(), game.getPlayerO(), game.getCurrentPlayer(), game.getCreatedAt(), game.isVsBot());

        // Обновляем статус после хода пользователя
        GameStatus newStatus = checkGameStatus(requestFieldDto);
        if (newStatus == GameStatus.X_WON || newStatus == GameStatus.O_WON || newStatus == GameStatus.DRAW) {
            updatedGame.setStatus(newStatus);
            updatedGame.setCurrentPlayer(null); // Игра окончена — ходов нет
        } else {
            // Игра продолжается: меняем текущего игрока
            UUID currentPlayer = game.getCurrentPlayer();
            UUID nextPlayer = currentPlayer.equals(game.getPlayerX()) ? game.getPlayerO() : game.getPlayerX();

            updatedGame.setCurrentPlayer(nextPlayer);

            updatedGame.setStatus(game.getPlayerX().equals(nextPlayer) ? GameStatus.PLAYER_X_TURN : GameStatus.PLAYER_O_TURN);
        }

        // Если игра с ботом и сейчас ход бота — бот делает ход
        if (updatedGame.getStatus() == GameStatus.PLAYER_O_TURN && updatedGame.getPlayerO() == null) {
            updatedGame = makeComputerMove(updatedGame);
        }

        // Сохраняем обновлённую игру
        gameBaseService.saveGame(GameBaseMapper.toBaseDto(updatedGame));
        return updatedGame;
    }

    @Override
    public GameDto makeComputerMove(GameDto game) {

        // Вычисляем лучший ход бота с помощью Минимакс
        GameFieldDto newField = findBestMove(game.getField());

        // Проверяем статус после хода бота
        GameStatus newStatus = checkGameStatus(newField);
        newStatus = newStatus != null ? newStatus : GameStatus.PLAYER_X_TURN;

        // Определить, кто ходит следующим — только если игра не завершена
        UUID nextPlayer;
        if (newStatus == GameStatus.PLAYER_X_TURN) {
            // Игра продолжается: после хода бота (O) — ходит X
            nextPlayer = game.getPlayerX();
        } else {
            // Игра завершена: победа/ничья — следующего хода нет
            nextPlayer = null;
        }

        // Возвращаем игру с обновлённым полем, статусом и следующим игроком
        return new GameDto(game.getId(), newField, newStatus, game.getPlayerX(), game.getPlayerO(),
                nextPlayer, game.getCreatedAt(), game.isVsBot());
    }

    /**
     * проверяет корректность хода пользователя
     *
     * @param previousGame игра до пользовательского хода
     * @param currentGame  игра после пользовательского хода
     */
    @Override
    public boolean validateUserMove(GameDto previousGame, GameDto currentGame) {
        // Проверяем, что игра не закончена
        GameStatus status = previousGame.getStatus();
        if (status != GameStatus.PLAYER_X_TURN && status != GameStatus.PLAYER_O_TURN) {
            return false;
        }
        int[][] prev = previousGame.getField().getCells();
        int[][] curr = currentGame.getField().getCells();
        // Проверяем, что все клетки, которые уже заняты, не изменились
        for (int r = 0; r < GameFieldDto.SIZE; r++) {
            for (int c = 0; c < GameFieldDto.SIZE; c++) {
                if (prev[r][c] != 0 && prev[r][c] != curr[r][c]) {
                    return false; // прежняя клетка была изменена
                }
            }
        }
        // Проверяем, что ровно одна новая клетка (игрок сделал один ход)
        int added = 0;
        int addedValue = 0;
        for (int r = 0; r < GameFieldDto.SIZE; r++) {
            for (int c = 0; c < GameFieldDto.SIZE; c++) {
                if (prev[r][c] == 0 && curr[r][c] != 0) {
                    added++;
                    if (added > 1) return false;
                    addedValue = curr[r][c];
                }
            }
        }
        // Проверка: ход должен быть от текущего игрока (X=1, O=2)
        int expectedValue = (status == GameStatus.PLAYER_X_TURN) ? 1 : 2;
        if (addedValue != expectedValue) {
            return false;
        }

        return true;
    }

    /**
     * Определяет конец игры (победа, ничья, продолжается).
     * Если игра завершена, возвращает статус игры. Если игра продолжается, возвращает null.
     *
     * @param gameFieldDto поле, которое нужно проверить.
     */
    @Override
    public GameStatus checkGameStatus(GameFieldDto gameFieldDto) {
        int[][] field = gameFieldDto.getCells();
        // Проверяем строки, столбцы и диагонали
        // Строки
        for (int r = 0; r < GameFieldDto.SIZE; r++) {
            if (field[r][0] != 0 && field[r][0] == field[r][1] && field[r][1] == field[r][2]) {
                return field[r][0] == 1 ? GameStatus.X_WON : GameStatus.O_WON;
            }
        }
        // Столбцы
        for (int c = 0; c < GameFieldDto.SIZE; c++) {
            if (field[0][c] != 0 && field[0][c] == field[1][c] && field[1][c] == field[2][c]) {
                return field[0][c] == 1 ? GameStatus.X_WON : GameStatus.O_WON;
            }
        }
        // Диагонали
        if (field[0][0] != 0 && field[0][0] == field[1][1] && field[1][1] == field[2][2]) {
            return field[0][0] == 1 ? GameStatus.X_WON : GameStatus.O_WON;
        } else if (field[0][2] != 0 && field[0][2] == field[1][1] && field[1][1] == field[2][0]) {
            return field[0][2] == 1 ? GameStatus.X_WON : GameStatus.O_WON;
        }
        // Проверяем наличие пустых клеток (если нет – ничья)
        for (int r = 0; r < GameFieldDto.SIZE; r++) {
            for (int c = 0; c < GameFieldDto.SIZE; c++) {
                if (field[r][c] == 0) {
                    return null; // // Игра продолжается — нет победы, нет ничьей
                }
            }
        }
        return GameStatus.DRAW;
    }

    public GameFieldDto findBestMove(GameFieldDto currentField) {
        int[][] board = currentField.getCells();
        // Проверить, есть ли выигрышный ход для компьютера (O = 2)
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == 0) {
                    board[r][c] = 2; // Попробуем поставить O
                    GameStatus status = checkGameStatus(new GameFieldDto(board));
                    if (status == GameStatus.O_WON) {
                        // Найден выигрышный ход — немедленно делаем его
                        GameFieldDto result = new GameFieldDto(board);
                        board[r][c] = 0; // Восстанавливаем доску
                        return result;
                    }
                    board[r][c] = 0; // Отменяем ход
                }
            }
        }
        // Проверить, нужно ли блокировать выигрыш игрока (X = 1)
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == 0) {
                    board[r][c] = 1; // Попробуем поставить X
                    GameStatus status = checkGameStatus(new GameFieldDto(board));
                    if (status == GameStatus.X_WON) {
                        // Игрок выиграет, если не заблокировать — блокируем
                        board[r][c] = 2; // Ставим O вместо X
                        GameFieldDto result = new GameFieldDto(board);
                        board[r][c] = 1; // Восстанавливаем доску
                        return result;
                    }
                    board[r][c] = 0; // Отменяем ход
                }
            }
        }
        // Только если нет немедленных выигрышей/блокировок — запускаем минимакс
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1}; // Пока не найден лучший ход

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == 0) { // Если клетка пуста
                    board[r][c] = 2;    // Попробуем поставить O (ход бота 2 = "O")
                    boolean isMaximizing = false; // Мы только что сделали ход за O (максимайзера), теперь следующий ход — за X (минимайзер).
                    int score = minimax(board, isMaximizing); // Оцениваем результат
                    board[r][c] = 0;    // Отменяем ход
                    // Если этот ход лучше предыдущих — запоминаем его
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{r, c}; // Запоминаем координаты
                    }
                }
            }
        }
        GameFieldDto newField = new GameFieldDto(board);
        newField.setCell(bestMove[0], bestMove[1], 2); // 0 = 2 - ход компьютера
        return newField;
    }

    private int minimax(int[][] board, boolean isMaximizing) {
        GameStatus status = checkGameStatus(new GameFieldDto(board));
        if (status == GameStatus.O_WON) return 1;
        if (status == GameStatus.X_WON) return -1;
        if (status == GameStatus.DRAW) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == 0) {
                        board[r][c] = 2;
                        int score = minimax(board, false);
                        board[r][c] = 0;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == 0) {
                        board[r][c] = 1;
                        int score = minimax(board, true);
                        board[r][c] = 0;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    @Override
    public List<GameDto> getActiveGames() {
        return gameBaseService.getAllGames().stream()
                .map(GameBaseMapper::toDomain)
                .filter(game -> game.getStatus() == GameStatus.WAITING_FOR_PLAYER ||
                        game.getStatus() == GameStatus.PLAYER_X_TURN ||
                        game.getStatus() == GameStatus.PLAYER_O_TURN)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDto> getCompletedGamesByPlayer(UUID playerId) {
        return gameBaseService.getCompletedGamesByPlayer(playerId)
                .stream()
                .map(GameBaseMapper::toDomain)
                .toList();
    }

}
