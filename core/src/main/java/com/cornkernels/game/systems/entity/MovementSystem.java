package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;

public class MovementSystem extends EntitySystem {

    @Override
    public void update(float delta) {
        for (Entity e : field.getEntities()) {
            if (!e.has(PositionComponent.class) || !e.has(VelocityComponent.class)) {
                continue;
            }

            if (e instanceof ZombieInstance zombie
                && zombie.get(ZombieStateComponent.class).state == ZombieStateComponent.State.EATING) {
                continue;
            }

            PositionComponent posComp = e.get(PositionComponent.class);
            VelocityComponent velComp = e.get(VelocityComponent.class);

            if (e instanceof HomingProjectile homingProj) {
                if (homingProj.target != null && homingProj.target.has(PositionComponent.class)) {
                    Vec2d pos = posComp.position;
                    Vec2d targetPos = homingProj.target.get(PositionComponent.class).position;
                    Vec2d vel = velComp.velocityPerTick;

                    double desiredAngle = Math.atan2(targetPos.getY() - pos.getY(), targetPos.getX() - pos.getX());
                    double currentAngle = Math.atan2(vel.getY(), vel.getX());

                    double deltaAngle = desiredAngle - currentAngle;

                    deltaAngle = Math.atan2(Math.sin(deltaAngle), Math.cos(deltaAngle));

                    double maxRotation = Math.toRadians(10);
                    double newAngle;

                    if (Math.abs(deltaAngle) <= maxRotation) {
                        newAngle = desiredAngle;
                    } else {
                        newAngle = currentAngle + Math.signum(deltaAngle) * maxRotation;
                    }

                    double speedMag = Math.hypot(vel.getX(), vel.getY());

                    velComp.velocityPerTick = new Vec2d(
                        (float) (Math.cos(newAngle) * speedMag),
                        (float) (Math.sin(newAngle) * speedMag)
                    );
                }

                posComp.position = new Vec2d(
                    posComp.position.getX() + velComp.velocityPerTick.getX(),
                    posComp.position.getY() + velComp.velocityPerTick.getY()
                );

                continue;
            }

            float speed = velComp.velocityPerTick.getX();
            if (e instanceof ZombieInstance) {
                speed *= delta;
            }
            posComp.position.subtractInPlace(new Vec2d(speed, 0));
        }
    }
}
