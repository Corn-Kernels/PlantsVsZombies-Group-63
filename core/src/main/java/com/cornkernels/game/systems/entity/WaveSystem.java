package com.cornkernels.game.systems.entity;

import com.badlogic.gdx.math.MathUtils;
import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.*;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.levels.LevelDef;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.menus.model.Zombie;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import pvz.libpvz.pam.PamPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

public class WaveSystem extends EntitySystem {

    public static final float OFFSCREEN_SPAWN_MARGIN_COLUMNS = 2f;
    private static final float BASE_SPAWN_DURATION = 3.0f;
    private static final float ARCADE_MACHINE_SPAWN_INTERVAL_SECONDS = 18f;
    private static final float WAVE_ADVANCE_DAMAGE_FRACTION = 0.5f;
    private static final double GRAVE_SPAWN_CHANCE = 0.2;
    private static final int ARCADE_MACHINE_HP = 600;
    private final int[] waveBudgets;
    private final List<ZombieDef> eligibleZombies;
    private final List<LevelDef.ObstacleSpawn> obstaclesSpawns;
    private final RandomGenerator rng;
    private final Field field;
    private final PamPlayer pamPlayer;
    private float spawnTimer = 0f;
    private int waveNumber = 0;
    private int currentWaveValueSpawned = 0;
    private float currentWaveMaxHealthTotal = 0f;
    private float currentWaveDamageDealt = 0f;
    private boolean obstaclesSpawned = false;
    private Consumer<ZombieDef> onZombieSpawned = def -> {
    };

    public WaveSystem(int @NotNull [] waveBudgets, Field field, RandomGenerator rng, List<ZombieDef> eligibleZombies,
                      List<LevelDef.ObstacleSpawn> obstacles, PamPlayer pamPlayer) {
        this.waveBudgets = waveBudgets;
        this.eligibleZombies = eligibleZombies;
        this.obstaclesSpawns = obstacles != null ? obstacles : List.of();
        this.rng = rng;
        this.field = field;
        this.pamPlayer = pamPlayer;
    }

    public void setOnZombieSpawnedListener(@NotNull Consumer<ZombieDef> listener) {
        this.onZombieSpawned = listener;
    }

    public void update(float deltaTick) {
        if (!obstaclesSpawned && field != null) {
            spawnLevelObstacles();
            obstaclesSpawned = true;
        }

        if (waveNumber == 0 || (!isFinalWave() && shouldStartNextWave())) startNextWave();
        if (isWaveSpawningDone()) return;

        spawnTimer -= deltaTick;
        if (spawnTimer <= 0f) {
            ZombieDef chosen = pickWeightedByValue(eligibleZombies);
            if (chosen != null) {
                spawnZombie(chosen);
                currentWaveValueSpawned += chosen.cost;
            }
            spawnTimer = spawnInterval();
        }
    }

    private void spawnLevelObstacles() {
        for (LevelDef.ObstacleSpawn spawn : obstaclesSpawns) {
            AbstractObstacle obstacle;
            switch (spawn.type) {
                case GRAVE -> {
                        obstacle = new Grave(new Vec2d(spawn.column, spawn.lane));
                    field.addObstacle(obstacle, spawn.lane, spawn.column);
                }
                case GLACIER -> {
                    obstacle = new Glacier(new Vec2d(spawn.column, spawn.lane), ZombieDef.IMP);
                    field.addObstacle(obstacle, spawn.lane, spawn.column);
                }
                case ARCADE -> {
                    obstacle = new ArcadeMachine(new Vec2d(spawn.column, spawn.lane), 600, 18);
                    field.addObstacle(obstacle, spawn.lane, spawn.column);
                }
                case REDIRECTOR_UP -> {
                    obstacle = new Redirector(new Vec2d(spawn.column, spawn.lane), Redirector.Direction.UP);
                    field.addObstacle(obstacle, spawn.lane, spawn.column);
                }
                case REDIRECTOR_DOWN -> {
                    obstacle = new Redirector(new Vec2d(spawn.column, spawn.lane), Redirector.Direction.DOWN);
                    field.addObstacle(obstacle, spawn.lane, spawn.column);
                }
                default -> {
                    break;
                }
            }
        }
    }

    private float spawnInterval() {
        return BASE_SPAWN_DURATION / Math.max(1, waveNumber);
    }

