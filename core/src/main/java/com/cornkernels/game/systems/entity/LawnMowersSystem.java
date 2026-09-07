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
            if (pos.column() <= -1) triggerLawnMowerOrLose(pos.lane());
        }

        advanceMowers(delta);
    }

    private void triggerLawnMowerOrLose(int lane) {
        LawnMower lawnMower = field.getLawnMowerAt(lane);
        if (lawnMower != null) {
            if (!lawnMower.isTriggered()) lawnMower.trigger();
        } else {
            markGameFinished = true;
        }
    }

    private void advanceMowers(float delta) {
        for (LawnMower mower : field.getActiveLawnMowers()) {
            if (!mower.isTriggered() || mower.isMarkedForRemoval()) continue;

            PositionComponent posComp = mower.get(PositionComponent.class);
            posComp.position = new Vec2d(posComp.position.getX() + MOWER_SPEED_COLUMNS_PER_SEC * delta, posComp.position.getY());

            applyMowerDamage(Math.round(posComp.position.getY()), posComp.position.getX());

            if (posComp.position.getX() > field.getTotalColumns()) {
                mower.markForRemoval();
            }
        }
    }

    private void applyMowerDamage(int lane, double mowerX) {
        for (ZombieInstance zombie : field.getZombiesInLane(lane)) {
            if (zombie.isMarkedForRemoval() || isDead(zombie)) continue;
            if (zombie.get(PositionComponent.class).position.getX() <= mowerX) {
                CombatSystem.applyDamage(zombie, MOWER_KILL_DAMAGE, true, field);
            }
        }
    }

    public boolean isGameLost() {
        return markGameFinished;
    }
}
