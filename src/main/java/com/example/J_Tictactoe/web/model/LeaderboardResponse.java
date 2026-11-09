package com.example.J_Tictactoe.web.model;

public class LeaderboardResponse {
    private String login;
    private double winRate;

    public LeaderboardResponse() {}

    public LeaderboardResponse(String login, double winRate) {
        this.login = login;
        this.winRate = winRate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }
}