    private void spawnZombie(@NotNull ZombieDef chosen) {
        int lane = rng.nextInt(field.getTotalLanes());
        AbstractObstacle randomObstacle = chooseRandomObstacle(chosen);

        Vec2d spawnPosition;
        if (randomObstacle != null) {
            spawnPosition = new Vec2d(
                GridPosition.fromContinuous(randomObstacle.get(PositionComponent.class).position).column(), lane);
            System.out.println(GridPosition.fromContinuous(randomObstacle.get(PositionComponent.class).position).column());
        } else {
            spawnPosition = new Vec2d(field.getTotalColumns() + OFFSCREEN_SPAWN_MARGIN_COLUMNS, lane);
        }

        ZombieInstance zombie = new ZombieInstance(chosen, spawnPosition);
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), chosen, "walk");
        ZombieAnimationLocator.applyArmorVisibility(chosen, zombie.get(PamAnimationComponent.class));
        trackWaveHealth(zombie);
        field.addZombie(zombie);

        if (chosen == ZombieDef.ARCADE) {
            int machineColumn = field.getTotalColumns() + 1;
            Vec2d machinePosition = new Vec2d(machineColumn, lane);
            if (field.getEntities().stream().filter(entity -> entity instanceof PushableObstacle
                && entity.get(PositionComponent.class).position.equals(machinePosition)).toList().isEmpty()) {
                field.addObstacle(new ArcadeMachine(machinePosition, ARCADE_MACHINE_HP,
                    ARCADE_MACHINE_SPAWN_INTERVAL_SECONDS), lane, machineColumn);
            }
        }

        if (chosen == ZombieDef.ICE_AGE_TROGLOBITE) {
            int column = field.getTotalColumns() + 1;
            Vec2d position = new Vec2d(column, lane);
            if (field.getEntities().stream().filter(entity -> entity instanceof PushableObstacle
                && entity.get(PositionComponent.class).position.equals(position)).toList().isEmpty()) {
                field.addObstacle(new Glacier(position, ZombieDef.IMP));
            }
        }
        onZombieSpawned.accept(chosen);
    }

    private @Nullable AbstractObstacle chooseRandomObstacle(ZombieDef chosen) {
        boolean spawnFromGrave = !field.getActiveObstacles().isEmpty()
            && (chosen != ZombieDef.DARK_KING && chosen != ZombieDef.GARGANTUAR);

        if (!spawnFromGrave) {
            return null;
        }

        if (ThreadLocalRandom.current().nextDouble() >= GRAVE_SPAWN_CHANCE) {
            return null;
        }

        List<AbstractObstacle> nonNullObstacles =
            field.getActiveObstacles().stream()
                .filter(entity -> entity != null
                    && !entity.isMarkedForRemoval()
                    && entity instanceof Grave
                    && entity.get(HealthComponent.class).currentHealth > 0)
                .toList();

        if (nonNullObstacles.isEmpty()) {
            return null;
        }

        return nonNullObstacles.get(
            ThreadLocalRandom.current().nextInt(nonNullObstacles.size()));

    }

    private void trackWaveHealth(@NotNull ZombieInstance zombie) {
        HealthComponent health = zombie.get(HealthComponent.class);
        if (health == null) return;

        currentWaveMaxHealthTotal += health.maxHealth;
        health.addListener(new HealthComponent.OnHealthChangedListener() {
            @Override
            public void OnHealthChanged(int currentHealth, int maxHealth, int delta) {
                currentWaveDamageDealt -= delta;
            }

            @Override
            public void onMaxHealthChanged(int maxHealth, int delta) {
            }
        });
    }

    private @Nullable ZombieDef pickWeightedByValue(@NotNull List<ZombieDef> candidates) {
        if (candidates.isEmpty()) return null;

        int totalWeight = candidates.stream().mapToInt(def -> {
            if (def.cost <= 1) return 10;
            if (def.cost <= 2) return 8;
            if (def.cost <= 4) return 5;
            return Math.max(1, 10 - def.cost);
        }).sum();

        if (totalWeight <= 0) return candidates.get(rng.nextInt(candidates.size()));

        int roll = rng.nextInt(totalWeight);
        int cumulative = 0;
        for (ZombieDef def : candidates) {
            int weight = def.cost <= 1 ? 10 : (def.cost <= 2 ? 8 : (def.cost <= 4 ? 5 : Math.max(1, 10 - def.cost)));
            cumulative += weight;
            if (roll < cumulative) return def;
        }
        return candidates.getLast();
    }

    public void startNextWave() {
        waveNumber++;
        currentWaveValueSpawned = 0;
        spawnTimer = 0f;
        currentWaveMaxHealthTotal = 0f;
        currentWaveDamageDealt = 0f;
    }

    private boolean shouldStartNextWave() {
        if (!isWaveSpawningDone()) return false;
        if (currentWaveMaxHealthTotal <= 0f) return true;
        if (currentWaveDamageDealt >= WAVE_ADVANCE_DAMAGE_FRACTION * currentWaveMaxHealthTotal) return true;
        return field.getActiveZombies().isEmpty();
    }

    public boolean isWaveSpawningDone() {
        return waveNumber > 0 && currentWaveValueSpawned >= waveBudgets[waveNumber - 1];
    }

    public boolean isFinalWave() {
        return waveNumber == waveBudgets.length;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getTotalWaves() {
        return waveBudgets.length;
    }

    public boolean isGameFinished() {
        return isFinalWave() && isWaveSpawningDone() && field.getActiveZombies().isEmpty();
    }

    public float getProgress() {
        int totalBudget = Arrays.stream(waveBudgets).sum();
        if (totalBudget <= 0) return 1f;

        int spawnedInPastWaves = 0;
        for (int i = 0; i < waveNumber - 1; i++) spawnedInPastWaves += waveBudgets[i];
        return MathUtils.clamp((spawnedInPastWaves + currentWaveValueSpawned) / (float) totalBudget, 0f, 1f);
    }

    public float @NotNull [] getWaveStartProgress() {
        int totalBudget = Arrays.stream(waveBudgets).sum();
        if (totalBudget <= 0 || waveBudgets.length <= 1) return new float[0];

        float[] result = new float[waveBudgets.length - 1];
        int cumulative = 0;
        for (int i = 0; i < result.length; i++) {
            cumulative += waveBudgets[i];
            result[i] = MathUtils.clamp(cumulative / (float) totalBudget, 0f, 1f);
        }
        return result;
    }
    //;) :o
}
