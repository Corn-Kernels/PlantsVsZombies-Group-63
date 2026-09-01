package com.cornkernels.game.entities.types.plants.behavior.plantfoods;

import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import com.cornkernels.game.map.Field;

public class DefensivePlantFoodBehavior implements PlantFoodBehavior {

    private final ArmorType armorType;

    public DefensivePlantFoodBehavior(ArmorType armorType) {
        this.armorType = armorType;
    }

    @Override
    public boolean plantFood(PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0) {

            // Execute the heal and armor application exactly once when the ability begins
            if (pf.timerTicks == pf.normalTime) {
                // 1. Heal to maximum health
                HealthComponent health = plant.get(HealthComponent.class);
                if (health != null) {
                    health.currentHealth = health.maxHealth;
                }

                // 2. Grant the generic ArmorComponent
                plant.removeAll(ArmorComponent.class); // Remove if refreshing existing armor
                plant.add(new ArmorComponent(armorType));
            }

            pf.timerTicks--;
            return false; // Reject new plant food while active
        }

        return true;
    }
}
