package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import org.jetbrains.annotations.NotNull;

public interface PlantAttackBehavior {

    void execute(Entity self, Field field);

    default boolean hasTarget(@NotNull Entity self, @NotNull Field field) {
        Vec2d selfPos = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(selfPos).lane();
        float selfX = selfPos.getX();

        // A zombie only counts as a target while it's still ahead of the plant (X >= the
        // plant's own X, matching the +X-is-toward-the-zombies convention) — once a zombie's X
        // drops below the plant's, it's already passed through this column, and this plant's
        // forward-facing shot (fired in +X) would fly further away from it, not toward it.
        return field.getZombiesInLane(lane).stream()
            .anyMatch(z -> !z.isMarkedForRemoval()
                && z.get(PositionComponent.class).position.getX() >= selfX);
    }
}
