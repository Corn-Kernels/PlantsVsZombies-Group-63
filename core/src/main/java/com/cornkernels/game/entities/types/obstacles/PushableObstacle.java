package com.cornkernels.game.entities.types.obstacles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.map.Field;

public class PushableObstacle extends AbstractObstacle {

    public PushableObstacle(Vec2d position, int maxHp) {
        super(position);

        HealthComponent health = new HealthComponent();
        health.maxHealth = maxHp;
        health.currentHealth = maxHp;
        this.add(health);
    }

    public void update(Field field) {
        if (isMarkedForRemoval()) return;
    }
}
