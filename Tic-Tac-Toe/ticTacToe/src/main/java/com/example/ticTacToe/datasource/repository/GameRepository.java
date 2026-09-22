package com.example.ticTacToe.datasource.repository;

import com.example.ticTacToe.datasource.model.GameBaseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameBaseDto, UUID> {

    @Query("SELECT g FROM GameBaseDto g WHERE " +
            "(g.status = 'X_WON' AND g.playerX = :playerId) OR " +
            "(g.status = 'O_WON' AND g.playerO = :playerId) OR " +
            "(g.status = 'DRAW' AND (g.playerX = :playerId OR g.playerO = :playerId)) " +
            "ORDER BY g.createdAt ASC")
    List<GameBaseDto> findCompletedGamesByPlayerId(@Param("playerId") UUID playerId);
}