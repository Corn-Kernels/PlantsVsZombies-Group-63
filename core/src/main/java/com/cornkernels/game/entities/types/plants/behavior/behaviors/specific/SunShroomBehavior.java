package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.SunShroomComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.map.Field;

public class SunShroomBehavior implements PlantAttackBehavior {
    private final int ticksToStage2;
    private final int ticksToStage3;
    private final int produceIntervalTicks;
    private final double doubleSunChance;

    public SunShroomBehavior(float secondsToStage2, float secondsToStage3, float produceIntervalSeconds, double doubleSunChance) {
        // 20 ticks per second
        this.ticksToStage2 = (int) (secondsToStage2 * 20);
        this.ticksToStage3 = (int) (secondsToStage3 * 20);
        this.produceIntervalTicks = (int) (produceIntervalSeconds * 20);
        this.doubleSunChance = doubleSunChance;
    }

    @Override
    public void execute(Entity self, Field field) {
        SunShroomComponent shroomState = self.get(SunShroomComponent.class);

        // Self-initialize the component if it hasn't been added yet
        if (shroomState == null) {
            shroomState = new SunShroomComponent();
            self.add(shroomState);
        }

        shroomState.ticksAlive++;
        shroomState.sunTimer++;

        // --- Growth Logic ---
        if (shroomState.currentStage == 1 && shroomState.ticksAlive >= ticksToStage2) {
            shroomState.currentStage = 2;
            // TODO: Update your PamAnimationComponent here to show the medium sprite
        } else if (shroomState.currentStage == 2 && shroomState.ticksAlive >= ticksToStage3) {
            shroomState.currentStage = 3;
            // TODO: Update your PamAnimationComponent here to show the large sprite
        }

        // --- Sun Production Logic ---
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
                field.addSun(new SunInstance(typeToSpawn, (int) pos.getY(), (int) pos.getX(), 0));
            }
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }
}
