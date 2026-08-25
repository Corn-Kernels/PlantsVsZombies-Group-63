package com.cornkernels.game.menus.model;

public class LeaderboardEntry {
    private String username;
    private String lastLevel;
    private int minigamesCompleted;
    private int dailyQuestsCompleted;
    private int nonDailyQuestsCompleted;
    private int highScore;

    public LeaderboardEntry(String username, String lastLevel, int minigamesCompleted,
                            int dailyQuestsCompleted, int nonDailyQuestsCompleted, int highScore) {
        this.username = username;
        this.lastLevel = lastLevel;
        this.minigamesCompleted = minigamesCompleted;
        this.dailyQuestsCompleted = dailyQuestsCompleted;
        this.nonDailyQuestsCompleted = nonDailyQuestsCompleted;
        this.highScore = highScore;
    }

    public String getUsername() { return username; }
    public String getLastLevel() { return lastLevel; }
    public int getMinigamesCompleted() { return minigamesCompleted; }
    public int getDailyQuestsCompleted() { return dailyQuestsCompleted; }
    public int getNonDailyQuestsCompleted() { return nonDailyQuestsCompleted; }
    public int getHighScore() { return highScore; }

    public void setUsername(String username) { this.username = username; }
    public void setLastLevel(String lastLevel) { this.lastLevel = lastLevel; }
    public void setMinigamesCompleted(int minigamesCompleted) { this.minigamesCompleted = minigamesCompleted; }
    public void setDailyQuestsCompleted(int dailyQuestsCompleted) { this.dailyQuestsCompleted = dailyQuestsCompleted; }
    public void setNonDailyQuestsCompleted(int nonDailyQuestsCompleted) { this.nonDailyQuestsCompleted = nonDailyQuestsCompleted; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
}
