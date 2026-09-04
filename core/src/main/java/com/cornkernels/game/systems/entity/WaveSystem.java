package com.cornkernels.game.systems.entity;

import com.badlogic.gdx.math.MathUtils;
import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.obstacles.ArcadeMachine;
import com.cornkernels.game.entities.types.obstacles.Glacier;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.obstacles.Redirector;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.levels.LevelDef;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

public class WaveSystem extends EntitySystem {

    public static final float OFFSCREEN_SPAWN_MARGIN_COLUMNS = 2f;
    private static final float BASE_SPAWN_DURATION = 3.0f;
    private static final float WAVE_ADVANCE_DAMAGE_FRACTION = 0.5f;
    private static final int ARCADE_MACHINE_HP = 600;
    private static final float ARCADE_MACHINE_SPAWN_INTERVAL_SECONDS = 18f;
    private final int[] waveBudgets;
    private final List<ZombieDef> eligibleZombies;
    private final List<LevelDef.ObstacleSpawn> obstacles;
    private final RandomGenerator rng;
    private final PamPlayer pamPlayer;
    private float spawnTimer = 0f;
    private int waveNumber = 0;
    private int currentWaveValueSpawned = 0;
    private float currentWaveMaxHealthTotal = 0f;
    private float currentWaveDamageDealt = 0f;
    private boolean obstaclesSpawned = false;
    private Consumer<ZombieDef> onZombieSpawned = def -> {
    };

    public WaveSystem(int @NotNull [] waveBudgets, RandomGenerator rng, List<ZombieDef> eligibleZombies, List<LevelDef.ObstacleSpawn> obstacles, PamPlayer pamPlayer) {
        this.waveBudgets = waveBudgets;
        this.eligibleZombies = eligibleZombies;
        this.obstacles = obstacles != null ? obstacles : List.of();
        this.rng = rng;
        this.pamPlayer = pamPlayer;
    }

    public void setOnZombieSpawnedListener(@NotNull Consumer<ZombieDef> listener) {
        this.onZombieSpawned = listener;
    }

    public void update(float deltaTick) {
        if (!obstaclesSpawned && field != null) {
            spawnLevelObstacles(field);
            obstaclesSpawned = true;
        }

        if (waveNumber == 0 || (!isFinalWave() && shouldStartNextWave())) startNextWave();
        if (isWaveSpawningDone()) return;

        spawnTimer -= deltaTick;
        if (spawnTimer <= 0f) {
            ZombieDef chosen = pickWeightedByValue(eligibleZombies);
            if (chosen != null) {
                spawnZombie(field, chosen);
                currentWaveValueSpawned += chosen.cost;
            }
            spawnTimer = spawnInterval();
        }
    }

    private void spawnLevelObstacles(Field field) {
        for (LevelDef.ObstacleSpawn spawn : obstacles) {
            switch (spawn.type()) {
                case GRAVE ->
                    field.addObstacle(new Grave(new Vec2d(spawn.column(), spawn.lane())), spawn.lane(), spawn.column());
                case GLACIER ->
                    field.addObstacle(new Glacier(new Vec2d(spawn.column(), spawn.lane()), ZombieDef.IMP), spawn.lane(), spawn.column());
                case ARCADE ->
                    field.addObstacle(new ArcadeMachine(new Vec2d(spawn.column(), spawn.lane()), 600, 18), spawn.lane(), spawn.column());
                case REDIRECTOR_UP ->
                    field.addObstacle(new Redirector(new Vec2d(spawn.column(), spawn.lane()), Redirector.Direction.UP), spawn.lane(), spawn.column());
                case REDIRECTOR_DOWN ->
                    field.addObstacle(new Redirector(new Vec2d(spawn.column(), spawn.lane()), Redirector.Direction.DOWN), spawn.lane(), spawn.column());
            }
        }
    }

    private float spawnInterval() {
        return BASE_SPAWN_DURATION / Math.max(1, waveNumber);
    }

    private void spawnZombie(@NonNull Field field, @NotNull ZombieDef chosen) {
        int lane = rng.nextInt(field.getTotalLanes());
        Vec2d spawnPosition = new Vec2d(field.getTotalColumns() + OFFSCREEN_SPAWN_MARGIN_COLUMNS, lane);

        ZombieInstance zombie = new ZombieInstance(chosen, spawnPosition);
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), chosen, "walk");
        ZombieAnimationLocator.applyArmorVisibility(chosen, zombie.get(PamAnimationComponent.class));
        trackWaveHealth(zombie);
        field.addZombie(zombie);

        if (chosen == ZombieDef.ARCADE) {
            int machineColumn = field.getTotalColumns() - 1;
            Vec2d machinePosition = new Vec2d(machineColumn, lane);
            field.addObstacle(new ArcadeMachine(machinePosition, ARCADE_MACHINE_HP, ARCADE_MACHINE_SPAWN_INTERVAL_SECONDS), lane, machineColumn);
        }

        onZombieSpawned.accept(chosen);
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

    private ZombieDef pickWeightedByValue(@NotNull List<ZombieDef> candidates) {
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

    public boolean isGameFinished(Field field) {
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
}
