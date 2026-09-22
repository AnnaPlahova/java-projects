package com.example.ticTacToe.datasource.service;

import com.example.ticTacToe.datasource.model.GameBaseDto;
import com.example.ticTacToe.datasource.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация сервиса, использующая репозиторий.
 */
// Аннотация @Service делает класс автоматически обнаруживаемым Spring‑контейнером
@Service
public class GameBaseServiceImpl implements GameBaseService {

    private final GameRepository gameRepository;

    public GameBaseServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void saveGame(GameBaseDto gameDto) {
        gameRepository.save(gameDto);
    }

    @Override
    public GameBaseDto getGame(UUID id) {
        Optional<GameBaseDto> optionalGameBaseDto = gameRepository.findById(id);
        return optionalGameBaseDto.orElse(null);
    }

    @Override
    public List<GameBaseDto> getAllGames() {
        List<GameBaseDto> allGames = (List<GameBaseDto>) gameRepository.findAll();
        return allGames;
    }

    @Override
    public List<GameBaseDto> getCompletedGamesByPlayer(UUID playerId) {
        return gameRepository.findCompletedGamesByPlayerId(playerId);
    }

}
