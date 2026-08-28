package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.entity.CombatSystem;

public class CustomMeleeBehavior implements ZombieBehavior {

    private final int windupTicks;
    private final int recoveryTicks;
    private final int damage;

    public CustomMeleeBehavior(int windupTicks, int recoveryTicks, int damage) {
        this.windupTicks = windupTicks;
        this.recoveryTicks = recoveryTicks;
        this.damage = damage;
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
        PlantInstance plant = field.getPlantAt(pos.lane(), pos.column());
        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        if (plant != null) {
            if (state.state != ZombieStateComponent.State.ACTION) {
                state.changeState(ZombieStateComponent.State.ACTION);
                state.targetPlant = plant;
            }

            if (state.state == ZombieStateComponent.State.ACTION && state.targetPlant == plant) {
                // Continuously force velocity to 0 to prevent DebuffSystem overrides
                vel.velocityPerTick = new Vec2d(0, 0);

                if (state.stateTicks == windupTicks) {
                    CombatSystem.applyDamage(plant, damage, true, field);
                } else if (state.stateTicks > windupTicks + recoveryTicks) {
                    state.changeState(ZombieStateComponent.State.WALKING);
                    vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
                }
            }
        } else {
            if (state.state == ZombieStateComponent.State.ACTION) {
                state.changeState(ZombieStateComponent.State.WALKING);
                vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
            }
        }
    }
}
