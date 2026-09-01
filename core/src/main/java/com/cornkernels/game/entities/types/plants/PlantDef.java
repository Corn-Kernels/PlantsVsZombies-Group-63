package com.cornkernels.game.entities.types.plants;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public enum PlantDef {

    // ==========================================
    // FULLY IMPLEMENTED PLANTS
    // ==========================================
    //region IMPLEMENTED PLANTS

    // ------------------------------------------
    // Sun Producers
    // Plants that generate sun points over time.
    // ------------------------------------------
    //region Sun Producers
    SUNFLOWER1(1001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 300, "0", 24.0f, 5f),
    SUNFLOWER2(2001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 300, "0", 22.0f, 5f),
    SUNFLOWER3(3001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 450, "0", 22.0f, 5f),
    SUNFLOWER4(4001, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 450, "0", 22.0f, 5f),

    TWIN_SUNFLOWER1(1002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 125, 300, "0", 24.0f, 15f),
    TWIN_SUNFLOWER2(2002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 125, 300, "0", 22.0f, 15f),
    TWIN_SUNFLOWER3(3002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 125, 450, "0", 22.0f, 15f),
    TWIN_SUNFLOWER4(4002, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 100, 450, "0", 22.0f, 15f),

    PRIMAL_SUNFLOWER1(1004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 75, 300, "0", 24.0f, 5f),
    PRIMAL_SUNFLOWER2(2004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 75, 300, "0", 22.0f, 5f),
    PRIMAL_SUNFLOWER3(3004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 75, 450, "0", 22.0f, 5f),
    PRIMAL_SUNFLOWER4(4004, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 50, 450, "0", 22.0f, 5f),

    SUN_SHROOM1(1003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 300, "0", 0.0f, 5f),
    SUN_SHROOM2(2003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 300, "0", 0.0f, 5f),
    SUN_SHROOM3(3003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 450, "0", 0.0f, 5f),
    SUN_SHROOM4(4003, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 450, "0", 0.0f, 5f),
    //endregion

    // ------------------------------------------
    // Peashooters
    // Straight-shooting projectile plants.
    // ------------------------------------------
    //region Peashooters
    PEASHOOTER1(1006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 300, "20", 1.0f, 5f),
    PEASHOOTER2(2006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 300, "30", 0.8f, 5f),
    PEASHOOTER3(3006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 450, "30", 0.8f, 5f),
    PEASHOOTER4(4006, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 75, 450, "30", 0.8f, 5f),

    REPEATER1(1007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 300, "20x2", 1.5f, 5f),
    REPEATER2(2007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 300, "30x2", 1.5f, 5f),
    REPEATER3(3007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 500, "30x2", 1.5f, 5f),
    REPEATER4(4007, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 500, "30x2", 1.5f, 5f),

    SNOW_PEA1(1009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 150, 300, "20", 1.5f, 5f),
    SNOW_PEA2(2009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 150, 300, "30", 1.5f, 5f),
    SNOW_PEA3(3009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 150, 450, "30", 1.5f, 5f),
    SNOW_PEA4(4009, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 125, 450, "30", 1.5f, 5f),

    FIRE_PEASHOOTER1(1018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 175, 300, "20", 1.5f, 5f),
    FIRE_PEASHOOTER2(2018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 175, 300, "30", 1.5f, 5f),
    FIRE_PEASHOOTER3(3018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 175, 500, "30", 1.5f, 5f),
    FIRE_PEASHOOTER4(4018, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 150, 500, "30", 1.5f, 5f),

    MEGA_GATLING_PEA1(1021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 400, 300, "20x4", 1.5f, 5f),
    MEGA_GATLING_PEA2(2021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 400, 300, "30x4", 1.5f, 5f),
    MEGA_GATLING_PEA3(3021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 400, 300, "30x4", 1.5f, 5f),
    MEGA_GATLING_PEA4(4021, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 350, 300, "30x4", 1.5f, 5f),

    THREEPEATER1(1008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 300, 300, "20", 1.5f, 5f),
    THREEPEATER2(2008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 275, 300, "20", 1.5f, 5f),
    THREEPEATER3(3008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 275, 300, "30", 1.5f, 5f),
    THREEPEATER4(4008, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 275, 500, "30", 1.5f, 5f),

    PEA_POD1(1011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 125, 300, "20/40/60/80/100", 1.5f, 5f),
    PEA_POD2(2011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 125, 300, "30/60/90/120/150", 1.5f, 5f),
    PEA_POD3(3011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 125, 500, "30/60/90/120/150", 1.5f, 5f),
    PEA_POD4(4011, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 100, 500, "30/60/90/120/150", 1.5f, 5f),

    SPLIT_PEA1(1012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 125, 300, "20", 1.5f, 5f),
    SPLIT_PEA2(2012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 125, 300, "30", 1.5f, 5f),
    SPLIT_PEA3(3012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 125, 500, "30", 1.5f, 5f),
    SPLIT_PEA4(4012, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 500, "30", 1.5f, 5f),
    //endregion

    // ------------------------------------------
    // Short Range & Spreaders
    // Plants that shoot multi-directionally or have limited range.
    // ------------------------------------------
    //region Short Range & Spreaders
    ROTOBAGA1(1010, "Rotobaga", PlantCategory.SHOOTER, tags(), 150, 300, "10x3", 1.5f, 5f),
    ROTOBAGA2(2010, "Rotobaga", PlantCategory.SHOOTER, tags(), 150, 300, "20x3", 1.5f, 5f),
    ROTOBAGA3(3010, "Rotobaga", PlantCategory.SHOOTER, tags(), 150, 450, "20x3", 1.5f, 5f),
    ROTOBAGA4(4010, "Rotobaga", PlantCategory.SHOOTER, tags(), 125, 450, "20x3", 1.5f, 5f),

    SEA_SHROOM1(1022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "20", 0.0f, 15f),
    SEA_SHROOM2(2022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "20", 0.0f, 15f),
    SEA_SHROOM3(3022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "25", 0.0f, 15f),
    SEA_SHROOM4(4022, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "25", 0.0f, 15f),

    PUFF_SHROOM1(1023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "20", 0.0f, 5f),
    PUFF_SHROOM2(2023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "20", 0.0f, 5f),
    PUFF_SHROOM3(3023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "30", 0.0f, 5f),
    PUFF_SHROOM4(4023, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "30", 0.0f, 5f),

    STARFRUIT1(1019, "Starfruit", PlantCategory.SHOOTER, tags(), 150, 300, "20", 1.5f, 5f),
    STARFRUIT2(2019, "Starfruit", PlantCategory.SHOOTER, tags(), 150, 300, "20", 1.35f, 5f),
    STARFRUIT3(3019, "Starfruit", PlantCategory.SHOOTER, tags(), 150, 300, "30", 1.35f, 5f),
    STARFRUIT4(4019, "Starfruit", PlantCategory.SHOOTER, tags(), 125, 300, "30", 1.35f, 5f),
    //endregion

    // ------------------------------------------
    // Charge & Special Shooters
    // Plants with custom shooting mechanics or ammo systems.
    // ------------------------------------------
    //region Charge & Special Shooters
    CITRON1(1013, "Citron", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 350, 300, "800", 9.0f, 5f),
    CITRON2(2013, "Citron", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 350, 300, "800", 8.0f, 5f),
    CITRON3(3013, "Citron", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 350, 300, "950", 8.0f, 5f),
    CITRON4(4013, "Citron", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 300, 300, "950", 8.0f, 5f),

    GOO_PEASHOOTER1(1020, "Goo Peashooter", PlantCategory.SHOOTER, tags(PlantTag.POISON), 125, 300, "20", 1.5f, 5f),
    GOO_PEASHOOTER2(2020, "Goo Peashooter", PlantCategory.SHOOTER, tags(PlantTag.POISON), 125, 300, "20", 1.5f, 5f),
    GOO_PEASHOOTER3(3020, "Goo Peashooter", PlantCategory.SHOOTER, tags(PlantTag.POISON), 125, 450, "20", 1.5f, 5f),
    GOO_PEASHOOTER4(4020, "Goo Peashooter", PlantCategory.SHOOTER, tags(PlantTag.POISON), 100, 450, "20", 1.5f, 5f),

    BOWLING_BULB1(1016, "Bowling Bulb", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 200, 300, "40/120/180", 0.0f, 5f),
    BOWLING_BULB2(2016, "Bowling Bulb", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 200, 300, "40/120/180", 0.0f, 5f),
    BOWLING_BULB3(3016, "Bowling Bulb", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 200, 300, "55/135/195", 0.0f, 5f),
    BOWLING_BULB4(4016, "Bowling Bulb", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 175, 300, "55/135/195", 0.0f, 5f),
    //endregion

    // ------------------------------------------
    // Strike-Through
    // Projectiles that pierce multiple targets.
    // ------------------------------------------
    //region Strike-Through
    CACTUS1(1017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 175, 300, "30", 1.5f, 5f),
    CACTUS2(2017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 175, 300, "30", 1.5f, 5f),
    CACTUS3(3017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 175, 300, "40", 1.5f, 5f),
    CACTUS4(4017, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 150, 300, "40", 1.5f, 5f),

    FUME_SHROOM1(1024, "Fume-shroom", PlantCategory.STRIKE_THROUGH, tags(PlantTag.SHROOM), 125, 300, "20", 1.5f, 5f),
    FUME_SHROOM2(2024, "Fume-shroom", PlantCategory.STRIKE_THROUGH, tags(PlantTag.SHROOM), 125, 300, "20", 1.5f, 5f),
    FUME_SHROOM3(3024, "Fume-shroom", PlantCategory.STRIKE_THROUGH, tags(PlantTag.SHROOM), 125, 300, "30", 1.5f, 5f),
    FUME_SHROOM4(4024, "Fume-shroom", PlantCategory.STRIKE_THROUGH, tags(PlantTag.SHROOM), 100, 300, "30", 1.5f, 5f),
    //endregion

    // ------------------------------------------
    // Lobbers
    // Plants that lob projectiles over obstacles.
    // ------------------------------------------
    //region Lobbers
    CABBAGE_PULT1(1025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 300, "40", 2.9f, 5f),
    CABBAGE_PULT2(2025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 300, "50", 2.9f, 5f),
    CABBAGE_PULT3(3025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 300, "50", 2.46f, 5f),
    CABBAGE_PULT4(4025, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 450, "50", 2.46f, 5f),

    KERNEL_PULT1(1026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 300, "30/40", 2.9f, 5f),
    KERNEL_PULT2(2026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 300, "30/40", 2.9f, 5f),
    KERNEL_PULT3(3026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 300, "40/50", 2.9f, 5f),
    KERNEL_PULT4(4026, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 450, "40/50", 2.9f, 5f),

    MELON_PULT1(1027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 325, 300, "80", 2.9f, 5f),
    MELON_PULT2(2027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 300, 300, "80", 2.9f, 5f),
    MELON_PULT3(3027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 300, 300, "80", 2.9f, 5f),
    MELON_PULT4(4027, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 300, 300, "110", 2.9f, 5f),

    WINTER_MELON1(1028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 500, 300, "80", 2.9f, 5f),
    WINTER_MELON2(2028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 450, 300, "80", 2.9f, 5f),
    WINTER_MELON3(3028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 450, 300, "80", 2.9f, 5f),
    WINTER_MELON4(4028, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 425, 300, "80", 2.9f, 5f),

    PEPPER_PULT1(1029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 200, 300, "50", 2.9f, 5f),
    PEPPER_PULT2(2029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 200, 300, "65", 2.9f, 5f),
    PEPPER_PULT3(3029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 200, 300, "65", 2.9f, 5f),
    PEPPER_PULT4(4029, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 175, 300, "65", 2.9f, 5f),
    //endregion

    // ------------------------------------------
    // Melee
    // Close-range physical attackers.
    // ------------------------------------------
    //region Melee
    BONK_CHOY1(1039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 300, "15", 0.25f, 5f),
    BONK_CHOY2(2039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 300, "20", 0.25f, 5f),
    BONK_CHOY3(3039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 300, "20", 0.22f, 5f),
    BONK_CHOY4(4039, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 500, "20", 0.22f, 5f),

    PHAT_BEET1(1040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 300, "15", 2.0f, 5f),
    PHAT_BEET2(2040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 300, "25", 2.0f, 5f),
    PHAT_BEET3(3040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 300, "25", 1.8f, 5f),
    PHAT_BEET4(4040, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 500, "25", 1.8f, 5f),

    CHOMPER1(1041, "Chomper", PlantCategory.MELEE, tags(), 150, 300, "Insta-kill", 0.0f, 5f),
    CHOMPER2(2041, "Chomper", PlantCategory.MELEE, tags(), 150, 300, "Insta-kill", 0.0f, 5f),
    CHOMPER3(3041, "Chomper", PlantCategory.MELEE, tags(), 150, 500, "Insta-kill", 0.0f, 5f),
    CHOMPER4(4041, "Chomper", PlantCategory.MELEE, tags(), 150, 500, "Insta-kill", 0.0f, 5f),

    WASABI_WHIP1(1042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 300, "40", 2.0f, 5f),
    WASABI_WHIP2(2042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 300, "50", 2.0f, 5f),
    WASABI_WHIP3(3042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 300, "50", 2.0f, 5f),
    WASABI_WHIP4(4042, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 500, "50", 2.0f, 5f),

    KIWIBEAST1(1043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 3000, "15/30/45", 2.0f, 5f),
    KIWIBEAST2(2043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 5000, "15/30/45", 2.0f, 5f),
    KIWIBEAST3(3043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 5000, "30/45/60", 2.0f, 5f),
    KIWIBEAST4(4043, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 5000, "30/45/60", 2.0f, 5f),
    //endregion

    // ------------------------------------------
    // Wall-nuts
    // Defensive plants with high HP.
    // ------------------------------------------
    //region Wall-nuts
    WALL_NUT1(1044, "Wall-nut", PlantCategory.WALL_NUT, tags(), 50, 4000, "0", null, 20f),
    WALL_NUT2(2044, "Wall-nut", PlantCategory.WALL_NUT, tags(), 50, 5000, "0", null, 20f),
    WALL_NUT3(3044, "Wall-nut", PlantCategory.WALL_NUT, tags(), 50, 5000, "0", null, 15f),
    WALL_NUT4(4044, "Wall-nut", PlantCategory.WALL_NUT, tags(), 50, 6500, "0", null, 15f),

    TALL_NUT1(1045, "Tall-nut", PlantCategory.WALL_NUT, tags(), 125, 8000, "0", null, 20f),
    TALL_NUT2(2045, "Tall-nut", PlantCategory.WALL_NUT, tags(), 125, 10000, "0", null, 20f),
    TALL_NUT3(3045, "Tall-nut", PlantCategory.WALL_NUT, tags(), 125, 10000, "0", null, 15f),
    TALL_NUT4(4045, "Tall-nut", PlantCategory.WALL_NUT, tags(), 125, 13000, "0", null, 15f),

    ENDURIAN1(1046, "Endurian", PlantCategory.WALL_NUT, tags(), 100, 3000, "20", null, 15f),
    ENDURIAN2(2046, "Endurian", PlantCategory.WALL_NUT, tags(), 100, 3000, "25", null, 15f),
    ENDURIAN3(3046, "Endurian", PlantCategory.WALL_NUT, tags(), 100, 4000, "25", null, 15f),
    ENDURIAN4(4046, "Endurian", PlantCategory.WALL_NUT, tags(), 75, 4000, "25", null, 15f),

    GARLIC1(1047, "Garlic", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 50, 300, "0", null, 20f),
    GARLIC2(2047, "Garlic", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 50, 450, "0", null, 20f),
    GARLIC3(3047, "Garlic", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 50, 450, "0", null, 17f),
    GARLIC4(4047, "Garlic", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 50, 700, "0", null, 17f),

    SWEET_POTATO1(1048, "Sweet Potato", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 150, 3000, "0", null, 20f),
    SWEET_POTATO2(2048, "Sweet Potato", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 150, 4000, "0", null, 20f),
    SWEET_POTATO3(3048, "Sweet Potato", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 150, 4000, "0", null, 15f),
    SWEET_POTATO4(4048, "Sweet Potato", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 150, 5500, "0", null, 15f),

    EXPLODE_O_NUT1(1049, "Explode-o-nut", PlantCategory.WALL_NUT, tags(PlantTag.EXPLOSIVE), 50, 8000, "1800", null, 20f),
    EXPLODE_O_NUT2(2049, "Explode-o-nut", PlantCategory.WALL_NUT, tags(PlantTag.EXPLOSIVE), 50, 10000, "1800", null, 20f),
    EXPLODE_O_NUT3(3049, "Explode-o-nut", PlantCategory.WALL_NUT, tags(PlantTag.EXPLOSIVE), 50, 10000, "2000", null, 20f),
    EXPLODE_O_NUT4(4049, "Explode-o-nut", PlantCategory.WALL_NUT, tags(PlantTag.EXPLOSIVE), 25, 10000, "2000", null, 20f),

    PUMPKIN1(1050, "Pumpkin", PlantCategory.WALL_NUT, tags(PlantTag.STACK), 150, 4000, "0", null, 20f),
    PUMPKIN2(2050, "Pumpkin", PlantCategory.WALL_NUT, tags(PlantTag.STACK), 150, 5000, "0", null, 20f),
    PUMPKIN3(3050, "Pumpkin", PlantCategory.WALL_NUT, tags(PlantTag.STACK), 150, 5000, "0", null, 15f),
    PUMPKIN4(4050, "Pumpkin", PlantCategory.WALL_NUT, tags(PlantTag.STACK), 150, 6500, "0", null, 15f),

    SUN_BEAN1(1051, "Sun Bean", PlantCategory.WALL_NUT, tags(PlantTag.SUN), 50, 1, "0", null, 20f),
    SUN_BEAN2(2051, "Sun Bean", PlantCategory.WALL_NUT, tags(PlantTag.SUN), 50, 1, "0", null, 20f),
    SUN_BEAN3(3051, "Sun Bean", PlantCategory.WALL_NUT, tags(PlantTag.SUN), 50, 1, "0", null, 20f),
    SUN_BEAN4(4051, "Sun Bean", PlantCategory.WALL_NUT, tags(PlantTag.SUN), 25, 1, "0", null, 20f),
    //endregion

    // ------------------------------------------
    // Explosives & Traps
    // Instant-use and armed proximity explosives.
    // ------------------------------------------
    //region Explosives
    POTATO_MINE1(1030, "Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 25, 100000, "1800", 0f, 25f),
    POTATO_MINE2(2030, "Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 25, 100000, "1800", 0f, 25f),
    POTATO_MINE3(3030, "Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 25, 100000, "1800", 0f, 20f),
    POTATO_MINE4(4030, "Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 25, 100000, "2400", 0f, 20f),

    PRIMAL_POTATO_MINE1(1031, "Primal Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 50, 100000, "2400", 0f, 5f),
    PRIMAL_POTATO_MINE2(2031, "Primal Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 50, 100000, "2400", 0f, 5f),
    PRIMAL_POTATO_MINE3(3031, "Primal Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 50, 100000, "2400", 0f, 2f),
    PRIMAL_POTATO_MINE4(4031, "Primal Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 50, 100000, "2800", 0f, 2f),

    CHERRY_BOMB1(1032, "Cherry Bomb", PlantCategory.EXPLOSIVE, tags(), 150, 100000, "1800", 0.0f, 35f),
    CHERRY_BOMB2(2032, "Cherry Bomb", PlantCategory.EXPLOSIVE, tags(), 150, 100000, "1800", 0.0f, 30f),
    CHERRY_BOMB3(3032, "Cherry Bomb", PlantCategory.EXPLOSIVE, tags(), 150, 100000, "2400", 0.0f, 30f),
    CHERRY_BOMB4(4032, "Cherry Bomb", PlantCategory.EXPLOSIVE, tags(), 125, 100000, "2400", 0.0f, 30f),

    JALAPENO1(1035, "Jalapeno", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 125, 100000, "1800", 0.0f, 35f),
    JALAPENO2(2035, "Jalapeno", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 125, 100000, "1800", 0.0f, 30f),
    JALAPENO3(3035, "Jalapeno", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 125, 100000, "2400", 0.0f, 30f),
    JALAPENO4(4035, "Jalapeno", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 100, 100000, "2400", 0.0f, 30f),

    DOOM_SHROOM1(1036, "Doom-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM), 125, 100000, "1800", 0.0f, 15f),
    DOOM_SHROOM2(2036, "Doom-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM), 125, 100000, "1800", 0.0f, 10f),
    DOOM_SHROOM3(3036, "Doom-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM), 125, 100000, "2600", 0.0f, 10f),
    DOOM_SHROOM4(4036, "Doom-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM), 75, 100000, "2600", 0.0f, 10f),

    ICE_SHROOM1(1057, "Ice-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM, PlantTag.ICE), 75, 100000, "20", 0.0f, 50f),
    ICE_SHROOM2(2057, "Ice-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM, PlantTag.ICE), 75, 100000, "20", 0.0f, 50f),
    ICE_SHROOM3(3057, "Ice-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM, PlantTag.ICE), 75, 100000, "20", 0.0f, 45f),
    ICE_SHROOM4(4057, "Ice-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM, PlantTag.ICE), 75, 100000, "70", 0.0f, 45f),

    GRAPESHOT1(1034, "Grapeshot", PlantCategory.EXPLOSIVE, tags(), 150, 100000, "1800", 0.0f, 35f),
    GRAPESHOT2(2034, "Grapeshot", PlantCategory.EXPLOSIVE, tags(), 150, 100000, "2400", 0.0f, 35f),
    GRAPESHOT3(3034, "Grapeshot", PlantCategory.EXPLOSIVE, tags(), 150, 100000, "2400", 0.0f, 35f),
    GRAPESHOT4(4034, "Grapeshot", PlantCategory.EXPLOSIVE, tags(), 125, 100000, "2400", 0.0f, 35f),

    TANGLE_KELP1(1037, "Tangle Kelp", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.WATER), 25, 100000, "Insta-kill", 0.0f, 15f),
    TANGLE_KELP2(2037, "Tangle Kelp", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.WATER), 25, 100000, "Insta-kill", 0.0f, 10f),
    TANGLE_KELP3(3037, "Tangle Kelp", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.WATER), 25, 100000, "Insta-kill", 0.0f, 10f),
    TANGLE_KELP4(4037, "Tangle Kelp", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.WATER), 0, 100000, "Insta-kill", 0.0f, 10f),

    SQUASH1(1033, "Squash", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP), 50, 100000, "1800", 0.0f, 20f),
    SQUASH2(2033, "Squash", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP), 50, 100000, "1800", 0.0f, 17f),
    SQUASH3(3033, "Squash", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP), 50, 100000, "2400", 0.0f, 17f),
    SQUASH4(4033, "Squash", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP), 50, 100000, "2400", 0.0f, 17f),

    HOT_POTATO1(1059, "Hot Potato", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 0, 100000, "0", 0.0f, 5f),
    HOT_POTATO2(2059, "Hot Potato", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 0, 100000, "0", 0.0f, 3f),
    HOT_POTATO3(3059, "Hot Potato", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 0, 100000, "0", 0.0f, 3f),
    HOT_POTATO4(4059, "Hot Potato", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 0, 100000, "1800", 0.0f, 3f),

    GRAVE_BUSTER1(1060, "Grave Buster", PlantCategory.EXPLOSIVE, tags(), 0, 300, "Insta-kill", 0.0f, 10f),
    GRAVE_BUSTER2(2060, "Grave Buster", PlantCategory.EXPLOSIVE, tags(), 0, 300, "Insta-kill", 0.0f, 10f),
    GRAVE_BUSTER3(3060, "Grave Buster", PlantCategory.EXPLOSIVE, tags(), 0, 300, "Insta-kill", 0.0f, 8f),
    GRAVE_BUSTER4(4060, "Grave Buster", PlantCategory.EXPLOSIVE, tags(), 0, 300, "1800", 0.0f, 8f),
    //endregion

    // ------------------------------------------
    // Homing
    // Plants whose projectiles track targets across lanes.
    // ------------------------------------------
    //region Homing
    CAT_TAIL1(1055, "Cat-tail", PlantCategory.HOMING, tags(), 175, 300, "15", 1.5f, 20f),
    CAT_TAIL2(2055, "Cat-tail", PlantCategory.HOMING, tags(), 175, 300, "25", 1.5f, 20f),
    CAT_TAIL3(3055, "Cat-tail", PlantCategory.HOMING, tags(), 175, 500, "25", 1.5f, 20f),
    CAT_TAIL4(4055, "Cat-tail", PlantCategory.HOMING, tags(), 150, 500, "25", 1.5f, 20f),

    ELECTRIC_BLUEBERRY1(1015, "Electric Blueberry", PlantCategory.HOMING, tags(PlantTag.CHARGE), 150, 300, "5000", 12.0f, 15f),
    ELECTRIC_BLUEBERRY2(2015, "Electric Blueberry", PlantCategory.HOMING, tags(PlantTag.CHARGE), 150, 300, "5000", 12.0f, 13f),
    ELECTRIC_BLUEBERRY3(3015, "Electric Blueberry", PlantCategory.HOMING, tags(PlantTag.CHARGE), 150, 300, "5000", 12.0f, 13f),
    ELECTRIC_BLUEBERRY4(4015, "Electric Blueberry", PlantCategory.HOMING, tags(PlantTag.CHARGE), 125, 300, "5000", 12.0f, 13f),

    CAULIPOWER1(1014, "Caulipower", PlantCategory.HOMING, tags(PlantTag.MAGIC, PlantTag.CHARGE), 250, 300, "9999999", 24.0f, 15f),
    CAULIPOWER2(2014, "Caulipower", PlantCategory.HOMING, tags(PlantTag.MAGIC, PlantTag.CHARGE), 250, 300, "9999999", 24.0f, 13f),
    CAULIPOWER3(3014, "Caulipower", PlantCategory.HOMING, tags(PlantTag.MAGIC, PlantTag.CHARGE), 250, 450, "9999999", 24.0f, 13f),
    CAULIPOWER4(4014, "Caulipower", PlantCategory.HOMING, tags(PlantTag.MAGIC, PlantTag.CHARGE), 200, 450, "9999999", 24.0f, 13f),
    //endregion

    TORCH_WOOD1(1052, "Torch Wood", PlantCategory.WALL_NUT, tags(PlantTag.FIRE), 175, 300, "0", 1000.0f, 5f),
    TORCH_WOOD2(2052, "Torch Wood", PlantCategory.WALL_NUT, tags(PlantTag.FIRE), 175, 600, "0", 1000.0f, 5f),
    TORCH_WOOD3(3052, "Torch Wood", PlantCategory.WALL_NUT, tags(PlantTag.FIRE), 175, 600, "0", 1000.0f, 5f),
    TORCH_WOOD4(4052, "Torch Wood", PlantCategory.WALL_NUT, tags(PlantTag.FIRE), 150, 600, "0", 1000.0f, 5f),


    // ==========================================
    // UNIMPLEMENTED PLANTS
    // These plants have mechanics handled externally
    // or are waiting for specific system triggers.
    // ==========================================
    //region UNIMPLEMENTED PLANTS

    // Shooters
    // None Currently

    // Homing
    MAGNET_SHROOM1(1053, "Magnet-shroom", PlantCategory.HOMING, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", 10.0f, 15f),
    MAGNET_SHROOM2(2053, "Magnet-shroom", PlantCategory.HOMING, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", 10.0f, 15f), // Range +1 Tile
    MAGNET_SHROOM3(3053, "Magnet-shroom", PlantCategory.HOMING, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", 10.0f, 10f), // Cooldown -5s
    MAGNET_SHROOM4(4053, "Magnet-shroom", PlantCategory.HOMING, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 500, "0", 10.0f, 10f), // HP +200

    // Explosives
    ICEBERG_LETTUCE1(1038, "Iceberg Lettuce", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.ICE), 0, 300, "0", null, 20f),
    ICEBERG_LETTUCE2(2038, "Iceberg Lettuce", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.ICE), 0, 300, "0", null, 18f), // Cooldown -2s
    ICEBERG_LETTUCE3(3038, "Iceberg Lettuce", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.ICE), 0, 300, "0", null, 18f), // Freeze Time +2s
    ICEBERG_LETTUCE4(4038, "Iceberg Lettuce", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.ICE), 0, 300, "0", null, 18f), // Cost -0

    // Modifiers / Environment
    HYPNO_SHROOM1(1054, "Hypno-shroom", PlantCategory.MODIFIER, tags(PlantTag.SHROOM, PlantTag.MAGIC), 125, 300, "0", null, 20f),
    HYPNO_SHROOM2(2054, "Hypno-shroom", PlantCategory.MODIFIER, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", null, 20f), // Cost -25
    HYPNO_SHROOM3(3054, "Hypno-shroom", PlantCategory.MODIFIER, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", null, 20f), // Zombie HP Buff
    HYPNO_SHROOM4(4054, "Hypno-shroom", PlantCategory.MODIFIER, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", null, 20f), // Zombie Dmg Buff

    IMITATER1(1056, "Imitater", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 0f),
    IMITATER2(2056, "Imitater", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 0f), // Cooldown -2s
    IMITATER3(3056, "Imitater", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 0f), // Cost -25
    IMITATER4(4056, "Imitater", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 0f), // plant food on enterance

    LILY_PAD1(1058, "Lily Pad", PlantCategory.MODIFIER, tags(PlantTag.WATER, PlantTag.STACK), 25, 300, "0", null, 5f),
    LILY_PAD2(2058, "Lily Pad", PlantCategory.MODIFIER, tags(PlantTag.WATER, PlantTag.STACK), 0, 300, "0", null, 5f), // Cost -25
    LILY_PAD3(3058, "Lily Pad", PlantCategory.MODIFIER, tags(PlantTag.WATER, PlantTag.STACK), 0, 500, "0", null, 5f), // HP +200
    LILY_PAD4(4058, "Lily Pad", PlantCategory.MODIFIER, tags(PlantTag.WATER, PlantTag.STACK), 0, 500, "0", null, 3f), // Cooldown -2s

    // Mints
    ENLIGHTEN_MINT1(1061, "Enlighten-mint", PlantCategory.SUN_PRODUCER, tags(), 0, 0, "0", null, 85f),
    ENLIGHTEN_MINT2(2061, "Enlighten-mint", PlantCategory.SUN_PRODUCER, tags(), 0, 0, "0", null, 85f), // Duration +1s
    ENLIGHTEN_MINT3(3061, "Enlighten-mint", PlantCategory.SUN_PRODUCER, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    ENLIGHTEN_MINT4(4061, "Enlighten-mint", PlantCategory.SUN_PRODUCER, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    APPEASE_MINT1(1062, "Appease-mint", PlantCategory.SHOOTER, tags(), 0, 0, "0", null, 85f),
    APPEASE_MINT2(2062, "Appease-mint", PlantCategory.SHOOTER, tags(), 0, 0, "0", null, 85f), // Duration +1s
    APPEASE_MINT3(3062, "Appease-mint", PlantCategory.SHOOTER, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    APPEASE_MINT4(4062, "Appease-mint", PlantCategory.SHOOTER, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    ARMA_MINT1(1063, "Arma-mint", PlantCategory.LOBBER, tags(), 0, 0, "0", null, 85f),
    ARMA_MINT2(2063, "Arma-mint", PlantCategory.LOBBER, tags(), 0, 0, "0", null, 85f), // Duration +1s
    ARMA_MINT3(3063, "Arma-mint", PlantCategory.LOBBER, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    ARMA_MINT4(4063, "Arma-mint", PlantCategory.LOBBER, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    BOMBARD_MINT1(1064, "Bombard-mint", PlantCategory.EXPLOSIVE, tags(), 0, 0, "0", null, 85f),
    BOMBARD_MINT2(2064, "Bombard-mint", PlantCategory.EXPLOSIVE, tags(), 0, 0, "0", null, 85f), // Duration +1s
    BOMBARD_MINT3(3064, "Bombard-mint", PlantCategory.EXPLOSIVE, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    BOMBARD_MINT4(4064, "Bombard-mint", PlantCategory.EXPLOSIVE, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    ENFORCE_MINT1(1065, "Enforce-mint", PlantCategory.MELEE, tags(), 0, 0, "0", null, 85f),
    ENFORCE_MINT2(2065, "Enforce-mint", PlantCategory.MELEE, tags(), 0, 0, "0", null, 85f), // Duration +1s
    ENFORCE_MINT3(3065, "Enforce-mint", PlantCategory.MELEE, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    ENFORCE_MINT4(4065, "Enforce-mint", PlantCategory.MELEE, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    REINFORCE_MINT1(1066, "Reinforce-mint", PlantCategory.WALL_NUT, tags(), 0, 0, "0", null, 85f),
    REINFORCE_MINT2(2066, "Reinforce-mint", PlantCategory.WALL_NUT, tags(), 0, 0, "0", null, 85f), // Duration +1s
    REINFORCE_MINT3(3066, "Reinforce-mint", PlantCategory.WALL_NUT, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    REINFORCE_MINT4(4066, "Reinforce-mint", PlantCategory.WALL_NUT, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    ENCHANT_MINT1(1067, "Enchant-mint", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 85f),
    ENCHANT_MINT2(2067, "Enchant-mint", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 85f), // Duration +1s
    ENCHANT_MINT3(3067, "Enchant-mint", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    ENCHANT_MINT4(4067, "Enchant-mint", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    PIERCE_MINT1(1068, "Pierce-mint", PlantCategory.STRIKE_THROUGH, tags(), 0, 0, "0", null, 85f),
    PIERCE_MINT2(2068, "Pierce-mint", PlantCategory.STRIKE_THROUGH, tags(), 0, 0, "0", null, 85f), // Duration +1s
    PIERCE_MINT3(3068, "Pierce-mint", PlantCategory.STRIKE_THROUGH, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    PIERCE_MINT4(4068, "Pierce-mint", PlantCategory.STRIKE_THROUGH, tags(), 0, 0, "0", null, 80f), // reset family cooldowns

    CATTAIL_MINT1(1069, "catTail-mint", PlantCategory.HOMING, tags(), 0, 0, "0", null, 85f),
    CATTAIL_MINT2(2069, "catTail-mint", PlantCategory.HOMING, tags(), 0, 0, "0", null, 85f), // Duration +1s
    CATTAIL_MINT3(3069, "catTail-mint", PlantCategory.HOMING, tags(), 0, 0, "0", null, 80f), // Cooldown -5s
    CATTAIL_MINT4(4069, "catTail-mint", PlantCategory.HOMING, tags(), 0, 0, "0", null, 80f); // reset family cooldowns
    //endregion

    private final int id;
    private final String plantName;
    private final PlantCategory category;
    private final PlantTag[] tags;
    private final int cost;
    private final int baseHp;
    private final String damage;
    private final Float actionInterval;
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

    public static @Nullable PlantDef getPlantTypeOfId(String plantId) {
        for (PlantDef plantDef : PlantDef.values()) {
            if (String.valueOf(plantDef.getId()).equals(plantId)) {
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

    @Contract(pure = true)
    public @NotNull Optional<Float> getActionInterval() {
        return Optional.ofNullable(actionInterval);
    }

    public float getRecharge() {
        return recharge;
    }
}
