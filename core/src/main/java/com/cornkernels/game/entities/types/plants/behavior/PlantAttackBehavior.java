package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import org.jetbrains.annotations.NotNull;

public interface PlantAttackBehavior {

    void execute(Entity self, Field field);

    default boolean hasTarget(@NotNull Entity self, @NotNull Field field) {
        int lane = GridPosition.fromContinuous(self.get(PositionComponent.class).position).lane();
        return field.getZombiesInLane(lane).stream().anyMatch(z -> !z.isMarkedForRemoval());
    }
}
