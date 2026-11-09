package com.example.J_Tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.domain.service.MinimaxBotServiceImpl;

public class MinimaxBotServiceImplTest {
    private MinimaxBotServiceImpl bot;

    @BeforeEach
    void setUp() {
        bot = new MinimaxBotServiceImpl();
    }

    @Test
    void makeBestMove_blocksUserWin() {
        GameBoard board = new GameBoard(new int[][]{
                {1, 1, 0},
                {0, 2, 0},
                {0, 0, 0}
        });

        int[] move = bot.makeBestMove(board, GameBoard.PLAYER_O);

        assertNotNull(move);
        assertEquals(0, move[0]);
        assertEquals(2, move[1]);
    }

    @Test
    void makeBestMove_takesWinningMove() {
        GameBoard board = new GameBoard(new int[][]{
                {2, 2, 0},
                {1, 0, 0},
                {1, 0, 1}
        });

        int[] move = bot.makeBestMove(board, GameBoard.PLAYER_O);

        assertNotNull(move);
        assertEquals(0, move[0]);
        assertEquals(2, move[1]);
    }

    @Test
    void makeBestMove_returnsNullOnFullBoard() {
        GameBoard board = new GameBoard(new int[][]{
                {1, 2, 1},
                {1, 2, 2},
                {2, 1, 1}
        });

        assertNull(bot.makeBestMove(board, GameBoard.PLAYER_O));
    }

    @Test
    void makeBestMove_doesNotMutateInputBoard() {
        GameBoard board = new GameBoard(new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        });

        bot.makeBestMove(board, GameBoard.PLAYER_O);

        assertEquals(GameBoard.EMPTY, board.getCell(0, 1));
        assertEquals(GameBoard.EMPTY, board.getCell(1, 1));
        assertEquals(GameBoard.EMPTY, board.getCell(1, 0));
    }
}
