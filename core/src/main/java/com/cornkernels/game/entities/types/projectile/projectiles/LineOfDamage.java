package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class LineOfDamage extends AbstractProjectile {

    public float length;
    public float width;
    public boolean used = false;

    public LineOfDamage(Vec2d position, float length, float width, int damage) {
        super(damage, new Vec2d(0, 0), position);
        this.length = length;
        this.width = width;
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        this.used = true; // Mark as used the moment collision is processed

        if (target instanceof ZombieInstance || target instanceof Grave) {
            Vec2d targetPos = target.get(PositionComponent.class).position;
            Vec2d myPos = this.get(PositionComponent.class).position;

            if (Math.abs(targetPos.getY() - myPos.getY()) <= (width / 2.0f) &&
                Math.abs(targetPos.getX() - myPos.getX()) <= (length / 2.0f)) {
                CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            }
        }

        // Always return false so the Line AOE is not destroyed immediately
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        return new LineOfDamage(newPosition, this.length, this.width, this.get(DamageComponent.class).amount);
    }
}
