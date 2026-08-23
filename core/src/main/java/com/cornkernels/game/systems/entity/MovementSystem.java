package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LineOfDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.GrapeshotProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.LightningCloudProjectile;

public class MovementSystem extends EntitySystem {

    @Override
    public void update(float delta) {
        for (Entity e : field.getEntities()) {
            if (!e.has(PositionComponent.class) || !e.has(VelocityComponent.class)) {
                continue;
            }

            PositionComponent posComp = e.get(PositionComponent.class);
            VelocityComponent velComp = e.get(VelocityComponent.class);

            // 1. Standard Homing Projectiles (Cattail, Hypno-shroom)
            // Uses an arcing turn speed limit of 10 degrees per tick
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

            // 2. Straight-Line Tracking (Electric Blueberry)
            // Bypasses the rotation limit to snap directly towards the target
            if (e instanceof LightningCloudProjectile cloudProj) {
                if (cloudProj.target != null && cloudProj.target.has(PositionComponent.class)) {
                    Vec2d pos = posComp.position;
                    Vec2d targetPos = cloudProj.target.get(PositionComponent.class).position;
                    Vec2d vel = velComp.velocityPerTick;

                    double desiredAngle = Math.atan2(targetPos.getY() - pos.getY(), targetPos.getX() - pos.getX());
                    double speedMag = Math.hypot(vel.getX(), vel.getY());

                    velComp.velocityPerTick = new Vec2d(
                        (float) (Math.cos(desiredAngle) * speedMag),
                        (float) (Math.sin(desiredAngle) * speedMag)
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

            // 3. AoE Processing (No movement applied)
            if (e instanceof AreaOfDamage) {
                if (((AreaOfDamage) e).used) {
                    e.markForRemoval();
                } else {
                    ((AreaOfDamage) e).used = true;
                }
                continue;
            }

            if (e instanceof LineOfDamage) {
                if (((LineOfDamage) e).used) {
                    e.markForRemoval();
                } else {
                    ((LineOfDamage) e).used = true;
                }
                continue;
            }

            // 4. Standard Movement Logic
            posComp.position = new Vec2d(
                posComp.position.getX() + velComp.velocityPerTick.getX(),
                posComp.position.getY() + velComp.velocityPerTick.getY()
            );
        }
    }
}
