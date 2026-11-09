package com.example.J_Tictactoe.domain.service;

import java.util.List;
import java.util.UUID;

import com.example.J_Tictactoe.datasource.repository.UserRepository;
import com.example.J_Tictactoe.domain.model.Role;
import com.example.J_Tictactoe.domain.model.User;
import com.example.J_Tictactoe.web.model.JwtRequest;
import com.example.J_Tictactoe.web.model.JwtResponse;
import com.example.J_Tictactoe.web.model.SignUpRequest;
import com.example.J_Tictactoe.web.security.JwtProvider;

import io.jsonwebtoken.Claims;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override 
    public boolean register(SignUpRequest request) {
        if(existsByLogin(request.getLogin())) {
            return false;
        } else {
            User user = new User(UUID.randomUUID(), request.getLogin(), request.getPassword(), List.of(Role.USER));
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public JwtResponse login(JwtRequest request) {
        User user = userRepository.findByLogin(request.getLogin());
        
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String accessToken = jwtProvider.accessToken(user);
        String refreshToken = jwtProvider.refreshToken(user);

        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Claims claims = jwtProvider.getClaims(refreshToken);
        UUID userId = UUID.fromString(claims.getSubject());
        User user = userRepository.findById(userId);

        String newAccess = jwtProvider.accessToken(user);
        String newRefresh = jwtProvider.refreshToken(user);

        return new JwtResponse("Bearer", newAccess, newRefresh);
    }

    @Override 
    public User getUserById(UUID id) {
        return userRepository.findById(id);
    }

    private boolean existsByLogin(String login) {
        try {
            userRepository.findByLogin(login);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
 