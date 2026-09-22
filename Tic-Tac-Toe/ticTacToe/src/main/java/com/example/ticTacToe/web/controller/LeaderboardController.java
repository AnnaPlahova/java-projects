package com.example.ticTacToe.web.controller;

import com.example.ticTacToe.datasource.model.LeaderboardEntryDto;
import com.example.ticTacToe.domain.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/top/{limit}")
    public ResponseEntity<List<LeaderboardEntryDto>> getTopPlayers(
            @PathVariable int limit) {
        List<LeaderboardEntryDto> topPlayers = leaderboardService.getTopPlayers(limit);
        return ResponseEntity.ok(topPlayers);
    }
}
