package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.systems.entity.CombatSystem;

public class AreaOfDamage extends AbstractProjectile {

    public float radius;

    /*
     * This class should be used to make a circular area to deal damage to zombies.
     * It should have a position and a radius and damage nothing else.
     * It has a lifespan of 1 tick.
     * It should be spawned in lobProjectile if areaOfEffect != 0.
     */
    public AreaOfDamage(Vec2d position, float radius, int damage) {
        super(damage, new Vec2d(0, 0), position);
        this.radius = radius;
    }

    @Override
    public boolean hit(Entity target) {
        if (super.hit(target)&&target.get(PositionComponent.class).position.distance(this.get(PositionComponent.class).position)<radius) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
        }
        // Always return false so the AOE is not destroyed immediately upon hitting one target
        return false;
    }


}
