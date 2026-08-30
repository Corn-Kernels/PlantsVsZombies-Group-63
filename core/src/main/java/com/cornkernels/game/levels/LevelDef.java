package com.cornkernels.game.levels;

import com.cornkernels.game.entities.types.zombies.ZombieDef;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum LevelDef {

    CHAPTER1_LEVEL1(1, 1,
        List.of(ZombieDef.DEFAULT),
        new int[]{3, 4, 6}),
    CHAPTER1_LEVEL2(1, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1),
        new int[]{4, 5, 5, 8}),
    CHAPTER1_LEVEL3(1, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.IMP),
        new int[]{4, 5, 6, 6, 10}),
    CHAPTER1_LEVEL4(1, 4,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ARMOR2, ZombieDef.IMP),
        new int[]{5, 6, 7, 7, 12}),

    CHAPTER2_LEVEL1(2, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ARMOR2),
        new int[]{6, 7, 8, 12}),
    CHAPTER2_LEVEL2(2, 2,
        List.of(ZombieDef.ARMOR1, ZombieDef.ARMOR2, ZombieDef.ARMOR4, ZombieDef.IMP),
        new int[]{6, 7, 8, 9, 14}),
    CHAPTER2_LEVEL3(2, 3,
        List.of(ZombieDef.ARMOR1, ZombieDef.ARMOR2, ZombieDef.ARMOR4, ZombieDef.IMP, ZombieDef.GARGANTUAR),
        new int[]{6, 8, 9, 10, 16}),
    CHAPTER2_LEVEL4(2, 4,
        List.of(ZombieDef.ARMOR2, ZombieDef.ARMOR4, ZombieDef.DARK_ARMOR3, ZombieDef.IMP, ZombieDef.GARGANTUAR),
        new int[]{7, 8, 10, 11, 18}),

    CHAPTER3_LEVEL1(3, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.RA),
        new int[]{8, 9, 10, 14}),
    CHAPTER3_LEVEL2(3, 2,
        List.of(ZombieDef.ARMOR1, ZombieDef.ARMOR2, ZombieDef.RA, ZombieDef.EXPLORER),
        new int[]{8, 10, 11, 16}),
    CHAPTER3_LEVEL3(3, 3,
        List.of(ZombieDef.ARMOR2, ZombieDef.ARMOR4, ZombieDef.EXPLORER, ZombieDef.TOMB_RAISER),
        new int[]{9, 10, 12, 18}),
    CHAPTER3_LEVEL4(3, 4,
        List.of(ZombieDef.ARMOR4, ZombieDef.DARK_ARMOR3, ZombieDef.TOMB_RAISER, ZombieDef.GARGANTUAR, ZombieDef.IMP),
        new int[]{9, 11, 13, 20}),

    CHAPTER4_LEVEL1(4, 1,
        List.of(ZombieDef.ARMOR2, ZombieDef.ARMOR4, ZombieDef.PROSPECTOR),
        new int[]{10, 12, 14, 18}),
    CHAPTER4_LEVEL2(4, 2,
        List.of(ZombieDef.ARMOR4, ZombieDef.DARK_ARMOR3, ZombieDef.WIZARD, ZombieDef.IMP),
        new int[]{11, 13, 15, 20}),
    CHAPTER4_LEVEL3(4, 3,
        List.of(ZombieDef.DARK_ARMOR3, ZombieDef.WIZARD, ZombieDef.DARK_JUGGLER, ZombieDef.GARGANTUAR),
        new int[]{12, 14, 16, 22}),
    CHAPTER4_LEVEL4(4, 4,
        List.of(ZombieDef.DARK_ARMOR3, ZombieDef.DARK_KING, ZombieDef.GARGANTUAR, ZombieDef.IMP, ZombieDef.DARK_JUGGLER),
        new int[]{13, 15, 17, 19, 25});

    public final int chapter;
    public final int levelNumber;
    public final List<ZombieDef> eligibleZombies;

    public final int[] zombiesPerWave;

    LevelDef(int chapter, int levelNumber, List<ZombieDef> eligibleZombies, int[] zombiesPerWave) {
        this.chapter = chapter;
        this.levelNumber = levelNumber;
        this.eligibleZombies = eligibleZombies;
        this.zombiesPerWave = zombiesPerWave;
    }

    public static @Nullable LevelDef of(int chapter, int levelNumber) {
        for (LevelDef def : values()) {
            if (def.chapter == chapter && def.levelNumber == levelNumber) return def;
        }
        return null;
    }

    public int getTotalWaves() {
        return zombiesPerWave.length;
    }

    public boolean isLastLevelInChapter() {
        return of(chapter, levelNumber + 1) == null;
    }
}
