package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.GridPositionComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.BullyComponent;
import com.cornkernels.game.entities.types.obstacles.PushableObstacle;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.ArrayList;
import java.util.List;

public class BullyBehavior implements ZombieBehavior {

    private final int pushDurationTicks;
    private final int cooldownTicks;

    public BullyBehavior(float pushDurationSeconds, float cooldownSeconds) {
        this.pushDurationTicks = (int) (pushDurationSeconds * 20); // 20 ticks = 1s
        this.cooldownTicks = (int) (cooldownSeconds * 20);
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);

        BullyComponent comp = zombie.get(BullyComponent.class);
        if (comp == null) {
            comp = new BullyComponent();
            zombie.add(comp);
        }

        if (comp.cooldownTimer > 0 && !comp.isPushing) {
            comp.cooldownTimer--;
        }

        // 1. Actively Pushing a Stack
        if (comp.isPushing) {
            if (state.state != ZombieStateComponent.State.ACTION) {
                state.changeState(ZombieStateComponent.State.ACTION);
            }
            vel.velocityPerTick = new Vec2d(0, 0); // Lock movement while pushing

            comp.ticksPushing++;
            double pushSpeed = 1.0 / pushDurationTicks;
            int lane = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position).lane();

            // Slide each obstacle in the stack
            for (int i = 0; i < comp.pushStack.size(); i++) {
                PushableObstacle obs = comp.pushStack.get(i);
                if (obs.isMarkedForRemoval()) continue;

                PositionComponent pComp = obs.get(PositionComponent.class);
                double currentX = pComp.position.getX() - pushSpeed;
                pComp.position = new Vec2d((float) currentX, pComp.position.getY());

                // Crush plants if the block overlaps their tile!
                int activeCol = (int) Math.round(currentX);
                PlantInstance plant = field.getPlantAt(lane, activeCol);
                if (plant != null && !plant.isMarkedForRemoval()) {
                    plant.markForRemoval();
                }
            }

            // Finish the push
            if (comp.ticksPushing >= pushDurationTicks) {
                for (int i = 0; i < comp.pushStack.size(); i++) {
                    PushableObstacle obs = comp.pushStack.get(i);
                    if (obs.isMarkedForRemoval()) continue;

                    // Snap the obstacle exactly onto the new grid column
                    int newCol = comp.initialCols.get(i) - 1;
                    PositionComponent pComp = obs.get(PositionComponent.class);
                    pComp.position = new Vec2d(newCol, pComp.position.getY());

                    if (obs.has(GridPositionComponent.class)) {
                        obs.get(GridPositionComponent.class).position = new GridPosition(lane, newCol);
                    } else {
                        obs.add(new GridPositionComponent(new GridPosition(lane, newCol)));
                    }
                }

                comp.isPushing = false;
                comp.pushStack.clear();
                comp.initialCols.clear();
                comp.cooldownTimer = cooldownTicks;
                state.changeState(ZombieStateComponent.State.WALKING);
            }
            return;
        }

        // 2. Scan for Obstacles to Push
        if (state.state == ZombieStateComponent.State.WALKING && comp.cooldownTimer == 0) {
            GridPosition zGrid = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            double zX = zombie.get(PositionComponent.class).position.getX();
            int lane = zGrid.lane();

            PushableObstacle targetObs = null;

            // Check immediately in front of the zombie
            for (Entity e : field.getEntities()) {
                if (e instanceof PushableObstacle && !e.isMarkedForRemoval()) {
                    GridPosition pGrid = GridPosition.fromContinuous(e.get(PositionComponent.class).position);
                    if (pGrid.lane() == lane) {
                        double dist = zX - e.get(PositionComponent.class).position.getX();
                        // If it's right in front of the zombie (up to 1.2 tiles ahead)
                        if (dist > 0 && dist <= 1.2) {
                            targetObs = (PushableObstacle) e;
                            break;
                        }
                    }
                }
            }

            // 3. Construct the Stack
            if (targetObs != null) {
                int startCol = GridPosition.fromContinuous(targetObs.get(PositionComponent.class).position).column();

                List<PushableObstacle> stack = new ArrayList<>();
                List<Integer> initialCols = new ArrayList<>();
                int checkCol = startCol;
                boolean hitEdge = false;

                // Loop through columns to the left until the stack breaks or hits column 0
                while (checkCol >= 0) {
                    PushableObstacle obs = getPushableAt(field, lane, checkCol);
                    if (obs != null) {
                        if (checkCol == 0) {
                            hitEdge = true; // Blocked by the edge of the screen
                        }
                        stack.add(obs);
                        initialCols.add(checkCol);
                        checkCol--;
                    } else {
                        break;
                    }
                }

                // If the block isn't jammed against the back wall, initiate the push!
                if (!hitEdge && !stack.isEmpty()) {
                    comp.isPushing = true;
                    comp.pushStack = stack;
                    comp.initialCols = initialCols;
                    comp.ticksPushing = 0;

                    state.changeState(ZombieStateComponent.State.ACTION);
                    vel.velocityPerTick = new Vec2d(0, 0);
                }
            }
        }
    }

    private PushableObstacle getPushableAt(Field field, int lane, int col) {
        for (Entity e : field.getEntities()) {
            if (e instanceof PushableObstacle && !e.isMarkedForRemoval()) {
                GridPosition p = GridPosition.fromContinuous(e.get(PositionComponent.class).position);
                if (p.lane() == lane && p.column() == col) {
                    return (PushableObstacle) e;
                }
            }
        }
        return null;
    }
}
