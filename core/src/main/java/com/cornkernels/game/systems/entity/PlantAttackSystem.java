package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.plant_specific.PlantAttackComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;

public class PlantAttackSystem extends EntitySystem {

    @Override
    public void update(float deltaTick) {
        for (PlantInstance plant : field.getActivePlants()) {
            if (plant.isMarkedForRemoval()) continue;

            // Check if plant is frozen solid
            PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
            if (freezeComp != null && freezeComp.frozenHp > 0) {
                continue; // Cannot attack or tick down cooldown while frozen!
            }

            PlantAttackComponent attack = plant.get(PlantAttackComponent.class);
            if (attack == null) continue;

            // Fixed math: Correctly decrement the cooldown timer by deltaTick without snapping to 0 instantly
            attack.cooldownRemaining = Math.max(0, attack.cooldownRemaining - deltaTick);

            if (attack.cooldownRemaining > 0) continue;

            if (!attack.behavior.hasTarget(plant, field)) continue;

            attack.behavior.execute(plant, field);
            attack.cooldownRemaining = attack.actionInterval;
        }
    }
}
