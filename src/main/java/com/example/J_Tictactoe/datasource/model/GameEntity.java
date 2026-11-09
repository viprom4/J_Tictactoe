package com.example.J_Tictactoe.datasource.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.example.J_Tictactoe.domain.model.GameStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "games")
@Entity
public class GameEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "field")
    @JdbcTypeCode(SqlTypes.JSON)
    private int[][] field;

    @Column(name = "player1")
    private UUID player1;

    @Column(name = "player2")
    private UUID player2;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "player1_Symbol")
    private int player1Symbol;

    @Column(name = "player2_Symbol")
    private int player2Symbol;

    @Column(name = "current_player")
    private int currentPlayer;

    @Column(name = "history")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<int[][]> history;

    @Column(name = "date")
    private LocalDateTime date;

    public GameEntity() {}

    public GameEntity(UUID id, int[][] field, UUID player1, UUID player2, GameStatus status,  int currentPlayer, int player1Symbol, int player2Symbol, List<int[][]> history, LocalDateTime date) {
        this.id = id;
        this.field = field;

        this.player1 = player1;
        this.player2 = player2;

        this.status = status;

        this.player1Symbol = player1Symbol;
        this.player2Symbol = player2Symbol;
        this.currentPlayer = currentPlayer;

        this.history = history;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
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

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<int[][]> getHistory() {
        return history;
    }

    public void setHistory(List<int[][]> history) {
        this.history = history;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
