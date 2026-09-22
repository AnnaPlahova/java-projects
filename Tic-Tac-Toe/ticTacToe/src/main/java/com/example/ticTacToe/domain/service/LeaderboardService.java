package com.example.ticTacToe.domain.service;

import com.example.ticTacToe.datasource.model.LeaderboardEntryDto;
import com.example.ticTacToe.datasource.repository.LeaderboardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;

    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public List<LeaderboardEntryDto> getTopPlayers(int limit) {
        // Очищаем таблицу leaderboard
        leaderboardRepository.deleteAll();

        // Получаем топ-игроков из БД (нативный SQL)
        List<Object[]> results = leaderboardRepository.findTopPlayers(limit);

        // Сохраняем результаты в таблицу leaderboard
        List<LeaderboardEntryDto> entries = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Object[] row = results.get(i);
            UUID userId = UUID.fromString(row[0].toString());
            String login = (String) row[1];
            double winRatio = (Double) row[2];

            LeaderboardEntryDto entry = new LeaderboardEntryDto(userId, login, winRatio);
            entry.setId((long) (i + 1));
            entries.add(entry);
        }

        // Сохраняем все записи
        leaderboardRepository.saveAll(entries);

        // Возвращаем сохранённые записи как список
        return new ArrayList<>((List<LeaderboardEntryDto>) leaderboardRepository.findAll());
    }
}