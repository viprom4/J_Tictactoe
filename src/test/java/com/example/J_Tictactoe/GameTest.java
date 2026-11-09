package com.example.J_Tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;

public class GameTest {
    @Test
    void constructorInitializesCorrectly() {
        Game game = new Game(new GameBoard(new int[3][3]));
        assertNotNull(game.getId());
        assertEquals(GameBoard.PLAYER_X, game.getCurrentPlayer());
        assertEquals(1, game.getHistory().size());
    }

    @Test
    void makeMoveUpdatesBoardAndPlayer() {
        Game game = new Game(new GameBoard(new int[3][3]));
        game.makeMove(0, 0);
        assertEquals(GameBoard.PLAYER_X, game.getBoard().getCell(0, 0));
        assertEquals(GameBoard.PLAYER_O, game.getCurrentPlayer());
    }

    @Test
    void makeMoveAddsHistoryEntry() {
        Game game = new Game(new GameBoard(new int[3][3]));
        game.makeMove(0, 0);
        assertEquals(2, game.getHistory().size());
        game.makeMove(1, 1);
        assertEquals(3, game.getHistory().size());
    }

    @Test
    void historyIsDeepCopied() {
        Game game = new Game(new GameBoard(new int[3][3]));
        game.makeMove(0, 0);
        game.makeMove(1, 1);

        int[][] firstMove = game.getHistory().get(1);
        assertEquals(GameBoard.PLAYER_X, firstMove[0][0]);
        assertEquals(GameBoard.EMPTY, firstMove[1][1]);
    }
}
