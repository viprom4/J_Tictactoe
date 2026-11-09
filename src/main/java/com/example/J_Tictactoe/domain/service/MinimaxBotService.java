package com.example.J_Tictactoe.domain.service;

import com.example.J_Tictactoe.domain.model.GameBoard;

public interface MinimaxBotService {
    int[] makeBestMove(GameBoard board, int computerPlayer);
}
