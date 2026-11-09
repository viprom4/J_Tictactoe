package com.example.J_Tictactoe.domain.service;

import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.domain.model.GameResult;

public class GameRules {
    public static GameResult checkGameOver(GameBoard gameBoard) {
        for (int row = 0; row < GameBoard.SIZE; row++) {
            int winner = checkLine(gameBoard.getCell(row, 0), gameBoard.getCell(row, 1), gameBoard.getCell(row, 2));
            if (winner == GameBoard.PLAYER_X) return GameResult.X_WINS;
            if (winner == GameBoard.PLAYER_O) return GameResult.O_WINS;
        }

        for (int col = 0; col < GameBoard.SIZE; col++) {
            int winner = checkLine(gameBoard.getCell(0, col), gameBoard.getCell(1, col), gameBoard.getCell(2, col));
            if (winner == GameBoard.PLAYER_X) return GameResult.X_WINS;
            if (winner == GameBoard.PLAYER_O) return GameResult.O_WINS;
        }

        int winnerD1 = checkLine(gameBoard.getCell(0, 0), gameBoard.getCell(1, 1), gameBoard.getCell(2, 2));
        if (winnerD1 == GameBoard.PLAYER_X) return GameResult.X_WINS;
        if (winnerD1 == GameBoard.PLAYER_O) return GameResult.O_WINS;

        int winnerD2 = checkLine(gameBoard.getCell(0, 2), gameBoard.getCell(1, 1), gameBoard.getCell(2, 0));
        if (winnerD2 == GameBoard.PLAYER_X) return GameResult.X_WINS;
        if (winnerD2 == GameBoard.PLAYER_O) return GameResult.O_WINS;

        if (gameBoard.isFieldFull()) return GameResult.DRAW;
        return GameResult.IN_PROGRESS;
    }

    private static int checkLine(int a, int b, int c) {
        if (a != GameBoard.EMPTY && (a == b && b == c)) {
            return a;
        }

        return GameBoard.EMPTY;
    }
}
