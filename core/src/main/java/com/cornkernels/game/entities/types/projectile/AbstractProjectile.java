package com.cornkernels.game.entities.types.projectile;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
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

    private static double distancePointToSegment(@NonNull Vec2d point, @NonNull Vec2d segStart, @NonNull Vec2d segEnd) {
        Vec2d segment = segEnd.subtract(segStart);
        float lengthSquared = segment.magnitudeSquared();
        if (lengthSquared <= 1e-6f) {
            return point.distance(segEnd);
        }

        float t = point.subtract(segStart).dot(segment) / lengthSquared;
        t = Math.clamp(t, 0f, 1f);
        Vec2d closest = segStart.add(segment.multiply(t));
        return point.distance(closest);
    }

    public AbstractProjectile clone(Vec2d newPosition) {
        return null;
    }

    public boolean hit(@NonNull Entity target, Field field) {
        Vec2d targetPos = target.get(PositionComponent.class).position;
        Vec2d currentPos = this.get(PositionComponent.class).position;

        VelocityComponent velComp = this.get(VelocityComponent.class);
        Vec2d previousPos = velComp != null ? currentPos.subtract(velComp.velocityPerTick) : currentPos;

        return distancePointToSegment(targetPos, previousPos, currentPos) <= HIT_DISTANCE;
    }
}
