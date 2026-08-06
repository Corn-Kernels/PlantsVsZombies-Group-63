package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.systems.entity.CombatSystem;

public class LobProjectile extends AbstractProjectile {

    private final static float LOB_SPEED = 1f;
    public Entity target;
    public Vec2d startPosition;
    public float areaOfEffect = 0f;

    public LobProjectile(int damage, Vec2d startPosition, Entity target,float radius) {
        super(damage, new Vec2d(LOB_SPEED, 0), startPosition);
        this.target = target;
        this.startPosition = startPosition;
        areaOfEffect=radius;
    }

    @Override
    public boolean hit(Entity target) {
        if (target == this.target && super.hit(target)) {
            if(areaOfEffect==0)
                CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            // else spawn the AOE
            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        // Preserves the assigned target from the original instance
        return new LobProjectile(damage, newPosition, this.target,this.areaOfEffect);
    }
}
