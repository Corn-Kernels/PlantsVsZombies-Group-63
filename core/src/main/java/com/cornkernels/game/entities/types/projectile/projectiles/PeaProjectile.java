package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.systems.entity.CombatSystem;

public class PeaProjectile extends AbstractProjectile {

    private static final float PEA_SPEED = 1f;

    public PeaProjectile(int damage, Vec2d startPosition) {
        super(damage, new Vec2d(PEA_SPEED, 0), startPosition);

    }

    @Override
    public boolean hit(Entity target){
        if(super.hit(target)){
                CombatSystem.applyDamage(target,this.get(DamageComponent.class).amount,false);
            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new PeaProjectile(damage, newPosition);
    }
}
