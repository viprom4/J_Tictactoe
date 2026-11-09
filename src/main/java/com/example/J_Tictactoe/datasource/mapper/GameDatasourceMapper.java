package com.example.J_Tictactoe.datasource.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.J_Tictactoe.datasource.model.GameEntity;
import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;

public class GameDatasourceMapper {
    public static GameEntity toEntity(Game game) {
        GameEntity gameEntity = new GameEntity();

        gameEntity.setId(game.getId());
        gameEntity.setField(game.getBoard().copyField());

        gameEntity.setPlayer1(game.getPlayer1());
        gameEntity.setPlayer2(game.getPlayer2());

        gameEntity.setStatus(game.getStatus());

        gameEntity.setPlayer1Symbol(game.getPlayer1Symbol());
        gameEntity.setPlayer2Symbol(game.getPlayer2Symbol());
        gameEntity.setCurrentPlayer(game.getCurrentPlayer());

        gameEntity.setDate(game.getDate());

        List<int[][]> history = new ArrayList<>();
        for (int i = 0; i < game.getHistory().size(); i++) {
            history.add(deepCopy(game.getHistory().get(i)));
        }
        gameEntity.setHistory(history);

        return gameEntity;
    }

    public static Game toDomain(GameEntity entity) {
        GameBoard gameBoard = new GameBoard(deepCopy(entity.getField()));

        List<int[][]> history = new ArrayList<>();
        for (int i = 0; i < entity.getHistory().size(); i++) {
            history.add(deepCopy(entity.getHistory().get(i)));
        }

        return new Game(
            entity.getId(), 
            gameBoard, 
            entity.getPlayer1(),
            entity.getPlayer2(),
            entity.getStatus(),
            entity.getPlayer1Symbol(),
            entity.getPlayer2Symbol(),
            entity.getCurrentPlayer(), 
            history,
            entity.getDate()
        );
    }

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[GameBoard.SIZE][GameBoard.SIZE];
        for (int i = 0; i < GameBoard.SIZE; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, GameBoard.SIZE);
        }
        return copy;
    }
}
