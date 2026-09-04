package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class ThreepeaterBehavior implements PlantAttackBehavior {
    private final AbstractProjectile projectile;

    public ThreepeaterBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int currentLane = GridPosition.fromContinuous(origin).lane();

        // Top lane (lane - 1)
        if (currentLane - 1 >= 0) {
            field.addProjectile(projectile.clone(new Vec2d(origin.getX() + 0.5f, origin.getY() - 1)));
        }

        // Current lane
        if (currentLane >= 0 && currentLane < field.getTotalLanes()) {
            field.addProjectile(projectile.clone(new Vec2d(origin.getX() + 0.5f, origin.getY())));
        }

        // Bottom lane (lane + 1)
        if (currentLane + 1 < field.getTotalLanes()) {
            field.addProjectile(projectile.clone(new Vec2d(origin.getX() + 0.5f, origin.getY() + 1)));
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int plantLane = GridPosition.fromContinuous(origin).lane();

        // Use the universal targeting logic
        for (Entity target : getAllValidTargets(field)) {
            if (!target.has(PositionComponent.class)) continue;

            Vec2d targetPos = target.get(PositionComponent.class).position;
            int targetLane = GridPosition.fromContinuous(targetPos).lane();

            // Check if the target is in front of the plant AND in one of the 3 adjacent lanes
            if (targetPos.getX() >= origin.getX() && Math.abs(targetLane - plantLane) <= 1) {
                return true;
            }
        }

        return false;
    }
}
