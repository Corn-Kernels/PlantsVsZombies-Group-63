package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.ButterComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.AllStarComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class AllStarBehavior implements ZombieBehavior {

    private final float tackleSpeedMultiplier;
    private final int recoveryTicks;

    public AllStarBehavior(float tackleSpeedMultiplier, float recoverySeconds) {
        this.tackleSpeedMultiplier = tackleSpeedMultiplier;
        this.recoveryTicks = (int) (recoverySeconds * 20); // 20 ticks = 1 second
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        AllStarComponent comp = zombie.get(AllStarComponent.class);

        // 1. Initialize component and immediately force the sprint state on spawn
        if (comp == null) {
            comp = new AllStarComponent();
            zombie.add(comp);
            state.changeState(ZombieStateComponent.State.ACTION);
        }

        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        // 2. Abort logic: If frozen or buttered before hitting a plant, permanently cancel the tackle
        if (!comp.hasTackled) {
            boolean isStunnedOrFrozen = zombie.has(ButterComponent.class);

            IceComponent ice = zombie.get(IceComponent.class);
            if (ice != null && ice.freezeLevel == 2) {
                isStunnedOrFrozen = true;
            }

            if (isStunnedOrFrozen) {
                comp.hasTackled = true;
                comp.isRecovering = false; // Skip recovery, just abort
                if (state.state == ZombieStateComponent.State.ACTION) {
                    state.changeState(ZombieStateComponent.State.WALKING);
                }
                return;
            }
        }

        // 3. Active Tackle Logic
        if (!comp.hasTackled) {
            if (state.state != ZombieStateComponent.State.ACTION) {
                state.changeState(ZombieStateComponent.State.ACTION);
            }

            // Force high-speed movement
            vel.velocityPerTick = new Vec2d(-def.baseSpeed * tackleSpeedMultiplier, 0f);

            // Check for collision
            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            PlantInstance plant = field.getPlantAt(pos.lane(), pos.column());

            if (plant != null && !plant.isMarkedForRemoval()) {
                plant.markForRemoval(); // Instantly destroy the plant
                comp.hasTackled = true;
                comp.isRecovering = true;
                comp.tickCounter = 0;
            }

            // 4. Recovery Pause Logic
        } else if (comp.isRecovering) {
            if (state.state != ZombieStateComponent.State.ACTION) {
                state.changeState(ZombieStateComponent.State.ACTION);
            }

            vel.velocityPerTick = new Vec2d(0, 0); // Stop moving while recovering

            comp.tickCounter++;
            if (comp.tickCounter >= recoveryTicks) {
                comp.isRecovering = false;
                state.changeState(ZombieStateComponent.State.WALKING);

                // Return to normal walk speed (ice debuffs will be naturally applied by your MovementSystem)
                vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
            }
        }

        // If hasTackled && !isRecovering, the behavior does nothing and allows standard Walking/Eating systems to take over.
    }
}
