package com.example.J_Tictactoe.datasource.repository;

import java.util.List;
import java.util.UUID;

import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameStatus;

public interface GameRepository {
    void save(Game game);
    Game findById(UUID id);
    List<Game> findByStatus(GameStatus status);
    List<Game> findCompletedGamesByUserId(UUID userId);
    List<Object[]> findTopPlayers(int n);
    void deleteById(UUID id);
}
