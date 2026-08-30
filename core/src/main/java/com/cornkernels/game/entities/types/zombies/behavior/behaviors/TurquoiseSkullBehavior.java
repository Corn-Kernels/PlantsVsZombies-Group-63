package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.TurquoiseSkullComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class TurquoiseSkullBehavior implements ZombieBehavior {

    private final int cooldownTicks;
    private final int windupTicks;
    private final int recoveryTicks;

    public TurquoiseSkullBehavior(float cooldownSeconds, float windupSeconds, float recoverySeconds) {
        this.cooldownTicks = (int) (cooldownSeconds * 20); // 20 ticks = 1 second
        this.windupTicks = (int) (windupSeconds * 20);
        this.recoveryTicks = (int) (recoverySeconds * 20);
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        TurquoiseSkullComponent comp = zombie.get(TurquoiseSkullComponent.class);
        if (comp == null) {
            comp = new TurquoiseSkullComponent();
            zombie.add(comp);
        }

        if (state.state == ZombieStateComponent.State.ACTION) {
            vel.velocityPerTick = new Vec2d(0, 0); // Lock velocity to completely stop movement

            // Execute the beam right as the windup finishes
            if (state.stateTicks == windupTicks) {
                Vec2d zPos = zombie.get(PositionComponent.class).position;
                GridPosition zGrid = GridPosition.fromContinuous(zPos);

                for (PlantInstance plant : field.getActivePlants()) {
                    if (plant.isMarkedForRemoval()) continue;

                    GridPosition pGrid = GridPosition.fromContinuous(plant.get(PositionComponent.class).position);

                    if (pGrid.lane() == zGrid.lane()) {
                        double distance = zPos.getX() - plant.get(PositionComponent.class).position.getX();

                        // Kill any plant within 3 tiles in front of the zombie
                        if (distance > 0 && distance <= 3.0) {
                            plant.markForRemoval();
                        }
                    }
                }
            } else if (state.stateTicks > windupTicks + recoveryTicks) {
                state.changeState(ZombieStateComponent.State.WALKING);
                vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
            }
            return;
        }

        comp.tickCounter++;

        // Only check sight if the cooldown is ready and we are actively walking
        if (comp.tickCounter >= cooldownTicks && state.state == ZombieStateComponent.State.WALKING) {
            if (hasTargetInSight(zombie, field)) {
                state.changeState(ZombieStateComponent.State.ACTION);
                vel.velocityPerTick = new Vec2d(0, 0);
                comp.tickCounter = 0;
            }
        }
    }

    private boolean hasTargetInSight(ZombieInstance zombie, Field field) {
        Vec2d zPos = zombie.get(PositionComponent.class).position;
        GridPosition zGrid = GridPosition.fromContinuous(zPos);

        for (PlantInstance plant : field.getActivePlants()) {
            if (plant.isMarkedForRemoval()) continue;

            GridPosition pGrid = GridPosition.fromContinuous(plant.get(PositionComponent.class).position);

            if (pGrid.lane() == zGrid.lane()) {
                double distance = zPos.getX() - plant.get(PositionComponent.class).position.getX();

                // Sight range is only 2 tiles (shorter than the attack range)
                if (distance > 0 && distance <= 2.0) {
                    return true;
                }
            }
        }
        return false;
    }
}
