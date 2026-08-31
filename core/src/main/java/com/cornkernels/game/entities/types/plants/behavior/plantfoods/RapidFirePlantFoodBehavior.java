package com.cornkernels.game.entities.types.plants.behavior.plantfoods;

import com.cornkernels.game.entities.components.plant_specific.PlantAttackComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.map.Field;

public class RapidFirePlantFoodBehavior implements PlantFoodBehavior {


    @Override
    public boolean plantFood(PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0&&pf.timerTicks%2==0) {
            // Retrieve the attack component to trigger the normal attack logic
            PlantAttackComponent attack = plant.get(PlantAttackComponent.class);
            if (attack != null) {
                attack.behavior.execute(plant, field);
            }

            pf.timerTicks--; // Must decrement so the plant food state eventually ends
            return false;
        }

        return true;
    }
}
