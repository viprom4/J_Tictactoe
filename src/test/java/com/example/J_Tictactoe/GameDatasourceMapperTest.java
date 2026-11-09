package com.example.J_Tictactoe;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.J_Tictactoe.datasource.mapper.GameDatasourceMapper;
import com.example.J_Tictactoe.datasource.model.GameEntity;
import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;

public class GameDatasourceMapperTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(new GameBoard(new int[3][3]));
        game.makeMove(0, 0);
        game.makeMove(1, 1);
    }

    @Test
    void roundTripPreservesData() {
        GameEntity entity = GameDatasourceMapper.toEntity(game);
        Game restored = GameDatasourceMapper.toDomain(entity);

        assertEquals(game.getId(), restored.getId());
        assertEquals(game.getCurrentPlayer(), restored.getCurrentPlayer());
        assertEquals(game.getHistory().size(), restored.getHistory().size());
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                assertEquals(game.getBoard().getCell(r, c), restored.getBoard().getCell(r, c));
    }

    @Test
    void toEntity_fieldIsIndependent() {
        GameEntity entity = GameDatasourceMapper.toEntity(game);
        entity.getField()[0][0] = 99;
        assertEquals(GameBoard.PLAYER_X, game.getBoard().getCell(0, 0));
    }

    @Test
    void toEntity_historyIsIndependent() {
        GameEntity entity = GameDatasourceMapper.toEntity(game);
        entity.getHistory().get(0)[0][0] = 99;
        assertEquals(GameBoard.EMPTY, game.getHistory().get(0)[0][0]);
    }

    @Test
    void toDomain_historyIsMutable() {
        GameEntity entity = GameDatasourceMapper.toEntity(game);
        Game restored = GameDatasourceMapper.toDomain(entity);
        assertDoesNotThrow(() -> restored.makeMove(2, 2));
    }

    @Test
    void toDomain_fieldIsIndependent() {
        GameEntity entity = GameDatasourceMapper.toEntity(game);
        Game restored = GameDatasourceMapper.toDomain(entity);
        restored.makeMove(2, 2);
        assertEquals(GameBoard.EMPTY, entity.getField()[2][2]);
    }
}
