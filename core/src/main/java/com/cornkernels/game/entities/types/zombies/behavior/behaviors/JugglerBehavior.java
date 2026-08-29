package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.JugglerComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.JugglerReflectedProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.PeaProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.GooPeaProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.TruePeaProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.List;

public class JugglerBehavior implements ZombieBehavior {

    private final double reflectRange;
    private final int spinDurationTicks;

    public JugglerBehavior(double reflectRange, float spinDurationSeconds) {
        this.reflectRange = reflectRange;
        this.spinDurationTicks = (int) (spinDurationSeconds * 20); // 20 ticks = 1 second
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent zVel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        JugglerComponent comp = zombie.get(JugglerComponent.class);
        if (comp == null) {
            comp = new JugglerComponent();
            zombie.add(comp);
        }

        // 1. Handle the Deflection Spin Animation State
        if (state.state == ZombieStateComponent.State.ACTION) {
            zVel.velocityPerTick = new Vec2d(zVel.velocityPerTick.getX()*1.5f, 0); // Stop moving while spinning

            comp.reflectTimerTicks++;
            if (comp.reflectTimerTicks >= spinDurationTicks) {
                state.changeState(ZombieStateComponent.State.WALKING);

                float walkSpeed = def.baseSpeed;
                IceComponent ice = zombie.get(IceComponent.class);
                if (ice != null && ice.freezeLevel == 1) walkSpeed *= 0.5f;
                zVel.velocityPerTick = new Vec2d(-walkSpeed, 0f);

                comp.reflectTimerTicks = 0;
            }
        }

        Vec2d zPos = zombie.get(PositionComponent.class).position;
        GridPosition zGrid = GridPosition.fromContinuous(zPos);

        // 2. Scan for incoming PeaProjectiles
        for (AbstractProjectile proj : List.copyOf(field.getActiveProjectiles())) {
            if (proj.isMarkedForRemoval()) continue;

            if (proj instanceof PeaProjectile || proj instanceof TruePeaProjectile || proj instanceof GooPeaProjectile) {
                Vec2d pPos = proj.get(PositionComponent.class).position;
                GridPosition pGrid = GridPosition.fromContinuous(pPos);

                if (pGrid.lane() == zGrid.lane()) {
                    double distance = zPos.getX() - pPos.getX();

                    // If the pea is in front of the juggler and within range
                    if (distance > 0 && distance <= reflectRange) {

                        // Extract stats from the plant projectile
                        DamageComponent dmg = proj.get(DamageComponent.class);
                        int damageAmount = (dmg != null) ? dmg.amount : 20; // Default to 20 just in case

                        PamAnimationComponent anim = proj.get(PamAnimationComponent.class);
                        VelocityComponent pVel = proj.get(VelocityComponent.class);

                        // Flip the velocity so it flies directly backwards
                        Vec2d reflectedVel = new Vec2d(-Math.abs(pVel.velocityPerTick.getX()), pVel.velocityPerTick.getY());

                        // Spawn the mirrored zombie projectile
                        JugglerReflectedProjectile reflected = new JugglerReflectedProjectile(damageAmount, pPos, reflectedVel, anim);
                        field.addZombieProjectile(reflected);

                        // Destroy the original plant projectile
                        proj.markForRemoval();

                        // Trigger the spinning animation
                        if (state.state != ZombieStateComponent.State.ACTION) {
                            state.changeState(ZombieStateComponent.State.ACTION);
                            comp.reflectTimerTicks = 0;
                        }
                    }
                }
            }
        }
    }
}
