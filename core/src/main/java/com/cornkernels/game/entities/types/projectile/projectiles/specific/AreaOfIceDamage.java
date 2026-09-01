package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class AreaOfIceDamage extends AbstractProjectile {

    public float radius;
    public boolean used = false;
    public int freezeDurationTicks;
    public int residualChillTicks;

    public AreaOfIceDamage(Vec2d position, float radius, int damage, int freezeDurationTicks, int residualChillTicks) {
        super(damage, new Vec2d(0, 0), position);
        this.radius = radius;
        this.freezeDurationTicks = freezeDurationTicks;
        this.residualChillTicks = residualChillTicks;
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        this.used = true; // Mark as used the moment collision is processed

        if (super.hit(target, field) && target.get(PositionComponent.class).position.distance(this.get(PositionComponent.class).position) < radius) {

            // Deal the standard Ice-shroom 20 damage
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);

            // Apply hard freeze and the lingering slow
            if (target.has(IceComponent.class)) {
                target.get(IceComponent.class).applyFreeze(freezeDurationTicks, residualChillTicks);
            }
        }

        // Always return false so the AOE is not destroyed immediately upon hitting just one target
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        return new AreaOfIceDamage(newPosition, this.radius, this.get(DamageComponent.class).amount, this.freezeDurationTicks, this.residualChillTicks);
    }
}
