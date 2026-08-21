package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.systems.entity.CombatSystem;

public class LineOfDamage extends AbstractProjectile {

    public float length;
    public float width;

    /*
     * This class should be used to make a rectangular area to deal damage to zombies (e.g., Jalapeno).
     * It has a lifespan of 1 tick.
     */
    public LineOfDamage(Vec2d position, float length, float width, int damage) {
        super(damage, new Vec2d(0, 0), position);
        this.length = length;
        this.width = width;
    }

    @Override
    public boolean hit(Entity target) {
        if (super.hit(target)) {
            Vec2d targetPos = target.get(PositionComponent.class).position;
            Vec2d myPos = this.get(PositionComponent.class).position;

            // Check if the target falls within the rectangle
            if (Math.abs(targetPos.getY() - myPos.getY()) <= (width / 2.0f) &&
                Math.abs(targetPos.getX() - myPos.getX()) <= (length / 2.0f)) {
                CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            }
        }
        // Always return false so the AOE is not destroyed immediately upon hitting one target
        return false;
    }
}
