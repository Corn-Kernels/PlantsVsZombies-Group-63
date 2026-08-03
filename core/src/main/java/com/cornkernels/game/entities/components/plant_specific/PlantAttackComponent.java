package com.cornkernels.game.entities.components.plant_specific;


import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;

public class PlantAttackComponent {

    public final PlantAttackBehavior behavior;
    public final float actionInterval;
    public float cooldownRemaining;

    public PlantAttackComponent(PlantAttackBehavior behavior, float actionInterval) {
        this.behavior = behavior;
        this.actionInterval = actionInterval;
        this.cooldownRemaining = 0f;
    }
}
