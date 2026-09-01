package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import org.jetbrains.annotations.NotNull;

public interface PlantAttackBehavior {

    void execute(Entity self, Field field);

    default boolean hasTarget(@NotNull Entity self, @NotNull Field field) {
        Vec2d selfPos = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(selfPos).lane();
        float selfX = selfPos.getX();

        return field.getZombiesInLane(lane).stream()
            .anyMatch(z -> !z.isMarkedForRemoval()
                && !z.has(HypnoComponent.class)
                && z.get(PositionComponent.class).position.getX() >= selfX);
    }
}
