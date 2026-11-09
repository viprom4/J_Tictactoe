package com.example.J_Tictactoe.datasource;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.example.J_Tictactoe.datasource.model.GameEntity;

public class GameStorage {
    private final ConcurrentHashMap<UUID, GameEntity> storage = new ConcurrentHashMap<>();

    public void save(GameEntity entity) {
        storage.put(entity.getId(), entity);
    }

    public GameEntity findById(UUID id) {
        return storage.get(id);
    }
}
