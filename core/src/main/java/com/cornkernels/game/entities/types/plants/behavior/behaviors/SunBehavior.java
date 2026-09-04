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

    public SunBehavior(int sunCount, SunType sunType) {
        this(sunCount, sunType, 0.0);
    }

    public SunBehavior(int sunCount, SunType sunType, double doubleSunChance) {
        this.sunCount = sunCount;
        this.sunType = sunType;
        this.doubleSunChance = doubleSunChance;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d sunSpawnPos = self.get(PositionComponent.class).position;

        int actualSunCount = this.sunCount;
        if (this.doubleSunChance > 0 && Math.random() < this.doubleSunChance) {
            actualSunCount *= 2;
        }

        for (int i = 0; i < actualSunCount; i++) {
            float baseLane = sunSpawnPos.getY();
            float baseColumn = sunSpawnPos.getX();

            float offsetLane = (float) (Math.random() - 1.2) / 2;

            // Start EXACTLY at the plant (plus the 0.5f vertical hop offset)
            float startY = baseLane + 0.5f;
            float startX = baseColumn;

            // End Y incorporates the random spread so it lands uniquely
            float endY = baseLane + offsetLane;

            SunInstance sun = new SunInstance(sunType, startY, startX, endY);
            SunComponent sunComp = sun.get(SunComponent.class);
            sunComp.state = SunComponent.State.SUN_FLOWER;

            // The velocityX creates the horizontal random spread as it arcs
            sunComp.velocityY = 0.05f;
            sunComp.velocityX = (float) (Math.random() * 0.06 - 0.03);

            field.getActiveSuns().add(sun);
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }
}
