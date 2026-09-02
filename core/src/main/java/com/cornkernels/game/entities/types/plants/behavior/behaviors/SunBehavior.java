package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.sun_specific.SunComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.map.Field;

public class SunBehavior implements PlantAttackBehavior {
    private final int sunCount;
    private final SunType sunType;
    private final double doubleSunChance;
    private static final float RANDOM_OFFSET_RANGE = 0.5f; // in grid cells

    // Standard constructor for 0% double chance
    public SunBehavior(int sunCount, SunType sunType) {
        this(sunCount, sunType, 0.0);
    }

    // Overloaded constructor for leveled plants with a double sun chance
    public SunBehavior(int sunCount, SunType sunType, double doubleSunChance) {
        this.sunCount = sunCount;
        this.sunType = sunType;
        this.doubleSunChance = doubleSunChance;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d sunSpawnPos = self.get(PositionComponent.class).position;

        int actualSunCount = this.sunCount;

        // Roll for double sun
        if (this.doubleSunChance > 0 && Math.random() < this.doubleSunChance) {
            actualSunCount *= 2;
        }

        for (int i = 0; i < actualSunCount; i++) {
            // Base grid position (lane = Y, column = X)
            float baseLane = sunSpawnPos.getY();
            float baseColumn = sunSpawnPos.getX();

            // Add small random offsets
            float offsetLane = (float)(Math.random() - 0.5) * 2 * RANDOM_OFFSET_RANGE;
            float offsetColumn = (float)(Math.random() - 0.5) * 3.5f * RANDOM_OFFSET_RANGE;

            // Create sun at the integer cell, then adjust position with offsets
            SunInstance sun = new SunInstance(sunType,baseLane+offsetLane,baseColumn+offsetColumn, 0.05f);
            sun.get(SunComponent.class).state= SunComponent.State.LANDED;
            field.getActiveSuns().add(sun);
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true; // You always make sun when you can, duh;
    }
}
