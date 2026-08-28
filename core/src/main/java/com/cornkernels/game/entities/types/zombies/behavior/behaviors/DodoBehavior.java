package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;

public class DodoBehavior implements ZombieBehavior {

    private final int walkDurationTicks;
    private final int flyDurationTicks;
    private final float flightSpeedMultiplier;
    private int tickCounter = 0;

    public DodoBehavior(float walkDurationSeconds, float flyDurationSeconds, float flightSpeedMultiplier) {
        this.walkDurationTicks = (int) (walkDurationSeconds * 20);
        this.flyDurationTicks = (int) (flyDurationSeconds * 20);
        this.flightSpeedMultiplier = flightSpeedMultiplier;
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        tickCounter++;

        if (state.state != ZombieStateComponent.State.ACTION) {
            // Currently walking (or eating). Check if it is time to take off.
            if (tickCounter >= walkDurationTicks) {
                state.changeState(ZombieStateComponent.State.ACTION);
                tickCounter = 0;
            }
        } else {
            // Currently flying. Continuously enforce the modified flight speed.
            float currentSpeed = def.baseSpeed * flightSpeedMultiplier;

            vel.velocityPerTick = new Vec2d(-currentSpeed, 0f);

            // Check if it is time to land.
            if (tickCounter >= flyDurationTicks) {
                state.changeState(ZombieStateComponent.State.WALKING);

                float walkSpeed = def.baseSpeed;
                vel.velocityPerTick = new Vec2d(-walkSpeed, 0f);

                tickCounter = 0;
            }
        }
    }
}
