package com.cornkernels.game.levels;

import com.cornkernels.game.entities.types.zombies.ZombieDef;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum LevelDef {

    // ---------------------------------------------------------
    // CHAPTER 1: The Lawn & The Graveyard
    // ---------------------------------------------------------
    CHAPTER1_LEVEL1(1, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.IMP),
        new int[]{
            1, 2, 3, 5, 8, 12, 16, 20 // Flag 1 (8 waves)
        },
        List.of()),

    CHAPTER1_LEVEL2(1, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.IMP, ZombieDef.ARMOR1, ZombieDef.ARMOR2),
        new int[]{
            1, 2, 4, 6, 9, 12, 16, 20,    // Flag 1 (8 waves)
            12, 15, 19, 24, 30, 38, 45    // Flag 2 (7 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 4, 7)
        )),

    CHAPTER1_LEVEL3(1, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ARMOR2, ZombieDef.NEWSPAPER, ZombieDef.TOMB_RAISER),
        new int[]{
            1, 2, 4, 7, 10, 14, 17, 20,   // Flag 1 (8 waves)
            14, 18, 23, 29, 36, 45, 55    // Flag 2 (7 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 1, 5),
            new ObstacleSpawn(ObstacleType.GRAVE, 3, 5),
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 0, 7),
            new ObstacleSpawn(ObstacleType.GRAVE, 4, 7)
        )),

    CHAPTER1_LEVEL4(1, 4,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ARMOR4, ZombieDef.NEWSPAPER, ZombieDef.GARGANTUAR),
        new int[]{
            1, 3, 5, 8, 12, 16, 18, 20,   // Flag 1 (8 waves)
            15, 20, 26, 34, 44, 56, 70    // Flag 2 (7 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 5),
            new ObstacleSpawn(ObstacleType.GRAVE, 1, 7),
            new ObstacleSpawn(ObstacleType.GRAVE, 3, 7)
        )),

    // ---------------------------------------------------------
    // CHAPTER 2: Speed, Sun, and Surf (Egypt + Beach/Lost City)
    // ---------------------------------------------------------
    CHAPTER2_LEVEL1(2, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.RA, ZombieDef.LOST_CITY_JANE),
        new int[]{
            1, 2, 4, 7, 10, 14, 17, 20,   // Flag 1 (8 waves)
            14, 18, 24, 32, 42, 52, 65    // Flag 2 (7 waves)
        },
        List.of()),

    CHAPTER2_LEVEL2(2, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.RA, ZombieDef.EXPLORER, ZombieDef.CRYSTAL_SKULL, ZombieDef.PROSPECTOR),
        new int[]{
            1, 3, 5, 8, 12, 16, 18, 20,   // Flag 1 (8 waves)
            16, 22, 30, 40, 52, 65, 80    // Flag 2 (7 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 0, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 4, 6)
        )),

    CHAPTER2_LEVEL3(2, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR2, ZombieDef.BEACH_SNORKEL, ZombieDef.BEACH_OCTOPUS, ZombieDef.BEACH_FISHERMAN),
        new int[]{
            1, 2, 4, 7, 10, 14, 17, 20,   // Flag 1 (8 waves)
            15, 20, 26, 34, 44, 55, 70,   // Flag 2 (7 waves)
            25, 35, 48, 65, 85, 110       // Flag 3 (6 waves)
        },
        List.of()),

    CHAPTER2_LEVEL4(2, 4,
        List.of(ZombieDef.DEFAULT, ZombieDef.RA, ZombieDef.EXPLORER, ZombieDef.CRYSTAL_SKULL, ZombieDef.PIANO),
        new int[]{
            1, 3, 5, 8, 12, 16, 18, 20,   // Flag 1 (8 waves)
            18, 24, 32, 42, 55, 70, 90,   // Flag 2 (7 waves)
            35, 48, 65, 85, 110, 140      // Flag 3 (6 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 2, 6)
        )),

    // ---------------------------------------------------------
    // CHAPTER 3: The Ice Age (Sliders, Glaciers & Modern/Arcade)
    // ---------------------------------------------------------
    CHAPTER3_LEVEL1(3, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ARMOR2, ZombieDef.ICE_AGE_DODO),
        new int[]{
            1, 3, 5, 8, 12, 16, 18, 20,   // Flag 1 (8 waves)
            18, 24, 32, 42, 55, 70, 90    // Flag 2 (7 waves)
        },
        // THE FUNNEL: Forces lanes 0, 1, 3, 4 into lane 2
        List.of(
            new ObstacleSpawn(ObstacleType.REDIRECTOR_UP, 0, 5),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_UP, 1, 3),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_DOWN, 4, 5),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_DOWN, 3, 3)
        )),

    CHAPTER3_LEVEL2(3, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.ARMOR1, ZombieDef.ICE_AGE_DODO, ZombieDef.MODERN_ALL_STAR),
        new int[]{
            1, 2, 4, 7, 10, 14, 17, 20,   // Flag 1 (8 waves)
            15, 20, 26, 34, 44, 55, 75,   // Flag 2 (7 waves)
            30, 40, 52, 66, 85, 115       // Flag 3 (6 waves)
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
            1, 3, 5, 8, 12, 16, 18, 20,   // Flag 1 (8 waves)
            18, 25, 34, 45, 58, 75, 100,  // Flag 2 (7 waves)
            40, 55, 75, 100, 130, 165     // Flag 3 (6 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GLACIER, 1, 7),
            new ObstacleSpawn(ObstacleType.GLACIER, 3, 7)
        )),

    CHAPTER3_LEVEL4(3, 4,
        List.of(ZombieDef.ARMOR2, ZombieDef.ICE_AGE_DODO, ZombieDef.ICE_AGE_HUNTER, ZombieDef.ICE_AGE_TROGLOBITE, ZombieDef.ARCADE, ZombieDef.GARGANTUAR),
        new int[]{
            2, 4, 7, 10, 14, 17, 19, 20,  // Flag 1 (8 waves)
            22, 30, 40, 52, 68, 88, 115,  // Flag 2 (7 waves)
            45, 65, 90, 120, 155, 200     // Flag 3 (6 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.REDIRECTOR_UP, 0, 7),
            new ObstacleSpawn(ObstacleType.REDIRECTOR_DOWN, 4, 7),
            new ObstacleSpawn(ObstacleType.GLACIER, 2, 5)
        )),

    // ---------------------------------------------------------
    // CHAPTER 4: Dark Ages
    // ---------------------------------------------------------
    CHAPTER4_LEVEL1(4, 1,
        List.of(ZombieDef.DEFAULT, ZombieDef.DARK_IMP_DRAGON, ZombieDef.DARK_ARMOR3, ZombieDef.WIZARD),
        new int[]{
            1, 3, 5, 8, 12, 16, 18, 20,   // Flag 1 (8 waves)
            18, 25, 34, 45, 58, 75, 100,  // Flag 2 (7 waves)
            40, 55, 75, 100, 130, 165     // Flag 3 (6 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.GRAVE, 1, 6),
            new ObstacleSpawn(ObstacleType.GRAVE, 3, 6)
        )),

    CHAPTER4_LEVEL2(4, 2,
        List.of(ZombieDef.DEFAULT, ZombieDef.DARK_IMP_DRAGON, ZombieDef.DARK_ARMOR3, ZombieDef.DARK_JUGGLER, ZombieDef.WIZARD),
        new int[]{
            2, 4, 7, 10, 14, 17, 19, 20,  // Flag 1 (8 waves)
            22, 30, 40, 52, 68, 88, 115,  // Flag 2 (7 waves)
            45, 65, 90, 120, 155, 200     // Flag 3 (6 waves)
        },
        List.of()),

    CHAPTER4_LEVEL3(4, 3,
        List.of(ZombieDef.DEFAULT, ZombieDef.DARK_ARMOR3, ZombieDef.DARK_JUGGLER, ZombieDef.WIZARD, ZombieDef.DARK_KING),
        new int[]{
            2, 5, 8, 12, 16, 18, 19, 20,  // Flag 1 (8 waves)
            25, 35, 48, 65, 85, 110, 140, // Flag 2 (7 waves)
            55, 75, 100, 135, 175, 230    // Flag 3 (6 waves)
        },
        List.of(
            new ObstacleSpawn(ObstacleType.ARCADE, 0, 8),
            new ObstacleSpawn(ObstacleType.ARCADE, 2, 8),
            new ObstacleSpawn(ObstacleType.ARCADE, 4, 8)
        )),

    CHAPTER4_LEVEL4(4, 4,
        List.of(ZombieDef.DARK_IMP_DRAGON, ZombieDef.DARK_ARMOR3, ZombieDef.DARK_JUGGLER, ZombieDef.WIZARD, ZombieDef.DARK_KING, ZombieDef.GARGANTUAR),
        new int[]{
            2, 4, 7, 10, 13, 16, 18, 20,       // Flag 1 (8 waves)
            15, 25, 40, 60, 85, 115, 150,      // Flag 2 (7 waves)
            50, 80, 115, 160, 215, 280,        // Flag 3 (6 waves)
            100, 160, 240, 340, 450            // Flag 4 (5 waves - The Ultimate Climax)
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
