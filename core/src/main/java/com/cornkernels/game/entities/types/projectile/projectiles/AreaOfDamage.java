package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.systems.entity.CombatSystem;

public class AreaOfDamage extends AbstractProjectile {

    public float radius;
    public boolean used = false;

    public AreaOfDamage(Vec2d position, float radius, int damage) {
        super(damage, new Vec2d(0, 0), position);
        this.radius = radius;
    }

    @Override
    public boolean hit(Entity target) {
        this.used = true; // Mark as used the moment collision is processed

        if (super.hit(target) && target.get(PositionComponent.class).position.distance(this.get(PositionComponent.class).position) < radius) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
        }

        // Always return false so the AOE is not destroyed immediately upon hitting just one target
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        return new AreaOfDamage(newPosition, this.radius, this.get(DamageComponent.class).amount);
    }
}
