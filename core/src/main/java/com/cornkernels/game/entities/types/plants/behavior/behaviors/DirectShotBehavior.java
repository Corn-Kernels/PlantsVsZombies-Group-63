package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.PeaProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class DirectShotBehavior implements PlantAttackBehavior {

    private final int shotCount;
    private final int damagePerShot;
    private final double shotSpacing = 0.3f;
    private final boolean threePeater = false;

    public DirectShotBehavior(int shotCount, int damagePerShot) {
        this.shotCount = shotCount;
        this.damagePerShot = damagePerShot;
    }


    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        for (int i = 0; i < shotCount; i++) {
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5 + i * shotSpacing), origin.getY());
            field.addProjectile(new PeaProjectile(damagePerShot, spawnPos));
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        if (!threePeater)
            return PlantAttackBehavior.super.hasTarget(self, field);
        else {
            int lane = GridPosition.fromContinuous(self.get(PositionComponent.class).position).lane();
            boolean flag = field.getZombiesInLane(lane).stream().anyMatch(z -> !z.isMarkedForRemoval())
                || field.getZombiesInLane(lane - 1).stream().anyMatch(z -> !z.isMarkedForRemoval())
                || field.getZombiesInLane(lane + 1).stream().anyMatch(z -> !z.isMarkedForRemoval());
            return flag;
        }
    }
}
