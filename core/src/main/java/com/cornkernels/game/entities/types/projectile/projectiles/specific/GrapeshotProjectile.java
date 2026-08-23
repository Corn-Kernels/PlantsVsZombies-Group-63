package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.projectile.projectiles.StrikeThroughProjectile;

public class GrapeshotProjectile extends StrikeThroughProjectile {
    public float lifetime;
    public int bouncesLeft;

    public GrapeshotProjectile(int damage, Vec2d startPosition, Vec2d velocity, int pierceCount, int maxBounces, float lifetime) {
        super(damage, startPosition, pierceCount);

        // StrikeThrough defaults to (1, 0) velocity, so we must overwrite it here
        VelocityComponent velComp = this.get(VelocityComponent.class);
        if (velComp != null) {
            velComp.velocityPerTick = velocity;
        }

        this.bouncesLeft = maxBounces;
        this.lifetime = lifetime;
    }

    @Override
    public GrapeshotProjectile clone(Vec2d newPosition) {
        VelocityComponent velComp = this.get(VelocityComponent.class);
        Vec2d vel = velComp != null ? new Vec2d(velComp.velocityPerTick.getX(), velComp.velocityPerTick.getY()) : new Vec2d(0, 0);
        return new GrapeshotProjectile(this.get(DamageComponent.class).amount, newPosition, vel, this.pierceLeft, this.bouncesLeft, this.lifetime);
    }


    public void handleBounceAndLifetime(float dt, float minX, float maxX, float minY, float maxY) {
        lifetime -= dt;
        if (lifetime <= 0) {
            this.markForRemoval();
            return;
        }

        if (bouncesLeft > 0) {
            Vec2d pos = this.get(PositionComponent.class).position;
            Vec2d vel = this.get(VelocityComponent.class).velocityPerTick;
            boolean bounced = false;

            // Bounce off Left/Right walls
            if (pos.getX() <= minX || pos.getX() >= maxX) {
                vel.setX(-vel.getX());
                // Clamp position slightly inside bounds to prevent getting stuck in the wall
                if (pos.getX() <= minX) pos.setX(minX + 0.05f);
                if (pos.getX() >= maxX) pos.setX(maxX - 0.05f);
                bounced = true;
            }

            // Bounce off Top/Bottom walls
            if (pos.getY() <= minY || pos.getY() >= maxY) {
                vel.setY(-vel.getY());
                // Clamp position
                if (pos.getY() <= minY) pos.setY(minY + 0.05f);
                if (pos.getY() >= maxY) pos.setY(maxY - 0.05f);
                bounced = true;
            }

            if (bounced) {
                bouncesLeft--;
            }
        }
    }
}
