package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.systems.entity.CombatSystem;

public class TruePeaProjectile extends AbstractProjectile {

    private static final float PEA_SPEED = 1f;
    public final int heat; // -1 for cold, 0 for normal, 1 for inflamed

    public TruePeaProjectile(int damage, Vec2d startPosition) {
        this(damage, startPosition, 0);
    }

    public TruePeaProjectile(int damage, Vec2d startPosition, int heat) {
        super(damage, new Vec2d(PEA_SPEED, 0), startPosition);
        this.heat = heat;
    }

    @Override
    public boolean hit(Entity target) {
        if (super.hit(target)) {
            int baseDamage = this.get(DamageComponent.class).amount;

            if (heat == 1) {
                // Inflamed: Deal double damage
                int finalDamage = baseDamage * 2;
                CombatSystem.applyDamage(target, finalDamage, false);

                // Spawn a small 0.3 radius AOE with 20% damage (rounded up)
                // TODO: add the aoe spawning

                // Note: Add 'aoe' to your field via your combat/projectile system if not handled automatically
            } else if (heat == -1) {
                // Cold: Deal normal damage + slow down effect
                CombatSystem.applyDamage(target, baseDamage, false);
                // TODO: Implement zombie slow-down effect here
            } else {
                // Normal pea
                CombatSystem.applyDamage(target, baseDamage, false);
            }

            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new TruePeaProjectile(damage, newPosition, this.heat);
    }
}
