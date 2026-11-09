package com.example.J_Tictactoe.datasource.repository;

import java.util.UUID;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(UUID id) {
        super("Game not found: " + id);
    }
}
