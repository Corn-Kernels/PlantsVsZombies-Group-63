package com.cornkernels.game.levels;

import com.cornkernels.game.entities.types.zombies.ZombieDef;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum LevelDef {

    // ---------------------------------------------------------
    // CHAPTER 1: The Lawn & The Graveyard
    // ---------------------------------------------------------
    CHAPTER1_LEVEL1(1, 1,
        List.of(ZombieDef.DEFAULT),
        new int[]{
            4, 4, 5, 6, 8, 10, 14, 25 // 1 Flag
        },
        List.of()),

    CHAPTER1_LEVEL2(1, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1),
        new int[]{
            4, 5, 6, 7, 9, 12, 16, 30,     // Flag 1
            12, 15, 18, 22, 28, 36, 45, 60 // Flag 2
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 4, 7)
        )),

    CHAPTER1_LEVEL3(1, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.TOMB_RAISER),
        new int[]{
            4, 6, 8, 10, 12, 16, 22, 35,   // Flag 1
            15, 18, 24, 30, 38, 48, 60, 75 // Flag 2
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 1, 5),
            new ObstacleSpawn(ObstacleType.GRAVE, 3, 5),
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 0, 7),
            new ObstacleSpawn(ObstacleType.GRAVE, 4, 7)
        )),

    CHAPTER1_LEVEL4(1, 4,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ARMOR2, ZombieDef.GARGANTUAR),
        new int[]{
            4, 6, 8, 12, 16, 20, 28, 45,   // Flag 1
            18, 24, 30, 38, 48, 60, 75, 95 // Flag 2 (Gargantuar introduced)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 5),
            new ObstacleSpawn(ObstacleType.GRAVE, 1, 7),
            new ObstacleSpawn(ObstacleType.GRAVE, 3, 7)
        )),

    // ---------------------------------------------------------
    // CHAPTER 2: Speed, Sun, and Surf
    // ---------------------------------------------------------
    CHAPTER2_LEVEL1(2, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.RA),
        new int[]{
            4, 5, 7, 9, 12, 16, 22, 35,    // Flag 1
            15, 20, 26, 32, 40, 50, 65, 85 // Flag 2
        },
        List.of()),

    CHAPTER2_LEVEL2(2, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.RA, ZombieDef.EXPLORER),
        new int[]{
            4, 6, 8, 12, 16, 22, 30, 45,     // Flag 1
            20, 25, 32, 40, 50, 65, 80, 100  // Flag 2
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 0, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 4, 6)
        )),

    CHAPTER2_LEVEL3(2, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR2, ZombieDef.BEACH_SNORKEL, ZombieDef.BEACH_OCTOPUS),
        new int[]{
            4, 5, 7, 9, 12, 16, 22, 35,        // Flag 1
            15, 20, 26, 34, 44, 55, 75, 100,   // Flag 2
            30, 40, 50, 65, 80, 100, 125, 150  // Flag 3
        },
        List.of()),

    CHAPTER2_LEVEL4(2, 4,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR2, ZombieDef.RA, ZombieDef.EXPLORER, ZombieDef.PIANO),
        new int[]{
            4, 6, 8, 12, 16, 22, 30, 45,       // Flag 1
            20, 26, 34, 45, 58, 75, 100, 130,  // Flag 2
            40, 50, 65, 85, 105, 130, 160, 200 // Flag 3
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 6)
        )),

    // ---------------------------------------------------------
    // CHAPTER 3: The Ice Age (Sliders & Glaciers)
    // ---------------------------------------------------------
    CHAPTER3_LEVEL1(3, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ARMOR2),
        new int[]{
            4, 6, 8, 12, 16, 22, 30, 45,    // Flag 1
            20, 25, 32, 40, 50, 65, 80, 100 // Flag 2
        },
        // THE FUNNEL: Forces lanes 0, 1, 3, 4 into lane 2
        List.of(
            new ObstacleSpawn(ObstacleType.REDIRECTOR_UP, 0, 5),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_UP, 1, 3),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_DOWN, 4, 5),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_DOWN, 3, 3)
        )),

    CHAPTER3_LEVEL2(3, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ICE_AGE_DODO),
        new int[]{
            4, 5, 7, 9, 12, 16, 22, 35,        // Flag 1
            15, 20, 26, 34, 44, 55, 75, 100,   // Flag 2
            30, 40, 52, 66, 82, 105, 130, 165  // Flag 3
        },
        // THE ICE WALL: Blocks the front lines
        List.of(
            new ObstacleSpawn(ObstacleType.GLACIER, 0, 6),
            new ObstacleSpawn(ObstacleType.GLACIER, 2, 6),
            new ObstacleSpawn(ObstacleType.GLACIER, 4, 6)
        )),

    CHAPTER3_LEVEL3(3, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR2, ZombieDef.ICE_AGE_HUNTER, ZombieDef.ICE_AGE_TROGLOBITE),
        new int[]{
            4, 6, 8, 12, 16, 22, 30, 45,       // Flag 1
            20, 26, 34, 45, 58, 75, 100, 130,  // Flag 2
            40, 52, 66, 85, 110, 140, 175, 220 // Flag 3
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GLACIER, 1, 7),
            new ObstacleSpawn(ObstacleType.GLACIER, 3, 7)
        )),

    CHAPTER3_LEVEL4(3, 4,
        List.of(ZombieDef.ARMOR2, ZombieDef.ICE_AGE_DODO, ZombieDef.ICE_AGE_HUNTER, ZombieDef.ICE_AGE_TROGLOBITE, ZombieDef.GARGANTUAR),
        new int[]{
            4, 6, 10, 14, 20, 28, 38, 55,       // Flag 1
            25, 32, 42, 55, 70, 90, 115, 145,   // Flag 2
            50, 65, 85, 110, 140, 175, 220, 260 // Flag 3
        },
        List.of(
            new ObstacleSpawn(ObstacleType.REDIRECTOR_UP, 0, 7),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_DOWN, 4, 7),
            new ObstacleSpawn(ObstacleType.GLACIER, 2, 5)
        )),

    // ---------------------------------------------------------
    // CHAPTER 4: Dark Ages & Modern Arcades
    // ---------------------------------------------------------
    CHAPTER4_LEVEL1(4, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.DARK_ARMOR3, ZombieDef.WIZARD),
        new int[]{
            4, 6, 8, 12, 16, 22, 30, 45,       // Flag 1
            20, 26, 34, 45, 58, 75, 100, 130,  // Flag 2
            40, 52, 66, 85, 110, 140, 175, 220 // Flag 3
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 1, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 3, 6)
        )),

    CHAPTER4_LEVEL2(4, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.DARK_ARMOR3, ZombieDef.DARK_JUGGLER, ZombieDef.DARK_IMP_DRAGON),
        new int[]{
            4, 6, 10, 14, 20, 28, 38, 55,       // Flag 1
            25, 32, 42, 55, 70, 90, 115, 145,   // Flag 2
            50, 65, 85, 110, 140, 175, 220, 260 // Flag 3
        },
        List.of()),

    CHAPTER4_LEVEL3(4, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR4, ZombieDef.NEWSPAPER, ZombieDef.ARCADE),
        new int[]{
            4, 7, 12, 18, 25, 35, 48, 65,       // Flag 1
            30, 40, 52, 68, 88, 112, 145, 180,  // Flag 2
            60, 75, 95, 125, 160, 205, 260, 320 // Flag 3
        },
        // ARCADE ALLEY: Machines drop in the back, spawning their own threats
        List.of(
            new ObstacleSpawn(ObstacleType.ARCADE, 0, 8),
            new ObstacleSpawn(ObstacleType.ARCADE, 2, 8),
            new ObstacleSpawn(ObstacleType.ARCADE, 4, 8)
        )),

    CHAPTER4_LEVEL4(4, 4,
        List.of(ZombieDef.DARK_ARMOR3, ZombieDef.WIZARD, ZombieDef.DARK_KING, ZombieDef.MODERN_ALL_STAR, ZombieDef.GARGANTUAR),
        new int[]{
            4, 8, 12, 18, 26, 36, 48, 70,           // Flag 1
            35, 45, 58, 75, 95, 120, 150, 195,      // Flag 2
            70, 85, 105, 130, 165, 210, 260, 325,   // Flag 3
            100, 125, 155, 195, 245, 310, 390, 480  // Flag 4 (The Ultimate Climax)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.ARCADE, 2, 8),
            new ObstacleSpawn(ObstacleType.GLACIER, 0, 6),
            new ObstacleSpawn(ObstacleType.GLACIER, 4, 6),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_UP, 1, 7),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_DOWN, 3, 7)
        ));

    public enum ObstacleType {
        GRAVE, GLACIER, ARCADE, REDIRECTOR_UP, REDIRECTOR_DOWN
    }

    public static class ObstacleSpawn {
        public final ObstacleType type;
        public final int lane;
        public final int column;

        public ObstacleSpawn(ObstacleType type, int lane, int column) {
            this.type = type;
            this.lane = lane;
            this.column = column;
        }
    }

    public final int chapter;
    public final int levelNumber;
    public final List<ZombieDef> eligibleZombies;
    public final int[] waveBudgets;
    public final List<ObstacleSpawn> obstacles;

    LevelDef(int chapter, int levelNumber, List<ZombieDef> eligibleZombies, int[] waveBudgets, List<ObstacleSpawn> obstacles) {
        this.chapter = chapter;
        this.levelNumber = levelNumber;
        this.eligibleZombies = eligibleZombies;
        this.waveBudgets = waveBudgets;
        this.obstacles = obstacles;
    }

    public static @Nullable LevelDef of(int chapter, int levelNumber) {
        for (LevelDef def : values()) {
            if (def.chapter == chapter && def.levelNumber == levelNumber) return def;
        }
        return null;
    }

    public int getTotalWaves() {
        return waveBudgets.length;
    }

    public boolean isLastLevelInChapter() {
        return of(chapter, levelNumber + 1) == null;
    }
}
