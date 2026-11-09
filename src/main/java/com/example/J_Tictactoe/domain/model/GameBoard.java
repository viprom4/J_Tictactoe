package com.example.J_Tictactoe.domain.model;

public class GameBoard {
    public static final int SIZE = 3;
    public static final int EMPTY = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    private int[][] field;

    public GameBoard(int[][] field) {
        this.field = field;
    }

    public int getCell(int row, int col) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            throw new ArrayIndexOutOfBoundsException("values outside the array boundaries");
        }

        return field[row][col];
    }

    public void setCell(int row, int col, int value) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            throw new ArrayIndexOutOfBoundsException("values outside the array boundaries");
        }

        if (value != PLAYER_X && value != PLAYER_O) {
            throw new IllegalArgumentException("Invalid move");
        }

        if (getCell(row, col) != EMPTY) {
            throw new IllegalArgumentException("The slot is occupied");
        }

        field[row][col] = value;
    }

    public boolean isFieldFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (field[row][col] == EMPTY) return false;
            }
        }

        return true;
    }

    public int[][] copyField() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(field[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public void clearCell(int row, int col) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            throw new ArrayIndexOutOfBoundsException("values outside the array boundaries");
        }
        field[row][col] = EMPTY;
    }
}
