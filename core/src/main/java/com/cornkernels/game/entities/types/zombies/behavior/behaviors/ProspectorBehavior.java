// ProspectorBehavior.java
package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.ProspectorComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;

public class ProspectorBehavior implements ZombieBehavior {

    private final int fuseTicks;
    private final int stunTicks;

    public ProspectorBehavior(float fuseSeconds, float stunSeconds) {
        this.fuseTicks = (int) (fuseSeconds * 20);
        this.stunTicks = (int) (stunSeconds * 20);
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        ProspectorComponent comp = zombie.get(ProspectorComponent.class);
        if (comp == null) {
            comp = new ProspectorComponent();
            zombie.add(comp);
        }

        IceComponent ice = zombie.get(IceComponent.class);
        if (ice != null && ice.freezeLevel != 0) {
            comp.dynamiteDefused = true;
        }

        switch (comp.phase) {
            case PRE_JUMP:
                if (comp.dynamiteDefused) return;

                comp.tickCounter++;
                if (comp.tickCounter >= fuseTicks && state.state == ZombieStateComponent.State.WALKING) {
                    comp.phase = ProspectorComponent.Phase.JUMPING;
                    state.changeState(ZombieStateComponent.State.ACTION);
                    comp.tickCounter = 0;
                }
                break;

            case JUMPING:
                if (state.state != ZombieStateComponent.State.ACTION) {
                    state.changeState(ZombieStateComponent.State.ACTION);
                }

                float jumpSpeed = def.baseSpeed * 12.0f;
                vel.velocityPerTick = new Vec2d(-jumpSpeed, 0f);

                double xPos = zombie.get(PositionComponent.class).position.getX();
                if (xPos <= 0.8) {
                    comp.phase = ProspectorComponent.Phase.LANDED_STUN;
                    comp.tickCounter = 0;
                }
                break;

            case LANDED_STUN:
                if (state.state != ZombieStateComponent.State.ACTION) {
                    state.changeState(ZombieStateComponent.State.ACTION);
                }

                vel.velocityPerTick = new Vec2d(0, 0);

                comp.tickCounter++;
                if (comp.tickCounter >= stunTicks) {
                    comp.phase = ProspectorComponent.Phase.WALKING_BACKWARDS;
                    state.changeState(ZombieStateComponent.State.WALKING);
                }
                break;

            case WALKING_BACKWARDS:
                if (state.state == ZombieStateComponent.State.WALKING) {
                    float currentSpeed = def.baseSpeed;
                    if (ice != null) {
                        if (ice.freezeLevel == 2) currentSpeed = 0f;
                        else if (ice.freezeLevel == 1) currentSpeed *= 0.5f;
                    }
                    vel.velocityPerTick = new Vec2d(currentSpeed, 0f);
                }
                break;
        }
    }
}
