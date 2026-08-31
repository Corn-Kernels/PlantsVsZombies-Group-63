package com.cornkernels.game.entities.types.plants.behavior.plantfoods.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.SunShroomComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.map.Field;

public class SunShroomPlantFoodBehavior implements PlantFoodBehavior {

    private final int sunsPerWave;
    private final SunType sunType;

    public SunShroomPlantFoodBehavior(int sunsPerWave, SunType sunType) {
        this.sunsPerWave = sunsPerWave;
        this.sunType = sunType;
    }

    @Override
    public boolean plantFood(PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0) {

            // Instantly grow to max stage upon receiving plant food
            SunShroomComponent shroomComp = plant.get(SunShroomComponent.class);
            if (shroomComp != null && shroomComp.currentStage < 3) {
                shroomComp.currentStage = 3;
            }

            // Spawn a wave exactly every 10 ticks
            if (pf.timerTicks % 10 == 0) {
                Vec2d origin = plant.get(PositionComponent.class).position;

                for (int i = 0; i < sunsPerWave; i++) {
                    field.addSun(new SunInstance(sunType, (int) origin.getY(), (int) origin.getX()));
                }
            }

            pf.timerTicks--;
            return false; // Reject new plant food while active
        }

        return true;
    }
}
