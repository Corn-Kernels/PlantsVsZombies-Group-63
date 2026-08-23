package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.types.projectile.projectiles.PeaProjectile;

public class GooPeaProjectile extends PeaProjectile {
    private final int poisonDamagePerTick;

    public GooPeaProjectile(int baseDamage, Vec2d startPosition, int poisonDamagePerTick) {
        super(baseDamage, startPosition);
        this.poisonDamagePerTick = poisonDamagePerTick;
    }

    @Override
    public boolean hit(Entity target) {
        if (super.hit(target)) {
            // TODO: Apply a PoisonComponent to the target using poisonDamagePerTick
            return true;
        }
        return false;
    }

    @Override
    public GooPeaProjectile clone(Vec2d newPosition) {
        return new GooPeaProjectile(this.get(com.cornkernels.game.entities.components.DamageComponent.class).amount, newPosition, this.poisonDamagePerTick);
    }
}
