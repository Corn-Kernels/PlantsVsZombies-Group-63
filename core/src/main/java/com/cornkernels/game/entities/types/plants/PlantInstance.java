package com.cornkernels.game.entities.types.plants;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantAttackComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantDefComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantStateComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehaviors;

public class PlantInstance extends Entity {

    public PlantInstance(PlantDef plantDef, Vec2d position) {
        super();

        add(new PlantDefComponent(plantDef));
        add(new PositionComponent(position));
        add(new PlantStateComponent());
        add(new PamAnimationComponent());

        HealthComponent health = new HealthComponent();
        health.maxHealth = plantDef.getBaseHp();
        health.currentHealth = plantDef.getBaseHp();
        add(health);

        PlantAttackBehavior behavior = PlantAttackBehaviors.get(plantDef);
        if (behavior != null) {
            float interval = plantDef.getActionInterval().orElse(0.0f);
            add(new PlantAttackComponent(behavior, interval));
        }
    }
}
