package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage; // Added import
import com.cornkernels.game.map.Field; // Added import
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

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
    public boolean hit(@NonNull Entity target, Field field) {
        if (super.hit(target, field)) {
            int baseDamage = this.get(DamageComponent.class).amount;

            if (heat == 1) {
                // Inflamed: Deal double damage.
                int finalDamage = baseDamage * 2;

                // Calculate splash damage (20% rounded up)
                int splashDamage = (int) Math.ceil(baseDamage * 0.20);

                // We subtract the splash damage from the direct hit so the primary target
                // doesn't accidentally take double damage when the AoE hits it next tick!
                CombatSystem.applyDamage(target, finalDamage - splashDamage, false);

                // Spawn a small 0.3 radius AOE with 20% damage
                field.addProjectile(new AreaOfDamage(this.get(PositionComponent.class).position, 0.3f, splashDamage));

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
