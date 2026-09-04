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

public class StarfruitBehavior implements PlantAttackBehavior {

    private final AbstractProjectile projectile;

    public StarfruitBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        double centerX = origin.getX();
        double centerY = origin.getY();

        float baseSpeed = 1.0f;
        VelocityComponent baseVelComp = projectile.get(VelocityComponent.class);
        if (baseVelComp != null && baseVelComp.velocityPerTick != null) {
            double speed = Math.hypot(baseVelComp.velocityPerTick.getX(), baseVelComp.velocityPerTick.getY());
            if (speed > 0) baseSpeed = (float) speed;
        }

        float diagSpeed = (float) (baseSpeed * 0.7071f);

        float[][] directions = {
            {-baseSpeed, 0},
            {0, -baseSpeed},
            {0, baseSpeed},
            {diagSpeed, -diagSpeed},
            {diagSpeed, diagSpeed}
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

        float halfWidth = 0.5f;
        float diagThreshold = (float) (halfWidth * Math.sqrt(2));
        float length = 12.0f;

        for (Entity e : getAllValidTargets(field)) {

                Vec2d pos = e.get(PositionComponent.class).position;
                double dx = pos.getX() - centerX;
                double dy = pos.getY() - centerY;

                if (Math.hypot(dx, dy) <= length) {
                    if (dx <= 0 && Math.abs(dy) <= halfWidth) return true;
                    if (dy <= 0 && Math.abs(dx) <= halfWidth) return true;
                    if (dy >= 0 && Math.abs(dx) <= halfWidth) return true;
                    if (dx >= 0 && dy <= 0 && Math.abs(dx + dy) <= diagThreshold) return true;
                    if (dx >= 0 && dy >= 0 && Math.abs(dy - dx) <= diagThreshold) return true;
                }
        }
        return false;
    }
}
