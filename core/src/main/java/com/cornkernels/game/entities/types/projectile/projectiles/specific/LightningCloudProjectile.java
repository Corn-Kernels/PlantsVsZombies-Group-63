package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.systems.entity.CombatSystem;

public class LightningCloudProjectile extends AbstractProjectile {
    public Entity target;

    public LightningCloudProjectile(int damage, Vec2d startPosition, Entity target) {
        // Lightning clouds move slightly faster than normal peas to ensure they reach their target
        super(damage, new Vec2d(2.0f, 0), startPosition);
        this.target = target;
    }

    @Override
    public boolean hit(Entity target) {
        if (target == this.target && super.hit(target)) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new LightningCloudProjectile(damage, newPosition, this.target);
    }
}
