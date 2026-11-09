package com.example.J_Tictactoe.web.model;

import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String login;

    public UserResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
