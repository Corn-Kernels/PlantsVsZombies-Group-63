package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class AreaOfDamage extends AbstractProjectile {

    public float radius;
    public boolean used = false;
    public boolean fiery;
    public int chillDurationTicks;

    // Overloaded to keep standard non-elemental explosions intact
    public AreaOfDamage(Vec2d position, float radius, int damage) {
        this(position, radius, damage, false, 0);
    }

    public AreaOfDamage(Vec2d position, float radius, int damage, boolean fiery, int chillDurationTicks) {
        super(damage, new Vec2d(0, 0), position);
        this.radius = radius;
        this.fiery = fiery;
        this.chillDurationTicks = chillDurationTicks;
    }

    @Override
    public boolean hit(Entity target, Field field) {
        this.used = true;

        if (super.hit(target, field) && target.get(PositionComponent.class).position.distance(this.get(PositionComponent.class).position) < radius) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);

            if (fiery && target.has(IceComponent.class)) {
                target.get(IceComponent.class).melt();
            }
            if (chillDurationTicks > 0 && target.has(IceComponent.class)) {
                target.get(IceComponent.class).applyChill(chillDurationTicks);
            }
        }

        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        return new AreaOfDamage(newPosition, this.radius, this.get(DamageComponent.class).amount, this.fiery, this.chillDurationTicks);
    }
}
