package com.example.J_Tictactoe.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.J_Tictactoe.domain.service.UserService;
import com.example.J_Tictactoe.web.model.JwtRequest;
import com.example.J_Tictactoe.web.model.JwtResponse;
import com.example.J_Tictactoe.web.model.RefreshJwtRequest;
import com.example.J_Tictactoe.web.model.SignUpRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String postMethodName(@RequestBody SignUpRequest request) {
        if (userService.register(request)) {
            return "Registration successful";
        } else {
            throw new IllegalArgumentException("Login already taken");
        }
    }
    
    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest request) {
        return userService.login(request);

    }

     @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshJwtRequest request) {
        return userService.refreshToken(request.getRefreshToken());
    }
}
