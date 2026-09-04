package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.ArrayList;
import java.util.List;

public class LobShotBehavior implements PlantAttackBehavior {

    private final int shotCount;
    private final double shotSpacing = 0.3f;
    private final AbstractProjectile projectile;

    public LobShotBehavior(int shotCount, AbstractProjectile projectile) {
        this.shotCount = shotCount;
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(origin).lane();
        List<Entity> laneTargets = new ArrayList<>();

        // Use the universal targeting method
        for (Entity target : getAllValidTargets(field)) {
            if (!target.has(PositionComponent.class)) continue;

            Vec2d targetPos = target.get(PositionComponent.class).position;
            int targetLane = GridPosition.fromContinuous(targetPos).lane();

            // Must be in the same lane and in front of the plant
            if (targetLane == lane && targetPos.getX() >= origin.getX()) {
                laneTargets.add(target);
            }
        }

        if (laneTargets.isEmpty()) {
            return;
        }

        // Find closest valid target in the lane
        Entity closestTarget = laneTargets.get(0);
        double minX = closestTarget.get(PositionComponent.class).position.getX();

        for (Entity target : laneTargets) {
            double currentX = target.get(PositionComponent.class).position.getX();
            if (currentX < minX) {
                minX = currentX;
                closestTarget = target;
            }
        }

        for (int i = 0; i < shotCount; i++) {
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5 + i * shotSpacing), origin.getY());
            AbstractProjectile spawned = projectile.clone(spawnPos);
            if (spawned instanceof LobProjectile) {
                ((LobProjectile) spawned).target = closestTarget;
            }
            if (spawned instanceof HomingProjectile) {
                ((HomingProjectile) spawned).target = closestTarget;
            }
            field.addProjectile(spawned);
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(origin).lane();

        for (Entity target : getAllValidTargets(field)) {
            if (!target.has(PositionComponent.class)) continue;

            Vec2d targetPos = target.get(PositionComponent.class).position;
            int targetLane = GridPosition.fromContinuous(targetPos).lane();

            if (targetLane == lane && targetPos.getX() >= origin.getX()) {
                return true;
            }
        }
        return false;
    }

    public AbstractProjectile getProjectile() {
        return projectile;
    }
}
