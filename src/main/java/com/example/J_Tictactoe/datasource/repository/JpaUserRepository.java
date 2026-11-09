package com.example.J_Tictactoe.datasource.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.J_Tictactoe.datasource.model.UserEntity;

public interface JpaUserRepository extends CrudRepository<UserEntity, UUID> {
    Optional<UserEntity> findByLogin(String login);
}
