package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.DirectShotBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.HomingShotBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.LobShotBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.SunBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.specific.SunShroomBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.PeaProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.StrikeThroughProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.TruePeaProjectile;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.map.Field;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PlantAttackBehaviors {

    private static final Map<PlantDef, PlantAttackBehavior> REGISTRY = new EnumMap<>(PlantDef.class);
    private static final Map<Integer, PlantAttackBehavior> REGISTRY_ID = new HashMap<>();

    private static final PlantAttackBehavior NO_ATTACK = new PlantAttackBehavior() {
        @Override
        public void execute(Entity self, Field field) {
            // No attack action required
        }

        @Override
        public boolean hasTarget(Entity self, Field field) {
            return false;
        }
    };

    static {
        // ==========================================
        // 1. SUN PRODUCERS
        // ==========================================
        //region Suns

        // --- Sunflower (Base: 50 Sun -> 1x BIG) ---
        register(PlantDef.SUNFLOWER1, new SunBehavior(1, SunType.BIG));
        register(PlantDef.SUNFLOWER2, new SunBehavior(1, SunType.BIG));
        register(PlantDef.SUNFLOWER3, new SunBehavior(1, SunType.BIG));
        // Lvl 4: Double Sun Chance (set to 50% here, adjust as needed)
        register(PlantDef.SUNFLOWER4, new SunBehavior(1, SunType.BIG, 0.2));

        // --- Twin Sunflower (Base: 100 Sun -> 2x BIG) ---
        register(PlantDef.TWIN_SUNFLOWER1, new SunBehavior(2, SunType.BIG));
        register(PlantDef.TWIN_SUNFLOWER2, new SunBehavior(2, SunType.BIG));
        register(PlantDef.TWIN_SUNFLOWER3, new SunBehavior(2, SunType.BIG));
        register(PlantDef.TWIN_SUNFLOWER4, new SunBehavior(2, SunType.BIG));

        // --- Primal Sunflower (Base: 75 Sun -> 1x LARGE) ---
        register(PlantDef.PRIMAL_SUNFLOWER1, new SunBehavior(1, SunType.LARGE));
        register(PlantDef.PRIMAL_SUNFLOWER2, new SunBehavior(1, SunType.LARGE));
        register(PlantDef.PRIMAL_SUNFLOWER3, new SunBehavior(1, SunType.LARGE));
        register(PlantDef.PRIMAL_SUNFLOWER4, new SunBehavior(1, SunType.LARGE));

        // --- Sun-shroom (Base ID: 3) ---
        // Parameters: (secondsToStage2, secondsToStage3, produceIntervalSeconds, doubleSunChance)
        register(PlantDef.SUN_SHROOM1, new SunShroomBehavior(24f, 72f, 24f, 0.0));
        // Lvl 2: Grow Time -5s (Stage 2 at 19s, Stage 3 at 67s)
        register(PlantDef.SUN_SHROOM2, new SunShroomBehavior(19f, 67f, 24f, 0.0));
        register(PlantDef.SUN_SHROOM3, new SunShroomBehavior(19f, 67f, 24f, 0.0));
        // Lvl 4: Double Sun Chance (50% applied here)
        register(PlantDef.SUN_SHROOM4, new SunShroomBehavior(19f, 67f, 24f, 0.5));
        //endregion

        // ==========================================
        // 2. DIRECT SHOOTERS
        // ==========================================

        //region NormalPeaShooters

        // --- LEVELED PEAS (Using TruePeaProjectile & Heat) ---

        // Peashooter (Heat 0: Normal)
        registerTruePeaShooter(PlantDef.PEASHOOTER1, 20, 1, 0);
        registerTruePeaShooter(PlantDef.PEASHOOTER2, 30, 1, 0);
        registerTruePeaShooter(PlantDef.PEASHOOTER3, 30, 1, 0);
        registerTruePeaShooter(PlantDef.PEASHOOTER4, 30, 1, 0);

        // Repeater (Heat 0: Normal) - Multi-shots are parsed via "20x2" string
        registerTruePeaShooter(PlantDef.REPEATER1, 20, 1, 0);
        registerTruePeaShooter(PlantDef.REPEATER2, 30, 1, 0);
        registerTruePeaShooter(PlantDef.REPEATER3, 30, 1, 0);
        registerTruePeaShooter(PlantDef.REPEATER4, 30, 1, 0);

        // Snow Pea (Heat -1: Cold)
        registerTruePeaShooter(PlantDef.SNOW_PEA1, 20, 1, -1);
        registerTruePeaShooter(PlantDef.SNOW_PEA2, 30, 1, -1);
        registerTruePeaShooter(PlantDef.SNOW_PEA3, 30, 1, -1);
        registerTruePeaShooter(PlantDef.SNOW_PEA4, 30, 1, -1);

        // Fire Peashooter (Heat 1: Inflamed) - Damage natively doubles inside TruePeaProjectile!
        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER1, 20, 1, 1);
        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER2, 30, 1, 1);
        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER3, 30, 1, 1);
        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER4, 30, 1, 1);

        // Mega Gatling Pea (Heat 0: Normal) - Multi-shots are parsed via "20x4" string
        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA1, 20, 1, 0);
        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA2, 30, 1, 0);
        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA3, 30, 1, 0);
        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA4, 30, 1, 0);

        //endregion

        //region rest

        // --- NON-LEVELED SHOOTERS (Fallbacks for plants you haven't given 4-digit IDs yet) ---
        //registerDirectShooter(PlantDef.THREEPEATER, 20, 1);
        registerDirectShooter(PlantDef.ROTOBAGA, 10, 3);
        //registerDirectShooter(PlantDef.PEA_POD, 20, 1);
        //registerDirectShooter(PlantDef.SPLIT_PEA, 20, 2);
        registerDirectShooter(PlantDef.CITRON, 800, 1);
        registerDirectShooter(PlantDef.BOWLING_BULB, 40, 1);
        registerDirectShooter(PlantDef.STARFRUIT, 20, 5);
        registerDirectShooter(PlantDef.GOO_PEASHOOTER, 20, 1);
        registerDirectShooter(PlantDef.SEA_SHROOM, 20, 1);
        registerDirectShooter(PlantDef.PUFF_SHROOM, 20, 1);
        //endregion

        // ==========================================
        // 3. STRIKE-THROUGH SHOOTERS (PIERCE)
        // ==========================================
        //region pierce
        register(PlantDef.CACTUS, new DirectShotBehavior(
            parseShotCount(PlantDef.CACTUS, 1),
            new StrikeThroughProjectile(parseDamage(PlantDef.CACTUS, 30), null, 3)
        ));
        register(PlantDef.FUME_SHROOM, new DirectShotBehavior(
            parseShotCount(PlantDef.FUME_SHROOM, 1),
            new StrikeThroughProjectile(parseDamage(PlantDef.FUME_SHROOM, 20), null, 100)
        ));
        //endregion

        // ==========================================
        // 4. LOBBERS
        // ==========================================
        registerLobber(PlantDef.CABBAGE_PULT, 40);
        registerLobber(PlantDef.KERNEL_PULT, 20);
        registerLobber(PlantDef.MELON_PULT, 80);
        registerLobber(PlantDef.WINTER_MELON, 80);
        registerLobber(PlantDef.PEPPER_PULT, 50);

        // ==========================================
        // 5. HOMING PLANTS
        // ==========================================
        registerHoming(PlantDef.CAULIPOWER, 1800);
        registerHoming(PlantDef.ELECTRIC_BLUEBERRY, 5000);
        registerHoming(PlantDef.CAT_TAIL, 15);

        // ==========================================
        // 6. FALLBACK FOR ALL REMAINING PLANTS
        // ==========================================
        for (PlantDef def : PlantDef.values()) {
            if (!REGISTRY.containsKey(def)) {
                register(def, NO_ATTACK);
            }
        }
    }

    private PlantAttackBehaviors() {
    }

    /**
     * Helper method to insert a behavior into both REGISTRY and REGISTRY_ID.
     */
    private static void register(PlantDef def, PlantAttackBehavior behavior) {
        REGISTRY.put(def, behavior);
        REGISTRY_ID.put(def.getId(), behavior);
    }

    /**
     * Helper to automatically parse damage and shot count from PlantDef for standard shooters.
     */
    private static void registerDirectShooter(PlantDef def, int defaultDamage, int defaultShots) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, defaultShots);
        register(def, new DirectShotBehavior(shots, new PeaProjectile(damage, null)));
    }

    /**
     * Helper to automatically parse damage from PlantDef for lobbers.
     */
    private static void registerLobber(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, 1);
        register(def, new LobShotBehavior(shots, new LobProjectile(damage, null, null, 0)));
    }

    /**
     * Helper to automatically parse damage from PlantDef for homing plants.
     */
    private static void registerHoming(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, 1);
        register(def, new HomingShotBehavior(shots, new HomingProjectile(damage, null, null)));
    }

    /**
     * Parses the base damage from strings like "20", "20x2", or "40/120/180".
     */
    private static int parseDamage(PlantDef def, int fallback) {
        String dmgStr = def.getDamage();
        if (dmgStr == null || dmgStr.isEmpty() || dmgStr.equalsIgnoreCase("Insta-kill")) {
            return fallback;
        }
        // Extracts the first numeric value before any 'x' or '/' separators
        String baseDmg = dmgStr.split("[x/]")[0].trim();
        try {
            return Integer.parseInt(baseDmg);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * Parses the shot count from multiplier strings like "20x2" or "20x4".
     */
    private static int parseShotCount(PlantDef def, int fallback) {
        String dmgStr = def.getDamage();
        if (dmgStr != null && dmgStr.contains("x")) {
            String[] parts = dmgStr.split("x");
            if (parts.length > 1) {
                try {
                    return Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    return fallback;
                }
            }
        }
        return fallback;
    }

    public static PlantAttackBehavior get(PlantDef def) {
        if (!REGISTRY.containsKey(def)) {
            throw new IllegalArgumentException("Unknown PlantDef: " + def);
        }
        return REGISTRY.get(def);
    }

    public static PlantAttackBehavior getById(int id) {
        if (REGISTRY_ID.containsKey(id)) {
            return REGISTRY_ID.get(id);
        }
        throw new IllegalArgumentException("Unknown PlantID: " + id);
    }

    /**
     * Helper to automatically parse damage and shot count from PlantDef for leveled True Peas.
     */
    private static void registerTruePeaShooter(PlantDef def, int defaultDamage, int defaultShots, int heat) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, defaultShots);
        register(def, new DirectShotBehavior(shots, new TruePeaProjectile(damage, null, heat)));
    }
}
