package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.sun_specific.SunComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import org.jetbrains.annotations.NotNull;

import java.util.random.RandomGenerator;

public class SunSystem extends EntitySystem {

    private static final int RADIOACTIVE_ZOMBIE_DAMAGE = 150;
    private static final int RADIOACTIVE_ZOMBIE_RADIUS = 2;
    private static final int RADIOACTIVE_PLANT_DAMAGE = 80;
    private static final int RADIOACTIVE_PLANT_RADIUS = 1;
    private final RandomGenerator randomGenerator;
    private float timeSinceGameStart = 0f;
    private float timeUntilNextSpawn;
    private boolean spawningEnabled = true;

    public SunSystem(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
        this.timeUntilNextSpawn = computeNextInterval();
    }

    public void setSpawningEnabled(boolean spawningEnabled) {
        this.spawningEnabled = spawningEnabled;
    }

    @Override
    public void update(float deltaTick) {
        timeSinceGameStart += deltaTick;
        if (!spawningEnabled) return;

        timeUntilNextSpawn -= deltaTick;
        if (timeUntilNextSpawn <= 0f) {
            spawnSun(field);
            timeUntilNextSpawn += computeNextInterval();
        }

        updateFallingSun(field, deltaTick);
    }

    private void updateFallingSun(@NotNull Field field, float deltaTick) {
        for (SunInstance sun : field.getActiveSuns()) {
            if (sun.isMarkedForRemoval()) continue;

            SunComponent component = sun.get(SunComponent.class);
            if (component.state != SunComponent.State.FALLING) continue;

            component.fallElapsed += deltaTick;
            if (component.fallElapsed >= component.fallDuration) {
                component.state = SunComponent.State.LANDED;

                if (component.type == SunType.RADIOACTIVE) {
                    component.type = SunType.NORMAL;
                }
                Vec2d position = sun.get(PositionComponent.class).position;
            }
        }
    }

    public boolean tryHarvest(@NotNull SunInstance sun, Field field, GameAttributes attributes) {
        if (sun.isMarkedForRemoval()) return false;

        SunComponent comp = sun.get(SunComponent.class);

        if (comp.type == SunType.RADIOACTIVE && comp.state == SunComponent.State.FALLING) {
            explode(sun, field);
            sun.markForRemoval();
            return true;
        }

        attributes.adjustSunAmount(comp.type.value);
        sun.markForRemoval();
        return true;
    }

    private void explode(@NotNull SunInstance sun, Field field) {
        GridPosition center = GridPosition.fromContinuous(sun.get(PositionComponent.class).position);
        int lane = center.lane();
        int column = center.column();

        for (int dl = -RADIOACTIVE_ZOMBIE_RADIUS; dl <= RADIOACTIVE_ZOMBIE_RADIUS; dl++) {
            for (ZombieInstance zombie : field.getZombiesInLane(lane + dl)) {
                int zombieColumn = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position).column();
                if (Math.abs(zombieColumn - column) <= RADIOACTIVE_ZOMBIE_RADIUS) {
                    CombatSystem.applyDamage(zombie, RADIOACTIVE_ZOMBIE_DAMAGE, false);
                }
            }
        }

        for (int dl = -RADIOACTIVE_PLANT_RADIUS; dl <= RADIOACTIVE_PLANT_RADIUS; dl++) {
            for (int dc = -RADIOACTIVE_PLANT_RADIUS; dc <= RADIOACTIVE_PLANT_RADIUS; dc++) {
                PlantInstance plant = field.getPlantAt(lane + dl, column + dc);
                if (plant != null) {
                    CombatSystem.applyDamage(plant, RADIOACTIVE_PLANT_DAMAGE, true);
                }
            }
        }
    }

    private float computeNextInterval() {
        return (float) Math.max(6.0 + 0.0 * timeSinceGameStart, 12.0f);
    }

    private void spawnSun(@NotNull Field field) {
        int lane = randomGenerator.nextInt(field.getTotalLanes());
        int column = randomGenerator.nextInt(field.getTotalColumns());
        SunType type = SunType.random(randomGenerator);

        field.addSun(new SunInstance(type, lane, column));
    }
}
