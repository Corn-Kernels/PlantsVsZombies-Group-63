package com.cornkernels.game.entities.types.plants;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public enum PlantDef {
    //region sun stuff
    SUNFLOWER1(1001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 300, "0", 24.0f, 5f),
    SUNFLOWER2(2001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 300, "0", 22.0f, 5f), // Lvl 2: Prod. Time -2s
    SUNFLOWER3(3001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 450, "0", 22.0f, 5f), // Lvl 3: HP +150
    SUNFLOWER4(4001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 450, "0", 22.0f, 5f), // Lvl 4: Double Sun Chance (Handled in Behavior)

    // --- Twin Sunflower (Base ID: 2) ---
    TWIN_SUNFLOWER1(1002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 125, 300, "0", 24.0f, 15f),
    TWIN_SUNFLOWER2(2002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 125, 300, "0", 22.0f, 15f), // Lvl 2: Prod. Time -2s
    TWIN_SUNFLOWER3(3002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 125, 450, "0", 22.0f, 15f), // Lvl 3: HP +150
    TWIN_SUNFLOWER4(4002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 100, 450, "0", 22.0f, 15f), // Lvl 4: Cost -25

    // --- Primal Sunflower (Base ID: 4) ---
    PRIMAL_SUNFLOWER1(1004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 75, 300, "0", 24.0f, 5f),
    PRIMAL_SUNFLOWER2(2004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 75, 300, "0", 22.0f, 5f), // Lvl 2: Prod. Time -2s
    PRIMAL_SUNFLOWER3(3004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 75, 450, "0", 22.0f, 5f), // Lvl 3: HP +150
    PRIMAL_SUNFLOWER4(4004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 50, 450, "0", 22.0f, 5f), // Lvl 4: Cost -25

    // --- Sun-shroom (Base ID: 3) ---
    SUN_SHROOM1(1003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 300, "0", 0.0f, 5f),
    SUN_SHROOM2(2003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 300, "0", 0.0f, 5f), // Lvl 2: Grow Time -5s
    SUN_SHROOM3(3003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 450, "0", 0.0f, 5f), // Lvl 3: HP +150
    SUN_SHROOM4(4003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 450, "0", 0.0f, 5f), // Lvl 4: Double Sun Chance

    //endregion


    //region simplePea
    // --- Peashooter (Base ID: 6) ---
    PEASHOOTER1(1006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 300, "20", 1.5f, 5f),
    PEASHOOTER2(2006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 300, "30", 1.5f, 5f),
    PEASHOOTER3(3006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 450, "30", 1.5f, 5f),
    PEASHOOTER4(4006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 75, 450, "30", 1.5f, 5f),

    // --- Repeater (Base ID: 7) ---
    REPEATER1(1007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 300, "20x2", 1.5f, 5f),
    REPEATER2(2007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 300, "30x2", 1.5f, 5f),
    REPEATER3(3007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 500, "30x2", 1.5f, 5f),
    REPEATER4(4007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 500, "30x2", 1.5f, 5f),

    // --- Snow Pea (Base ID: 9) ---
    SNOW_PEA1(1009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 150, 300, "20", 1.5f, 5f),
    SNOW_PEA2(2009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 150, 300, "30", 1.5f, 5f),
    SNOW_PEA3(3009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 150, 450, "30", 1.5f, 5f),
    SNOW_PEA4(4009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 125, 450, "30", 1.5f, 5f),

    // --- Fire Peashooter (Base ID: 18) ---
    FIRE_PEASHOOTER1(1018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 175, 300, "20", 1.5f, 5f),
    FIRE_PEASHOOTER2(2018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 175, 300, "30", 1.5f, 5f),
    FIRE_PEASHOOTER3(3018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 175, 500, "30", 1.5f, 5f),
    FIRE_PEASHOOTER4(4018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 150, 500, "30", 1.5f, 5f),

    // --- Mega Gatling Pea (Base ID: 21) ---
    MEGA_GATLING_PEA1(1021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 400, 300, "20x4", 1.5f, 5f),
    MEGA_GATLING_PEA2(2021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 400, 300, "30x4", 1.5f, 5f),
    MEGA_GATLING_PEA3(3021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 400, 300, "30x4", 1.5f, 5f),
    MEGA_GATLING_PEA4(4021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 350, 300, "30x4", 1.5f, 5f),
    //endregion
    //region specialPea
    // --- Threepeater (Base ID: 8) ---
    THREEPEATER1(1008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 300, 300, "20", 1.5f, 5f),
    THREEPEATER2(2008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 275, 300, "20", 1.5f, 5f), // Lvl 2: Dmg +10
    THREEPEATER3(3008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 275, 300, "30", 1.5f, 5f), // Lvl 3: HP +150
    THREEPEATER4(4008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 275, 500, "30", 1.5f, 5f), // Lvl 4: Cost -25

    // --- Pea Pod (Base ID: 11) ---
    PEA_POD1(1011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 125, 300, "20/40/60/80/100", 1.5f, 5f),
    PEA_POD2(2011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 125, 300, "30/60/90/120/150", 1.5f, 5f), // Lvl 2: Dmg +10 per stack
    PEA_POD3(3011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 125, 500, "30/60/90/120/150", 1.5f, 5f), // Lvl 3: HP +150
    PEA_POD4(4011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 100, 500, "30/60/90/120/150", 1.5f, 5f), // Lvl 4: Cost -25

    // --- Split Pea (Base ID: 12) ---
    SPLIT_PEA1(1012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 125, 300, "20", 1.5f, 5f),
    SPLIT_PEA2(2012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 125, 300, "30", 1.5f, 5f), // Lvl 2: Dmg +10
    SPLIT_PEA3(3012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 125, 500, "30", 1.5f, 5f), // Lvl 3: HP +150
    SPLIT_PEA4(4012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 500, "30", 1.5f, 5f), // Lvl 4: Cost -25
    //endregion

    // --- Rotobaga (Base ID: 10) ---
    // Level Upgrades: Base -> Lvl 2: Dmg +10 -> Lvl 3: HP +150 -> Lvl 4: Cost -25
    ROTOBAGA1(1010, "Rotobaga", PlantCategory.SHOOTER, tags(), 150, 300, "10x3", 1.5f, 5f),
    ROTOBAGA2(2010, "Rotobaga", PlantCategory.SHOOTER, tags(), 150, 300, "20x3", 1.5f, 5f),
    ROTOBAGA3(3010, "Rotobaga", PlantCategory.SHOOTER, tags(), 150, 450, "20x3", 1.5f, 5f),
    ROTOBAGA4(4010, "Rotobaga", PlantCategory.SHOOTER, tags(), 125, 450, "20x3", 1.5f, 5f),

    // --- Sea-shroom (Base ID: 22) ---
    // Level Upgrades: Base -> Lvl 2: Range +1 Tile -> Lvl 3: Dmg +5 -> Lvl 4: Lifespan +10s
    SEA_SHROOM1(1022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "20", 0.0f, 15f),
    SEA_SHROOM2(2022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "20", 0.0f, 15f),
    SEA_SHROOM3(3022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "25", 0.0f, 15f),
    SEA_SHROOM4(4022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "25", 0.0f, 15f),

    // --- Puff-shroom (Base ID: 23) ---
    // Level Upgrades: Base -> Lvl 2: Lifespan +10s -> Lvl 3: Dmg +10 -> Lvl 4: Range +1 Tile
    PUFF_SHROOM1(1023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "20", 0.0f, 5f),
    PUFF_SHROOM2(2023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "20", 0.0f, 5f),
    PUFF_SHROOM3(3023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "30", 0.0f, 5f),
    PUFF_SHROOM4(4023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "30", 0.0f, 5f),

    //region pulters
    // --- Cabbage-pult (Base ID: 25) ---
    CABBAGE_PULT1(1025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 300, "40", 2.9f, 5f),
    CABBAGE_PULT2(2025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 300, "50", 2.9f, 5f), // Lvl 2: Dmg +10
    CABBAGE_PULT3(3025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 300, "50", 2.46f, 5f), // Lvl 3: Atk Speed +15%
    CABBAGE_PULT4(4025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 450, "50", 2.46f, 5f), // Lvl 4: HP +150

    // --- Kernel-pult (Base ID: 26) ---
    KERNEL_PULT1(1026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 300, "30/40", 2.9f, 5f),
    KERNEL_PULT2(2026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 300, "30/40", 2.9f, 5f), // Lvl 2: Butter +5% (Logic handled in projectile)
    KERNEL_PULT3(3026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 300, "40/50", 2.9f, 5f), // Lvl 3: Dmg +10
    KERNEL_PULT4(4026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 450, "40/50", 2.9f, 5f), // Lvl 4: HP +150
    // --- Melon-pult (Base ID: 27) ---
    MELON_PULT1(1027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 325, 300, "80", 2.9f, 5f),
    MELON_PULT2(2027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 300, 300, "80", 2.9f, 5f), // Lvl 2: Cost -25
    MELON_PULT3(3027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 300, 300, "80", 2.9f, 5f), // Lvl 3: AoE Dmg +15
    MELON_PULT4(4027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 300, 300, "110", 2.9f, 5f), // Lvl 4: Dmg +30

    // --- Winter Melon (Base ID: 28) ---
    WINTER_MELON1(1028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 500, 300, "80", 2.9f, 5f),
    WINTER_MELON2(2028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 450, 300, "80", 2.9f, 5f), // Lvl 2: Cost -50
    WINTER_MELON3(3028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 450, 300, "80", 2.9f, 5f), // Lvl 3: AoE Dmg +15
    WINTER_MELON4(4028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 425, 300, "80", 2.9f, 5f), // Lvl 4: Cost -25

    // --- Pepper-pult (Base ID: 29) ---
    PEPPER_PULT1(1029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 200, 300, "50", 2.9f, 5f),
    PEPPER_PULT2(2029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 200, 300, "65", 2.9f, 5f), // Lvl 2: Dmg +15
    PEPPER_PULT3(3029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 200, 300, "65", 2.9f, 5f), // Lvl 3: Warmth Radius +1
    PEPPER_PULT4(4029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 175, 300, "65", 2.9f, 5f), // Lvl 4: Cost -25
    //endregion

    // --- Cactus (Base ID: 17) ---
    CACTUS1(1017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 175, 300, "30", 1.5f, 5f),
    CACTUS2(2017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 175, 300, "30", 1.5f, 5f), // Lvl 2: Pierce +1 (Handled in behavior)
    CACTUS3(3017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 175, 300, "40", 1.5f, 5f), // Lvl 3: Dmg +10
    CACTUS4(4017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 150, 300, "40", 1.5f, 5f), // Lvl 4: Cost -25

    // --- Starfruit (Base ID: 19) ---
    STARFRUIT1(1019, "Starfruit", PlantCategory.SHOOTER, tags(), 150, 300, "20", 1.5f, 5f),
    STARFRUIT2(2019, "Starfruit", PlantCategory.SHOOTER, tags(), 150, 300, "20", 1.35f, 5f), // Lvl 2: Atk Speed +10%
    STARFRUIT3(3019, "Starfruit", PlantCategory.SHOOTER, tags(), 150, 300, "30", 1.35f, 5f), // Lvl 3: Dmg +10
    STARFRUIT4(4019, "Starfruit", PlantCategory.SHOOTER, tags(), 125, 300, "30", 1.35f, 5f), // Lvl 4: Cost -25


    CITRON(13, "Citron", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 350, 300, "800", 9.0f, 5f),
    CAULIPOWER(14, "Caulipower", PlantCategory.HOMING, tags(PlantTag.MAGIC, PlantTag.CHARGE), 250, 300, "Insta-kill", 12.0f, 15f),
    ELECTRIC_BLUEBERRY(15, "Electric Blueberry", PlantCategory.HOMING, tags(PlantTag.CHARGE), 150, 300, "5000", 12.0f, 15f),
    BOWLING_BULB(16, "Bowling Bulb", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 200, 300, "40/120/180", 2.0f, 5f),
    GOO_PEASHOOTER(20, "Goo Peashooter", PlantCategory.SHOOTER, tags(PlantTag.POISON), 125, 300, "20", 1.5f, 5f),
    FUME_SHROOM(24, "Fume-shroom", PlantCategory.STRIKE_THROUGH, tags(PlantTag.SHROOM), 125, 300, "20", 1.5f, 5f),


    POTATO_MINE(30, "Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 25, 300, "1800", null, 25f),
    PRIMAL_POTATO_MINE(31, "Primal Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 50, 300, "2400", null, 5f),
    CHERRY_BOMB(32, "Cherry Bomb", PlantCategory.EXPLOSIVE, tags(), 150, 0, "1800", null, 35f),
    SQUASH(33, "Squash", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP), 50, 300, "1800", null, 20f),
    GRAPESHOT(34, "Grapeshot", PlantCategory.EXPLOSIVE, tags(), 150, 0, "1800", null, 35f),
    JALAPENO(35, "Jalapeno", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 125, 0, "1800", null, 35f),
    DOOM_SHROOM(36, "Doom-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM), 125, 0, "1800", null, 15f),
    TANGLE_KELP(37, "Tangle Kelp", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.WATER), 25, 300, "Insta-kill", null, 15f),
    ICEBERG_LETTUCE(38, "Iceberg Lettuce", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.ICE), 0, 300, "0", null, 20f),

    //region Melee
    // --- Bonk Choy (Base ID: 39) ---
    BONK_CHOY1(1039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 300, "15", 0.25f, 5f),
    BONK_CHOY2(2039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 300, "20", 0.25f, 5f), // Lvl 2: Dmg +5
    BONK_CHOY3(3039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 300, "20", 0.22f, 5f), // Lvl 3: Atk Speed +10% (0.25 -> 0.225)
    BONK_CHOY4(4039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 500, "20", 0.22f, 5f), // Lvl 4: HP +200

    // --- Phat Beet (Base ID: 40) ---
    PHAT_BEET1(1040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 300, "15", 2.0f, 5f),
    PHAT_BEET2(2040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 300, "25", 2.0f, 5f), // Lvl 2: Dmg +10
    PHAT_BEET3(3040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 300, "25", 1.8f, 5f), // Lvl 3: Atk Speed +10%
    PHAT_BEET4(4040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 500, "25", 1.8f, 5f), // Lvl 4: HP +200

    // --- Chomper (Base ID: 41) ---
    CHOMPER1(1041, "Chomper", PlantCategory.MELEE, tags(), 150, 300, "Insta-kill", 40.0f, 5f),
    CHOMPER2(2041, "Chomper", PlantCategory.MELEE, tags(), 150, 300, "Insta-kill", 38.0f, 5f), // Lvl 2: Digest -2s
    CHOMPER3(3041, "Chomper", PlantCategory.MELEE, tags(), 150, 500, "Insta-kill", 38.0f, 5f), // Lvl 3: HP +200
    CHOMPER4(4041, "Chomper", PlantCategory.MELEE, tags(), 150, 500, "Insta-kill", 35.0f, 5f), // Lvl 4: Digest -3s

    // --- Wasabi Whip (Base ID: 42) ---
    WASABI_WHIP1(1042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 300, "40", 2.0f, 5f),
    WASABI_WHIP2(2042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 300, "50", 2.0f, 5f), // Lvl 2: Dmg +10
    WASABI_WHIP3(3042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 300, "50", 2.0f, 5f), // Lvl 3: Range +1 Tile
    WASABI_WHIP4(4042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 500, "50", 2.0f, 5f), // Lvl 4: HP +200

    // --- Kiwibeast (Base ID: 43) ---
    KIWIBEAST1(1043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 300, "15/30/45", 2.0f, 5f),
    KIWIBEAST2(2043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 500, "15/30/45", 2.0f, 5f), // Lvl 2: HP +200
    KIWIBEAST3(3043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 500, "30/45/60", 2.0f, 5f), // Lvl 3: Dmg +15
    KIWIBEAST4(4043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 500, "30/45/60", 2.0f, 5f), // Lvl 4: Max Size +1 (Handled in behavior)
    //endregion

    WALL_NUT(44, "Wall-nut", PlantCategory.WALL_NUT, tags(), 50, 4000, "0", null, 20f),
    TALL_NUT(45, "Tall-nut", PlantCategory.WALL_NUT, tags(), 125, 8000, "0", null, 20f),
    ENDURIAN(46, "Endurian", PlantCategory.WALL_NUT, tags(), 100, 3000, "20", null, 15f),
    GARLIC(47, "Garlic", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 50, 300, "0", null, 20f),
    SWEET_POTATO(48, "Sweet Potato", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 150, 3000, "0", null, 20f),
    EXPLODE_O_NUT(49, "Explode-o-nut", PlantCategory.WALL_NUT, tags(PlantTag.EXPLOSIVE), 50, 4000, "1800", null, 20f),
    PUMPKIN(50, "Pumpkin", PlantCategory.WALL_NUT, tags(PlantTag.STACK), 150, 4000, "0", null, 20f),
    SUN_BEAN(51, "Sun Bean", PlantCategory.WALL_NUT, tags(PlantTag.SUN), 50, 1000, "0", null, 20f),

    TORCHWOOD(52, "Torchwood", PlantCategory.MODIFIER, tags(PlantTag.FIRE), 175, 300, "0", null, 5f),
    MAGNET_SHROOM(53, "Magnet-shroom", PlantCategory.HOMING, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", 10.0f, 15f),
    HYPNO_SHROOM(54, "Hypno-shroom", PlantCategory.MODIFIER, tags(PlantTag.SHROOM, PlantTag.MAGIC), 125, 300, "0", null, 20f),
    CAT_TAIL(55, "Cat-tail", PlantCategory.HOMING, tags(), 175, 300, "15", 1.5f, 20f),
    IMITATER(56, "Imitater", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 0f),
    ICE_SHROOM(57, "Ice-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM, PlantTag.ICE), 75, 0, "0", null, 50f),
    LILY_PAD(58, "Lily Pad", PlantCategory.MODIFIER, tags(PlantTag.WATER, PlantTag.STACK), 25, 300, "0", null, 5f),
    HOT_POTATO(59, "Hot Potato", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 0, 0, "0", null, 5f),
    GRAVE_BUSTER(60, "Grave Buster", PlantCategory.EXPLOSIVE, tags(), 0, 0, "Insta-kill", null, 10f),

    ENLIGHTEN_MINT(61, "Enlighten-mint", PlantCategory.SUN_PRODUCER, tags(), 0, 0, "0", null, 85f),
    APPEASE_MINT(62, "Appease-mint", PlantCategory.SHOOTER, tags(), 0, 0, "0", null, 85f),
    ARMA_MINT(63, "Arma-mint", PlantCategory.LOBBER, tags(), 0, 0, "0", null, 85f),
    BOMBARD_MINT(64, "Bombard-mint", PlantCategory.EXPLOSIVE, tags(), 0, 0, "0", null, 85f),
    ENFORCE_MINT(65, "Enforce-mint", PlantCategory.MELEE, tags(), 0, 0, "0", null, 85f),
    REINFORCE_MINT(66, "Reinforce-mint", PlantCategory.WALL_NUT, tags(), 0, 0, "0", null, 85f),
    ENCHANT_MINT(67, "Enchant-mint", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 85f),
    PIERCE_MINT(68, "Pierce-mint", PlantCategory.STRIKE_THROUGH, tags(), 0, 0, "0", null, 85f),
    CATTAIL_MINT(69, "catTail-mint", PlantCategory.HOMING, tags(), 0, 0, "0", null, 85f);

    private final int id;
    private final String plantName;
    private final PlantCategory category;
    private final PlantTag[] tags;
    private final int cost;
    private final int baseHp;
    private final String damage;
    private final Float actionInterval; // null when the plant has no repeating action interval
    private final float recharge;

    PlantDef(int id, String plantName, PlantCategory category, PlantTag[] tags,
             int cost, int baseHp, String damage, Float actionInterval, float recharge) {
        this.id = id;
        this.plantName = plantName;
        this.category = category;
        this.tags = tags;
        this.cost = cost;
        this.baseHp = baseHp;
        this.damage = damage;
        this.actionInterval = actionInterval;
        this.recharge = recharge;
    }

    private static PlantTag[] tags(PlantTag... tags) {
        return tags;
    }

    public static @Nullable PlantDef getPlantTypeOfName(String plantName) {
        for (PlantDef plantDef : PlantDef.values()) {
            if (plantDef.getPlantName().equals(plantName)) {
                return plantDef;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getPlantName() {
        return plantName;
    }

    public PlantCategory getCategory() {
        return category;
    }

    public PlantTag[] getTags() {
        return tags;
    }

    public int getCost() {
        return cost;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public String getDamage() {
        return damage;
    }

    /**
     * @return the action interval in seconds, or empty if this plant has none.
     */
    @Contract(pure = true)
    public @NotNull Optional<Float> getActionInterval() {
        return Optional.ofNullable(actionInterval);
    }

    public float getRecharge() {
        return recharge;
    }
}
