package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.PuffShroomComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import org.jspecify.annotations.NonNull;

public class PuffShotBehavior extends DirectShotBehavior {

    public final float lifespan;
    private final float maxRangeTiles;
    private final float actionInterval;

    public PuffShotBehavior(int shotCount, AbstractProjectile projectile, float maxRangeTiles, float actionInterval, float lifespan) {
        super(shotCount, projectile);
        this.maxRangeTiles = maxRangeTiles;
        this.actionInterval = actionInterval;
        this.lifespan = lifespan;
    }

    public PuffShotBehavior(int shotCount, AbstractProjectile projectile, float maxRangeTiles, float actionInterval) {
        this(shotCount, projectile, maxRangeTiles, actionInterval, -1.0f);
    }

    @Override
    public boolean hasTarget(@NonNull Entity self, @NonNull Field field) {
        return true;
    }

    @Override
    public void execute(Entity self, Field field) {
        PuffShroomComponent comp = self.get(PuffShroomComponent.class);
        if (comp == null) {
            comp = new PuffShroomComponent(actionInterval);
            self.add(comp);
        }

        float dt = (1.0f / 20.0f);

        if (lifespan > 0.0f) {
            comp.lifeTimer += dt;
            if (comp.lifeTimer >= lifespan) {
                self.markForRemoval();
                return;
            }
        }

        comp.shootTimer += dt;
        if (comp.shootTimer >= actionInterval) {
            if (isEnemyInRange(self, field)) {
                super.execute(self, field);
                comp.shootTimer = 0.0f;
            } else {
                comp.shootTimer = actionInterval;
            }
        }
    }

    private boolean isEnemyInRange(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(origin).lane();

        for (Entity z : getAllValidTargets(field)) {
            double zX = z.get(PositionComponent.class).position.getX();
            if (zX >= origin.getX() && zX <= origin.getX() + maxRangeTiles) {
                return true;
            }
        }

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
}
