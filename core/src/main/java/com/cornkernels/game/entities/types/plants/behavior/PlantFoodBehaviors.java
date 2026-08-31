package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.plantfoods.*;
import com.cornkernels.game.entities.types.plants.behavior.plantfoods.specific.RepeaterPlantFoodBehavior;
import com.cornkernels.game.entities.types.plants.behavior.plantfoods.specific.SunShroomPlantFoodBehavior;
import com.cornkernels.game.entities.types.plants.behavior.plantfoods.specific.ThreepeaterPlantFoodBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.PeaProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.TruePeaProjectile;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import com.cornkernels.game.map.Field;

import java.util.EnumMap;
import java.util.Map;

public class PlantFoodBehaviors {

    // 1. New record to bundle the behavior and its timer duration
    public record PlantFoodEntry(PlantFoodBehavior behavior, int activeTimeTicks) {}

    private static final Map<PlantDef, PlantFoodEntry> REGISTRY = new EnumMap<>(PlantDef.class);

    private static final PlantFoodBehavior NO_EFFECT = new PlantFoodBehavior() {
        @Override
        public boolean plantFood(PlantInstance plant, Field field) {
            return false;
        }
    };

    // Fallback entry for unimplemented plants (0 ticks)
    private static final PlantFoodEntry NO_EFFECT_ENTRY = new PlantFoodEntry(NO_EFFECT, 0);

