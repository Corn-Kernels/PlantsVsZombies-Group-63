package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.*;
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
import com.cornkernels.game.entities.types.zombies.ZombieLimbs;
import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import static com.cornkernels.game.systems.entity.WaveSystem.OFFSCREEN_SPAWN_MARGIN_COLUMNS;

public class ZombieSystem extends EntitySystem {

    private static final long BITE_INTERVAL_TICKS = 30;
    private static PamPlayer pamPlayer;

    public ZombieSystem(PamPlayer pamPlayer) {
        this.pamPlayer = pamPlayer;
    }

    @Override
    public void update(float deltaTick) {
        for (ZombieInstance zombie : field.getActiveZombies()) {
            if (zombie.isMarkedForRemoval()) continue;
            if (checkOffscreenDespawn(zombie)) continue;

            ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
            if (state.state == ZombieStateComponent.State.DEAD) {
                updateDying(zombie, deltaTick);
                continue;
            }

            updateZombieStateAndArmor(zombie, state);
            updateZombieBehaviorAndFacing(zombie);

            if (state.state == ZombieStateComponent.State.ACTION) continue;

            Entity target = findTarget(zombie);
            if (target == null) {
                clearTarget(zombie, state);
                continue;
            }

            processEating(zombie, state, target);
        }
    }

    private boolean checkOffscreenDespawn(ZombieInstance zombie) {
        PositionComponent positionComponent = zombie.get(PositionComponent.class);
        if (positionComponent.position.getX() > field.getTotalColumns() + OFFSCREEN_SPAWN_MARGIN_COLUMNS + 1f) {
            zombie.markForRemoval();
            return true;
        }
        return false;
    }

    private void updateZombieStateAndArmor(ZombieInstance zombie, ZombieStateComponent state) {
        state.stateTicks++;
        if (state.stateTicks == 1 && state.state == ZombieStateComponent.State.WALKING) {
            applyClip(zombie);
        }

        ArmorComponent armor = zombie.get(ArmorComponent.class);
        if (armor != null && armor.armorType == ArmorType.NEWSPAPER && armor.isDestroyed() && !zombie.has(EnragedComponent.class)) {
            EnragedComponent enraged = new EnragedComponent();
            enraged.damageMultiplier = 2;
            enraged.speedMultiplier = 2;
            zombie.add(enraged);
            applyClip(zombie);
        }

        HealthComponent hc = zombie.get(HealthComponent.class);
        if (hc != null && hc.currentHealth <= hc.maxHealth / 2) {
            ZombieLimbs arm = ZombieAnimationLocator.applyArmLossVisibility(zombie, pamPlayer);
            if (arm != null) field.addEntity(arm);
        }
    }

    private void updateZombieBehaviorAndFacing(ZombieInstance zombie) {
        ZombieBehaviorComponent behaviorComp = zombie.get(ZombieBehaviorComponent.class);
        if (behaviorComp != null) {
            behaviorComp.behavior.update(zombie, field);
        }

        VelocityComponent velComp = zombie.get(VelocityComponent.class);
        PamAnimationComponent pamAnim = zombie.get(PamAnimationComponent.class);
        if (velComp != null && pamAnim != null) {
            if (velComp.velocityPerTick.getX() > 0) pamAnim.flipX = true;
            else if (velComp.velocityPerTick.getX() < 0) pamAnim.flipX = false;
        }
    }

    private Entity findTarget(ZombieInstance zombie) {
        GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
        boolean isHypnotized = zombie.has(HypnoComponent.class);

        for (ZombieInstance z : field.getZombiesInLane(pos.lane())) {
            if (z != zombie && !z.isMarkedForRemoval() && z.has(HypnoComponent.class) != isHypnotized) {
                GridPosition zPos = GridPosition.fromContinuous(z.get(PositionComponent.class).position);
                if (zPos.column() == pos.column()) return z;
            }
        }

        if (!isHypnotized) {
            PlantInstance plant = field.getPlantAt(pos.lane(), pos.column());
            if (plant != null && !plant.has(OctoedComponent.class) && !plant.has(SheepedComponent.class)) {
                PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
                if (freezeComp == null || freezeComp.frozenHp <= 0) return plant;
            }
        }
        return null;
    }

