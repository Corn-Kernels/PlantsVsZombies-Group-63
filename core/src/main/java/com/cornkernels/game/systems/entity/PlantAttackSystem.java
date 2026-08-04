package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.plant_specific.PlantAttackComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;

public class PlantAttackSystem extends EntitySystem {

    @Override
    public void update(float deltaTick) {
        for (PlantInstance plant : field.getActivePlants()) {
            if (plant.isMarkedForRemoval()) continue;

            PlantAttackComponent attack = plant.get(PlantAttackComponent.class);
            if (attack == null) continue;

            attack.cooldownRemaining -= Math.clamp(attack.cooldownRemaining - deltaTick, 0, attack.actionInterval);
            if (attack.cooldownRemaining > 0) continue;

            if (!attack.behavior.hasTarget(plant, field)) continue;

            attack.behavior.execute(plant, field);
            attack.cooldownRemaining = attack.actionInterval;
        }
    }
}
