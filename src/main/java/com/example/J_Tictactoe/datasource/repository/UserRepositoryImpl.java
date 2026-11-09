package com.example.J_Tictactoe.datasource.repository;

import java.util.UUID;

import com.example.J_Tictactoe.datasource.mapper.UserDatasourceMapper;
import com.example.J_Tictactoe.datasource.model.UserEntity;
import com.example.J_Tictactoe.domain.model.User;

public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository ) {
        this.jpaUserRepository = jpaUserRepository;
    }

    public void save(User user) {
        jpaUserRepository.save(UserDatasourceMapper.toEntity(user));
    }

    public User findByLogin(String login) {
        UserEntity entity = jpaUserRepository.findByLogin(login).orElseThrow(() -> new IllegalArgumentException("User not found: " + login));

        return UserDatasourceMapper.toDomain(entity);
    }

    public  User findById(UUID id) {
        UserEntity entity = jpaUserRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        return UserDatasourceMapper.toDomain(entity);
    }
}
