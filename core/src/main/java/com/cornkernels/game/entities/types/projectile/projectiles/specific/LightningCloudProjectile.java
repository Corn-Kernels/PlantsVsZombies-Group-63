package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class LightningCloudProjectile extends AbstractProjectile {

    public Entity target;

    private int hitsRemaining = 10;
    private int ticker = 10;
    private final int totalDamage;

    public LightningCloudProjectile(int damage, Vec2d startPosition, Entity target) {
        super(damage, new Vec2d(0.4f, 0), startPosition);
        this.target = target;
        this.totalDamage = damage;
    }

    private LightningCloudProjectile(int damage, Vec2d startPosition, Entity target, int hitsRemaining, int ticker, int totalDamage) {
        super(damage, new Vec2d(0.4f, 0), startPosition);
        this.target = target;
        this.hitsRemaining = hitsRemaining;
        this.ticker = ticker;
        this.totalDamage = totalDamage;
    }

    @Override
    public boolean hit(@NonNull Entity hitTarget, Field field) {
        // Disappear if the target is already dead
        if (this.target == null || this.target.isMarkedForRemoval() || target.get(HealthComponent.class).currentHealth<=0) {
            return true;
        }


        if (hitTarget == this.target && super.hit(hitTarget, field)) {

            // Stop horizontal movement
            VelocityComponent vel = this.get(VelocityComponent.class);
            if (vel != null) {
                vel.velocityPerTick.setX(0);
                vel.velocityPerTick.setY(0);
            }

            // Hover slightly above the target
            PositionComponent myPos = this.get(PositionComponent.class);
            PositionComponent targetPos = this.target.get(PositionComponent.class);
            if (myPos != null && targetPos != null) {
                myPos.position.setX(targetPos.position.getX());
                myPos.position.setY(targetPos.position.getY() + 0.5f);
            }

            // Tick down every time hit() is called
            ticker--;
            if (ticker <= 0) {
                CombatSystem.applyDamage(this.target, totalDamage / 10, false);
                hitsRemaining--;
                ticker = 10; // Reset ticker

                // Destroy cloud if all 10 hits are done or target dies from this hit
                if (hitsRemaining <= 0 || this.target.isMarkedForRemoval()) {
                    return true;
                }
            }

            return false; // Keep the projectile alive to continue hitting
        }

        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        return new LightningCloudProjectile(
            this.get(DamageComponent.class).amount,
            newPosition,
            this.target,
            this.hitsRemaining,
            this.ticker,
            this.totalDamage
        );
    }
}
