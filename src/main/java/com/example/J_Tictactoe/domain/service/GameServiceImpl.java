package com.example.J_Tictactoe.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.J_Tictactoe.datasource.repository.GameRepository;
import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.domain.model.GameResult;
import com.example.J_Tictactoe.domain.model.GameStatus;
import com.example.J_Tictactoe.web.model.LeaderboardResponse;

public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final MinimaxBotService minimaxBotService;
    private final UserService userService;

    public GameServiceImpl(GameRepository repo, MinimaxBotService bot, UserService userService) {
        this.gameRepository = repo;
        this.minimaxBotService = bot;
        this.userService = userService;
    }

    @Override 
    public Game getNextMove(Game game) {
        if (checkGameOver(game.getBoard()) != GameResult.IN_PROGRESS) {
            return game;
        }

        int[] move = minimaxBotService.makeBestMove(game.getBoard(), game.getCurrentPlayer());
        if (move != null) game.makeMove(move[0], move[1]);
        return game;
    }

    @Override
    public boolean validateBoard(Game game, GameBoard submitted) {
        GameBoard gameBoard = game.getBoard();

        int newMoves = 0;
        for (int row = 0; row < GameBoard.SIZE; row++) {
            for (int col = 0; col < GameBoard.SIZE; col++) {
                int before = gameBoard.getCell(row, col);
                int after = submitted.getCell(row, col);

                if (before != GameBoard.EMPTY) {
                    if (after != before) return false;
                } else if (after != GameBoard.EMPTY) {
                    if (after != game.getCurrentPlayer()) return false;
                    newMoves++;
                    if (newMoves > 1) return false;
                }
            }
        }
        
        return newMoves == 1;
    }

    @Override
    public GameResult checkGameOver(GameBoard gameBoard) {
        return GameRules.checkGameOver(gameBoard);
    }

    @Override 
    public Game getGame(UUID id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game processUserMove(UUID id, GameBoard submittedBoard) {
        Game game = gameRepository.findById(id);

        if (checkGameOver(game.getBoard()) != GameResult.IN_PROGRESS) {
            throw new IllegalStateException("Game is already finished");
        }

        if (!validateBoard(game, submittedBoard)) {
            throw new IllegalArgumentException("Invalid move");
        }

        int userRow = -1;
        int userCol = -1;
        for (int r = 0; r < GameBoard.SIZE; r++) {
            for (int c = 0; c < GameBoard.SIZE; c++) {
                if (game.getBoard().getCell(r, c) != submittedBoard.getCell(r, c)) {
                    userRow = r;
                    userCol = c;
                }
            }
        }

        if (userRow == -1) {
            throw new IllegalArgumentException("No move detected");
        }

        game.makeMove(userRow, userCol);

        if (game.getPlayer2() == null && checkGameOver(game.getBoard()) == GameResult.IN_PROGRESS) {
            int[] botMove = minimaxBotService.makeBestMove(game.getBoard(), game.getCurrentPlayer());
            if (botMove != null) {
                game.makeMove(botMove[0], botMove[1]);
            }
        }

        GameResult result = checkGameOver(game.getBoard());
        if (result == GameResult.X_WINS) {
            game.setStatus(GameStatus.X_WINS);
        } else if (result == GameResult.O_WINS) {
            game.setStatus(GameStatus.O_WINS);
        } else if (result == GameResult.DRAW) {
            game.setStatus(GameStatus.DRAW);
        } else {
            game.setStatus(game.getCurrentPlayer() == 1 ? GameStatus.X_TURN : GameStatus.O_TURN);
        }

        gameRepository.save(game);
        
        return game;
    }

    @Override
    public Game createGameWithBot(UUID player1Id) {
        Game game = new Game(player1Id);
        gameRepository.save(game);
        return game;
    }

    @Override
    public Game createMultiplayerGame(UUID player1Id) {
        Game game = new Game(player1Id, true);
        gameRepository.save(game);
        return game;
    }

    @Override
    public List<Game> getAvailableGames() {
        return gameRepository.findByStatus(GameStatus.WAITING_FOR_PLAYERS);
    }

    @Override 
    public Game joinGame(UUID gameId, UUID player2Id) {
        Game game = gameRepository.findById(gameId);
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS || game.getPlayer2() != null) {
            throw new IllegalStateException("Game is not available for joining");
        }

        game.setPlayer2(player2Id);
        game.setStatus(GameStatus.X_TURN);
        gameRepository.save(game);

        return game;
    }

    @Override 
    public List<Game> getCompletedGames(UUID userId) {
        return gameRepository.findCompletedGamesByUserId(userId);
    }

    @Override
    public List<LeaderboardResponse> getLeaderboard(int n) {
        List<Object[]> topPlayers = gameRepository.findTopPlayers(n);
        List<LeaderboardResponse> leaderboardResponses = new ArrayList<>();

        for (Object[] topPlayer : topPlayers) {
            UUID userId = UUID.fromString(topPlayer[0].toString());
            
            double winRate = 0.0;
            if (topPlayer[1] != null) {
                winRate = ((Number) topPlayer[1]).doubleValue();
            }

            String login = userService.getUserById(userId).getLogin();
            LeaderboardResponse leaderboardResponse = new LeaderboardResponse(login, winRate);
            leaderboardResponses.add(leaderboardResponse);
        }

        return leaderboardResponses;
    }

    @Override
    public void cancelGame(UUID gameId, UUID userId) {
        Game game = gameRepository.findById(gameId);
        
        if (!game.getPlayer1().equals(userId)) {
            throw new IllegalArgumentException("Only creator can cancel the game");
        }
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalStateException("Cannot cancel a game that has already started");
        }
        
        gameRepository.deleteById(gameId);
    }
}