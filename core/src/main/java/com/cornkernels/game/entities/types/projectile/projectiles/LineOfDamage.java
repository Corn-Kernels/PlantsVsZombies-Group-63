package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class LineOfDamage extends AbstractProjectile {

    public float length;
    public float width;
    public boolean used = false;
    public boolean fiery;

    // Overloaded constructor so existing non-fire plants don't break
    public LineOfDamage(Vec2d position, float length, float width, int damage) {
        this(position, length, width, damage, false);
    }

    public LineOfDamage(Vec2d position, float length, float width, int damage, boolean fiery) {
        super(damage, new Vec2d(0, 0), position);
        this.length = length;
        this.width = width;
        this.fiery = fiery;
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        this.used = true;

        Vec2d targetPos = target.get(PositionComponent.class).position;
        Vec2d myPos = this.get(PositionComponent.class).position;

        if (Math.abs(targetPos.getY() - myPos.getY()) <= (width / 2.0f) &&
            Math.abs(targetPos.getX() - myPos.getX()) <= (length / 2.0f)) {

            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);

            // Melt the zombie if this is a fire line (like Jalapeno)
            if (fiery && target.has(IceComponent.class)) {
                target.get(IceComponent.class).melt();
            }
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        return new LineOfDamage(newPosition, this.length, this.width, this.get(DamageComponent.class).amount, this.fiery);
    }
}
