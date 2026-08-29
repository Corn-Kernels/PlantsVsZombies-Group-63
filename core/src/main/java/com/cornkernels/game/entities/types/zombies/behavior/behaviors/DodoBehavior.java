// DodoBehavior.java
package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.DodoComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;

public class DodoBehavior implements ZombieBehavior {

    private final int walkDurationTicks;
    private final int flyDurationTicks;
    private final float flightSpeedMultiplier;

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

        DodoComponent comp = zombie.get(DodoComponent.class);
        if (comp == null) {
            comp = new DodoComponent();
            zombie.add(comp);
        }

        comp.tickCounter++;

        if (state.state != ZombieStateComponent.State.ACTION) {
            if (comp.tickCounter >= walkDurationTicks) {
                state.changeState(ZombieStateComponent.State.ACTION);
                comp.tickCounter = 0;
            }
        } else {
            float currentSpeed = def.baseSpeed * flightSpeedMultiplier;

            IceComponent ice = zombie.get(IceComponent.class);
            if (ice != null) {
                if (ice.freezeLevel == 2) currentSpeed = 0f;
                else if (ice.freezeLevel == 1) currentSpeed *= 0.5f;
            }

            vel.velocityPerTick = new Vec2d(-currentSpeed, 0f);

            if (comp.tickCounter >= flyDurationTicks) {
                state.changeState(ZombieStateComponent.State.WALKING);

                float walkSpeed = def.baseSpeed;
                if (ice != null && ice.freezeLevel == 1) walkSpeed *= 0.5f;
                vel.velocityPerTick = new Vec2d(-walkSpeed, 0f);

                comp.tickCounter = 0;
            }
        }
    }
}
