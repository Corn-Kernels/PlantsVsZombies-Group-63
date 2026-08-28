package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.BoneProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.ArrayList;
import java.util.List;

public class TombRaiserBehavior implements ZombieBehavior {

    private final int cooldownTicks;
    private final int windupTicks;
    private final int recoveryTicks;
    private int tickCounter = 0;

    private GridPosition currentTargetTile = null;

    public TombRaiserBehavior(float cooldownSeconds, float windupSeconds, float recoverySeconds) {
        this.cooldownTicks = (int) (cooldownSeconds * 20);
        this.windupTicks = (int) (windupSeconds * 20);
        this.recoveryTicks = (int) (recoverySeconds * 20);
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        if (state.state == ZombieStateComponent.State.ACTION) {
            // Continuously force velocity to 0 to prevent DebuffSystem overrides
            vel.velocityPerTick = new Vec2d(0, 0);

            if (state.stateTicks == windupTicks && currentTargetTile != null) {
                Vec2d spawnPos = zombie.get(PositionComponent.class).position;
                field.addZombieProjectile(new BoneProjectile(spawnPos, currentTargetTile));
            } else if (state.stateTicks > windupTicks + recoveryTicks) {
                state.changeState(ZombieStateComponent.State.WALKING);
                vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
                currentTargetTile = null;
            }
            return;
        }

        tickCounter++;

        if (tickCounter >= cooldownTicks && state.state == ZombieStateComponent.State.WALKING) {
            int rightMostCol = field.getTotalColumns() - 1;
            int secondRightCol = field.getTotalColumns() - 2;

            List<GridPosition> rightMostEmpty = getEmptyTilesInColumn(field, rightMostCol);
            List<GridPosition> secondRightEmpty = getEmptyTilesInColumn(field, secondRightCol);

            GridPosition chosenTarget = null;

            if (!rightMostEmpty.isEmpty() && !secondRightEmpty.isEmpty()) {
                if (Math.random() < 0.75) {
                    chosenTarget = rightMostEmpty.get((int) (Math.random() * rightMostEmpty.size()));
                } else {
                    chosenTarget = secondRightEmpty.get((int) (Math.random() * secondRightEmpty.size()));
                }
            } else if (!rightMostEmpty.isEmpty()) {
                chosenTarget = rightMostEmpty.get((int) (Math.random() * rightMostEmpty.size()));
            } else if (!secondRightEmpty.isEmpty()) {
                chosenTarget = secondRightEmpty.get((int) (Math.random() * secondRightEmpty.size()));
            }

            if (chosenTarget != null) {
                currentTargetTile = chosenTarget;
                state.changeState(ZombieStateComponent.State.ACTION);
                vel.velocityPerTick = new Vec2d(0, 0);
                tickCounter = 0;
            }
        }
    }

    private List<GridPosition> getEmptyTilesInColumn(Field field, int col) {
        List<GridPosition> emptyTiles = new ArrayList<>();
        for (int lane = 0; lane < field.getTotalLanes(); lane++) {
            if (isTileEmpty(field, lane, col)) {
                emptyTiles.add(new GridPosition(lane, col));
            }
        }
        return emptyTiles;
    }

    private boolean isTileEmpty(Field field, int lane, int col) {
        if (field.getPlantAt(lane, col) != null) return false;

        for (Entity e : field.getEntities()) {
            if (e instanceof Grave) {
                GridPosition gravePos = GridPosition.fromContinuous(e.get(PositionComponent.class).position);
                if (gravePos.lane() == lane && gravePos.column() == col) return false;
            }
        }
        return true;
    }
}
