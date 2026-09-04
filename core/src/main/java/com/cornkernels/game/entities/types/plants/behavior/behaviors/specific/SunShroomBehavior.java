package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.SunShroomComponent;
import com.cornkernels.game.entities.components.sun_specific.SunComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.map.Field;

public class SunShroomBehavior implements PlantAttackBehavior {
    private final int ticksToStage2;
    private final int ticksToStage3;
    private final int produceIntervalTicks;
    private final double doubleSunChance;
    private static final float RANDOM_OFFSET_RANGE = 0.5f;

    public SunShroomBehavior(float secondsToStage2, float secondsToStage3, float produceIntervalSeconds, double doubleSunChance) {
        this.ticksToStage2 = (int) (secondsToStage2 * 20);
        this.ticksToStage3 = (int) (secondsToStage3 * 20);
        this.produceIntervalTicks = (int) (produceIntervalSeconds * 20);
        this.doubleSunChance = doubleSunChance;
    }

    @Override
    public void execute(Entity self, Field field) {
        SunShroomComponent shroomState = self.get(SunShroomComponent.class);

        if (shroomState == null) {
            shroomState = new SunShroomComponent();
            self.add(shroomState);
        }

        shroomState.ticksAlive++;
        shroomState.sunTimer++;

        // Growth Logic
        if (shroomState.currentStage == 1 && shroomState.ticksAlive >= ticksToStage2) {
            shroomState.currentStage = 2;
        } else if (shroomState.currentStage == 2 && shroomState.ticksAlive >= ticksToStage3) {
            shroomState.currentStage = 3;
        }

        // Sun Production Logic
        if (shroomState.sunTimer >= produceIntervalTicks) {
            shroomState.sunTimer = 0;

            SunType typeToSpawn = SunType.NORMAL; // 25 sun
            if (shroomState.currentStage == 2) {
                typeToSpawn = SunType.BIG; // 50 sun
            } else if (shroomState.currentStage == 3) {
                typeToSpawn = SunType.LARGE; // 75 sun
            }

            int amountToSpawn = 1;
            if (this.doubleSunChance > 0 && Math.random() < this.doubleSunChance) {
                amountToSpawn = 2;
            }

            Vec2d pos = self.get(PositionComponent.class).position;
            for (int i = 0; i < amountToSpawn; i++) {
                float offsetLane = (float)(Math.random() -1.2) / 2 ;

                // Spawns precisely on the shroom, then arcs out to the endY/velocityX trajectory
                float startY = pos.getY() + 0.5f;
                float startX = pos.getX();
                float endY = pos.getY() + offsetLane;

                SunInstance sun = new SunInstance(typeToSpawn, startY, startX, endY);
                SunComponent sunComp = sun.get(SunComponent.class);
                sunComp.state = SunComponent.State.SUN_FLOWER;

                // Hop Up and Out
                sunComp.velocityY = 0.05f;
                sunComp.velocityX = (float) (Math.random() * 0.06 - 0.03);

                field.addSun(sun);
            }
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }
}