    private void clearTarget(ZombieInstance zombie, ZombieStateComponent state) {
        if (state.state == ZombieStateComponent.State.EATING) {
            state.changeState(ZombieStateComponent.State.WALKING);
            state.targetEntity = null;
            applyClip(zombie);
        }
    }

    private void processEating(ZombieInstance zombie, ZombieStateComponent state, Entity target) {
        if (state.state != ZombieStateComponent.State.EATING || state.targetEntity != target) {
            state.changeState(ZombieStateComponent.State.EATING);
            state.targetEntity = target;
            state.ticksUntilNextBite = BITE_INTERVAL_TICKS;
            applyClip(zombie);
        }

        state.ticksUntilNextBite--;
        if (state.ticksUntilNextBite <= 0) {
            ZombieDef def = zombie.get(ZombieDefComponent.class).def();
            int currentDamage = def.eatDps;

            EnragedComponent enraged = zombie.get(EnragedComponent.class);
            if (enraged != null) currentDamage = (int) (currentDamage * enraged.damageMultiplier);

            if (target instanceof PlantInstance plantTarget) {
                if (processInstantConsume(zombie, state, plantTarget)) return;
            }

            CombatSystem.applyDamage(state.targetEntity, currentDamage, false);
            state.ticksUntilNextBite = BITE_INTERVAL_TICKS;
        }
    }

    private boolean processInstantConsume(ZombieInstance zombie, ZombieStateComponent state, PlantInstance plantTarget) {
        PlantDefComponent plantDefComp = plantTarget.get(PlantDefComponent.class);
        if (plantDefComp == null) return false;

        int plantId = plantDefComp.def().getId();
        int baseId = plantId % 1000;
        int level = plantId / 1000;
        boolean instantConsume = false;

        if (baseId == 38) { // Iceberg Lettuce
            IceComponent ice = zombie.get(IceComponent.class);
            if (ice != null) {
                ice.freezeLevel = 2;
                ice.applyFreeze((level >= 3) ? 240 : 200, (level >= 3) ? 240 : 200);
            }
            instantConsume = true;
        } else if (baseId == 54) { // Hypno-shroom
            zombie.add(new HypnoComponent());
            VelocityComponent vel = zombie.get(VelocityComponent.class);
            if (vel != null) vel.velocityPerTick.setX(Math.abs(vel.velocityPerTick.getX()));

            HealthComponent healthComp = zombie.get(HealthComponent.class);
            if (healthComp != null && level >= 3) {
                healthComp.currentHealth *= 2;
                if (level >= 4) {
                    EnragedComponent rage = new EnragedComponent();
                    rage.damageMultiplier = 2;
                    rage.speedMultiplier = 1;
                    zombie.add(rage);
                }
            }
            instantConsume = true;
        } else if (baseId == 51) { // Sun Bean
            zombie.add(new SunInfectedComponent());
        }

        if (instantConsume) {
            plantTarget.markForRemoval();
            clearTarget(zombie, state);
            return true;
        }
        return false;
    }

    public static void applyClip(@NonNull ZombieInstance zombie) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state == null) return;

        String baseClip = state.state == ZombieStateComponent.State.EATING ? "eat" : state.state == ZombieStateComponent.State.WALKING ? "walk" : null;
        if (baseClip == null) return;

        ArmorComponent armor = zombie.get(ArmorComponent.class);
        boolean hasNewspaper = armor != null && armor.armorType == ArmorType.NEWSPAPER && !armor.isDestroyed() && !zombie.has(EnragedComponent.class);
        String resolvedClipName = hasNewspaper ? baseClip + "_newspaper" : baseClip;

        ZombieDef def = zombie.get(ZombieDefComponent.class).def();
        ZombieAnimationLocator.applyClip(pamPlayer, zombie.get(PamAnimationComponent.class), def, resolvedClipName);
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
            death.deathClipDuration = ZombieAnimationLocator.applyDeathClip(pamPlayer, zombie.get(PamAnimationComponent.class), def);

            PositionComponent pos = zombie.get(PositionComponent.class);
            field.addEntity(new ZombieLimbs(ZombieLimbs.LimbType.HEAD, pos.position, pos.position.getY(), def, pamPlayer));
        }

        death.elapsed += deltaTick;
        if (death.isFadeComplete()) zombie.markForRemoval();
    }
}
