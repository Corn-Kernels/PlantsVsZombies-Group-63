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
        //region IMPLEMENTED BEHAVIORS

        //region Sun Producers
        register(PlantDef.SUNFLOWER1, new SunBehavior(1, SunType.BIG));
        register(PlantDef.SUNFLOWER2, new SunBehavior(1, SunType.BIG));
        register(PlantDef.SUNFLOWER3, new SunBehavior(1, SunType.BIG));
        register(PlantDef.SUNFLOWER4, new SunBehavior(1, SunType.BIG, 0.2));

        register(PlantDef.TWIN_SUNFLOWER1, new SunBehavior(2, SunType.BIG));
        register(PlantDef.TWIN_SUNFLOWER2, new SunBehavior(2, SunType.BIG));
        register(PlantDef.TWIN_SUNFLOWER3, new SunBehavior(2, SunType.BIG));
        register(PlantDef.TWIN_SUNFLOWER4, new SunBehavior(2, SunType.BIG));

        register(PlantDef.PRIMAL_SUNFLOWER1, new SunBehavior(1, SunType.LARGE));
        register(PlantDef.PRIMAL_SUNFLOWER2, new SunBehavior(1, SunType.LARGE));
        register(PlantDef.PRIMAL_SUNFLOWER3, new SunBehavior(1, SunType.LARGE));
        register(PlantDef.PRIMAL_SUNFLOWER4, new SunBehavior(1, SunType.LARGE));

        register(PlantDef.SUN_SHROOM1, new SunShroomBehavior(24f, 72f, 24f, 0.0));
        register(PlantDef.SUN_SHROOM2, new SunShroomBehavior(19f, 67f, 24f, 0.0));
        register(PlantDef.SUN_SHROOM3, new SunShroomBehavior(19f, 67f, 24f, 0.0));
        register(PlantDef.SUN_SHROOM4, new SunShroomBehavior(19f, 67f, 24f, 0.5));
        //endregion

        //region Peashooters
        registerTruePeaShooter(PlantDef.PEASHOOTER1, 20, 1, 0);
        registerTruePeaShooter(PlantDef.PEASHOOTER2, 30, 1, 0);
        registerTruePeaShooter(PlantDef.PEASHOOTER3, 30, 1, 0);
        registerTruePeaShooter(PlantDef.PEASHOOTER4, 30, 1, 0);

        registerTruePeaShooter(PlantDef.REPEATER1, 20, 1, 0);
        registerTruePeaShooter(PlantDef.REPEATER2, 30, 1, 0);
        registerTruePeaShooter(PlantDef.REPEATER3, 30, 1, 0);
        registerTruePeaShooter(PlantDef.REPEATER4, 30, 1, 0);

        registerTruePeaShooter(PlantDef.SNOW_PEA1, 20, 1, -1);
        registerTruePeaShooter(PlantDef.SNOW_PEA2, 30, 1, -1);
        registerTruePeaShooter(PlantDef.SNOW_PEA3, 30, 1, -1);
        registerTruePeaShooter(PlantDef.SNOW_PEA4, 30, 1, -1);

        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER1, 20, 1, 1);
        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER2, 30, 1, 1);
        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER3, 30, 1, 1);
        registerTruePeaShooter(PlantDef.FIRE_PEASHOOTER4, 30, 1, 1);

        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA1, 20, 1, 0);
        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA2, 30, 1, 0);
        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA3, 30, 1, 0);
        registerTruePeaShooter(PlantDef.MEGA_GATLING_PEA4, 30, 1, 0);

        registerThreepeater(PlantDef.THREEPEATER1, 20, 0);
        registerThreepeater(PlantDef.THREEPEATER2, 30, 0);
        registerThreepeater(PlantDef.THREEPEATER3, 30, 0);
        registerThreepeater(PlantDef.THREEPEATER4, 30, 0);

        registerPeaPod(PlantDef.PEA_POD1, 20, 0);
        registerPeaPod(PlantDef.PEA_POD2, 30, 0);
        registerPeaPod(PlantDef.PEA_POD3, 30, 0);
        registerPeaPod(PlantDef.PEA_POD4, 30, 0);

        registerSplitPea(PlantDef.SPLIT_PEA1, 20, 0);
        registerSplitPea(PlantDef.SPLIT_PEA2, 30, 0);
        registerSplitPea(PlantDef.SPLIT_PEA3, 30, 0);
        registerSplitPea(PlantDef.SPLIT_PEA4, 30, 0);
        //endregion

        //region Short Range & Spreaders
        registerRotobaga(PlantDef.ROTOBAGA1, 10);
        registerRotobaga(PlantDef.ROTOBAGA2, 20);
        registerRotobaga(PlantDef.ROTOBAGA3, 20);
        registerRotobaga(PlantDef.ROTOBAGA4, 20);

        register(PlantDef.SEA_SHROOM1, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM1, 20), null), 3.0f, 1.5f, 60.0f));
        register(PlantDef.SEA_SHROOM2, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM2, 20), null), 4.0f, 1.5f, 60.0f));
        register(PlantDef.SEA_SHROOM3, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM3, 25), null), 4.0f, 1.5f, 60.0f));
        register(PlantDef.SEA_SHROOM4, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.SEA_SHROOM4, 25), null), 4.0f, 1.5f, 70.0f));

        register(PlantDef.PUFF_SHROOM1, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM1, 20), null), 3.0f, 1.5f, 60.0f));
        register(PlantDef.PUFF_SHROOM2, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM2, 20), null), 3.0f, 1.5f, 70.0f));
        register(PlantDef.PUFF_SHROOM3, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM3, 30), null), 3.0f, 1.5f, 70.0f));
        register(PlantDef.PUFF_SHROOM4, new PuffShotBehavior(1, new PeaProjectile(parseDamage(PlantDef.PUFF_SHROOM4, 30), null), 4.0f, 1.5f, 70.0f));

        registerStarfruit(PlantDef.STARFRUIT1, 20);
        registerStarfruit(PlantDef.STARFRUIT2, 20);
        registerStarfruit(PlantDef.STARFRUIT3, 30);
        registerStarfruit(PlantDef.STARFRUIT4, 30);
        //endregion

        //region Strike-Through
        registerCactus(PlantDef.CACTUS1, 30, 3);
        registerCactus(PlantDef.CACTUS2, 30, 4);
        registerCactus(PlantDef.CACTUS3, 40, 4);
        registerCactus(PlantDef.CACTUS4, 40, 4);
        //endregion

        //region Lobbers
        registerLobber(PlantDef.CABBAGE_PULT1, 40);
        registerLobber(PlantDef.CABBAGE_PULT2, 50);
        registerLobber(PlantDef.CABBAGE_PULT3, 50);
        registerLobber(PlantDef.CABBAGE_PULT4, 50);

        registerKernelPult(PlantDef.KERNEL_PULT1, 20, 40, 0.25);
        registerKernelPult(PlantDef.KERNEL_PULT2, 20, 40, 0.30);
        registerKernelPult(PlantDef.KERNEL_PULT3, 30, 50, 0.30);
        registerKernelPult(PlantDef.KERNEL_PULT4, 30, 50, 0.30);

        registerLobberAoE(PlantDef.MELON_PULT1, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.MELON_PULT2, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.MELON_PULT3, 80, 1.5f, 55);
        registerLobberAoE(PlantDef.MELON_PULT4, 110, 1.5f, 55);

        registerLobberAoE(PlantDef.WINTER_MELON1, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.WINTER_MELON2, 80, 1.5f, 40);
        registerLobberAoE(PlantDef.WINTER_MELON3, 80, 1.5f, 55);
        registerLobberAoE(PlantDef.WINTER_MELON4, 80, 1.5f, 55);

        registerLobberAoE(PlantDef.PEPPER_PULT1, 50, 1.5f, 25);
        registerLobberAoE(PlantDef.PEPPER_PULT2, 65, 1.5f, 32);
        registerLobberAoE(PlantDef.PEPPER_PULT3, 65, 1.5f, 32);
        registerLobberAoE(PlantDef.PEPPER_PULT4, 65, 1.5f, 32);
        //endregion

        //region Melee
        registerBonkChoy(PlantDef.BONK_CHOY1, 15, 1.5f, 1.5f, 1);
        registerBonkChoy(PlantDef.BONK_CHOY2, 20, 1.5f, 1.5f, 1);
        registerBonkChoy(PlantDef.BONK_CHOY3, 20, 1.5f, 1.5f, 1);
        registerBonkChoy(PlantDef.BONK_CHOY4, 20, 1.5f, 1.5f, 1);

        registerBonkChoy(PlantDef.WASABI_WHIP1, 40, 1.5f, 1.5f, 1);
        registerBonkChoy(PlantDef.WASABI_WHIP2, 50, 1.5f, 1.5f, 1);
        registerBonkChoy(PlantDef.WASABI_WHIP3, 50, 2.5f, 2.5f, 1);
        registerBonkChoy(PlantDef.WASABI_WHIP4, 50, 2.5f, 2.5f, 1);

        registerChomper(PlantDef.CHOMPER1, 40.0f, 1.5f, 1.5f, 150);
        registerChomper(PlantDef.CHOMPER2, 38.0f, 1.5f, 1.5f, 150);
        registerChomper(PlantDef.CHOMPER3, 38.0f, 1.5f, 1.5f, 150);
        registerChomper(PlantDef.CHOMPER4, 35.0f, 1.5f, 1.5f, 150);

        registerAreaMelee(PlantDef.PHAT_BEET1, new int[]{15}, new float[]{1.5f}, new float[]{});
        registerAreaMelee(PlantDef.PHAT_BEET2, new int[]{25}, new float[]{1.5f}, new float[]{});
        registerAreaMelee(PlantDef.PHAT_BEET3, new int[]{25}, new float[]{1.5f}, new float[]{});
        registerAreaMelee(PlantDef.PHAT_BEET4, new int[]{25}, new float[]{1.5f}, new float[]{});

        int[] kiwiBaseDmg = {15, 30, 45};
        float[] kiwiBaseRange = {0.5f, 1.5f, 2.5f};
        float[] kiwiBaseThresholds = {0.66f, 0.33f};

        registerAreaMelee(PlantDef.KIWIBEAST1, kiwiBaseDmg, kiwiBaseRange, kiwiBaseThresholds);
        registerAreaMelee(PlantDef.KIWIBEAST2, kiwiBaseDmg, kiwiBaseRange, kiwiBaseThresholds);

        int[] kiwiLvl3Dmg = {30, 45, 60};
        registerAreaMelee(PlantDef.KIWIBEAST3, kiwiLvl3Dmg, kiwiBaseRange, kiwiBaseThresholds);

        int[] kiwiLvl4Dmg = {30, 45, 60, 75};
        float[] kiwiLvl4Range = {0.5f, 1.5f, 2.5f, 3.5f};
        float[] kiwiLvl4Thresholds = {0.75f, 0.50f, 0.25f};

        registerAreaMelee(PlantDef.KIWIBEAST4, kiwiLvl4Dmg, kiwiLvl4Range, kiwiLvl4Thresholds);
        //endregion

        //region Wall-nuts
        registerEndurian(PlantDef.ENDURIAN1, 20);
        registerEndurian(PlantDef.ENDURIAN2, 25);
        registerEndurian(PlantDef.ENDURIAN3, 25);
        registerEndurian(PlantDef.ENDURIAN4, 25);

        registerExplodeONut(PlantDef.EXPLODE_O_NUT1, 1800);
        registerExplodeONut(PlantDef.EXPLODE_O_NUT2, 1800);
        registerExplodeONut(PlantDef.EXPLODE_O_NUT3, 2000);
        registerExplodeONut(PlantDef.EXPLODE_O_NUT4, 2000);

        // The remaining Wall-nuts safely fall through to NO_ATTACK naturally!
        //endregion

        //region Explosives

        // Potato Mine (Base arm time is 15s. Level 2 drops it to 12s. Radius is 0.5 tiles for 1x1 area)
        registerTrapExplosive(PlantDef.POTATO_MINE1, 1800, 0.5f, 15.0f);
        registerTrapExplosive(PlantDef.POTATO_MINE2, 1800, 0.5f, 12.0f); // Lvl 2: Arm Time -3s
        registerTrapExplosive(PlantDef.POTATO_MINE3, 1800, 0.5f, 12.0f);
        registerTrapExplosive(PlantDef.POTATO_MINE4, 2400, 0.5f, 12.0f); // Lvl 4: Dmg +600

        // Primal Potato Mine (Base arm time is 5s. Level 2 drops it to 4s. Radius is 1.5 tiles for 3x3 area)
        registerTrapExplosive(PlantDef.PRIMAL_POTATO_MINE1, 2400, 1.5f, 5.0f);
        registerTrapExplosive(PlantDef.PRIMAL_POTATO_MINE2, 2400, 1.5f, 4.0f); // Lvl 2: Arm Time -1s
        registerTrapExplosive(PlantDef.PRIMAL_POTATO_MINE3, 2400, 1.5f, 4.0f);
        registerTrapExplosive(PlantDef.PRIMAL_POTATO_MINE4, 2800, 1.5f, 4.0f); // Lvl 4: Dmg +400

        // --- Cherry Bomb (3x3 area -> 1.5 tile radius, 1.0s wait) ---
        registerInstaExplosion(PlantDef.CHERRY_BOMB1, 1800, 1.5f, 1.0f);
        registerInstaExplosion(PlantDef.CHERRY_BOMB2, 1800, 1.5f, 1.0f);
        registerInstaExplosion(PlantDef.CHERRY_BOMB3, 2400, 1.5f, 1.0f); // Lvl 3: Dmg +600
        registerInstaExplosion(PlantDef.CHERRY_BOMB4, 2400, 1.5f, 1.0f);

        // --- Doom-shroom (7x7 area -> 3.5 tile radius, 1.0s wait) ---
        registerInstaExplosion(PlantDef.DOOM_SHROOM1, 1800, 3.5f, 1.0f);
        registerInstaExplosion(PlantDef.DOOM_SHROOM2, 1800, 3.5f, 1.0f);
        registerInstaExplosion(PlantDef.DOOM_SHROOM3, 2600, 3.5f, 1.0f); // Lvl 3: Dmg +800
        registerInstaExplosion(PlantDef.DOOM_SHROOM4, 2600, 3.5f, 1.0f);

        // --- Ice-shroom (Full board -> 100 tile radius, 1.0s wait) ---
        registerInstaExplosion(PlantDef.ICE_SHROOM1, 20, 100.0f, 1.0f);
        registerInstaExplosion(PlantDef.ICE_SHROOM2, 20, 100.0f, 1.0f);
        registerInstaExplosion(PlantDef.ICE_SHROOM3, 20, 100.0f, 1.0f);
        registerInstaExplosion(PlantDef.ICE_SHROOM4, 70, 100.0f, 1.0f); // Lvl 4: Dmg +50

        // --- Jalapeno (Full lane -> 15 tile length, 1.0 tile width, 1.0s wait) ---
        registerLineExplosion(PlantDef.JALAPENO1, 1800, 15.0f, 1.0f, 1.0f);
        registerLineExplosion(PlantDef.JALAPENO2, 1800, 15.0f, 1.0f, 1.0f);
        registerLineExplosion(PlantDef.JALAPENO3, 2400, 15.0f, 1.0f, 1.0f); // Lvl 3: Dmg +600
        registerLineExplosion(PlantDef.JALAPENO4, 2400, 15.0f, 1.0f, 1.0f);

        // --- Grapeshot (3x3 area -> 1.5 tile radius, 1.0s wait) ---
        registerGrapeshot(PlantDef.GRAPESHOT1, 1800, 200, 1.0f, 3);
        registerGrapeshot(PlantDef.GRAPESHOT2, 2400, 260, 1.0f, 3); // Lvl 2: Dmg +600
        registerGrapeshot(PlantDef.GRAPESHOT3, 2400, 260, 1.0f, 4); // Lvl 3: Bounces +1
        registerGrapeshot(PlantDef.GRAPESHOT4, 2400, 260, 1.0f, 4); // Lvl 4: Cost -25

        //endregion

        //endregion


        // ==========================================
        // UNIMPLEMENTED FALLBACKS
        // ==========================================

        // Shooters Fallback
        registerDirectShooter(PlantDef.CITRON, 800, 1);
        registerDirectShooter(PlantDef.BOWLING_BULB, 40, 1);
        registerDirectShooter(PlantDef.GOO_PEASHOOTER, 20, 1);

        // Strike-Through Fallback
        register(PlantDef.FUME_SHROOM, new DirectShotBehavior(
            parseShotCount(PlantDef.FUME_SHROOM, 1),
            new StrikeThroughProjectile(parseDamage(PlantDef.FUME_SHROOM, 20), null, 100)
        ));

        // Homing Fallback
        registerHoming(PlantDef.CAULIPOWER, 1800);
        registerHoming(PlantDef.ELECTRIC_BLUEBERRY, 5000);
        registerHoming(PlantDef.CAT_TAIL, 15);

        // Global Fallback
        for (PlantDef def : PlantDef.values()) {
            if (!REGISTRY.containsKey(def)) {
                register(def, NO_ATTACK);
            }
        }
    }

    private PlantAttackBehaviors() {
    }

    //region Helper Methods

    private static void register(PlantDef def, PlantAttackBehavior behavior) {
        REGISTRY.put(def, behavior);
        REGISTRY_ID.put(def.getId(), behavior);
    }

    private static void registerDirectShooter(PlantDef def, int defaultDamage, int defaultShots) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, defaultShots);
        register(def, new DirectShotBehavior(shots, new PeaProjectile(damage, null)));
    }

    private static void registerLobber(PlantDef def, int defaultDamage) {
        registerLobberAoE(def, defaultDamage, 0f, 0);
    }

    private static void registerLobberAoE(PlantDef def, int defaultDamage, float radius, int aoeDamage) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, 1);
        register(def, new LobShotBehavior(shots, new LobProjectile(damage, null, null, radius, aoeDamage)));
    }

    private static void registerKernelPult(PlantDef def, int defaultNormal, int defaultButter, double butterChance) {
        int normalDmg = defaultNormal;
        int butterDmg = defaultButter;

        String dmgStr = def.getDamage();
        if (dmgStr != null && dmgStr.contains("/")) {
            String[] parts = dmgStr.split("/");
            try {
                normalDmg = Integer.parseInt(parts[0].trim());
                butterDmg = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException ignored) {}
        }

        int shots = parseShotCount(def, 1);

        register(def, new KernelPultBehavior(
            shots,
            new LobProjectile(normalDmg, null, null, 0f, 0),
            new LobProjectile(butterDmg, null, null, 0f, 0),
            butterChance
        ));
    }

    private static void registerHoming(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, 1);
        register(def, new HomingShotBehavior(shots, new HomingProjectile(damage, null, null)));
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

    private static void registerTruePeaShooter(PlantDef def, int defaultDamage, int defaultShots, int heat) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, defaultShots);
        register(def, new DirectShotBehavior(shots, new TruePeaProjectile(damage, null, heat)));
    }

    private static void registerThreepeater(PlantDef def, int defaultDamage, int heat) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new ThreepeaterBehavior(new TruePeaProjectile(damage, null, heat)));
    }

    private static void registerPeaPod(PlantDef def, int defaultDamage, int heat) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new PeaPodBehavior(new TruePeaProjectile(damage, null, heat)));
    }

    private static void registerSplitPea(PlantDef def, int defaultDamage, int heat) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new SplitPeaBehavior(new TruePeaProjectile(damage, null, heat)));
    }

    private static void registerRotobaga(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new RotobagaBehavior(new PeaProjectile(damage, null)));
    }

    private static void registerCactus(PlantDef def, int defaultDamage, int pierceCount) {
        int damage = parseDamage(def, defaultDamage);
        int shots = parseShotCount(def, 1);
        register(def, new DirectShotBehavior(
            shots,
            new StrikeThroughProjectile(damage, null, pierceCount)
        ));
    }

    private static void registerStarfruit(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new StarfruitBehavior(new PeaProjectile(damage, null)));
    }

    private static void registerBonkChoy(PlantDef def, int defaultDamage, float frontRange, float backRange, int pierce) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new MeleeAttackBehavior(frontRange, backRange, pierce, damage));
    }

    private static void registerChomper(PlantDef def, float digestSeconds, float biteCooldownSeconds, float frontRange, int chompDamage) {
        register(def, new ChomperBehavior(digestSeconds, biteCooldownSeconds, frontRange, chompDamage));
    }

    private static void registerAreaMelee(PlantDef def, int[] damages, float[] ranges, float[] hpThresholds) {
        register(def, new AreaMeleeBehavior(damages, ranges, hpThresholds));
    }

    /**
     * Helper to parse and register Endurian levels using an invisible standard PeaProjectile.
     */
    private static void registerEndurian(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new EndurianBehavior(new PeaProjectile(damage, null)));
    }

    /**
     * Helper to parse and register Explode-o-nut levels.
     */
    private static void registerExplodeONut(PlantDef def, int defaultDamage) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new ExplodeONutBehavior(damage));
    }
    /**
     * Helper to register proximity traps with custom radii and arm times.
     */
    private static void registerTrapExplosive(PlantDef def, int defaultDamage, float radius, float armTimeSeconds) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new TrapExplosiveBehavior(radius, damage, armTimeSeconds));
    }

    /**
     * Helper to register immediate radial explosives (Cherry Bomb, Doom-shroom, Ice-shroom).
     */
    private static void registerInstaExplosion(PlantDef def, int defaultDamage, float radius, float waitTime) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new InstaExplosiveBehavior(radius, damage, waitTime));
    }

    /**
     * Helper to register immediate linear explosives (Jalapeno).
     */
    private static void registerLineExplosion(PlantDef def, int defaultDamage, float length, float width, float waitTime) {
        int damage = parseDamage(def, defaultDamage);
        register(def, new LineExplosiveBehavior(length, width, damage, waitTime));
    }

    /**
     * Helper to parse and register Grapeshot levels.
     */
    private static void registerGrapeshot(PlantDef def, int defaultAoeDmg, int grapeDmg, float waitTime, int bounces) {
        int aoeDmg = parseDamage(def, defaultAoeDmg);
        register(def, new GrapeshotBehavior(1.5f, aoeDmg, grapeDmg, waitTime, bounces));
    }

    //endregion
}
