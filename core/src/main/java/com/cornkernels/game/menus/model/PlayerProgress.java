package com.cornkernels.game.menus.model;

import java.util.ArrayList;
import java.util.List;

public class PlayerProgress {
    private int coins;
    private int diamonds;
    private int highScore;
    private List<String> unlockedChapters;
    private List<String> ownedPlants;
    private List<String> seenZombies;
    private int completedLevels;
    private int gamesPlayed;

    private int pots;
    private int plantFood;
    private List<String> seedPackets;

    private int difficultyLevel;
    private float gameSpeed;
    private boolean showGrid;
    private boolean debugMode;

    private int minigamesCompleted;
    private int dailyQuestsCompleted;
    private int nonDailyQuestsCompleted;
    private List<NewsItem> newsList;
    private Garden garden;

    public PlayerProgress() {
        this.coins = 1000;
        this.diamonds = 10;
        this.highScore = 0;
        this.unlockedChapters = new ArrayList<>();
        this.unlockedChapters.add("Chapter 1");
        this.ownedPlants = new ArrayList<>();
        this.ownedPlants.add("SUNFLOWER");
        this.ownedPlants.add("PEASHOOTER");
        this.ownedPlants.add("WALL_NUT");
        this.seenZombies = new ArrayList<>();
        this.completedLevels = 0;
        this.gamesPlayed = 0;

        this.pots = 0;
        this.plantFood = 0;
        this.seedPackets = new ArrayList<>();

        this.difficultyLevel = 3;
        this.gameSpeed = 1.0f;
        this.showGrid = false;
        this.debugMode = false;

        this.minigamesCompleted = 0;
        this.dailyQuestsCompleted = 0;
        this.nonDailyQuestsCompleted = 0;
        this.newsList = new ArrayList<>();
        this.garden = new Garden();
    }

    private static String normalizePlantKey(String plant) {
        return plant == null ? "" : plant.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    // ===== Getters and Setters =====
    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public boolean deductCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public void addDiamonds(int amount) {
        this.diamonds += amount;
    }

    public boolean deductDiamonds(int amount) {
        if (diamonds >= amount) {
            diamonds -= amount;
            return true;
        }
        return false;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public List<String> getUnlockedChapters() {
        return unlockedChapters;
    }

    public void setUnlockedChapters(List<String> unlockedChapters) {
        this.unlockedChapters = unlockedChapters;
    }

    public void addUnlockedChapter(String chapter) {
        if (!unlockedChapters.contains(chapter)) {
            unlockedChapters.add(chapter);
        }
    }

    // ===== متدهای مربوط به گیاهان =====
    public List<String> getOwnedPlants() {
        return ownedPlants;
    }

    public void setOwnedPlants(List<String> ownedPlants) {
        this.ownedPlants = ownedPlants;
    }

    // ===== متد کمکی برای سازگاری با GreenhouseScreen =====
    public List<String> getUnlockedPlants() {
        return ownedPlants;  // همان ownedPlants رو برمی‌گردونه
    }

    // Plant names are written inconsistently across the app ("Wall-nut", "WALL_NUT", "wall nut", ...),
    // so ownership is matched on a normalized (letters/digits only, uppercase) key rather than exact
    // string equality — otherwise plants added under one spelling silently fail to match another.
    public boolean hasPlant(String plant) {
        String key = normalizePlantKey(plant);
        for (String owned : ownedPlants) {
            if (normalizePlantKey(owned).equals(key)) return true;
        }
        return false;
    }

    public void addPlant(String plant) {
        if (!hasPlant(plant)) {
            ownedPlants.add(plant);
        }
    }

    // ===== زامبی‌ها =====
    public List<String> getSeenZombies() {
        return seenZombies;
    }

    public void setSeenZombies(List<String> seenZombies) {
        this.seenZombies = seenZombies;
    }

    public void addSeenZombie(String zombie) {
        if (!seenZombies.contains(zombie)) {
            seenZombies.add(zombie);
        }
    }

    public int getCompletedLevels() {
        return completedLevels;
    }

    public void setCompletedLevels(int completedLevels) {
        this.completedLevels = completedLevels;
    }

    public void incrementCompletedLevels() {
        this.completedLevels++;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    // ===== گلخانه =====
    public int getPots() {
        return pots;
    }

    public void setPots(int pots) {
        this.pots = pots;
    }

    public int getPlantFood() {
        return plantFood;
    }

    public void setPlantFood(int plantFood) {
        this.plantFood = plantFood;
    }

    public List<String> getSeedPackets() {
        return seedPackets;
    }

    public void setSeedPackets(List<String> seedPackets) {
        this.seedPackets = seedPackets;
    }

    public void addSeedPacket(String seed) {
        seedPackets.add(seed);
    }

    public Garden getGarden() {
        return garden;
    }

    public void setGarden(Garden garden) {
        this.garden = garden;
    }

    // ===== تنظیمات =====
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = Math.max(1, Math.min(5, difficultyLevel));
    }

    public float getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = Math.max(0.5f, Math.min(3.0f, gameSpeed));
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    // ===== کوئست‌ها =====
    public int getMinigamesCompleted() {
        return minigamesCompleted;
    }

    public void setMinigamesCompleted(int minigamesCompleted) {
        this.minigamesCompleted = minigamesCompleted;
    }

    public void incrementMinigamesCompleted() {
        this.minigamesCompleted++;
    }

    public int getDailyQuestsCompleted() {
        return dailyQuestsCompleted;
    }

    public void setDailyQuestsCompleted(int dailyQuestsCompleted) {
        this.dailyQuestsCompleted = dailyQuestsCompleted;
    }

    public void incrementDailyQuestsCompleted() {
        this.dailyQuestsCompleted++;
    }

    public int getNonDailyQuestsCompleted() {
        return nonDailyQuestsCompleted;
    }

    public void setNonDailyQuestsCompleted(int nonDailyQuestsCompleted) {
        this.nonDailyQuestsCompleted = nonDailyQuestsCompleted;
    }

    public void incrementNonDailyQuestsCompleted() {
        this.nonDailyQuestsCompleted++;
    }

    // ===== اخبار =====
    public List<NewsItem> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    public void addNews(NewsItem news) {
        newsList.add(news);
    }

    public int getUnreadNewsCount() {
        int count = 0;
        for (NewsItem news : newsList) {
            if (!news.isRead()) count++;
        }
        return count;
    }

    public List<NewsItem> getUnreadNews() {
        List<NewsItem> unread = new ArrayList<>();
        for (NewsItem news : newsList) {
            if (!news.isRead()) unread.add(news);
        }
        return unread;
    }

    // ===== آخرین مرحله =====
    public String getLastLevel() {
        if (unlockedChapters.isEmpty()) return "None";
        String lastChapter = unlockedChapters.get(unlockedChapters.size() - 1);
        int level = (completedLevels % 4) + 1;
        return lastChapter + " - Level " + level;
    }
}
