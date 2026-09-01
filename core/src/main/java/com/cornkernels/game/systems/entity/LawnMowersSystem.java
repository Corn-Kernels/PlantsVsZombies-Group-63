package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.lawnmower.LawnMower;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.grid.GridPosition;
import org.jetbrains.annotations.NotNull;

public class LawnMowersSystem extends EntitySystem {

    private static final float MOWER_SPEED_COLUMNS_PER_SEC = 3f;
    private static final int MOWER_KILL_DAMAGE = 99999;

    private boolean markGameFinished = false;

    private static boolean isDead(@NotNull ZombieInstance zombie) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        return state != null && state.state == ZombieStateComponent.State.DEAD;
    }

    @Override
    public void update(float delta) {
        for (ZombieInstance zombie : field.getActiveZombies()) {
            if (zombie.isMarkedForRemoval() || isDead(zombie)) continue;

            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            if (pos.column() <= -1) {
                LawnMower lawnMower = field.getLawnMowerAt(pos.lane());
                if (lawnMower != null) {
                    if (!lawnMower.isTriggered()) {
                        lawnMower.trigger();
                    }
                } else {
                    markGameFinished = true;
                }
            }
        }

        advanceMowers(delta);
    }

    private void advanceMowers(float delta) {
        for (LawnMower mower : field.getActiveLawnMowers()) {
            if (!mower.isTriggered() || mower.isMarkedForRemoval()) continue;

            PositionComponent posComp = mower.get(PositionComponent.class);
            Vec2d pos = posComp.position;
            posComp.position = new Vec2d(pos.getX() + MOWER_SPEED_COLUMNS_PER_SEC * delta, pos.getY());

            int lane = Math.round(pos.getY());
            for (ZombieInstance zombie : field.getZombiesInLane(lane)) {
                if (zombie.isMarkedForRemoval() || isDead(zombie)) continue;
                double zombieX = zombie.get(PositionComponent.class).position.getX();
                if (zombieX <= posComp.position.getX()) {
                    CombatSystem.applyDamage(zombie, MOWER_KILL_DAMAGE, true, field);
                }
            }

            if (posComp.position.getX() > field.getTotalColumns()) {
                mower.markForRemoval();
            }
        }
    }

    public boolean isGameLost() {
        return markGameFinished;
    }
}
