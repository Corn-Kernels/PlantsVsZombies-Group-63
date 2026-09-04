package com.cornkernels.game.entities.types.sun;

import org.jetbrains.annotations.NotNull;

import java.util.random.RandomGenerator;

public enum SunType {
    SMALL(0, 5),
    NORMAL(80, 25),
    BIG(0, 50),
    LARGE(0, 75),
    SPECIAL(15, 100),
    RADIOACTIVE(5, 25);

    public final int spawnProbability;
    public final int value;

    SunType(int spawnProbability, int value) {
        this.spawnProbability = spawnProbability;
        this.value = value;
    }

    public static SunType random(@NotNull RandomGenerator rng) {
        int roll = rng.nextInt(100);
        int cumulative = 0;
        for (SunType type : values()) {
            cumulative += type.spawnProbability;
            if (roll < cumulative) return type;
        }
        return NORMAL;
    }
}
