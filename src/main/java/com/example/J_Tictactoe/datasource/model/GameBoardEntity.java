package com.example.J_Tictactoe.datasource.model;

public class GameBoardEntity {
    private int[][] field;

    public GameBoardEntity() {}

    public GameBoardEntity(int[][] field) {
        this.field = field;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }
}
