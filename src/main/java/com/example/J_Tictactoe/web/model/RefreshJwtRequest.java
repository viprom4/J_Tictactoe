package com.example.J_Tictactoe.web.model;

public class RefreshJwtRequest {
    private String refreshToken;

    public RefreshJwtRequest() {}

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
