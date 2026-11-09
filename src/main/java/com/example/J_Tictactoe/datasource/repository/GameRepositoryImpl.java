package com.example.J_Tictactoe.datasource.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.J_Tictactoe.datasource.mapper.GameDatasourceMapper;
import com.example.J_Tictactoe.datasource.model.GameEntity;
import com.example.J_Tictactoe.domain.model.Game;
import com.example.J_Tictactoe.domain.model.GameStatus;

public class GameRepositoryImpl implements GameRepository {
    private final JpaGameRepository jpaGameRepository;

    public GameRepositoryImpl(JpaGameRepository jpaGameRepository ) {
        this.jpaGameRepository = jpaGameRepository;
    }

    @Override
    public void save(Game game) {
        jpaGameRepository.save(GameDatasourceMapper.toEntity(game));
    }

    @Override 
    public Game findById(UUID id) {
        GameEntity entity = jpaGameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));

        return GameDatasourceMapper.toDomain(entity);
    }

    @Override 
    public List<Game> findByStatus(GameStatus status) {
        List<GameEntity> gameEntities = jpaGameRepository.findByStatus(status);
        List<Game> availableGames = new ArrayList<>();

        for (GameEntity gameEntity : gameEntities) {
            Game game = GameDatasourceMapper.toDomain(gameEntity);
            availableGames.add(game);
        }

        return availableGames;
    }

    @Override 
    public List<Game> findCompletedGamesByUserId(UUID userId) {
        List<GameEntity> gameEntities = jpaGameRepository.findCompletedGamesByUserId(userId);

        List<Game> availableGames = new ArrayList<>();
        for (GameEntity gameEntity : gameEntities) {
            Game game = GameDatasourceMapper.toDomain(gameEntity);
            availableGames.add(game);
        }

        return availableGames;
    }

    @Override 
    public List<Object[]> findTopPlayers(int n) {
        return jpaGameRepository.findTopPlayers(n);
    }

    @Override
    public void deleteById(UUID id) {
        jpaGameRepository.deleteById(id);
    }
}