    static {
        // ==========================================
        // IMPLEMENTED PLANT FOOD BEHAVIORS
        // ==========================================

        PlantDef[] rapidFirePlants = {
            // Standard Peas
            PlantDef.PEASHOOTER1, PlantDef.PEASHOOTER2, PlantDef.PEASHOOTER3, PlantDef.PEASHOOTER4,
            PlantDef.SNOW_PEA1, PlantDef.SNOW_PEA2, PlantDef.SNOW_PEA3, PlantDef.SNOW_PEA4,
            PlantDef.FIRE_PEASHOOTER1, PlantDef.FIRE_PEASHOOTER2, PlantDef.FIRE_PEASHOOTER3, PlantDef.FIRE_PEASHOOTER4,
            PlantDef.MEGA_GATLING_PEA1, PlantDef.MEGA_GATLING_PEA2, PlantDef.MEGA_GATLING_PEA3, PlantDef.MEGA_GATLING_PEA4,
            PlantDef.GOO_PEASHOOTER1, PlantDef.GOO_PEASHOOTER2, PlantDef.GOO_PEASHOOTER3, PlantDef.GOO_PEASHOOTER4,

            // Spreaders & Multi-Directional
            PlantDef.SPLIT_PEA1, PlantDef.SPLIT_PEA2, PlantDef.SPLIT_PEA3, PlantDef.SPLIT_PEA4,
            PlantDef.ROTOBAGA1, PlantDef.ROTOBAGA2, PlantDef.ROTOBAGA3, PlantDef.ROTOBAGA4,
            PlantDef.STARFRUIT1, PlantDef.STARFRUIT2, PlantDef.STARFRUIT3, PlantDef.STARFRUIT4,

            // Specialized Stackers & Piercers
            PlantDef.PEA_POD1, PlantDef.PEA_POD2, PlantDef.PEA_POD3, PlantDef.PEA_POD4,
            PlantDef.CACTUS1, PlantDef.CACTUS2, PlantDef.CACTUS3, PlantDef.CACTUS4
        };

        // Repeaters (Time = 40)[cite: 18]
        registerRepeaterPF(PlantDef.REPEATER1, 20, 80);
        registerRepeaterPF(PlantDef.REPEATER2, 30, 80);
        registerRepeaterPF(PlantDef.REPEATER3, 30, 80);
        registerRepeaterPF(PlantDef.REPEATER4, 30, 80);

        // Threepeaters (Time = 30)
        registerThreepeaterPF(PlantDef.THREEPEATER1, 20, 0, 60);
        registerThreepeaterPF(PlantDef.THREEPEATER2, 30, 0, 60);
        registerThreepeaterPF(PlantDef.THREEPEATER3, 30, 0, 60);
        registerThreepeaterPF(PlantDef.THREEPEATER4, 30, 0, 60);

        registerSunProducerPF(PlantDef.SUNFLOWER1, 2, SunType.BIG, 30);
        registerSunProducerPF(PlantDef.SUNFLOWER2, 2, SunType.BIG, 30);
        registerSunProducerPF(PlantDef.SUNFLOWER3, 2, SunType.BIG, 30);
        registerSunProducerPF(PlantDef.SUNFLOWER4, 2, SunType.BIG, 30);

        // Twin Sunflowers (Time = 30, 3 per wave)
        registerSunProducerPF(PlantDef.TWIN_SUNFLOWER1, 3, SunType.BIG, 30);
        registerSunProducerPF(PlantDef.TWIN_SUNFLOWER2, 3, SunType.BIG, 30);
        registerSunProducerPF(PlantDef.TWIN_SUNFLOWER3, 3, SunType.BIG, 30);
        registerSunProducerPF(PlantDef.TWIN_SUNFLOWER4, 3, SunType.BIG, 30);

        // Primal Sunflowers (Time = 30, 2 per wave, LARGE suns)
        registerSunProducerPF(PlantDef.PRIMAL_SUNFLOWER1, 2, SunType.LARGE, 30);
        registerSunProducerPF(PlantDef.PRIMAL_SUNFLOWER2, 2, SunType.LARGE, 30);
        registerSunProducerPF(PlantDef.PRIMAL_SUNFLOWER3, 2, SunType.LARGE, 30);
        registerSunProducerPF(PlantDef.PRIMAL_SUNFLOWER4, 2, SunType.LARGE, 30);

        registerSunShroomPF(PlantDef.SUN_SHROOM1, 3, SunType.LARGE, 30);
        registerSunShroomPF(PlantDef.SUN_SHROOM2, 3, SunType.LARGE, 30);
        registerSunShroomPF(PlantDef.SUN_SHROOM3, 3, SunType.LARGE, 30);
        registerSunShroomPF(PlantDef.SUN_SHROOM4, 3, SunType.LARGE, 30);

        registerDefensivePF(PlantDef.WALL_NUT1, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.WALL_NUT2, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.WALL_NUT3, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.WALL_NUT4, ArmorType.PLANT_ARMOR, 30);

        registerDefensivePF(PlantDef.ENDURIAN1, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.ENDURIAN2, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.ENDURIAN3, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.ENDURIAN4, ArmorType.PLANT_ARMOR, 30);

        registerDefensivePF(PlantDef.GARLIC1, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.GARLIC2, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.GARLIC3, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.GARLIC4, ArmorType.PLANT_ARMOR, 30);

        registerDefensivePF(PlantDef.SWEET_POTATO1, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.SWEET_POTATO2, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.SWEET_POTATO3, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.SWEET_POTATO4, ArmorType.PLANT_ARMOR, 30);

        registerDefensivePF(PlantDef.EXPLODE_O_NUT1, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.EXPLODE_O_NUT2, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.EXPLODE_O_NUT3, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.EXPLODE_O_NUT4, ArmorType.PLANT_ARMOR, 30);

        registerDefensivePF(PlantDef.PUMPKIN1, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.PUMPKIN2, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.PUMPKIN3, ArmorType.PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.PUMPKIN4, ArmorType.PLANT_ARMOR, 30);

        // Tall-nuts use the heavier armor variant
        registerDefensivePF(PlantDef.TALL_NUT1, ArmorType.TALL_PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.TALL_NUT2, ArmorType.TALL_PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.TALL_NUT3, ArmorType.TALL_PLANT_ARMOR, 30);
        registerDefensivePF(PlantDef.TALL_NUT4, ArmorType.TALL_PLANT_ARMOR, 30);

        // Swarm Refreshers (Time = 60, resets all shrooms, fires 30 shots)
        registerPuffShroomPF(PlantDef.PUFF_SHROOM1, 20, 60);
        registerPuffShroomPF(PlantDef.PUFF_SHROOM2, 20, 60);
        registerPuffShroomPF(PlantDef.PUFF_SHROOM3, 30, 60);
        registerPuffShroomPF(PlantDef.PUFF_SHROOM4, 30, 60);

        registerPuffShroomPF(PlantDef.SEA_SHROOM1, 20, 60);
        registerPuffShroomPF(PlantDef.SEA_SHROOM2, 20, 60);
        registerPuffShroomPF(PlantDef.SEA_SHROOM3, 25, 60);
        registerPuffShroomPF(PlantDef.SEA_SHROOM4, 25, 60);

        PlantDef[] lobbers = {
            PlantDef.CABBAGE_PULT1, PlantDef.CABBAGE_PULT2, PlantDef.CABBAGE_PULT3, PlantDef.CABBAGE_PULT4,
            PlantDef.MELON_PULT1, PlantDef.MELON_PULT2, PlantDef.MELON_PULT3, PlantDef.MELON_PULT4,
            PlantDef.WINTER_MELON1, PlantDef.WINTER_MELON2, PlantDef.WINTER_MELON3, PlantDef.WINTER_MELON4,
            PlantDef.PEPPER_PULT1, PlantDef.PEPPER_PULT2, PlantDef.PEPPER_PULT3, PlantDef.PEPPER_PULT4,
            PlantDef.KERNEL_PULT1, PlantDef.KERNEL_PULT2, PlantDef.KERNEL_PULT3, PlantDef.KERNEL_PULT4
        };
        LobberPlantFoodBehavior LOBBER_PF = new LobberPlantFoodBehavior();
        for (PlantDef def : lobbers) {
            register(def, LOBBER_PF, 30);
        }

        // ==========================================
        // UNIMPLEMENTED FALLBACKS
        // ==========================================
        RapidFirePlantFoodBehavior RAPID_PF = new RapidFirePlantFoodBehavior();
        for (PlantDef def : rapidFirePlants) {
        register(def, RAPID_PF, 60);
        }

        for (PlantDef def : PlantDef.values()) {
            if (!REGISTRY.containsKey(def)) {
                register(def, NO_EFFECT, 0);
            }
        }
    }

