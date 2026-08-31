package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.map.Field;

public interface PlantFoodBehavior {
    default boolean plantFood(PlantInstance plant, Field field) {
        return false;
    }
}
