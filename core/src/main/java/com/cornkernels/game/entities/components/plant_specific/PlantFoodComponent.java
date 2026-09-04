package com.cornkernels.game.entities.components.plant_specific;

import com.cornkernels.game.entities.types.effects.PlantFoodEffect;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;

public class PlantFoodComponent {

    public final PlantFoodBehavior behavior;

    public int timerTicks = 0;
    public int normalTime = 0;

    public PlantFoodEffect effectEntity;

    public PlantFoodComponent(PlantFoodBehavior behavior, int time) {
        this.behavior = behavior;
        this.normalTime = time;
    }

    public boolean isActive() {
        return timerTicks > 0;
    }

    public void activate() {
        if (timerTicks == 0) timerTicks = normalTime;
    }
}
