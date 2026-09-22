package com.example.ticTacToe.datasource.mapper;

import com.example.ticTacToe.datasource.model.GameBaseDto;
import com.example.ticTacToe.domain.model.GameDto;
import com.example.ticTacToe.domain.model.GameFieldDto;

/**
 * Конвертация между Domain‑моделью и DTO.
 */
public final class GameBaseMapper {

    public static GameBaseDto toBaseDto(GameDto gameDto) {
        String gameFieldBaseDto = GameFieldBaseMapper.toStringDto(gameDto.getField());
        return new GameBaseDto(gameDto.getId(), gameFieldBaseDto, gameDto.getStatus(), gameDto.getPlayerX(),
                gameDto.getPlayerO(), gameDto.getCurrentPlayer(), gameDto.getCreatedAt(), gameDto.isVsBot());
    }

    public static GameDto toDomain(GameBaseDto baseDto) {
        GameFieldDto field = GameFieldBaseMapper.toDomain(baseDto.getField());
        return new GameDto(baseDto.getId(), field, baseDto.getStatus(), baseDto.getPlayerX(), baseDto.getPlayerO(),
                baseDto.getCurrentPlayer(), baseDto.getCreatedAt(), baseDto.isVsBot());
    }
}
