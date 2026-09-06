package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MeleeAttackBehavior implements PlantAttackBehavior {
    private final float frontRange;
    private final float backRange;
    private final int pierceCount;
    private final int damage;
    private final boolean fiery;

    public MeleeAttackBehavior(float frontRange, float backRange, int pierceCount, int damage, boolean fiery) {
        this.frontRange = frontRange;
        this.backRange = backRange;
        this.pierceCount = pierceCount;
        this.damage = damage;
        this.fiery = fiery;
    }

    @Override
    public void execute(Entity self, Field field) {
        List<Entity> targets = findTargets(self, field);

        for (Entity target : targets) {
            CombatSystem.applyDamage(target, damage, false);

            if (fiery && target.has(IceComponent.class)) {
                target.get(IceComponent.class).melt();
            }
        }
    }

    @Override
    public boolean hasTarget(@NonNull Entity self, @NonNull Field field) {
        return !findTargets(self, field).isEmpty();
    }

    private List<Entity> findTargets(Entity self, Field field) {
        List<Entity> validTargets = new ArrayList<>();
        Vec2d origin = self.get(PositionComponent.class).position;
        double plantX = origin.getX() + 0.5;
        int lane = GridPosition.fromContinuous(origin).lane();

        for (Entity e : getAllValidTargets(field)) {
            if (GridPosition.fromContinuous(e.get(PositionComponent.class).position).lane() == lane) {
                double targetX = e.get(PositionComponent.class).position.getX() + 0.5;

                if ((targetX >= plantX && targetX - plantX <= frontRange) ||
                    (targetX < plantX && plantX - targetX <= backRange)) {
                    validTargets.add(e);
                }
            }
        }

        validTargets.sort((e1, e2) -> {
            double dist1 = Math.abs((e1.get(PositionComponent.class).position.getX() + 0.5) - plantX);
            double dist2 = Math.abs((e2.get(PositionComponent.class).position.getX() + 0.5) - plantX);
            return Double.compare(dist1, dist2);
        });

        if (validTargets.size() > pierceCount) {
            return validTargets.subList(0, pierceCount);
        }

        return validTargets;
    }
}
