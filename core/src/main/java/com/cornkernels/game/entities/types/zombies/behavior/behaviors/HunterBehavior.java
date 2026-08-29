// HunterBehavior.java
package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.HunterComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.SnowballProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class HunterBehavior implements ZombieBehavior {

    private final int cooldownTicks;
    private final int windupTicks = 20;
    private final int throwIntervalTicks = 10;
    private final int recoveryTicks = 10;
    private final int totalThrows = 4;

    public HunterBehavior(float cooldownSeconds) {
        this.cooldownTicks = (int) (cooldownSeconds * 20);
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        HunterComponent comp = zombie.get(HunterComponent.class);
        if (comp == null) {
            comp = new HunterComponent();
            zombie.add(comp);
        }

        IceComponent ice = zombie.get(IceComponent.class);
        if (ice != null && ice.freezeLevel == 2) {
            ice.freezeLevel = 1;
            ice.freezeTicksRemaining = 0;
        }

        if (state.state == ZombieStateComponent.State.ACTION) {
            vel.velocityPerTick = new Vec2d(0, 0);

            int actionDuration = windupTicks + ((totalThrows - 1) * throwIntervalTicks) + recoveryTicks;

            boolean isThrowTick = false;
            for (int i = 0; i < totalThrows; i++) {
                if (state.stateTicks == windupTicks + (i * throwIntervalTicks)) {
                    isThrowTick = true;
                    break;
                }
            }

            if (isThrowTick) {
                Vec2d spawnPos = zombie.get(PositionComponent.class).position;
                field.addZombieProjectile(new SnowballProjectile(spawnPos));
            } else if (state.stateTicks > actionDuration) {
                state.changeState(ZombieStateComponent.State.WALKING);
                vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
            }
            return;
        }

        comp.tickCounter++;

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
        GridPosition gridPos = GridPosition.fromContinuous(zPos);

        for (PlantInstance plant : field.getActivePlants()) {
            GridPosition pPos = GridPosition.fromContinuous(plant.get(PositionComponent.class).position);
            if (pPos.lane() != gridPos.lane()) continue;

            double distance = zPos.getX() - plant.get(PositionComponent.class).position.getX();

            if (distance > 0 && distance <= 4.0) {
                PlantFreezeComponent freeze = plant.get(PlantFreezeComponent.class);
                if (freeze == null || freeze.frozenHp <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
