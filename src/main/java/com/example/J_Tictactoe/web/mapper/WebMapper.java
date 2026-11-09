package com.example.J_Tictactoe.web.mapper;

import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.web.model.BoardRequest;
import com.example.J_Tictactoe.web.model.GameResponse;

public class WebMapper {
    public static GameResponse toResponse(Game game) {
        GameResponse response = new GameResponse();

        response.setId(game.getId());
        response.setField(game.getBoard().copyField());
        response.setPlayer1(game.getPlayer1());
        response.setPlayer2(game.getPlayer2());
        response.setCurrentPlayer(game.getCurrentPlayer());
        response.setStatus(game.getStatus());
        response.setDate(game.getDate());

        return response;
    }

    public static GameBoard toDomain(BoardRequest request) {
        if (request == null || request.getField() == null) {
            throw new IllegalArgumentException("field is required");
        }

        int[][] field = request.getField();
        if (field.length != GameBoard.SIZE) {
            throw new IllegalArgumentException("Board must be " + GameBoard.SIZE + "x" + GameBoard.SIZE);
        }
        
        for (int[] row : field) {
            if (row == null || row.length != GameBoard.SIZE) {
                throw new IllegalArgumentException("Board must be " + GameBoard.SIZE + "x" + GameBoard.SIZE);
            }
        }

        return new GameBoard(deepCopy(request.getField()));
    }

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[GameBoard.SIZE][GameBoard.SIZE];
        for (int i = 0; i < GameBoard.SIZE; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, GameBoard.SIZE);
        }
        return copy;
    }
}
