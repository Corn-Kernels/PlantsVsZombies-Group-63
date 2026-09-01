package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantDefComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.plant_specific.SheepedComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieBehaviorComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDeathComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.SunInfectedComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.EnragedComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import static com.cornkernels.game.systems.entity.WaveSystem.OFFSCREEN_SPAWN_MARGIN_COLUMNS;

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

            PositionComponent positionComponent = zombie.get(PositionComponent.class);
            if (positionComponent.position.getX() > field.getTotalColumns() + OFFSCREEN_SPAWN_MARGIN_COLUMNS + 1f) {
                zombie.markForRemoval();
                continue;
            }

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

            VelocityComponent velComp = zombie.get(VelocityComponent.class);
            PamAnimationComponent pamAnim = zombie.get(PamAnimationComponent.class);
            if (velComp != null && pamAnim != null) {
                if (velComp.velocityPerTick.getX() > 0) {
                    pamAnim.flipX = true;
                } else if (velComp.velocityPerTick.getX() < 0) {
                    pamAnim.flipX = false;
                }
            }

            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            Entity target = null;
            boolean isHypnotized = zombie.has(HypnoComponent.class);

            if (isHypnotized) {
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
                for (ZombieInstance z : field.getZombiesInLane(pos.lane())) {
                    if (!z.isMarkedForRemoval() && z.has(HypnoComponent.class)) {
                        GridPosition zPos = GridPosition.fromContinuous(z.get(PositionComponent.class).position);
                        if (zPos.column() == pos.column()) {
                            target = z;
                            break;
                        }
                    }
                }

                if (target == null) {
                    PlantInstance plant = field.getPlantAt(pos.lane(), pos.column());
                    if (plant != null) {
                        if (!plant.has(OctoedComponent.class) && !plant.has(SheepedComponent.class)) {
                            PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
                            if (freezeComp == null || freezeComp.frozenHp <= 0) {
                                target = plant;
                            }
                        }
                    }
                }
            }

            if (target == null) {
                if (state.state == ZombieStateComponent.State.EATING) {
                    state.changeState(ZombieStateComponent.State.WALKING);
                    state.targetEntity = null;
                    applyClip(zombie, "walk");
                }
                continue;
            }

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

                EnragedComponent enraged = zombie.get(EnragedComponent.class);
                if (enraged != null) {
                    currentDamage = (int) (currentDamage * enraged.damageMultiplier);
                }

                boolean instantConsume = false;

                if (state.targetEntity instanceof PlantInstance plantTarget) {
                    PlantDefComponent plantDefComp = plantTarget.get(PlantDefComponent.class);
                    if (plantDefComp != null) {
                        int plantId = plantDefComp.def().getId();
                        int baseId = plantId % 1000;
                        int level = plantId / 1000;

                        // 1. Iceberg Lettuce (ID: --38)
                        if (baseId == 38) {
                            IceComponent ice = zombie.get(IceComponent.class);
                            if (ice != null) {
                                ice.freezeLevel = 2; // Frozen solid
                                // Base freeze: ~10s (200 ticks). Level 3+ gets +2s (40 ticks).
                                ice.applyFreeze((level >= 3) ? 240 : 200,(level >= 3) ? 240 : 200);
                            }
                            instantConsume = true;
                        }
                        // 2. Hypno-shroom (ID: --54)
                        else if (baseId == 54) {
                            zombie.add(new HypnoComponent());
                            VelocityComponent vel = zombie.get(VelocityComponent.class);
                            if (vel != null) {
                                vel.velocityPerTick.setX(Math.abs(vel.velocityPerTick.getX())); // Force walk right
                            }

                            HealthComponent hc = zombie.get(HealthComponent.class);
                            if (hc != null) {
                                if (level == 3) {
                                    hc.currentHealth *= 2;
                                } else if (level >= 4) {
                                    hc.currentHealth *= 2;
                                    EnragedComponent rage =new EnragedComponent();
                                    zombie.add(rage);
                                    rage.damageMultiplier=2;
                                    rage.speedMultiplier=1;
                                }
                            }
                            instantConsume = true;
                        }
                        // 3. Sun Bean (ID: --51)
                        else if (baseId == 51) {
                            zombie.add(new SunInfectedComponent());
                        }
                    }

                    // If it was an instant-consume trap, destroy it and abort the standard bite sequence
                    if (instantConsume) {
                        plantTarget.markForRemoval();
                        state.changeState(ZombieStateComponent.State.WALKING);
                        state.targetEntity = null;
                        applyClip(zombie, "walk");
                        continue;
                    }
                }

                // Standard combat logic (bypassed if the plant was just instantly consumed)
                CombatSystem.applyDamage(state.targetEntity, currentDamage, false);
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
