package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LineOfDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.GrapeshotProjectile;

public class MovementSystem extends EntitySystem {

    @Override
    public void update(float delta) {
        for (Entity e : field.getEntities()) {
            if (!e.has(PositionComponent.class) || !e.has(VelocityComponent.class)) {
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

            if (e instanceof GrapeshotProjectile grapeshotProjectile) {
                grapeshotProjectile.handleBounceAndLifetime(1f/20f, 0.5f, 9.5f, 0.5f, 5.5f);
            }

            if (e instanceof AreaOfDamage) {
                if (((AreaOfDamage) e).used) {
                    e.markForRemoval();
                } else {
                    ((AreaOfDamage) e).used = true;
                }
                continue; // AoEs don't move, skip standard movement logic
            }

            if (e instanceof LineOfDamage) {
                if (((LineOfDamage) e).used) {
                    e.markForRemoval();
                } else {
                    ((LineOfDamage) e).used = true;
                }
                continue; // Line AoEs don't move, skip standard movement logic
            }

            // Standard Movement Logic (Now correctly applies X and Y velocities for bouncing projectiles!)
            posComp.position = new Vec2d(
                posComp.position.getX() + velComp.velocityPerTick.getX(),
                posComp.position.getY() + velComp.velocityPerTick.getY()
            );
        }
    }
}
