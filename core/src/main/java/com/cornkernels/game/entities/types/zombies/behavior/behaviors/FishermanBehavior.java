package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.GridPositionComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.FishermanComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class FishermanBehavior implements ZombieBehavior {

    private final int pullCooldownTicks;

    public FishermanBehavior(float cooldownSeconds) {
        this.pullCooldownTicks = (int) (cooldownSeconds * 20); // 20 ticks = 1 second
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);

        // 1. Permanently lock into ACTION state to prevent movement and standard eating
        if (state.state != ZombieStateComponent.State.ACTION) {
            state.changeState(ZombieStateComponent.State.ACTION);
        }
        vel.velocityPerTick = new Vec2d(0, 0);

        FishermanComponent comp = zombie.get(FishermanComponent.class);
        if (comp == null) {
            comp = new FishermanComponent();
            zombie.add(comp);
        }

        // 2. Actively Pulling a Plant
        if (comp.pullingPlant != null) {
            // Drop the line if the plant was manually shoveled or eaten
            if (comp.pullingPlant.isMarkedForRemoval()) {
                comp.pullingPlant = null;
                return;
            }

            PositionComponent plantPos = comp.pullingPlant.get(PositionComponent.class);
            double currentX = plantPos.position.getX();

            // Instant kill if dragged past column 12
            if (currentX > 12.0) {
                comp.pullingPlant.markForRemoval();
                comp.pullingPlant = null;
                return;
            }

            // Interpolate position based on fast or slow pull
            double speed = comp.fastPull ? 0.35 : 0.05;
            currentX += speed;

            if (currentX >= comp.targetX) {
                currentX = comp.targetX;
                plantPos.position = new Vec2d((float) currentX, plantPos.position.getY());

                // Update GridPositionComponent ONLY after arriving[cite: 23]
                GridPositionComponent gridComp = comp.pullingPlant.get(GridPositionComponent.class);
                int lane = gridComp.position.lane();
                int oldCol = comp.initialGridCol;
                int newCol = (int) comp.targetX;

                // Safely unlink from the old grid tile
                if (oldCol >= 0 && oldCol < field.getTotalColumns()) {
                    if (field.getGridObjectAt(lane, oldCol).getPlant() == comp.pullingPlant) {
                        field.getGridObjectAt(lane, oldCol).providePlant(null);
                    }
                }

                gridComp.position = new GridPosition(lane, newCol);

                // Safely link to the new grid tile (if still in bounds)
                if (newCol >= 0 && newCol < field.getTotalColumns()) {
                    field.getGridObjectAt(lane, newCol).providePlant(comp.pullingPlant);
                }

                comp.pullingPlant = null; // Pull sequence complete
            } else {
                plantPos.position = new Vec2d((float) currentX, plantPos.position.getY());
            }

            // 3. Waiting for Cooldown to Hook a Plant
        } else {
            comp.tickCounter++;
            if (comp.tickCounter >= pullCooldownTicks) {
                GridPosition zGrid = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);

                PlantInstance rightMost = null;
                double maxX = -1.0;

                // Scan for the right-most plant in the exact same lane
                for (PlantInstance plant : field.getActivePlants()) {
                    GridPosition pGrid = GridPosition.fromContinuous(plant.get(PositionComponent.class).position);
                    if (pGrid.lane() == zGrid.lane()) {
                        double x = plant.get(PositionComponent.class).position.getX();
                        if (x > maxX) {
                            maxX = x;
                            rightMost = plant;
                        }
                    }
                }

                if (rightMost != null) {
                    comp.pullingPlant = rightMost;
                    comp.initialGridCol = rightMost.get(GridPositionComponent.class).position.column();

                    int intendedCol = comp.initialGridCol + 1;

                    // Trigger massive fast-pull if they reach column 9
                    if (intendedCol >= 9) {
                        comp.targetX = 15;
                        comp.fastPull = true;
                    } else {
                        comp.targetX = intendedCol;
                        comp.fastPull = false;
                    }

                    comp.tickCounter = 0;
                }
            }
        }
    }
}
