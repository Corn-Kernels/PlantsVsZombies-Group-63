package com.cornkernels.game.menus.model;

import org.jspecify.annotations.NonNull;

public class Plant {
    private int id;
    private String name;
    private String category;
    private String[] tags;
    private int cost;
    private int baseHp;
    private int damage;
    private String baseAbility;
    private String plantFoodEffect;
    private int level2;
    private int level3;
    private int level4;
    private float actionInterval;
    private float recharge;
    private String imagePath;
    //private int level;
    private int seedPackets;
    private int seedPacketsNeeded;
    private boolean isUnlocked;
    private String description;
    private boolean isBoosted;
    private boolean isUpgradable;
    private int maxLevel = 5;

    // ===== Full Constructor =====
    public Plant(int id, String name, String category, String[] tags, int cost,
                 int baseHp, int damage, String baseAbility, String plantFoodEffect,
                 int level2, int level3, int level4, float actionInterval, float recharge,
                 String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tags = tags;
        this.cost = cost;
        this.baseHp = baseHp;
        this.damage = damage;
        this.baseAbility = baseAbility;
        this.plantFoodEffect = plantFoodEffect;
        this.level2 = level2;
        this.level3 = level3;
        this.level4 = level4;
        this.actionInterval = actionInterval;
        this.recharge = recharge;
        this.imagePath = imagePath;
        //this.level = 1;
        this.seedPackets = 0;
        this.seedPacketsNeeded = 10;
        this.isUnlocked = false;
        this.description = baseAbility;
        this.isBoosted = false;
        this.isUpgradable = false;
        this.maxLevel = 5;
    }

    // ===== Getters =====
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String[] getTags() {
        return tags;
    }

    public int getCost() {
        return cost;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public int getDamage() {
        return damage;
    }

    public String getBaseAbility() {
        return baseAbility;
    }

    public String getPlantFoodEffect() {
        return plantFoodEffect;
    }

    public int getLevel2() {
        return level2;
    }

    public int getLevel3() {
        return level3;
    }

    public int getLevel4() {
        return level4;
    }

    public float getActionInterval() {
        return actionInterval;
    }

    public float getRecharge() {
        return recharge;
    }

    public String getImagePath() {
        return imagePath;
    }

    //public int getLevel() { return level; }
    public int getSeedPackets() {
        return seedPackets;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    // ===== Setters =====
    //public void setLevel(int level) { this.level = level; }
    //public void setSeedPackets(int seedPackets) { this.seedPackets = seedPackets; }
    //public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
    //public void addSeedPacket() { this.seedPackets++; }
    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBoosted() {
        return isBoosted;
    }

    public void setBoosted(boolean boosted) {
        isBoosted = boosted;
    }

    public boolean isUpgradable() {
        return isUpgradable;
    }

    public void setUpgradable(boolean upgradable) {
        isUpgradable = upgradable;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getLevel(@NonNull PlayerProgress progress) {
        return progress.getPlantLevel(this.name);
    }

    public void setLevel(@NonNull PlayerProgress progress, int level) {
        progress.setPlantLevel(this.name, level);
    }

    public int getSeedPacketsNeeded(PlayerProgress progress) {
        int currentLevel = getLevel(progress);
        return currentLevel + 1;
    }

    public int getUpgradeCost(PlayerProgress progress) {
        int currentLevel = getLevel(progress);
        return 50 + (currentLevel * 10);
    }

    public boolean canUpgrade(@NonNull PlayerProgress progress) {
        int currentLevel = getLevel(progress);
        return isUnlocked && currentLevel < maxLevel;
    }

    public void performUpgrade(PlayerProgress progress) {
        if (canUpgrade(progress)) {
            int needed = getSeedPacketsNeeded(progress);
            int currentLevel = getLevel(progress);

            // ===== کم کردن بذر =====
            progress.removePlantSeed(this.name, needed);

            // ===== افزایش سطح =====
            setLevel(progress, currentLevel + 1);

            // ===== افزایش قدرت (بر اساس سطح جدید) =====
            int newLevel = getLevel(progress);
            baseHp = 300 + (newLevel - 1) * 50;
            damage = 20 + (newLevel - 1) * 5;
            cost = Math.max(25, 50 - (newLevel - 1) * 5);
            recharge = Math.max(1.0f, 5.0f - (newLevel - 1) * 0.5f);
        }
    }
}
