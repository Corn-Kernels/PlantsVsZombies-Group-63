package io.github.some_example_name.model;

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
    private int level;
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
        this.level = 1;
        this.seedPackets = 0;
        this.seedPacketsNeeded = 10;
        this.isUnlocked = false;
        this.description = baseAbility;
        this.isBoosted = false;
        this.isUpgradable = false;
        this.maxLevel = 5;
    }

    // ===== Getters =====
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String[] getTags() { return tags; }
    public int getCost() { return cost; }
    public int getBaseHp() { return baseHp; }
    public int getDamage() { return damage; }
    public String getBaseAbility() { return baseAbility; }
    public String getPlantFoodEffect() { return plantFoodEffect; }
    public int getLevel2() { return level2; }
    public int getLevel3() { return level3; }
    public int getLevel4() { return level4; }
    public float getActionInterval() { return actionInterval; }
    public float getRecharge() { return recharge; }
    public String getImagePath() { return imagePath; }
    public int getLevel() { return level; }
    public int getSeedPackets() { return seedPackets; }
    public int getSeedPacketsNeeded() { return seedPacketsNeeded + (level - 1) * 2; }
    public boolean isUnlocked() { return isUnlocked; }
    public String getDescription() { return description; }
    public boolean isBoosted() { return isBoosted; }
    public boolean isUpgradable() { return isUpgradable; }
    public int getMaxLevel() { return maxLevel; }

    // ===== Setters =====
    public void setLevel(int level) { this.level = level; }
    public void setSeedPackets(int seedPackets) { this.seedPackets = seedPackets; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
    public void addSeedPacket() { this.seedPackets++; }
    public void setDescription(String description) { this.description = description; }
    public void setBoosted(boolean boosted) { isBoosted = boosted; }
    public void setUpgradable(boolean upgradable) { isUpgradable = upgradable; }
    public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }

    // ===== متدهای ارتقا =====
    public boolean canUpgrade() {
        return isUnlocked && level < maxLevel && seedPackets >= getSeedPacketsNeeded();
    }

    public int getUpgradeCost() {
        return 50 + (level * 10);
    }

    public void performUpgrade() {
        if (canUpgrade()) {
            int needed = getSeedPacketsNeeded();
            level++;
            seedPackets -= needed;
            // افزایش قدرت
            baseHp += 50;
            damage += 5;
        }
    }
}
