package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.*;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.specific.*;
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
        // Lvl 4: Double Sun Chance (set to 20% here, adjust as needed)
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

        //region TruePeaShooters

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

        // --- Threepeater ---
        registerThreepeater(PlantDef.THREEPEATER1, 20, 0);
        registerThreepeater(PlantDef.THREEPEATER2, 30, 0); // Using 30 as default fallback to match Lvl 2+ power
        registerThreepeater(PlantDef.THREEPEATER3, 30, 0);
        registerThreepeater(PlantDef.THREEPEATER4, 30, 0);

        // --- Pea Pod ---
        registerPeaPod(PlantDef.PEA_POD1, 20, 0);
        registerPeaPod(PlantDef.PEA_POD2, 30, 0);
        registerPeaPod(PlantDef.PEA_POD3, 30, 0);
        registerPeaPod(PlantDef.PEA_POD4, 30, 0);

        // --- Split Pea ---
        registerSplitPea(PlantDef.SPLIT_PEA1, 20, 0);
        registerSplitPea(PlantDef.SPLIT_PEA2, 30, 0);
        registerSplitPea(PlantDef.SPLIT_PEA3, 30, 0);
        registerSplitPea(PlantDef.SPLIT_PEA4, 30, 0);

        //endregion

        //region rest

        // ==========================================
        // ROTOBAGA & SHORT RANGE ATTACKERS
        // ==========================================

        // --- Rotobaga ---
        registerRotobaga(PlantDef.ROTOBAGA1, 10);
        registerRotobaga(PlantDef.ROTOBAGA2, 20);
        registerRotobaga(PlantDef.ROTOBAGA3, 20);
        registerRotobaga(PlantDef.ROTOBAGA4, 20);

        // --- Sea-shroom ---
        // Base Range: 3.0f tiles | Lvl 2+ Range: 4.0f tiles
        // Base Lifespan: 60s | Lvl 4 Lifespan: 70s
        register(PlantDef.SEA_SHROOM1, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM1, 20), null), 3.0f, 1.5f, 60.0f));
        register(PlantDef.SEA_SHROOM2, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM2, 20), null), 4.0f, 1.5f, 60.0f));
        register(PlantDef.SEA_SHROOM3, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM3, 25), null), 4.0f, 1.5f, 60.0f));
        register(PlantDef.SEA_SHROOM4, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM4, 25), null), 4.0f, 1.5f, 70.0f));

        // --- Puff-shroom ---
        // Base Lifespan: 60s | Lvl 2+ Lifespan: 70s
        // Base Range: 3.0f tiles | Lvl 4 Range: 4.0f tiles
        register(PlantDef.PUFF_SHROOM1, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM1, 20), null), 3.0f, 1.5f, 60.0f));
        register(PlantDef.PUFF_SHROOM2, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM2, 20), null), 3.0f, 1.5f, 70.0f));
        register(PlantDef.PUFF_SHROOM3, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM3, 30), null), 3.0f, 1.5f, 70.0f));
        register(PlantDef.PUFF_SHROOM4, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM4, 30), null), 4.0f, 1.5f, 70.0f));

        // ==========================================
        // CACTUS & STARFRUIT
        // ==========================================

        // --- Cactus ---
        registerCactus(PlantDef.CACTUS1, 30, 3);
        registerCactus(PlantDef.CACTUS2, 30, 4); // Lvl 2: Pierce +1
        registerCactus(PlantDef.CACTUS3, 40, 4); // Lvl 3: Dmg +10
        registerCactus(PlantDef.CACTUS4, 40, 4); // Lvl 4: Cost -25

        // --- Starfruit ---
        registerStarfruit(PlantDef.STARFRUIT1, 20);
        registerStarfruit(PlantDef.STARFRUIT2, 20);
        registerStarfruit(PlantDef.STARFRUIT3, 30); // Lvl 3: Dmg +10
        registerStarfruit(PlantDef.STARFRUIT4, 30); // Lvl 4: Cost -25

        // --- NON-LEVELED SHOOTERS (Fallbacks for plants you haven't given 4-digit IDs yet) ---
        registerDirectShooter(PlantDef.CITRON, 800, 1);
        registerDirectShooter(PlantDef.BOWLING_BULB, 40, 1);
        registerDirectShooter(PlantDef.GOO_PEASHOOTER, 20, 1);

        //endregion

        // ==========================================
        // 3. STRIKE-THROUGH SHOOTERS (PIERCE)
        // ==========================================
        //region pierce
        register(PlantDef.FUME_SHROOM, new DirectShotBehavior(
            parseShotCount(PlantDef.FUME_SHROOM, 1),
            new StrikeThroughProjectile(parseDamage(PlantDef.FUME_SHROOM, 20), null, 100)
        ));
        //endregion

        // ==========================================
        // 4. LOBBERS
        // ==========================================
        //region lobbers
        registerLobber(PlantDef.CABBAGE_PULT1, 40);
        registerLobber(PlantDef.CABBAGE_PULT2, 50);
        registerLobber(PlantDef.CABBAGE_PULT3, 50);
        registerLobber(PlantDef.CABBAGE_PULT4, 50);

        // --- Kernel-pult ---
        // (Lvl 1 base: 25% butter chance. Lvl 2+: +5% butter chance -> 30%)
        registerKernelPult(PlantDef.KERNEL_PULT1, 20, 40, 0.25);
        registerKernelPult(PlantDef.KERNEL_PULT2, 20, 40, 0.30); // Lvl 2: Butter +5%
        registerKernelPult(PlantDef.KERNEL_PULT3, 30, 50, 0.30); // Lvl 3: Dmg +10
        registerKernelPult(PlantDef.KERNEL_PULT4, 30, 50, 0.30); // Lvl 4: HP +150

        // --- Melon-pult (Base AoE: ~40 Dmg, 1.5 tile radius for 3x3) ---
        registerLobberAoE(PlantDef.MELON_PULT1, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.MELON_PULT2, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.MELON_PULT3, 80, 1.5f, 55); // Lvl 3: AoE Dmg +15
        registerLobberAoE(PlantDef.MELON_PULT4, 110, 1.5f, 55);

        // --- Winter Melon ---
        registerLobberAoE(PlantDef.WINTER_MELON1, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.WINTER_MELON2, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.WINTER_MELON3, 80, 1.5f, 55); // Lvl 3: AoE Dmg +15
        registerLobberAoE(PlantDef.WINTER_MELON4, 80, 1.5f, 55);

        // --- Pepper-pult ---
        registerLobberAoE(PlantDef.PEPPER_PULT1, 50, 1.5f, 25);
        registerLobberAoE(PlantDef.PEPPER_PULT2, 65, 1.5f, 32);
        registerLobberAoE(PlantDef.PEPPER_PULT3, 65, 1.5f, 32);
        registerLobberAoE(PlantDef.PEPPER_PULT4, 65, 1.5f, 32);
        //endregion



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
        registerLobberAoE(def, defaultDamage, 0f, 0);
    }
    private static void registerLobberAoE(PlantDef def, int defaultDamage, float radius, int aoeDamage) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, 1);
        register(def, new LobShotBehavior(shots, new LobProjectile(damage, null, null, radius, aoeDamage)));
    }

    /**
     * Helper to parse and register Kernel-pult levels, supporting split normal/butter damage
     * and a percentage chance for the special butter projectile.
     */
    private static void registerKernelPult(PlantDef def, int defaultNormal, int defaultButter, double butterChance) {
        int normalDmg = defaultNormal;
        int butterDmg = defaultButter;

        // Parse "20/40" damage format
        String dmgStr = def.getDamage();
        if (dmgStr != null && dmgStr.contains("/")) {
            String[] parts = dmgStr.split("/");
            try {
                normalDmg = Integer.parseInt(parts[0].trim());
                butterDmg = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException ignored) {}
        }

        int shots = parseShotCount(def, 1);

        // Currently uses LobProjectile for both since they both lob.
        // Once you build a stun component, you can easily swap the second one out for a custom ButterProjectile!
        register(def, new KernelPultBehavior(
            shots,
            new LobProjectile(normalDmg, null, null, 0f, 0),
            new LobProjectile(butterDmg, null, null, 0f, 0),
            butterChance
        ));
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
    /**
     * Helper to register Threepeater levels with its unique behavior.
     */
    private static void registerThreepeater(PlantDef def, int defaultDamage, int heat) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new ThreepeaterBehavior(new TruePeaProjectile(damage, null, heat)));
    }

    /**
     * Helper to register Pea Pod levels with its unique behavior.
     */
    private static void registerPeaPod(PlantDef def, int defaultDamage, int heat) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new PeaPodBehavior(new TruePeaProjectile(damage, null, heat)));
    }

    /**
     * Helper to register Split Pea levels with its unique behavior.
     */
    private static void registerSplitPea(PlantDef def, int defaultDamage, int heat) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new SplitPeaBehavior(new TruePeaProjectile(damage, null, heat)));
    }

    /**
     * Helper to register Rotobaga levels with its unique behavior.
     */
    private static void registerRotobaga(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new RotobagaBehavior(new PeaProjectile(damage, null)));
    }

    /**
     * Helper to parse and register Cactus levels, applying specific damage and pierce counts.
     */
    private static void registerCactus(PlantDef def, int defaultDamage, int pierceCount) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, 1);
        register(def, new DirectShotBehavior(
            shots,
            new StrikeThroughProjectile(damage, null, pierceCount)
        ));
    }

    /**
     * Helper to parse and register Starfruit levels.
     */
    private static void registerStarfruit(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new StarfruitBehavior(new PeaProjectile(damage, null)));
    }

    /**
     * Helper to automatically parse and register Melee plants with ranges and pierce counts.
     */
    private static void registerMelee(PlantDef def, int defaultDamage, float frontRange, float backRange, int pierce) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new MeleeAttackBehavior(frontRange, backRange, pierce, damage));
    }
}
