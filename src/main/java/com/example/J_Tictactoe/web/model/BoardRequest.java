package com.example.J_Tictactoe.web.model;

public class BoardRequest {
    private int[][] field;

    public BoardRequest() {}

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }
}
