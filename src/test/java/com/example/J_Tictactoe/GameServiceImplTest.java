package com.example.J_Tictactoe;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.J_Tictactoe.datasource.repository.GameNotFoundException;
import com.example.J_Tictactoe.datasource.repository.GameRepository;
import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameBoard;
import com.example.J_Tictactoe.domain.model.GameResult;
import com.example.J_Tictactoe.domain.service.GameServiceImpl;
import com.example.J_Tictactoe.domain.service.MinimaxBotServiceImpl;
import com.example.J_Tictactoe.domain.service.UserService;

public class GameServiceImplTest {
    private GameServiceImpl service;
    private GameRepository gameRepositoryMock;
    private UserService userServiceMock;

    @BeforeEach
    void setUp() {
        gameRepositoryMock = mock(GameRepository.class);
        userServiceMock = mock(UserService.class);
        
        service = new GameServiceImpl(gameRepositoryMock, new MinimaxBotServiceImpl(), userServiceMock);
    }

    @Test
    void checkGameOver_XWinsRow() {
        GameBoard b = new GameBoard(new int[][]{
                {1, 1, 1}, {2, 0, 0}, {0, 0, 0}
        });
        assertEquals(GameResult.X_WINS, service.checkGameOver(b));
    }

    @Test
    void checkGameOver_OWinsColumn() {
        GameBoard b = new GameBoard(new int[][]{
                {1, 2, 0}, {1, 2, 0}, {0, 2, 0}
        });
        assertEquals(GameResult.O_WINS, service.checkGameOver(b));
    }

    @Test
    void validateBoard_validMove() {
        Game game = new Game(new GameBoard(new int[3][3]));
        game.makeMove(0, 0);
        GameBoard submitted = new GameBoard(game.getBoard().copyField());
        submitted.setCell(1, 1, GameBoard.PLAYER_O);
        assertTrue(service.validateBoard(game, submitted));
    }

    @Test
    void validateBoard_modifiedPreviousMove() {
        Game game = new Game(new GameBoard(new int[3][3]));
        game.makeMove(0, 0);

        int[][] hacked = game.getBoard().copyField();
        hacked[0][0] = GameBoard.EMPTY;
        hacked[1][1] = GameBoard.PLAYER_O;
        GameBoard submitted = new GameBoard(hacked);

        assertFalse(service.validateBoard(game, submitted));
    }

    @Test
    void getGame_nonexistentThrows() {
        UUID randomId = UUID.randomUUID();
        when(gameRepositoryMock.findById(randomId)).thenThrow(new GameNotFoundException(randomId));

        assertThrows(GameNotFoundException.class,
                () -> service.getGame(randomId));
    }
}