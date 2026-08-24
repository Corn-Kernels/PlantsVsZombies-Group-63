package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;
import java.util.random.RandomGenerator;

public class WaveSystem extends EntitySystem {

    private static final int BASE_WAVE_COST = 2000;
    private static final float BASE_SPAWN_DURATION = 3.0f;

    private final List<ZombieDef> eligibleZombies;
    private final int totalWaves;
    private final RandomGenerator rng;
    private final PamPlayer pamPlayer;

    private float spawnTimer = 0f;
    private int waveNumber = 0;
    private int waveTotalCost = BASE_WAVE_COST;
    private int remainingWaveCost = 0;

    public WaveSystem(int totalWaves, RandomGenerator rng, List<ZombieDef> eligibleZombies, PamPlayer pamPlayer) {
        this.totalWaves = totalWaves;
        this.eligibleZombies = eligibleZombies;
        this.rng = rng;
        this.pamPlayer = pamPlayer;
    }

    public void update(float deltaTick) {
        if (waveNumber == 0 || shouldStartNextWave(field)) {
            startNextWave();
        }

        spawnTimer -= deltaTick;
        if (spawnTimer <= 0f) {
            ZombieInstance spawned = spawnZombie(field);
            spawnTimer = spawnInterval();
            if (spawned == null) {
                remainingWaveCost = 0;
            }
        }
    }

    private float spawnInterval() {
        return BASE_SPAWN_DURATION / waveNumber;
    }

    private @Nullable ZombieInstance spawnZombie(Field field) {
        List<ZombieDef> affordable = eligibleZombies.stream().
            filter(zombieDef -> zombieDef.wavePointCost <= remainingWaveCost).toList();

        if (affordable.isEmpty()) return null;

        ZombieDef chosen = pickWeighted(affordable);
        int lane = rng.nextInt(field.getTotalLanes());
        Vec2d spawnPosition = new Vec2d(field.getTotalColumns(), lane);

        ZombieInstance zombie = new ZombieInstance(chosen, spawnPosition);
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), chosen, "walk");
        field.addZombie(zombie);
        remainingWaveCost -= chosen.wavePointCost;

        return zombie;
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
        boolean isFlagWave = waveNumber == totalWaves;

        if (waveNumber > 1) {
            waveTotalCost = Math.round(waveTotalCost * (isFlagWave ? 2.0f : 1.25f));
        }

        remainingWaveCost = waveTotalCost;
        spawnTimer = 0f;
    }

    private boolean shouldStartNextWave(@NotNull Field field) {
        List<ZombieInstance> zombies = field.getActiveZombies();

        boolean zombiesHealthLow = zombies.stream().allMatch(zombie -> {
            HealthComponent health = zombie.get(HealthComponent.class);
            return !zombie.isMarkedForRemoval() && health.currentHealth <= 0.25 * health.maxHealth && !isFinalWave();
        });

        return isWaveSpawningDone() && zombiesHealthLow;
    }

    public boolean isWaveSpawningDone() {
        return remainingWaveCost <= 0;
    }

    public boolean isFinalWave() {
        return waveNumber == totalWaves;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public boolean isGameFinished(Field field) {
        return isFinalWave() && isWaveSpawningDone() && field.getActiveZombies().isEmpty();
    }
}
