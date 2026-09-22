package com.example.ticTacToe.datasource.repository;

import com.example.ticTacToe.datasource.model.LeaderboardEntryDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardRepository extends CrudRepository<LeaderboardEntryDto, Long> {
    // метод для лидерборда (нативный SQL)
    @Query(
            value = "SELECT " +
                    "    u.id AS user_id, " +
                    "    u.login, " +
                    "    CASE " +
                    "        WHEN (SUM(stats.losses) + SUM(stats.draws)) = 0 THEN SUM(stats.wins) " +
                    "        ELSE CAST(SUM(stats.wins) AS DOUBLE PRECISION) / (SUM(stats.losses) + SUM(stats.draws)) " +
                    "    END AS win_ratio " +
                    "FROM ( " +
                    "    SELECT " +
                    "        player_x_id AS id, " +
                    "        COUNT(CASE WHEN status = 'X_WON' THEN 1 END) AS wins, " +
                    "        COUNT(CASE WHEN status = 'O_WON' THEN 1 END) AS losses, " +
                    "        COUNT(CASE WHEN status = 'DRAW' THEN 1 END) AS draws " +
                    "    FROM games WHERE player_x_id IS NOT NULL GROUP BY player_x_id " +
                    "    UNION ALL " +
                    "    SELECT " +
                    "        player_o_id AS id, " +
                    "        COUNT(CASE WHEN status = 'O_WON' THEN 1 END) AS wins, " +
                    "        COUNT(CASE WHEN status = 'X_WON' THEN 1 END) AS losses, " +
                    "        COUNT(CASE WHEN status = 'DRAW' THEN 1 END) AS draws " +
                    "    FROM games WHERE player_o_id IS NOT NULL GROUP BY player_o_id " +
                    ") AS stats " +
                    "JOIN users u ON u.id = stats.id " +
                    "GROUP BY u.id, u.login " +
                    "ORDER BY win_ratio DESC " +
                    "LIMIT :limit",
            nativeQuery = true
    )
    List<Object[]> findTopPlayers(@Param("limit") int limit); // Возвращает массив объектов [userId, login, winRatio]
}
