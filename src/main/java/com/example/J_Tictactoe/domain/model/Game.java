package com.example.J_Tictactoe.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private final UUID id;
    private final GameBoard gameBoard;

    private UUID player1;
    private UUID player2;

    private GameStatus status;

    private int player1Symbol;
    private int player2Symbol;
    private int currentPlayer;

    private List<int[][]> history;
    private LocalDateTime date;

    public Game(GameBoard gameBoard) {
        this.id = UUID.randomUUID();
        this.gameBoard = gameBoard;
        this.currentPlayer = GameBoard.PLAYER_X;

        this.history = new ArrayList<>();
        this.history.add(gameBoard.copyField());
        this.date = LocalDateTime.now();
    }

    public Game(UUID id, GameBoard gameBoard, UUID player1, UUID player2, GameStatus status, int player1Symbol, int player2Symbol, int currentPlayer, List<int[][]> history, LocalDateTime date) {
        this.id = id;
        this.gameBoard = gameBoard;

        this.player1 = player1;
        this.player2 = player2;

        this.status = status;

        this.player1Symbol = player1Symbol;
        this.player2Symbol = player2Symbol;
        this.currentPlayer = currentPlayer;

        this.history = history;
        this.date = date;
    }

    public Game(UUID player1) {
        this.id = UUID.randomUUID();
        this.gameBoard = new GameBoard(new int[3][3]);

        this.player1 = player1;
        this.player2 = null;

        this.status = GameStatus.X_TURN;

        this.player1Symbol = 1;
        this.player2Symbol = 2;
        this.currentPlayer = 1;

        this.history = new ArrayList<>();
        this.history.add(gameBoard.copyField());
        this.date = LocalDateTime.now();
    }

    public Game(UUID player1, boolean isMultiplayer) {
        this.id = UUID.randomUUID();
        this.gameBoard = new GameBoard(new int[3][3]);

        this.player1 = player1;
        this.player2 = null;

        this.status = GameStatus.WAITING_FOR_PLAYERS;

        this.player1Symbol = 1;
        this.player2Symbol = 2;
        this.currentPlayer = 1;

        this.history = new ArrayList<>();
        this.history.add(gameBoard.copyField());
        this.date = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public GameBoard getBoard() {
        return gameBoard;
    }

    public UUID getPlayer1() {
        return player1;
    }

    public void setPlayer1(UUID player1) {
        this.player1 = player1;
    }

    public UUID getPlayer2() {
        return player2;
    }

    public void setPlayer2(UUID player2) {
        this.player2 = player2;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public int getPlayer1Symbol() {
        return player1Symbol;
    }

    public void setPlayer1Symbol(int player1Symbol) {
        this.player1Symbol = player1Symbol;
    }

    public int getPlayer2Symbol() {
        return player2Symbol;
    }

    public void setPlayer2Symbol(int player2Symbol) {
        this.player2Symbol = player2Symbol;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public List<int[][]> getHistory() {
        return history;
    }

    public void makeMove(int row, int col) {
        gameBoard.setCell(row, col, currentPlayer);
        this.history.add(gameBoard.copyField());
        currentPlayer = (currentPlayer == GameBoard.PLAYER_X) ? GameBoard.PLAYER_O : GameBoard.PLAYER_X;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
