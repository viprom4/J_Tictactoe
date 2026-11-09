package com.example.J_Tictactoe.datasource.repository;

import java.util.UUID;

import com.example.J_Tictactoe.domain.model.User;

public interface UserRepository {
    void save(User user);
    User findByLogin(String login);
    User findById(UUID id);
}
