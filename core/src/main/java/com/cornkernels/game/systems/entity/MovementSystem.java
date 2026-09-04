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
            if (processSpecialMovement(e, delta)) continue;

            if (!e.has(PositionComponent.class) || !e.has(VelocityComponent.class)) continue;
            if (isZombieEating(e)) continue;

            PositionComponent posComp = e.get(PositionComponent.class);
            VelocityComponent velComp = e.get(VelocityComponent.class);

            if (processProjectileTracking(e, posComp, velComp)) continue;
            if (processAoE(e)) continue;

            applyStandardMovement(e, posComp, velComp, delta);
        }
    }

    private boolean processSpecialMovement(Entity e, float delta) {
        if (e instanceof ZombieLimbs limbs) {
            limbs.update(delta);
            return true;
        }
        if (e instanceof SunInstance sun) {
            updateSunFalling(sun, delta);
            return true;
        }
        return false;
    }

    private void updateSunFalling(SunInstance sun, float delta) {
        SunComponent sunComp = sun.get(SunComponent.class);
        if (sunComp.state == SunComponent.State.LANDED) return;

        PositionComponent pComp = sun.get(PositionComponent.class);
        float dt = delta * 20f;

        if (sunComp.state == SunComponent.State.SUN_FLOWER) {
            sunComp.velocityY += -0.01f * dt;
        } else if (sunComp.state == SunComponent.State.FALLING) {
            sunComp.velocityY = -0.035f;
        }

        float newX = pComp.position.getX() + (sunComp.velocityX * dt);
        float newY = pComp.position.getY() + (sunComp.velocityY * dt);

        if (newY <= sunComp.endY) {
            newY = sunComp.endY;
            landSun(sun, sunComp);
        }
        pComp.position = new Vec2d(newX, newY);
    }

    private void landSun(SunInstance sun, SunComponent sunComp) {
        sunComp.state = SunComponent.State.LANDED;
        sunComp.velocityX = 0f;
        sunComp.velocityY = 0f;

        if (sunComp.type == SunType.RADIOACTIVE) {
            sunComp.type = SunType.NORMAL;
            if (sun.has(PamAnimationComponent.class)) {
                sun.get(PamAnimationComponent.class).tint.set(1f, 1f, 1f, 1f);
            }
        }
    }

    private boolean isZombieEating(Entity e) {
        if (e instanceof ZombieInstance zombie) {
            ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
            return state != null && state.state == ZombieStateComponent.State.EATING;
        }
        return false;
    }

    private boolean processProjectileTracking(Entity e, PositionComponent posComp, VelocityComponent velComp) {
        if (e instanceof TruePeaProjectile pea) {
            pea.checkTorchwood(field);
        } else if (e instanceof HomingProjectile homingProj) {
            updateHomingProjectile(homingProj, posComp, velComp);
            return true;
        } else if (e instanceof LightningCloudProjectile cloudProj) {
            updateLightningCloud(cloudProj, posComp, velComp);
            return true;
        } else if (e instanceof GrapeshotProjectile grape) {
            grape.handleBounceAndLifetime(1f / 20f, 0.5f, 9.5f, 0.5f, 5.5f);
        } else if (e instanceof BoneProjectile bone) {
            updateBoneProjectile(bone, posComp, velComp);
            return true;
        } else if (e instanceof OctopusProjectile octo) {
            updateOctopusProjectile(octo, posComp, velComp);
            return true;
        } else if (e instanceof LobProjectile lobProj) {
            velComp.velocityPerTick.set((0.5f * (getTargetX(lobProj) - lobProj.startPosition.getX() - 0.3f) / 9f), 0);
        }
        return false;
    }

    private float getTargetX(LobProjectile lobProj) {
        return lobProj.target != null ? lobProj.target.get(PositionComponent.class).position.getX() : 14f;
    }

    private void updateHomingProjectile(HomingProjectile homingProj, PositionComponent posComp, VelocityComponent velComp) {
        if (homingProj.target != null && homingProj.target.has(PositionComponent.class)) {
            Vec2d targetPos = homingProj.target.get(PositionComponent.class).position;
            double desiredAngle = Math.atan2(targetPos.getY() - posComp.position.getY(), targetPos.getX() - posComp.position.getX());
            double currentAngle = Math.atan2(velComp.velocityPerTick.getY(), velComp.velocityPerTick.getX());

            double deltaAngle = Math.atan2(Math.sin(desiredAngle - currentAngle), Math.cos(desiredAngle - currentAngle));
            double maxRotation = Math.toRadians(10);

            double newAngle = Math.abs(deltaAngle) <= maxRotation ? desiredAngle : currentAngle + Math.signum(deltaAngle) * maxRotation;
            double speedMag = Math.hypot(velComp.velocityPerTick.getX(), velComp.velocityPerTick.getY());

            velComp.velocityPerTick = new Vec2d((float) (Math.cos(newAngle) * speedMag), (float) (Math.sin(newAngle) * speedMag));
        }
        updateProjectilePosition(posComp, velComp);
    }

    private void updateLightningCloud(LightningCloudProjectile cloudProj, PositionComponent posComp, VelocityComponent velComp) {
        if (cloudProj.target != null && cloudProj.target.has(PositionComponent.class)) {
            Vec2d targetPos = cloudProj.target.get(PositionComponent.class).position;
            double desiredAngle = Math.atan2(targetPos.getY() - posComp.position.getY(), targetPos.getX() - posComp.position.getX());
            double speedMag = 0.4f;

            velComp.velocityPerTick = new Vec2d((float) (Math.cos(desiredAngle) * speedMag), (float) (Math.sin(desiredAngle) * speedMag));
        }
        updateProjectilePosition(posComp, velComp);
        removeIfOffBoard(cloudProj);
    }

    private void updateBoneProjectile(BoneProjectile boneProj, PositionComponent posComp, VelocityComponent velComp) {
        double targetX = boneProj.getTargetTile().column();
        double targetY = boneProj.getTargetTile().lane();
        double desiredAngle = Math.atan2(targetY - posComp.position.getY(), targetX - posComp.position.getX());
        double speedMag = 0.05;

        velComp.velocityPerTick = new Vec2d((float) (Math.cos(desiredAngle) * speedMag), (float) (Math.sin(desiredAngle) * speedMag));
        updateProjectilePosition(posComp, velComp);
    }

    private void updateOctopusProjectile(OctopusProjectile octoProj, PositionComponent posComp, VelocityComponent velComp) {
        PlantInstance targetPlant = octoProj.getTargetPlant();
        if (targetPlant == null || targetPlant.isMarkedForRemoval()) {
            octoProj.markForRemoval();
            return;
        }

        Vec2d targetPos = targetPlant.get(PositionComponent.class).position;
        double desiredAngle = Math.atan2(targetPos.getY() - posComp.position.getY(), targetPos.getX() - posComp.position.getX());
        double speedMag = 0.05;

        velComp.velocityPerTick = new Vec2d((float) (Math.cos(desiredAngle) * speedMag), (float) (Math.sin(desiredAngle) * speedMag));
        updateProjectilePosition(posComp, velComp);
    }

    private void updateProjectilePosition(PositionComponent posComp, VelocityComponent velComp) {
        posComp.position = new Vec2d(
            posComp.position.getX() + velComp.velocityPerTick.getX(),
            posComp.position.getY() + velComp.velocityPerTick.getY()
        );
    }

    private boolean processAoE(Entity e) {
        if (e instanceof AreaOfDamage aod) {
            if (aod.used) aod.markForRemoval();
            return true;
        }
        if (e instanceof AreaOfIceDamage aoice) {
            if (aoice.used) aoice.markForRemoval();
            return true;
        }
        if (e instanceof LineOfDamage lod) {
            if (lod.used) lod.markForRemoval();
            else lod.used = true;
            return true;
        }
        return false;
    }

    private void applyStandardMovement(Entity e, PositionComponent posComp, VelocityComponent velComp, float delta) {
        float vx = calculateBaseVelocityX(e, velComp, delta);
        float vy = calculateBaseVelocityY(e, velComp, delta);

        if (e instanceof ZombieInstance zombie) {
            Vec2d modified = applyZombieMovementModifiers(zombie, posComp, vx, vy, delta);
            vx = modified.getX();
            vy = modified.getY();
        }

        posComp.position = new Vec2d(posComp.position.getX() + vx, posComp.position.getY() + vy);
        applyLobArc(e, posComp);
    }

    private float calculateBaseVelocityX(Entity e, VelocityComponent velComp, float delta) {
        float vx = velComp.velocityPerTick.getX();
        vx = e instanceof ZombieInstance ? vx * delta : vx / 5f;
        if (e.has(EnragedComponent.class)) vx *= e.get(EnragedComponent.class).speedMultiplier;
        return vx;
    }

    private float calculateBaseVelocityY(Entity e, VelocityComponent velComp, float delta) {
        float vy = velComp.velocityPerTick.getY();
        return e instanceof ZombieInstance ? vy * delta : vy / 5f;
    }

    private Vec2d applyZombieMovementModifiers(ZombieInstance zombie, PositionComponent posComp, float vx, float vy, float delta) {
        Vec2d afterSliders = applyTileSliders(posComp, vx, vy, delta);
        vx = afterSliders.getX();
        vy = afterSliders.getY();

        Vec2d afterIce = applyIceDebuff(zombie, vx, vy);
        vx = afterIce.getX();
        vy = afterIce.getY();

        return applyButterDebuff(zombie, vx, vy);
    }

    private Vec2d applyTileSliders(PositionComponent posComp, float vx, float vy, float delta) {
        for (Entity ent : field.getEntities()) {
            if (ent instanceof Redirector r && r.has(PositionComponent.class)) {
                Vec2d rPos = r.get(PositionComponent.class).position;

                if (Math.abs(posComp.position.getX() - rPos.getX()) < 0.15f) {
                    if (r.direction == Redirector.Direction.UP && posComp.position.getY() >= rPos.getY() - 0.05f && posComp.position.getY() < rPos.getY() + 1.0f) {
                        vx = 0;
                        vy = 1.5f * delta;
                        posComp.position.setX(rPos.getX());
                        if (posComp.position.getY() + vy > rPos.getY() + 1.0f)
                            vy = (float) (rPos.getY() + 1.0f - posComp.position.getY());
                        break;
                    } else if (r.direction == Redirector.Direction.DOWN && posComp.position.getY() <= rPos.getY() + 0.05f && posComp.position.getY() > rPos.getY() - 1.0f) {
                        vx = 0;
                        vy = -1.5f * delta;
                        posComp.position.setX(rPos.getX());
                        if (posComp.position.getY() + vy < rPos.getY() - 1.0f)
                            vy = (float) (rPos.getY() - 1.0f - posComp.position.getY());
                        break;
                    }
                }
            }
        }
        return new Vec2d(vx, vy);
    }

    private Vec2d applyIceDebuff(ZombieInstance zombie, float vx, float vy) {
        IceComponent ice = zombie.get(IceComponent.class);
        if (ice != null && ice.freezeLevel > 0) {
            if (ice.freezeLevel == 2) {
                vx = 0;
                vy = 0;
            }
            if (ice.freezeLevel == 1) {
                vx /= 2;
                vy /= 2;
            }

            if (ice.freezeTicksRemaining > 0) {
                ice.freezeTicksRemaining--;
                if (ice.freezeTicksRemaining <= 0) ice.freezeLevel = 1;
            }
            if (ice.slowTicksRemaining > 0) {
                ice.slowTicksRemaining--;
                if (ice.slowTicksRemaining <= 0 && ice.freezeTicksRemaining <= 0) ice.melt();
            }
        }
        return new Vec2d(vx, vy);
    }

    private Vec2d applyButterDebuff(ZombieInstance zombie, float vx, float vy) {
        if (zombie.has(ButterComponent.class)) {
            List<ButterComponent> stuns = zombie.getAll(ButterComponent.class);
            Iterator<ButterComponent> iterator = stuns.iterator();

            while (iterator.hasNext()) {
                ButterComponent stun = iterator.next();
                stun.stunTicksRemaining--;
                vx = 0;
                vy = 0;
                if (stun.stunTicksRemaining <= 0) iterator.remove();
            }
        }
        return new Vec2d(vx, vy);
    }

    private void applyLobArc(Entity e, PositionComponent posComp) {
        if (e instanceof LobProjectile lobProj) {
            float startX = lobProj.startPosition.getX();
            float endX = lobProj.target != null ? lobProj.target.get(PositionComponent.class).position.getX() : 14f;
            float endY = lobProj.target != null ? lobProj.target.get(PositionComponent.class).position.getY() : lobProj.startPosition.getY();

            float currentX = posComp.position.getX();
            float totalDistance = endX - startX;
            float progress = Math.clamp((totalDistance == 0) ? 1.0f : (currentX - startX) / totalDistance, 0.0f, 1.0f);

            float heightOffset = 16.0f * progress * (1.0f - progress);
            float baseY = lobProj.startPosition.getY() + (endY - lobProj.startPosition.getY()) * progress;

            posComp.position.setY(baseY + heightOffset);
            if (currentX > endX) posComp.position.setY(20);
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
