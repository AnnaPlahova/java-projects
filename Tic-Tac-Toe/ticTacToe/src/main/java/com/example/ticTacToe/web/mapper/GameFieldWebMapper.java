package com.example.ticTacToe.web.mapper;

import com.example.ticTacToe.domain.model.GameFieldDto;
import com.example.ticTacToe.web.model.GameFieldWebDto;

/**
 * Конвертация между доменной моделью и веб‑DTO.
 */
public final class GameFieldWebMapper {
    public static GameFieldWebDto toWeb(GameFieldDto gameFieldDto) {
        return new GameFieldWebDto(gameFieldDto.getCells());
    }

    public static GameFieldDto toDomain(GameFieldWebDto web) {
        return new GameFieldDto(web.getField());
    }
}
