package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;

public class StarfruitBehavior implements PlantAttackBehavior {

    private final AbstractProjectile projectile;

    public StarfruitBehavior(AbstractProjectile projectile) {
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

        // Calculate diagonal speed (cos(45) and sin(45) are approx 0.707)
        float diagSpeed = (float) (baseSpeed * 0.7071f);

        // Define the 5 standard Starfruit directions (Velocity X, Velocity Y)
        float[][] directions = {
            {-baseSpeed, 0},         // Backward
            {0, -baseSpeed},         // Up
            {0, baseSpeed},          // Down
            {diagSpeed, -diagSpeed}, // Top-Right (Forward-Up)
            {diagSpeed, diagSpeed}   // Bottom-Right (Forward-Down)
        };

        for (float[] dir : directions) {
            Vec2d spawnPos = new Vec2d((float) centerX, (float) centerY);
            AbstractProjectile p = projectile.clone(spawnPos);

            VelocityComponent velComp = p.get(VelocityComponent.class);
            if (velComp != null && velComp.velocityPerTick != null) {
                velComp.velocityPerTick.setX(dir[0]);
                velComp.velocityPerTick.setY(dir[1]);
            }

            field.addProjectile(p);
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        double centerX = origin.getX() + 0.5;
        double centerY = origin.getY();

        float halfWidth = 0.5f; // Threshold for lane width tolerance
        float diagThreshold = (float) (halfWidth * Math.sqrt(2));
        float length = 12.0f; // Max tile scanning distance (since starfruit hits the whole screen)

        for (Entity e : field.getEntities()) {
            if ((e instanceof ZombieInstance || e instanceof Grave) && !e.isMarkedForRemoval()) {
                Vec2d pos = e.get(PositionComponent.class).position;
                double dx = pos.getX() - centerX;
                double dy = pos.getY() - centerY;

                if (Math.hypot(dx, dy) <= length) {
                    // Backward (-X, 0Y)
                    if (dx <= 0 && Math.abs(dy) <= halfWidth) return true;
                    // Up (0X, -Y)
                    if (dy <= 0 && Math.abs(dx) <= halfWidth) return true;
                    // Down (0X, +Y)
                    if (dy >= 0 && Math.abs(dx) <= halfWidth) return true;
                    // Top-Right (+X, -Y) -> Line: y = -x
                    if (dx >= 0 && dy <= 0 && Math.abs(dx + dy) <= diagThreshold) return true;
                    // Bottom-Right (+X, +Y) -> Line: y = x
                    if (dx >= 0 && dy >= 0 && Math.abs(dy - dx) <= diagThreshold) return true;
                }
            }
        }
        return false;
    }
}
