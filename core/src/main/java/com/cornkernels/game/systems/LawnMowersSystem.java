package com.cornkernels.game.systems;

import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.lawnmower.LawnMower;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import org.jetbrains.annotations.NotNull;

public class LawnMowersSystem {

    private boolean markGameFinished = false;

    public void update(@NotNull Field field) {
        for (ZombieInstance zombie : field.getActiveZombies()) {
            if (zombie.isMarkedForRemoval()) continue;

            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            if (pos.column() <= 0) {
                LawnMower lawnMower = field.getLawnMowerAt(pos.lane());
                if (lawnMower != null) {
                    lawnMower.markForRemoval();
                    field.removeZombiesInLane(pos.lane());
                    System.out.println("The lawn mower in row " +
                        pos.lane() + " is triggered and killed these zombies:");
                } else {
                    markGameFinished = true;
                }
            }
        }
    }

    public boolean isGameFinished() {
        return markGameFinished;
    }
}
