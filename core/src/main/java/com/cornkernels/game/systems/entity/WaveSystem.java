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
import java.util.random.RandomGenerator;

public class WaveSystem extends EntitySystem {

    private static final float BASE_SPAWN_DURATION = 3.0f;

    private final int[] zombiesPerWave;
    private final List<ZombieDef> eligibleZombies;
    private final RandomGenerator rng;
    private final PamPlayer pamPlayer;

    private float spawnTimer = 0f;
    private int waveNumber = 0;
    private int zombiesSpawnedThisWave = 0;

    public WaveSystem(@NotNull int[] zombiesPerWave, RandomGenerator rng,
                      List<ZombieDef> eligibleZombies, PamPlayer pamPlayer) {
        this.zombiesPerWave = zombiesPerWave;
        this.eligibleZombies = eligibleZombies;
        this.rng = rng;
        this.pamPlayer = pamPlayer;
    }

    public void update(float deltaTick) {
        if (waveNumber == 0 || (!isFinalWave() && shouldStartNextWave(field))) {
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
        Vec2d spawnPosition = new Vec2d(field.getTotalColumns(), lane);

        ZombieInstance zombie = new ZombieInstance(chosen, spawnPosition);
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), chosen, "walk");
        field.addZombie(zombie);
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
    }

    private boolean shouldStartNextWave(@NotNull Field field) {
        List<ZombieInstance> zombies = field.getActiveZombies();

        boolean zombiesHealthLow = zombies.stream().allMatch(zombie -> {
            HealthComponent health = zombie.get(HealthComponent.class);
            return !zombie.isMarkedForRemoval() && health.currentHealth <= 0.05 * health.maxHealth && !isFinalWave();
        });

        return isWaveSpawningDone() && zombiesHealthLow;
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
}
