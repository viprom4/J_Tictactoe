package com.example.J_Tictactoe.domain.service;

import java.util.List;
import java.util.UUID;

import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.domain.model.GameResult;
import com.example.J_Tictactoe.web.model.LeaderboardResponse;

public interface GameService {
    Game getNextMove(Game game);
    boolean validateBoard(Game game, GameBoard submitted);
    GameResult checkGameOver(GameBoard gameBoard);

    Game getGame(UUID id);
    Game processUserMove(UUID id, GameBoard submittedBoard);

    Game createGameWithBot(UUID player1Id);
    Game createMultiplayerGame(UUID player1Id);
    List<Game> getAvailableGames();
    Game joinGame(UUID gameId, UUID player2Id);

    List<Game> getCompletedGames(UUID userId);
    List<LeaderboardResponse> getLeaderboard(int n);
    void cancelGame(UUID gameId, UUID userId);
}