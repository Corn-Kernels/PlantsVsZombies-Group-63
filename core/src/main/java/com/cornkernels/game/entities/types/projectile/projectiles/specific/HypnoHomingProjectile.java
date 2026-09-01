package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class HypnoHomingProjectile extends HomingProjectile {

    public HypnoHomingProjectile(int damage, Vec2d startPosition, Entity target) {
        super(0, startPosition, target);
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        if(super.hit(target, field) && target instanceof ZombieInstance){
            target.add(new HypnoComponent());
            VelocityComponent vel = target.get(VelocityComponent.class);
            vel.velocityPerTick.setX(Math.abs(vel.velocityPerTick.getX())); // Forces X to be positive (moving right)
            return true;
        }
        return false;
    }

    @Override
    public HypnoHomingProjectile clone(Vec2d newPosition) {
        return new HypnoHomingProjectile(this.get(com.cornkernels.game.entities.components.DamageComponent.class).amount, newPosition, this.target);
    }
}
