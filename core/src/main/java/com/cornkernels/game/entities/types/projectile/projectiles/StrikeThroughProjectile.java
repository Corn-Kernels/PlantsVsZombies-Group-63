package com.cornkernels.game.entities.types.projectile.projectiles;

import java.util.ArrayList;
import java.util.List;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class StrikeThroughProjectile extends AbstractProjectile {

    private static final float PIERCE_SPEED = 1f;
    public int pierceLeft;
    public List<Entity> hitTargets = new ArrayList<>();

    public StrikeThroughProjectile(int damage, Vec2d startPosition, int pierceCount) {
        super(damage, new Vec2d(PIERCE_SPEED, 0), startPosition);
        this.pierceLeft = pierceCount;
    }

    @Override
    public boolean hit(Entity target, Field field) {
        // Ensure the target is valid (Zombie or Grave) and has not already been hit by this projectile
        if (super.hit(target, field) && !hitTargets.contains(target)) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            hitTargets.add(target);
            pierceLeft--;

            // Return true to destroy the projectile only when pierce left reaches 0
            return pierceLeft <= 0;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new StrikeThroughProjectile(damage, newPosition, this.pierceLeft);
    }
}
