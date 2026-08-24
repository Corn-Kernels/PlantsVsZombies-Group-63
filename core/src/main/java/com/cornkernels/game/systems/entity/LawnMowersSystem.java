package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.lawnmower.LawnMower;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.grid.GridPosition;

public class LawnMowersSystem extends EntitySystem {

    private boolean markGameFinished = false;

    @Override
    public void update(float delta) {
        for (ZombieInstance zombie : field.getActiveZombies()) {
            if (zombie.isMarkedForRemoval()) continue;

            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            if (pos.column() <= 0) {
                LawnMower lawnMower = field.getLawnMowerAt(pos.lane());
                if (lawnMower != null) {
                    lawnMower.markForRemoval();
                    field.removeZombiesInLane(pos.lane());
                } else {
                    markGameFinished = true;
                }
            }
        }
    }

    public boolean isGameLost() {
        return markGameFinished;
    }
}
