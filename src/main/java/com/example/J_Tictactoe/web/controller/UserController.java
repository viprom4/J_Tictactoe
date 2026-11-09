package com.example.J_Tictactoe.web.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.J_Tictactoe.domain.model.User;
import com.example.J_Tictactoe.domain.service.UserService;
import com.example.J_Tictactoe.web.model.UserResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setLogin(user.getLogin());
        
        return response;
    }

}
