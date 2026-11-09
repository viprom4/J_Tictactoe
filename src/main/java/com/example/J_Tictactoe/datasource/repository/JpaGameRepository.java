package com.example.J_Tictactoe.datasource.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.J_Tictactoe.datasource.model.GameEntity;
import com.example.J_Tictactoe.domain.model.GameStatus;

public interface JpaGameRepository extends CrudRepository<GameEntity, UUID> {
    List<GameEntity> findByStatus(GameStatus status);

    @Query("SELECT g FROM GameEntity g WHERE (g.player1 = :userId OR g.player2 = :userId) AND g.status IN ('X_WINS', 'O_WINS', 'DRAW') ORDER BY g.date DESC")
    List<GameEntity> findCompletedGamesByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT user_id, " +
       "(CAST(wins AS numeric) / NULLIF((wins + losses + draws), 0)) as win_ratio " +
       "FROM ( " +
       "  SELECT player1 as user_id, " +
       "         SUM(CASE WHEN status = 'X_WINS' THEN 1 ELSE 0 END) as wins, " +
       "         SUM(CASE WHEN status = 'O_WINS' THEN 1 ELSE 0 END) as losses, " +
       "         SUM(CASE WHEN status = 'DRAW' THEN 1 ELSE 0 END) as draws " +
       "  FROM games WHERE player1 IS NOT NULL AND status IN ('X_WINS', 'O_WINS', 'DRAW') GROUP BY player1 " +
       "  UNION ALL " +
       "  SELECT player2 as user_id, " +
       "         SUM(CASE WHEN status = 'O_WINS' THEN 1 ELSE 0 END) as wins, " +
       "         SUM(CASE WHEN status = 'X_WINS' THEN 1 ELSE 0 END) as losses, " +
       "         SUM(CASE WHEN status = 'DRAW' THEN 1 ELSE 0 END) as draws " +
       "  FROM games WHERE player2 IS NOT NULL AND status IN ('X_WINS', 'O_WINS', 'DRAW') GROUP BY player2 " + // <-- Добавили условие
       ") AS stats " +
       "GROUP BY user_id, wins, losses, draws " +
       "HAVING (CAST(SUM(wins) AS numeric) / NULLIF(SUM(wins + losses + draws), 0)) > 0 " + // <-- Отсекаем нулевой винрейт
       "ORDER BY win_ratio DESC " +
       "LIMIT :n", nativeQuery = true)
    List<Object[]> findTopPlayers(@Param("n") int n);
}