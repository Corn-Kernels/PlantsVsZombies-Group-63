package com.cornkernels.game.entities.components.plant_specific;

import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;

public class PlantFoodComponent {

    public final PlantFoodBehavior behavior;

    public int timerTicks = 0;
    public int normalTime=0;
    public PlantFoodComponent(PlantFoodBehavior behavior,int time) {
        this.behavior=behavior;
        this.normalTime=time;
    }

    public void activate(){
        timerTicks=normalTime;
    }
}
