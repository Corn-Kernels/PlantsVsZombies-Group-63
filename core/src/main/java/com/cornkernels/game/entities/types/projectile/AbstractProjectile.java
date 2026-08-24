package com.cornkernels.game.entities.types.projectile;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field; // Added import
import org.jspecify.annotations.NonNull;

public class AbstractProjectile extends Entity {

    private static final double HIT_DISTANCE = 0.3f;

    public AbstractProjectile(int damage, Vec2d velocity, Vec2d Position) {
        super();
        add(new DamageComponent(damage));
        add(new VelocityComponent(velocity));
        add(new PositionComponent(Position));
        add(new PamAnimationComponent());
    }

    public AbstractProjectile clone(Vec2d newPosition) {
        return null;
    }

    public boolean hit(@NonNull Entity target, Field field) {
        double distance = Math.abs(target.get(PositionComponent.class).position.distance(this.get(PositionComponent.class).position));

        return (target instanceof ZombieInstance || target instanceof Grave) && distance <= HIT_DISTANCE;
    }
}
