package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.PoisonComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class GooPeaProjectile extends AbstractProjectile {

    private static final float PEA_SPEED = 1f;
    public int poisonTickDamage;

    public GooPeaProjectile(int damage, Vec2d startPosition, int poisonTickDamage) {
        super(damage, new Vec2d(PEA_SPEED, 0), startPosition);
        this.poisonTickDamage = poisonTickDamage;
    }

    @Override
    public boolean hit(Entity target, Field field) {
        if (super.hit(target, field)) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);

            // 100 ticks total lifetime (5 sec), ticks damage every 20 ticks (1 sec)
            target.add(new PoisonComponent(poisonTickDamage, 100, 20));
            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new GooPeaProjectile(damage, newPosition, this.poisonTickDamage);
    }
}
