package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.LineOfDamage;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class FumeShroomBehavior implements PlantAttackBehavior {
    private final float range;
    private final int damage;

    public FumeShroomBehavior(float range, int damage) {
        this.range = range;
        this.damage = damage;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;

        float centerX = (float) (origin.getX() + 0.5f + (range / 2.0f));
        Vec2d spawnPos = new Vec2d(centerX, origin.getY());

        field.addProjectile(new LineOfDamage(spawnPos, range, 1.0f, damage));
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        double plantX = origin.getX() + 0.5;
        int lane = GridPosition.fromContinuous(origin).lane();

        for (Entity e : field.getEntities()) {
            if ((e instanceof ZombieInstance || e instanceof Grave) && !e.isMarkedForRemoval()) {
                if (GridPosition.fromContinuous(e.get(PositionComponent.class).position).lane() == lane) {
                    double targetX = e.get(PositionComponent.class).position.getX() + 0.5;
                    if (targetX >= plantX && targetX - plantX <= range) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
