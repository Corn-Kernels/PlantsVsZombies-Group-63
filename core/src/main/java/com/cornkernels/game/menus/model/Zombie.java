package com.cornkernels.game.menus.model;

public class Zombie {
    private String alias;
    private String name;
    private int hitpoints;
    private float speed;
    private int eatDPS;
    private String[] armorTypes;
    private String description;
    private String imagePath;
    private boolean isSeen;
    private String chapter;

    // ===== Full Constructor =====
    public Zombie(String alias, String name, int hitpoints, float speed, int eatDPS,
                  String[] armorTypes, String description, String imagePath, String chapter) {
        this.alias = alias;
        this.name = name;
        this.hitpoints = hitpoints;
        this.speed = speed;
        this.eatDPS = eatDPS;
        this.armorTypes = armorTypes;
        this.description = description;
        this.imagePath = imagePath;
        this.isSeen = false;
        this.chapter = chapter;
    }

    // ===== Getters =====
    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public float getSpeed() {
        return speed;
    }

    public int getEatDPS() {
        return eatDPS;
    }

    public String[] getArmorTypes() {
        return armorTypes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isSeen() {
        return isSeen;
    }

    // ===== Setters =====
    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getChapter() {
        return chapter;
    }
}
