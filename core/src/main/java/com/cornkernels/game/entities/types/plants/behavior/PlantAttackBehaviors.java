package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.DirectShotBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.HomingShotBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.LobShotBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.SunBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.PeaProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.StrikeThroughProjectile;
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
        PlantAttackBehavior sunBehavior = new SunBehavior();
        register(PlantDef.SUNFLOWER, sunBehavior);
        register(PlantDef.TWIN_SUNFLOWER, sunBehavior);
        register(PlantDef.SUN_SHROOM, sunBehavior);
        register(PlantDef.PRIMAL_SUNFLOWER, sunBehavior);
        register(PlantDef.GOLD_BLOOM, sunBehavior);
        register(PlantDef.ENLIGHTEN_MINT, sunBehavior);
        //endregion
        // ==========================================
        // 2. DIRECT SHOOTERS
        // ==========================================
        //region Shooters
        // Reads damage and shot counts (e.g. "20x2", "20x4") directly from PlantDef
        registerDirectShooter(PlantDef.PEASHOOTER, 20, 1);
        registerDirectShooter(PlantDef.REPEATER, 20, 1);
        registerDirectShooter(PlantDef.THREEPEATER, 20, 1);
        registerDirectShooter(PlantDef.SNOW_PEA, 20, 1);
        registerDirectShooter(PlantDef.ROTOBAGA, 10, 3);
        registerDirectShooter(PlantDef.PEA_POD, 20, 1);
        registerDirectShooter(PlantDef.SPLIT_PEA, 20, 2);
        registerDirectShooter(PlantDef.CITRON, 800, 1);
        registerDirectShooter(PlantDef.BOWLING_BULB, 40, 1);
        registerDirectShooter(PlantDef.FIRE_PEASHOOTER, 40, 1);
        registerDirectShooter(PlantDef.STARFRUIT, 20, 5); // 5 directions
        registerDirectShooter(PlantDef.GOO_PEASHOOTER, 20, 1);
        registerDirectShooter(PlantDef.MEGA_GATLING_PEA, 20, 1);
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
        register(def, new LobShotBehavior(shots, new LobProjectile(damage, null, null,0)));
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
     * Parses the base damage from strings like "20", "20x2", or "40/120/180"[cite: 12].
     */
    private static int parseDamage(PlantDef def, int fallback) {
        String dmgStr = def.getDamage();
        if (dmgStr == null || dmgStr.isEmpty() || dmgStr.equalsIgnoreCase("Insta-kill")) {
            return fallback;
        }
        // Extracts the first numeric value before any 'x' or '/' separators[cite: 12]
        String baseDmg = dmgStr.split("[x/]")[0].trim();
        try {
            return Integer.parseInt(baseDmg);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * Parses the shot count from multiplier strings like "20x2" or "20x4"[cite: 12].
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
}
