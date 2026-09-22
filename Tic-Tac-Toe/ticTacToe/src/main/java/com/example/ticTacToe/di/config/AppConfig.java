package com.example.ticTacToe.di.config;

import com.example.ticTacToe.datasource.service.GameBaseService;
import com.example.ticTacToe.domain.service.GameLogicService;
import com.example.ticTacToe.domain.service.GameLogicServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration, описывает граф зависимостей.
 * <p>
 * Внутри создаются:
 * 1. Сервис (GameLogicService) – бизнес‑логика, использует GameBaseService
 */
@Configuration
public class AppConfig {
    /**
     * Сервис бизнес‑логики. Внедряется GameBaseService через конструктор.
     */
    @Bean
    public GameLogicService gameLogicService(GameBaseService gameBaseService) {
        return new GameLogicServiceImpl(gameBaseService);
    }
}
