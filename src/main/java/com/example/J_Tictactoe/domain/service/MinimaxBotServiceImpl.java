package com.example.J_Tictactoe.domain.service;

import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.domain.model.GameResult;

public class MinimaxBotServiceImpl implements MinimaxBotService {
    public int[] makeBestMove(GameBoard board, int computerPlayer) {
        GameBoard copy = new GameBoard(board.copyField());

        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        int opponentPlayer = (computerPlayer == GameBoard.PLAYER_X) ? GameBoard.PLAYER_O : GameBoard.PLAYER_X;

        for (int row = 0; row < GameBoard.SIZE; row++) {
            for (int col = 0; col < GameBoard.SIZE; col++) {
                if (copy.getCell(row, col) == GameBoard.EMPTY) {
                    copy.setCell(row, col, computerPlayer);

                    int score = minimax(copy, 0, false, opponentPlayer, computerPlayer);

                    copy.clearCell(row, col);
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = row;
                        bestCol = col;
                    }
                }
            }
        }

        if (bestRow == -1) return null;
        return new int[]{bestRow, bestCol};
    }

    private int minimax(GameBoard gameBoard, int depth, boolean isMaximizing, int playerTurn, int computerPlayer) {
        GameResult result = GameRules.checkGameOver(gameBoard);
        if (result == GameResult.X_WINS) {
            return computerPlayer == GameBoard.PLAYER_X ? (10 - depth) : (depth - 10);
        }
        if (result == GameResult.O_WINS) {
            return computerPlayer == GameBoard.PLAYER_O ? (10 - depth) : (depth - 10);
        }
        if (result == GameResult.DRAW) return 0;

        int opponent = (playerTurn == GameBoard.PLAYER_X) ? GameBoard.PLAYER_O : GameBoard.PLAYER_X;
        int best = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < GameBoard.SIZE; row++) {
            for (int col = 0; col < GameBoard.SIZE; col++) {
                if (gameBoard.getCell(row, col) == GameBoard.EMPTY) {
                    gameBoard.setCell(row, col, playerTurn);
                    int eval = minimax(gameBoard, depth + 1, !isMaximizing, opponent, computerPlayer);
                    gameBoard.clearCell(row, col);
                    best = isMaximizing ? Math.max(best, eval) : Math.min(best, eval);
                }
            }
        }
        return best;
    }
}
