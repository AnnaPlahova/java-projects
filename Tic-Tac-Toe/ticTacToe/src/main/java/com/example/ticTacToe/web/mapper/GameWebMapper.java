package com.example.ticTacToe.web.mapper;

import com.example.ticTacToe.domain.model.GameDto;
import com.example.ticTacToe.domain.model.GameFieldDto;
import com.example.ticTacToe.web.model.GameFieldWebDto;
import com.example.ticTacToe.web.model.GameWebDto;

/**
 * Конвертация между доменной моделью и веб‑DTO.
 */
public final class GameWebMapper {
    public static GameWebDto toWeb(GameDto domain) {
        GameFieldWebDto fieldWeb = GameFieldWebMapper.toWeb(domain.getField());
        return new GameWebDto(domain.getId(), fieldWeb, domain.getStatus(), domain.getPlayerX(),
                domain.getPlayerO(), domain.getCurrentPlayer(), domain.getCreatedAt(), domain.isVsBot());
    }

    public static GameDto toDomain(GameWebDto web) {
        GameFieldDto field = GameFieldWebMapper.toDomain(web.getField());
        return new GameDto(web.getId(), field, web.getStatus(), web.getPlayerX(),
                web.getPlayerO(), web.getCurrentPlayer(), web.getCreatedAt(), web.isVsBot());
    }
}
