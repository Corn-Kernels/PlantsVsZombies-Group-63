package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class HomingProjectile extends AbstractProjectile {

    private final static float HOMING_SPEED = 1f;
    public Entity target;

    public HomingProjectile(int damage, Vec2d startPosition, Entity target) {
        super(damage, new Vec2d(HOMING_SPEED, 0), startPosition);
        this.target = target;
    }

    @Override
    public boolean hit(Entity target, Field field) {
        if (target == this.target && super.hit(target, field)) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new HomingProjectile(damage, newPosition, this.target);
    }
}
