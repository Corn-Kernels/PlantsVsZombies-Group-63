package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantDefComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.plant_specific.SheepedComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieBehaviorComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDeathComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.SunInfectedComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.EnragedComponent;
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
            if (state.state == ZombieStateComponent.State.DEAD) {
                updateDying(zombie, deltaTick);
                continue;
            }

            state.stateTicks++;

            ZombieBehaviorComponent behaviorComp = zombie.get(ZombieBehaviorComponent.class);
            if (behaviorComp != null) {
                behaviorComp.behavior.update(zombie, field);
            }

            if (state.state == ZombieStateComponent.State.ACTION) {
                continue;
            }

            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            Entity target = null;
            boolean isHypnotized = zombie.has(HypnoComponent.class);

            if (isHypnotized) {
                // 1. Hypnotized zombies target normal zombies in the same column
                for (ZombieInstance z : field.getZombiesInLane(pos.lane())) {
                    if (z != zombie && !z.isMarkedForRemoval() && !z.has(HypnoComponent.class)) {
                        GridPosition zPos = GridPosition.fromContinuous(z.get(PositionComponent.class).position);
                        if (zPos.column() == pos.column()) {
                            target = z;
                            break;
                        }
                    }
                }
            } else {
                // 2. Normal zombies target hypnotized zombies first
                for (ZombieInstance z : field.getZombiesInLane(pos.lane())) {
                    if (!z.isMarkedForRemoval() && z.has(HypnoComponent.class)) {
                        GridPosition zPos = GridPosition.fromContinuous(z.get(PositionComponent.class).position);
                        if (zPos.column() == pos.column()) {
                            target = z;
                            break;
                        }
                    }
                }

                // 3. If no hypnotized zombie is found, target plants normally
                if (target == null) {
                    PlantInstance plant = field.getPlantAt(pos.lane(), pos.column());
                    if (plant != null) {
                        // Ignore plants that have an octopus or sheep on them
                        if (!plant.has(OctoedComponent.class) && !plant.has(SheepedComponent.class)) {
                            // Ignore fully frozen plants
                            PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
                            if (freezeComp == null || freezeComp.frozenHp <= 0) {
                                target = plant;
                            }
                        }
                    }
                }
            }

            // State switching if no target is found
            if (target == null) {
                if (state.state == ZombieStateComponent.State.EATING) {
                    state.changeState(ZombieStateComponent.State.WALKING);
                    state.targetEntity = null;
                    applyClip(zombie, "walk");
                }
                continue;
            }

            // Lock onto new target and start eating
            if (state.state != ZombieStateComponent.State.EATING || state.targetEntity != target) {
                state.changeState(ZombieStateComponent.State.EATING);
                state.targetEntity = target;
                state.ticksUntilNextBite = BITE_INTERVAL_TICKS;
                applyClip(zombie, "eat");
            }

            state.ticksUntilNextBite--;
            if (state.ticksUntilNextBite <= 0) {
                ZombieDef def = zombie.get(ZombieDefComponent.class).def();
                int currentDamage = def.eatDps;

                // Check if the zombie is enraged to apply the damage buff
                EnragedComponent enraged = zombie.get(EnragedComponent.class);
                if (enraged != null) {
                    currentDamage = (int) (currentDamage * enraged.damageMultiplier);
                }

                // Apply damage dynamically to whatever the target is (Plant or Zombie)
                CombatSystem.applyDamage(state.targetEntity, currentDamage, false);

                // Sun Bean infection logic (only applies if the target is a PlantInstance)
                if (state.targetEntity instanceof PlantInstance plantTarget) {
                    PlantDefComponent plantDefComp = plantTarget.get(PlantDefComponent.class);
                    if (plantDefComp != null && plantDefComp.def().getId() % 1000 == 51) {
                        zombie.add(new SunInfectedComponent());
                    }
                }

                state.ticksUntilNextBite = BITE_INTERVAL_TICKS;
            }
        }
    }

    private void applyClip(@NonNull ZombieInstance zombie, String clipName) {
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), def, clipName);
    }

    private void updateDying(@NonNull ZombieInstance zombie, float deltaTick) {
        ZombieDeathComponent death = zombie.get(ZombieDeathComponent.class);
        if (death == null) {
            zombie.markForRemoval();
            return;
        }

        if (!death.deathClipStarted) {
            death.deathClipStarted = true;
            ZombieDef def = zombie.get(ZombieDefComponent.class).def();
            death.deathClipDuration = ZombieAnimationLocator.applyDeathClip(
                pamPlayer, zombie.get(PamAnimationComponent.class), def);
        }

        death.elapsed += deltaTick;
        if (death.isFadeComplete()) {
            zombie.markForRemoval();
        }
    }
}
