package com.example.ticTacToe.web.controller;

import com.example.ticTacToe.domain.model.GameDto;
import com.example.ticTacToe.domain.model.GameFieldDto;
import com.example.ticTacToe.domain.service.GameLogicService;
import com.example.ticTacToe.web.mapper.GameWebMapper;
import com.example.ticTacToe.web.model.GameFieldWebDto;
import com.example.ticTacToe.web.model.GameWebDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST‑контроллер для работы с игрой.
 * <p>
 * POST /game/{id}
 * - тело запроса содержит обновленное поле от пользователя (его ход).
 * - контроллер проверяет корректность хода, затем запрашивает у доменного сервиса
 * компьютерный ход, обновляет поле и возвращает результат.
 */
@RestController
@RequestMapping("/game")
public class GameController {

    private final GameLogicService gameLogicService;

    public GameController(GameLogicService gameLogicService) {
        this.gameLogicService = gameLogicService;
    }

    /**
     * Генерирует новую игру (с ботом или с человеком).
     */
    @PostMapping("/start")
    public ResponseEntity<GameWebDto> startGame(@AuthenticationPrincipal UUID playerId,
                                                @RequestParam(defaultValue = "false") boolean vsBot) {
        GameDto newGameDto = gameLogicService.startNewGame(playerId, vsBot);
        GameWebDto responseWebDto = GameWebMapper.toWeb(newGameDto); // конвертируем в web модель
        return ResponseEntity.ok(responseWebDto);
    }

    /**
     * Присоединиться к игре (как игрок O).
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<GameWebDto> joinGame(@PathVariable UUID id,
                                               @AuthenticationPrincipal UUID playerId) {
        GameDto game = gameLogicService.joinGame(id, playerId);
        GameWebDto gameWebDto = GameWebMapper.toWeb(game);
        return ResponseEntity.ok(gameWebDto);
    }

    /**
     * Обрабатывает ход пользователя и генерирует ответ компьютерного хода или передает ход следующему пользователю.
     */
    @PostMapping("/{id}/move")
    public ResponseEntity<GameWebDto> makeMove(@PathVariable UUID id,
                                               @Valid @RequestBody GameFieldWebDto requestFieldWebDto,
                                               @AuthenticationPrincipal UUID playerId) {
        GameFieldDto requestFieldDto = new GameFieldDto(requestFieldWebDto.getField());
        GameDto updatedGameDto = gameLogicService.processUserMove(id, requestFieldDto, playerId);
        GameWebDto updatedGameWebDto = GameWebMapper.toWeb(updatedGameDto);
        return ResponseEntity.ok(updatedGameWebDto);
    }

    /**
     * Получить текущую игру по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameWebDto> getGame(@PathVariable UUID id) {
        GameDto game = gameLogicService.getGame(id);
        GameWebDto gameWebDto = GameWebMapper.toWeb(game);
        return ResponseEntity.ok(gameWebDto);
    }

    /**
     * Получить список активных игр (ожидание или в процессе).
     */
    @GetMapping("/list")
    public ResponseEntity<List<GameWebDto>> getActiveGames() {
        List<GameDto> activeGames = gameLogicService.getActiveGames();
        List<GameWebDto> webDtos = activeGames.stream()
                .map(GameWebMapper::toWeb)
                .toList();
        return ResponseEntity.ok(webDtos);
    }

    /**
     * Получить список завершенных игр (победа, ничья) пользователя.
     */
    @GetMapping("/history")
    public ResponseEntity<List<GameWebDto>> getGameHistory(@AuthenticationPrincipal UUID playerId) {
        List<GameDto> completedGames = gameLogicService.getCompletedGamesByPlayer(playerId);
        List<GameWebDto> response = completedGames.stream()
                .map(GameWebMapper::toWeb)
                .toList();
        return ResponseEntity.ok(response);
    }

}
