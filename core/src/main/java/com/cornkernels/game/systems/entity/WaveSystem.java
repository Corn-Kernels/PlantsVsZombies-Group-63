package com.cornkernels.game.systems.entity;

import com.badlogic.gdx.math.MathUtils;
import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jetbrains.annotations.NotNull;
import pvz.libpvz.pam.PamPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

public class WaveSystem extends EntitySystem {

    private static final float BASE_SPAWN_DURATION = 3.0f;
    private static final float WAVE_ADVANCE_DAMAGE_FRACTION = 0.5f;

    public static final float OFFSCREEN_SPAWN_MARGIN_COLUMNS = 2f;

    private final int[] zombiesPerWave;
    private final List<ZombieDef> eligibleZombies;
    private final RandomGenerator rng;
    private final PamPlayer pamPlayer;

    private float spawnTimer = 0f;
    private int waveNumber = 0;
    private int zombiesSpawnedThisWave = 0;

    private float currentWaveMaxHealthTotal = 0f;
    private float currentWaveDamageDealt = 0f;

    private Consumer<ZombieDef> onZombieSpawned = def -> {
    };

    public WaveSystem(int @NotNull [] zombiesPerWave, RandomGenerator rng,
                      List<ZombieDef> eligibleZombies, PamPlayer pamPlayer) {
        this.zombiesPerWave = zombiesPerWave;
        this.eligibleZombies = eligibleZombies;
        this.rng = rng;
        this.pamPlayer = pamPlayer;
    }

    public void setOnZombieSpawnedListener(@NotNull Consumer<ZombieDef> listener) {
        this.onZombieSpawned = listener;
    }

    public void update(float deltaTick) {
        if (waveNumber == 0 || (!isFinalWave() && shouldStartNextWave())) {
            startNextWave();
        }

        if (isWaveSpawningDone()) return;

        spawnTimer -= deltaTick;
        if (spawnTimer <= 0f) {
            spawnZombie(field);
            zombiesSpawnedThisWave++;
            spawnTimer = spawnInterval();
        }
    }

    private float spawnInterval() {
        return BASE_SPAWN_DURATION / Math.max(1, waveNumber);
    }

    private void spawnZombie(Field field) {
        if (eligibleZombies.isEmpty()) return;

        ZombieDef chosen = pickWeighted(eligibleZombies);
        int lane = rng.nextInt(field.getTotalLanes());
        Vec2d spawnPosition = new Vec2d(field.getTotalColumns() + OFFSCREEN_SPAWN_MARGIN_COLUMNS, lane);

        ZombieInstance zombie = new ZombieInstance(chosen, spawnPosition);
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), chosen, "walk");
        ZombieAnimationLocator.applyArmorVisibility(chosen, zombie.get(PamAnimationComponent.class));
        trackWaveHealth(zombie);
        field.addZombie(zombie);
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

    private @NotNull ZombieDef pickWeighted(@NotNull List<ZombieDef> candidates) {
        int totalWeight = candidates.stream().mapToInt(def -> def.weight).sum();
        if (totalWeight <= 0) return candidates.get(rng.nextInt(candidates.size()));

        int roll = rng.nextInt(totalWeight);
        int cumulative = 0;
        for (ZombieDef def : candidates) {
            cumulative += def.weight;
            if (roll < cumulative) return def;
        }
        return candidates.getLast();
    }

    public void startNextWave() {
        waveNumber++;
        zombiesSpawnedThisWave = 0;
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
        return waveNumber > 0 && zombiesSpawnedThisWave >= zombiesPerWave[waveNumber - 1];
    }

    public boolean isFinalWave() {
        return waveNumber == zombiesPerWave.length;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getTotalWaves() {
        return zombiesPerWave.length;
    }

    public boolean isGameFinished(Field field) {
        return isFinalWave() && isWaveSpawningDone() && field.getActiveZombies().isEmpty();
    }

    public float getProgress() {
        int totalZombies = Arrays.stream(zombiesPerWave).sum();
        if (totalZombies <= 0) return 1f;

        int spawnedInPastWaves = 0;
        for (int i = 0; i < waveNumber - 1; i++) {
            spawnedInPastWaves += zombiesPerWave[i];
        }
        return MathUtils.clamp((spawnedInPastWaves + zombiesSpawnedThisWave) / (float) totalZombies, 0f, 1f);
    }

    public float @NotNull [] getWaveStartProgress() {
        int totalZombies = Arrays.stream(zombiesPerWave).sum();
        if (totalZombies <= 0 || zombiesPerWave.length <= 1) return new float[0];

        float[] result = new float[zombiesPerWave.length - 1];
        int cumulative = 0;
        for (int i = 0; i < result.length; i++) {
            cumulative += zombiesPerWave[i];
            result[i] = MathUtils.clamp(cumulative / (float) totalZombies, 0f, 1f);
        }
        return result;
    }
    //;) :o
}
