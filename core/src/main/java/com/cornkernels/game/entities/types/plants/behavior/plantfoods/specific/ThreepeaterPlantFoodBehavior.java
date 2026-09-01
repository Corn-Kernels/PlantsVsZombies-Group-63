package com.cornkernels.game.entities.types.plants.behavior.plantfoods.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;

public class ThreepeaterPlantFoodBehavior implements PlantFoodBehavior {

    private final AbstractProjectile projectile;

    public ThreepeaterPlantFoodBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public boolean plantFood(PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0 && pf.timerTicks % 2 == 0) {
            Vec2d origin = plant.get(PositionComponent.class).position;
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());

            // 1. The straight line pea
            field.addProjectile(projectile.clone(spawnPos));

            // 2. The sweeping peas
            int elapsed = pf.normalTime - pf.timerTicks;

            // Start at 60 and -60, changing by 3 degrees per tick
            double angle1 = 60.0 - (3.0 * elapsed);
            double angle2 = -60.0 + (3.0 * elapsed);

            spawnAngledPea(field, spawnPos, angle1);
            spawnAngledPea(field, spawnPos, angle2);

            pf.timerTicks--;
            return false; // Reject new plant food while active
        }

        return true;
    }

    private void spawnAngledPea(Field field, Vec2d spawnPos, double angleDeg) {
        AbstractProjectile pea = projectile.clone(spawnPos);
        VelocityComponent velComp = pea.get(VelocityComponent.class);

        if (velComp != null) {
            float vx = velComp.velocityPerTick.getX();
            float vy = velComp.velocityPerTick.getY();

            // Get the base speed magnitude of the cloned projectile
            double speed = Math.hypot(vx, vy);
            double rad = Math.toRadians(angleDeg);

            // Rotate the velocity vector
            velComp.velocityPerTick = new Vec2d(
                (float) (speed * Math.cos(rad)),
                (float) (speed * Math.sin(rad))
            );
        }

        field.addProjectile(pea);
    }
}
