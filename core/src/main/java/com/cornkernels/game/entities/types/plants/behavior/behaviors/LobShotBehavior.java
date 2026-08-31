package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
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

        for (ZombieInstance z : field.getZombiesInLane(lane)) {
            if (!z.isMarkedForRemoval()) {
                Vec2d targetPos = z.get(PositionComponent.class).position;

                if (targetPos.getX() >= origin.getX()) {
                    laneTargets.add(z);
                }
            }
        }

        // Fetch Graves (as they likely aren't returned by getZombiesInLane)
        if (laneTargets.isEmpty()) {
            for (Entity e : field.getEntities()) {
                if (e instanceof Grave && !e.isMarkedForRemoval()) {
                    Vec2d targetPos = e.get(PositionComponent.class).position;

                    if (targetPos.getY() == origin.getY() && targetPos.getX() >= origin.getX()) {
                        laneTargets.add(e);
                    }
                }
            }
        }

        if (laneTargets.isEmpty()) {
            return;
        }

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

    // Add this to the bottom of LobShotBehavior.java
    public AbstractProjectile getProjectile() {
        return projectile;
    }
}