    private static void registerRepeaterPF(PlantDef def, int defaultDamage, int time) {
        int normalDamage = parseDamage(def, defaultDamage);
        int bigDamage = normalDamage * 60;

        register(def, new RepeaterPlantFoodBehavior(
            new TruePeaProjectile(normalDamage, null, 0),
            new TruePeaProjectile(bigDamage, null, 0)
        ), time);
    }

    private static void register(PlantDef def, PlantFoodBehavior behavior, int time) {
        REGISTRY.put(def, new PlantFoodEntry(behavior, time));
    }

    private static int parseDamage(PlantDef def, int fallback) {
        String dmgStr = def.getDamage();
        if (dmgStr == null || dmgStr.isEmpty() || dmgStr.equalsIgnoreCase("Insta-kill")) {
            return fallback;
        }
        String baseDmg = dmgStr.split("[x/]")[0].trim();
        try {
            return Integer.parseInt(baseDmg);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    // 2. Return the bundled Entry instead of just the behavior
    public static PlantFoodEntry getEntry(PlantDef def) {
        if (!REGISTRY.containsKey(def)) {
            return NO_EFFECT_ENTRY;
        }
        return REGISTRY.get(def);
    }

    private static void registerThreepeaterPF(PlantDef def, int defaultDamage, int heat, int time) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new ThreepeaterPlantFoodBehavior(new TruePeaProjectile(damage, null, heat)), time);
    }

    // Helper method for Sun Producers
    private static void registerSunProducerPF(PlantDef def, int sunsPerWave, SunType sunType, int time) {
        register(def, new SunProducerPlantFoodBehavior(sunsPerWave, sunType), time);
    }

    private static void registerSunShroomPF(PlantDef def, int sunsPerWave, SunType sunType, int time) {
        register(def, new SunShroomPlantFoodBehavior(sunsPerWave, sunType), time);
    }

    private static void registerDefensivePF(PlantDef def, ArmorType armorType, int time) {
        register(def, new DefensivePlantFoodBehavior(armorType), time);
    }

    private static void registerPuffShroomPF(PlantDef def, int defaultDamage, int time) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new PuffShroomPlantFoodBehavior(new PeaProjectile(damage, null)), time);
    }

}
