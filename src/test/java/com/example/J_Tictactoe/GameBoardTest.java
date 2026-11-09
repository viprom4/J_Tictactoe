package com.example.J_Tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.J_Tictactoe.domain.model.GameBoard;

public class GameBoardTest {
     private GameBoard board;

    @BeforeEach
    void setUp() {
        board = new GameBoard(new int[GameBoard.SIZE][GameBoard.SIZE]);
    }

    @Test
    void initialBoardIsEmpty() {
        for (int r = 0; r < GameBoard.SIZE; r++)
            for (int c = 0; c < GameBoard.SIZE; c++)
                assertEquals(GameBoard.EMPTY, board.getCell(r, c));
    }

    @Test
    void setCellValid() {
        board.setCell(0, 0, GameBoard.PLAYER_X);
        assertEquals(GameBoard.PLAYER_X, board.getCell(0, 0));
    }

    @Test
    void setCellOccupiedThrows() {
        board.setCell(0, 0, GameBoard.PLAYER_X);
        assertThrows(IllegalArgumentException.class,
                () -> board.setCell(0, 0, GameBoard.PLAYER_O));
    }

    @Test
    void setCellInvalidValueThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> board.setCell(0, 0, 99));
    }

    @Test
    void setCellOutOfBoundsThrows() {
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> board.setCell(3, 0, GameBoard.PLAYER_X));
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> board.setCell(0, -1, GameBoard.PLAYER_X));
    }

    @Test
    void isFieldFullEmptyBoard() {
        assertFalse(board.isFieldFull());
    }

    @Test
    void isFieldFullCompleteBoard() {
        for (int r = 0; r < GameBoard.SIZE; r++)
            for (int c = 0; c < GameBoard.SIZE; c++)
                board.setCell(r, c, GameBoard.PLAYER_X);
        assertTrue(board.isFieldFull());
    }

    @Test
    void copyFieldIsDeepCopy() {
        board.setCell(0, 0, GameBoard.PLAYER_X);
        int[][] copy = board.copyField();
        copy[0][0] = GameBoard.PLAYER_O;
        assertEquals(GameBoard.PLAYER_X, board.getCell(0, 0));
    }

    @Test
    void clearCellWorks() {
        board.setCell(0, 0, GameBoard.PLAYER_X);
        board.clearCell(0, 0);
        assertEquals(GameBoard.EMPTY, board.getCell(0, 0));
    }
}
