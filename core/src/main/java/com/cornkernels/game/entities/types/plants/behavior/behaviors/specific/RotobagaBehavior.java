package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import org.jspecify.annotations.NonNull;

public class RotobagaBehavior implements PlantAttackBehavior {

    private final AbstractProjectile projectile;
    private final int shotCount = 3;
    private final double shotSpacing = 0.3;
    private final float length = 5.0f;
    private final float halfWidth = 0.75f;

    public RotobagaBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        double centerX = origin.getX() + 0.5;
        double centerY = origin.getY();

        float baseSpeed = 1.0f;
        VelocityComponent baseVelComp = projectile.get(VelocityComponent.class);
        if (baseVelComp != null && baseVelComp.velocityPerTick != null) {
            double speed = Math.hypot(baseVelComp.velocityPerTick.getX(), baseVelComp.velocityPerTick.getY());
            if (speed > 0) baseSpeed = (float) speed;
        }

        float diagSpeed = (float) (baseSpeed / Math.sqrt(2));
        float diagThreshold = (float) (halfWidth * Math.sqrt(2));

        boolean shootTopRight = false;
        boolean shootBottomRight = false;
        boolean shootTopLeft = false;
        boolean shootBottomLeft = false;

        for (Entity e : getAllValidTargets(field)) {

            Vec2d pos = e.get(PositionComponent.class).position;
            double dx = pos.getX() - centerX;
            double dy = pos.getY() - centerY;

            if (Math.hypot(dx, dy) <= length) {
                if (dx >= 0 && dy <= 0 && Math.abs(dx + dy) <= diagThreshold) {
                    shootTopRight = true;
                } else if (dx >= 0 && dy >= 0 && Math.abs(dy - dx) <= diagThreshold) {
                    shootBottomRight = true;
                } else if (dx <= 0 && dy <= 0 && Math.abs(dy - dx) <= diagThreshold) {
                    shootTopLeft = true;
                } else if (dx <= 0 && dy >= 0 && Math.abs(dx + dy) <= diagThreshold) {
                    shootBottomLeft = true;
                }
            }
        }

        if (shootTopRight) spawnProjectiles(field, centerX, centerY, diagSpeed, -diagSpeed, 1, -1);
        if (shootBottomRight) spawnProjectiles(field, centerX, centerY, diagSpeed, diagSpeed, 1, 1);
        if (shootTopLeft) spawnProjectiles(field, centerX, centerY, -diagSpeed, -diagSpeed, -1, -1);
        if (shootBottomLeft) spawnProjectiles(field, centerX, centerY, -diagSpeed, diagSpeed, -1, 1);
    }

    private void spawnProjectiles(Field field, double startX, double startY, float velX, float velY, int dirX, int dirY) {
        double offsetMult = shotSpacing / Math.sqrt(2);
        for (int i = 0; i < shotCount; i++) {
            Vec2d spawnPos = new Vec2d(
                (float) (startX + dirX * i * offsetMult),
                (float) (startY + dirY * i * offsetMult)
            );

            AbstractProjectile p = projectile.clone(spawnPos);
            VelocityComponent velComp = p.get(VelocityComponent.class);
            if (velComp != null && velComp.velocityPerTick != null) {
                velComp.velocityPerTick.setX(velX);
                velComp.velocityPerTick.setY(velY);
            }
            field.addProjectile(p);
        }
    }

    @Override
    public boolean hasTarget(@NonNull Entity self, @NonNull Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        double centerX = origin.getX() + 0.5;
        double centerY = origin.getY();
        float diagThreshold = (float) (halfWidth * Math.sqrt(2));

        for (Entity e : field.getEntities()) {
            if ((e instanceof ZombieInstance || e instanceof Grave) && !e.isMarkedForRemoval()) {
                if (e instanceof ZombieInstance && e.has(HypnoComponent.class)) continue;

                Vec2d pos = e.get(PositionComponent.class).position;
                double dx = pos.getX() - centerX;
                double dy = pos.getY() - centerY;

                if (Math.hypot(dx, dy) <= length) {
                    if (dx >= 0 && dy <= 0 && Math.abs(dx + dy) <= diagThreshold) return true;
                    if (dx >= 0 && dy >= 0 && Math.abs(dy - dx) <= diagThreshold) return true;
                    if (dx <= 0 && dy <= 0 && Math.abs(dy - dx) <= diagThreshold) return true;
                    if (dx <= 0 && dy >= 0 && Math.abs(dx + dy) <= diagThreshold) return true;
                }
            }
        }
        return false;
    }
}
