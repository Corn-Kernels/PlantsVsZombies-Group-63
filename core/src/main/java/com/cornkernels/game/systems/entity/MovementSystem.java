package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.sun_specific.SunComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.ButterComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.EnragedComponent;
import com.cornkernels.game.entities.types.obstacles.Redirector;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.BoneProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.OctopusProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LineOfDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.AreaOfIceDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.GrapeshotProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.LightningCloudProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.TruePeaProjectile;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.ZombieLimbs;

import java.util.Iterator;
import java.util.List;

public class MovementSystem extends EntitySystem {

    @Override
    public void update(float delta) {
        for (Entity e : field.getEntities()) {

            if(e instanceof ZombieLimbs){
                ((ZombieLimbs) e).update(delta);
                continue;
            }

            // New Physics-Based Sun Logic
            if (e instanceof SunInstance sun) {
                SunComponent sunComp = sun.get(SunComponent.class);
                if (sunComp.state != SunComponent.State.LANDED) {
                    PositionComponent pComp = sun.get(PositionComponent.class);
                    float dt = delta * 20f;

                    if (sunComp.state == SunComponent.State.SUN_FLOWER) {
                        sunComp.velocityY += -0.01f * dt; // Gravity arc
                    } else if (sunComp.state == SunComponent.State.FALLING) {
                        sunComp.velocityY = -0.035f; // Constant steady drop from the sky
                    }

                    float newX = pComp.position.getX() + (sunComp.velocityX * dt);
                    float newY = pComp.position.getY() + (sunComp.velocityY * dt);

                    // Stop on landing
                    if (newY <= sunComp.endY) {
                        newY = sunComp.endY;
                        sunComp.state = SunComponent.State.LANDED;
                        sunComp.velocityX = 0f;
                        sunComp.velocityY = 0f;

                        // Defuse unclicked Radioactive suns upon landing
                        if (sunComp.type == SunType.RADIOACTIVE) {
                            sunComp.type = SunType.NORMAL;
                            if (sun.has(PamAnimationComponent.class)) {
                                sun.get(PamAnimationComponent.class).tint.set(1f, 1f, 1f, 1f);
                            }
                        }
                    }
                    pComp.position = new Vec2d(newX, newY);
                }
                continue;
            }

            if (!e.has(PositionComponent.class) || !e.has(VelocityComponent.class)) {
                continue;
            }

            if (e instanceof ZombieInstance zombie
                && zombie.get(ZombieStateComponent.class).state == ZombieStateComponent.State.EATING) {
                continue;
            }

            PositionComponent posComp = e.get(PositionComponent.class);
            VelocityComponent velComp = e.get(VelocityComponent.class);

            switch (e) {
                case TruePeaProjectile pea -> {
                    pea.checkTorchwood(field);
                }
                case HomingProjectile homingProj -> {
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
                case LightningCloudProjectile cloudProj -> {
                    if (cloudProj.target != null && cloudProj.target.has(PositionComponent.class)) {
                        Vec2d pos = posComp.position;
                        Vec2d targetPos = cloudProj.target.get(PositionComponent.class).position;
                        Vec2d vel = new Vec2d(0.4f,0);

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

                    removeIfOffBoard(cloudProj);
                    continue;
                }
                case GrapeshotProjectile grapeshotProjectile ->
                    grapeshotProjectile.handleBounceAndLifetime(1f / 20f, 0.5f, 9.5f, 0.5f, 5.5f);
                default -> {
                }
            }

            // 3. AoE Processing (No movement applied)
            if (e instanceof AreaOfDamage) {
                if (((AreaOfDamage) e).used)e.markForRemoval();
                continue;
            }

            if (e instanceof AreaOfIceDamage) {
                if (((AreaOfIceDamage) e).used) e.markForRemoval();
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

            // 3. Bone Projectile Tracking & Arrival
            if (e instanceof BoneProjectile boneProj) {
                Vec2d pos = posComp.position;
                double targetX = boneProj.getTargetTile().column();
                double targetY = boneProj.getTargetTile().lane();

                double dist = Math.hypot(targetX - pos.getX(), targetY - pos.getY());

                // Dynamic Tracking (Adjust speedMag as needed for throw speed)
                double desiredAngle = Math.atan2(targetY - pos.getY(), targetX - pos.getX());
                double speedMag = 0.05;

                velComp.velocityPerTick = new Vec2d(
                    (float) (Math.cos(desiredAngle) * speedMag),
                    (float) (Math.sin(desiredAngle) * speedMag)
                );

                posComp.position = new Vec2d(
                    posComp.position.getX() + velComp.velocityPerTick.getX(),
                    posComp.position.getY() + velComp.velocityPerTick.getY()
                );
                continue;
            }
            // 4. Octopus Projectile Tracking
            if (e instanceof OctopusProjectile octoProj) {
                PlantInstance targetPlant = octoProj.getTargetPlant();

                // Drop out of the sky if the plant was dug up or destroyed early
                if (targetPlant == null || targetPlant.isMarkedForRemoval()) {
                    octoProj.markForRemoval();
                    continue;
                }

                Vec2d pos = posComp.position;
                Vec2d targetPos = targetPlant.get(PositionComponent.class).position;

                double targetX = targetPos.getX();
                double targetY = targetPos.getY();

                // Dynamic Tracking
                double desiredAngle = Math.atan2(targetY - pos.getY(), targetX - pos.getX());
                double speedMag = 0.05;

                velComp.velocityPerTick = new Vec2d(
                    (float) (Math.cos(desiredAngle) * speedMag),
                    (float) (Math.sin(desiredAngle) * speedMag)
                );

                posComp.position = new Vec2d(
                    posComp.position.getX() + velComp.velocityPerTick.getX(),
                    posComp.position.getY() + velComp.velocityPerTick.getY()
                );
                continue;
            }

            if(e instanceof LobProjectile){
                e.get(VelocityComponent.class).velocityPerTick.set((0.5f*(((LobProjectile) e).target.get(PositionComponent.class).position.getX()-((LobProjectile) e).startPosition.getX()-0.3f)/9f),0);
            }

            // 4. Standard Movement Logic
            float vx = velComp.velocityPerTick.getX();
            float vy = velComp.velocityPerTick.getY();

            if (e instanceof ZombieInstance) {
                vx *= delta;
                vy *= delta;
            } else {
                vx /= 5;
                vy /= 5;
            }

            if (e.has(EnragedComponent.class)) {
                vx *= e.get(EnragedComponent.class).speedMultiplier;
            }

            if(e instanceof ZombieInstance){
                ZombieInstance zombie=(ZombieInstance) e;

                // 1. Tile Slider Logic (Redirectors)
                for (Entity ent : field.getEntities()) {
                    if (ent instanceof Redirector r && r.has(PositionComponent.class)) {
                        Vec2d rPos = r.get(PositionComponent.class).position;

                        // Check if within the horizontal capture window
                        if (Math.abs(posComp.position.getX() - rPos.getX()) < 0.15f) {

                            // Check if sliding UP (+Y)
                            if (r.direction == Redirector.Direction.UP
                                && posComp.position.getY() >= rPos.getY() - 0.05f
                                && posComp.position.getY() < rPos.getY() + 1.0f) {

                                vx = 0; // Stop walking forward
                                vy = 1.5f * delta; // Slide speed
                                posComp.position.setX(rPos.getX()); // Snap to exact column center to cleanly ride the slide

                                // Snap cleanly to target lane exactly to prevent overshooting
                                if (posComp.position.getY() + vy > rPos.getY() + 1.0f) {
                                    vy = (float) (rPos.getY() + 1.0f - posComp.position.getY());
                                }
                                break;

                                // Check if sliding DOWN (-Y)
                            } else if (r.direction == Redirector.Direction.DOWN
                                && posComp.position.getY() <= rPos.getY() + 0.05f
                                && posComp.position.getY() > rPos.getY() - 1.0f) {

                                vx = 0;
                                vy = -1.5f * delta;
                                posComp.position.setX(rPos.getX());

                                if (posComp.position.getY() + vy < rPos.getY() - 1.0f) {
                                    vy = (float) (rPos.getY() - 1.0f - posComp.position.getY());
                                }
                                break;
                            }
                        }
                    }
                }

                // 2. Process Ice (Freeze & Slow)
                IceComponent ice = zombie.get(IceComponent.class);
                if (ice != null && ice.freezeLevel > 0) {
                    if (ice.freezeLevel == 2) {
                        vx=0;
                        vy=0;
                    }
                    if (ice.freezeLevel == 1) {
                        vx/=2;
                        vy/=2;
                    }
                    if (ice.freezeTicksRemaining > 0) {
                        ice.freezeTicksRemaining--;
                        if (ice.freezeTicksRemaining <= 0) {
                            ice.freezeLevel = 1; // Thaw into a slow/chill state
                        }
                    }

                    if (ice.slowTicksRemaining > 0) {
                        ice.slowTicksRemaining--;
                        if (ice.slowTicksRemaining <= 0 && ice.freezeTicksRemaining <= 0) {
                            ice.melt(); // Completely clear the debuff
                        }
                    }
                }

                // 3. Process Stuns (Butter)
                if (zombie.has(ButterComponent.class)) {
                    List<ButterComponent> stuns = zombie.getAll(ButterComponent.class);
                    Iterator<ButterComponent> iterator = stuns.iterator();

                    while (iterator.hasNext()) {
                        ButterComponent stun = iterator.next();
                        stun.stunTicksRemaining--;
                        vx=0;
                        vy=0;
                        if (stun.stunTicksRemaining <= 0) {
                            iterator.remove();
                        }
                    }
                }
            }


            posComp.position = new Vec2d(
                posComp.position.getX() + vx,
                posComp.position.getY() + vy
            );

            if(e instanceof LobProjectile lobProj){
                float startX = lobProj.startPosition.getX();
                float endX;
                float endY;

                if (lobProj.target != null) {
                    endX = lobProj.target.get(PositionComponent.class).position.getX();
                    endY = lobProj.target.get(PositionComponent.class).position.getY();
                } else {
                    endX = 14;
                    endY = lobProj.startPosition.getY();
                }

                float currentX = posComp.position.getX();
                float totalDistance = endX - startX;
                float progress = (totalDistance == 0) ? 1.0f : (currentX - startX) / totalDistance;
                progress = Math.max(0.0f, Math.min(1.0f, progress));

                // 16.0f determines the apex height of the arc
                float heightOffset = 16.0f * progress * (1.0f - progress);

                // Linearly interpolate the base Y between the start lane and target lane
                float startY = lobProj.startPosition.getY();
                float baseY = startY + (endY - startY) * progress;

                // Combine the interpolated Y with the parabolic height offset
                e.get(PositionComponent.class).position.setY(baseY + heightOffset);

                if(currentX > endX) {
                    e.get(PositionComponent.class).position.setY(20);
                }
            }

        }
    }

    private void removeIfOffBoard(Entity e) {
        if (!(e instanceof AbstractProjectile projectile) || projectile instanceof GrapeshotProjectile) return;

        Vec2d pos = projectile.get(PositionComponent.class).position;
        boolean offBoard = pos.getX() < -2.0f || pos.getX() > field.getTotalColumns() + 2
            || pos.getY() < -2f || pos.getY() > field.getTotalLanes() + 1;
        if (offBoard) {
            projectile.markForRemoval();
        }
    }
}
