package com.example.J_Tictactoe.web.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.J_Tictactoe.domain.model.GameStatus;


public class GameResponse {
    private UUID id;
    private int[][] field;
    private UUID player1;
    private UUID player2;
    private int currentPlayer;
    private GameStatus status;

    private String player1Login;
    private String player2Login;

    private java.time.LocalDateTime date;

    public GameResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public String getPlayer1Login() {
        return player1Login;
    }

     public void setPlayer1Login(String player1Login) {
        this.player1Login = player1Login;
    }

    public String getPlayer2Login() {
        return player2Login;
    }

     public void setPlayer2Login(String player2Login) {
        this.player2Login = player2Login;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
