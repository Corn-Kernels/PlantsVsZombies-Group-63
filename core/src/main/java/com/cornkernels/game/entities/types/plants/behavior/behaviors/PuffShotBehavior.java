package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class PuffShotBehavior extends DirectShotBehavior {

    private final float maxRangeTiles;
    private final float actionInterval;
    private final float lifespan;

    private float shootTimer = 0.0f;
    private float lifeTimer = 0.0f;

    public PuffShotBehavior(int shotCount, AbstractProjectile projectile, float maxRangeTiles, float actionInterval, float lifespan) {
        super(shotCount, projectile);
        this.maxRangeTiles = maxRangeTiles;
        this.actionInterval = actionInterval;
        this.lifespan = lifespan;
        this.shootTimer = actionInterval;
    }

    public PuffShotBehavior(int shotCount, AbstractProjectile projectile, float maxRangeTiles, float actionInterval) {
        this(shotCount, projectile, maxRangeTiles, actionInterval, -1.0f);
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        // Always return true so the game engine constantly calls execute().
        // This ensures the lifespan timer ticks down even when no zombies are around.
        return true;
    }

    @Override
    public void execute(Entity self, Field field) {
        float dt = (1.0f / 20.0f);

        // 1. Handle Lifespan Clock (Independent of enemies)
        if (lifespan > 0.0f) {
            lifeTimer += dt;
            if (lifeTimer >= lifespan) {
                self.markForRemoval();
                return;
            }
        }

        // 2. Handle Shooting Clock
        shootTimer += dt;
        if (shootTimer >= actionInterval) {
            if (isEnemyInRange(self, field)) {
                // Trigger the base DirectShotBehavior to actually fire
                super.execute(self, field);
                shootTimer = 0.0f; // Reset shoot clock after firing
            } else {
                // Keep timer capped so it fires instantly when a zombie enters range
                shootTimer = actionInterval;
            }
        }
    }

    /**
     * Internal helper to validate if a target is actually within the plant's firing range.
     */
    private boolean isEnemyInRange(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(origin).lane();

        // Check for zombies within range in the current lane
        for (ZombieInstance z : field.getZombiesInLane(lane)) {
            if (!z.isMarkedForRemoval()) {
                double zX = z.get(PositionComponent.class).position.getX();
                if (zX >= origin.getX() && zX <= origin.getX() + maxRangeTiles) {
                    return true;
                }
            }
        }

        // Check for graves within range in the current lane
        for (Entity e : field.getEntities()) {
            if (e instanceof Grave && !e.isMarkedForRemoval()) {
                Vec2d targetPos = e.get(PositionComponent.class).position;
                if (targetPos.getY() == origin.getY() && targetPos.getX() >= origin.getX() && targetPos.getX() <= origin.getX() + maxRangeTiles) {
                    return true;
                }
            }
        }

        return false;
    }

    public void resetLifespan() {
        this.lifeTimer = 0.0f;
    }

    public float getLifeTimer() {
        return lifeTimer;
    }

    public float getLifespan() {
        return lifespan;
    }
}
