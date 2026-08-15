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

        // Top lane
        field.addProjectile(projectile.clone(new Vec2d(origin.getX() + 0.5f, origin.getY() - 1)));
        // Current lane
        field.addProjectile(projectile.clone(new Vec2d(origin.getX() + 0.5f, origin.getY())));
        // Bottom lane
        field.addProjectile(projectile.clone(new Vec2d(origin.getX() + 0.5f, origin.getY() + 1)));
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        int lane = GridPosition.fromContinuous(self.get(PositionComponent.class).position).lane();

        return field.getZombiesInLane(lane).stream().anyMatch(z -> !z.isMarkedForRemoval())
            || field.getZombiesInLane(lane - 1).stream().anyMatch(z -> !z.isMarkedForRemoval())
            || field.getZombiesInLane(lane + 1).stream().anyMatch(z -> !z.isMarkedForRemoval());
    }
}
