package com.cornkernels.game.entities.types.zombies.armors;

public enum ArmorType {
    CONE(370, false),
    BUCKET(1100, true),
    BRICK(2200, false),
    SHOULDER(1600, true),
    CROWN(1600, true),
    NEWSPAPER(800, false);

    private final int armorDamage;
    private final boolean isMetallic;

    ArmorType(int armorDamage, boolean isMetallic) {
        this.armorDamage = armorDamage;
        this.isMetallic = isMetallic;
    }

    public int getArmorDamage() {
        return armorDamage;
    }

    public boolean isMetallic() {
        return isMetallic;
    }
}
