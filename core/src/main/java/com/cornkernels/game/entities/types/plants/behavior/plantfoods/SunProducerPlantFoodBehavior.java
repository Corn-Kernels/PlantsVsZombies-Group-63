package com.cornkernels.game.entities.types.plants.behavior.plantfoods;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantAttackComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.map.Field;
import org.jspecify.annotations.NonNull;

public class SunProducerPlantFoodBehavior implements PlantFoodBehavior {

    private final int sunsPerWave;
    private final SunType sunType;

    public SunProducerPlantFoodBehavior(int sunsPerWave, SunType sunType) {
        this.sunsPerWave = sunsPerWave;
        this.sunType = sunType;
    }

    @Override
    public boolean plantFood(@NonNull PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0) {

            // Spawn a wave exactly every 10 ticks (e.g., at tick 30, 20, 10)
            if (pf.timerTicks % 10 == 0) {
                Vec2d origin = plant.get(PositionComponent.class).position;

                plant.get(PlantAttackComponent.class).behavior.execute(plant,field);
            }

            pf.timerTicks--;
            return false; // Reject new plant food while active
        }

        return true;
    }
}
