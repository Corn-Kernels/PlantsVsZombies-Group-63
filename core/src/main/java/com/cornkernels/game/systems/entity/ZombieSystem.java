package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieBehaviorComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.SunInfectedComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

public class ZombieSystem extends EntitySystem {

    private static final long BITE_INTERVAL_TICKS = 30;
    private final PamPlayer pamPlayer;

    public ZombieSystem(PamPlayer pamPlayer) {
        this.pamPlayer = pamPlayer;
    }

    @Override
    public void update(float deltaTick) {
        for (ZombieInstance zombie : field.getActiveZombies()) {
            if (zombie.isMarkedForRemoval()) continue;

            ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
            if (state.state == ZombieStateComponent.State.DEAD) continue;

            state.stateTicks++;

            ZombieBehaviorComponent behaviorComp = zombie.get(ZombieBehaviorComponent.class);
            if (behaviorComp != null) {
                behaviorComp.behavior.update(zombie, field);
            }

            if (state.state == ZombieStateComponent.State.ACTION) {
                continue;
            }

            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            PlantInstance plant = field.getPlantAt(pos.lane(), pos.column());

            if (plant == null) {
                if (state.state == ZombieStateComponent.State.EATING) {
                    state.changeState(ZombieStateComponent.State.WALKING);
                    state.targetPlant = null;
                    applyClip(zombie, "walk");
                }
                continue;
            }

            if (state.state != ZombieStateComponent.State.EATING || state.targetPlant != plant) {
                state.changeState(ZombieStateComponent.State.EATING);
                state.targetPlant = plant;
                state.ticksUntilNextBite = BITE_INTERVAL_TICKS;
                applyClip(zombie, "eat");
            }

            state.ticksUntilNextBite--;
            if (state.ticksUntilNextBite <= 0) {
                ZombieDef def = zombie.get(ZombieDefComponent.class).def();

                PlantDefComponent plantDefComp = plant.get(PlantDefComponent.class);
                if (plantDefComp != null && plantDefComp.def().getId()%1000==51) {
                    zombie.add(new SunInfectedComponent());
                }

                CombatSystem.applyDamage(plant, def.eatDps, true);
                state.ticksUntilNextBite = BITE_INTERVAL_TICKS;
            }
        }
    }

    private void applyClip(@NonNull ZombieInstance zombie, String clipName) {
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), def, clipName);
    }
}
