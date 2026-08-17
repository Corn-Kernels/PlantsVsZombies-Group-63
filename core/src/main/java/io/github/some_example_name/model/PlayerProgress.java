package io.github.some_example_name.model;

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

    private int pots;
    private int plantFood;
    private List<String> seedPackets;

    private int difficultyLevel;
    private float gameSpeed;
    private boolean showGrid;
    private boolean debugMode;

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

        this.pots = 0;
        this.plantFood = 0;
        this.seedPackets = new ArrayList<>();

        this.difficultyLevel = 3;
        this.gameSpeed = 1.0f;
        this.showGrid = false;
        this.debugMode = false;
        this.garden = new Garden();
    }
    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }
    public void addCoins(int amount) { this.coins += amount; }
    public boolean deductCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }
    public int getDiamonds() { return diamonds; }
    public void setDiamonds(int diamonds) { this.diamonds = diamonds; }
    public void addDiamonds(int amount) { this.diamonds += amount; }
    public boolean deductDiamonds(int amount) {
        if (diamonds >= amount) {
            diamonds -= amount;
            return true;
        }
        return false;
    }
    public int getHighScore() { return highScore; }
    public void setHighScore(int highScore) { this.highScore = highScore; }
    public List<String> getUnlockedChapters() { return unlockedChapters; }
    public List<String> getOwnedPlants() { return ownedPlants; }
    public List<String> getSeenZombies() { return seenZombies; }
    public int getCompletedLevels() { return completedLevels; }
    public void incrementCompletedLevels() { this.completedLevels++; }

    public boolean hasPlant(String plant) {
        return ownedPlants.contains(plant);
    }
    public void addPlant(String plant) {
        if (!hasPlant(plant)) {
            ownedPlants.add(plant);
        }
    }
    public void addSeenZombie(String zombie) {
        if (!seenZombies.contains(zombie)) {
            seenZombies.add(zombie);
        }
    }
    public int getPots() { return pots; }
    public void setPots(int pots) { this.pots = pots; }

    public int getPlantFood() { return plantFood; }
    public void setPlantFood(int plantFood) { this.plantFood = plantFood; }

    public List<String> getSeedPackets() { return seedPackets; }
    public void addSeedPacket(String seed) {
        seedPackets.add(seed);
    }
    public Garden getGarden() { return garden; }
    public void setGarden(Garden garden) { this.garden = garden; }

    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = Math.max(1, Math.min(5, difficultyLevel));
    }

    public float getGameSpeed() { return gameSpeed; }
    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = Math.max(0.5f, Math.min(3.0f, gameSpeed));
    }
    public boolean isShowGrid() { return showGrid; }
    public void setShowGrid(boolean showGrid) { this.showGrid = showGrid; }

    public boolean isDebugMode() { return debugMode; }
    public void setDebugMode(boolean debugMode) { this.debugMode = debugMode; }

    @Override
    public String toString() {
        return "PlayerProgress{" +
            "coins=" + coins +
            ", diamonds=" + diamonds +
            ", highScore=" + highScore +
            ", unlockedChapters=" + unlockedChapters +
            ", ownedPlants=" + ownedPlants +
            ", completedLevels=" + completedLevels +
            ", difficultyLevel=" + difficultyLevel +
            ", gameSpeed=" + gameSpeed +
            '}';
    }
}
