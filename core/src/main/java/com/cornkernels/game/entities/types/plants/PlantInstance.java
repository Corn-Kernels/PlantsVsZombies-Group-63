package com.cornkernels.game.entities.types.plants;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.GridPositionComponent;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.*;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehaviors;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehaviors;
import com.cornkernels.game.map.grid.GridPosition;

public class PlantInstance extends Entity {

    public PlantInstance(PlantDef plantDef, GridPosition position) {
        super();

        add(new PlantDefComponent(plantDef));
        add(new PositionComponent(GridPosition.toVec2d(position)));
        add(new GridPositionComponent(position));
        add(new PlantStateComponent());
        add(new PamAnimationComponent());
        add(new PlantFreezeComponent());

        HealthComponent health = new HealthComponent();
        health.maxHealth = plantDef.getBaseHp();
        health.currentHealth = plantDef.getBaseHp();
        add(health);

        PlantAttackBehavior behavior = PlantAttackBehaviors.get(plantDef);
        if (behavior != null) {
            float interval = plantDef.getActionInterval().orElse(0.0f);
            add(new PlantAttackComponent(behavior, interval));
        }

        // Unpack the bundled PlantFoodEntry
        PlantFoodBehaviors.PlantFoodEntry pfEntry = PlantFoodBehaviors.getEntry(plantDef);
        if (pfEntry != null && pfEntry.activeTimeTicks() > 0) {
            add(new PlantFoodComponent(pfEntry.behavior(), pfEntry.activeTimeTicks()));
        }
    }
}
