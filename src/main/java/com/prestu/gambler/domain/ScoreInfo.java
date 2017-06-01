package com.prestu.gambler.domain;

public class ScoreInfo {

    private String username;
    private String gameName;
    private int score;

    public ScoreInfo(String username, String gameName, int score) {
        this.username = username;
        this.gameName = gameName;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
