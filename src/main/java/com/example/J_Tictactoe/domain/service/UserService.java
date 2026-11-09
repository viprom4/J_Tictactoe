package com.example.J_Tictactoe.domain.service;

import java.util.UUID;

import com.example.J_Tictactoe.domain.model.User;
import com.example.J_Tictactoe.web.model.JwtRequest;
import com.example.J_Tictactoe.web.model.JwtResponse;
import com.example.J_Tictactoe.web.model.SignUpRequest;

public interface UserService {
    boolean register(SignUpRequest request);
    JwtResponse login(JwtRequest request); 
    JwtResponse refreshToken(String refreshToken); 
    User getUserById(UUID id);
}
