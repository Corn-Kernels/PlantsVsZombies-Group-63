package com.cornkernels.game.entities.types.projectile;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.map.Field;

public class AbstractZombieProjectile extends Entity {

    private static final double HIT_DISTANCE = 0.3f;

    public AbstractZombieProjectile(int damage, Vec2d velocity, Vec2d Position) {
        super();
        add(new DamageComponent(damage));
        add(new VelocityComponent(velocity));
        add(new PositionComponent(Position));
        add(new PamAnimationComponent());
    }

    public AbstractProjectile clone(Vec2d newPosition) {
        return null;
    }

    public boolean hit(Entity target, Field field) {
        double distance = Math.abs(target.get(PositionComponent.class).position.distance(this.get(PositionComponent.class).position));

        return (target instanceof PlantInstance) && distance <= HIT_DISTANCE;
    }
}
