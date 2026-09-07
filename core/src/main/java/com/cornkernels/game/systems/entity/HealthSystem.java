package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.map.Field;

public class HealthSystem extends EntitySystem {

    private Field field;

    public HealthSystem(Field field) {
        this.field = field;
    }

    @Override
    public void update(float delta) {
        for (Entity entity : field.getEntitiesWith(HealthComponent.class)) {
            HealthComponent health = entity.get(HealthComponent.class);
            if (health.currentHealth <= 0) entity.markForRemoval();
        }
    }
}
