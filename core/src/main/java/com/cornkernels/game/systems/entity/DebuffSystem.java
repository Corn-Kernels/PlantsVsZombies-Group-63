package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.ButterComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.PoisonComponent;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;

import java.util.Iterator;
import java.util.List;

public class DebuffSystem extends EntitySystem {

    @Override
    public void update(float delta) {
        for (ZombieInstance zombie : field.getActiveZombies()) {
            if (zombie.isMarkedForRemoval()) continue;

            // 1. Process Poison Stacks
            if (zombie.has(PoisonComponent.class)) {
                List<PoisonComponent> poisons = zombie.getAll(PoisonComponent.class);
                Iterator<PoisonComponent> iterator = poisons.iterator();

                while (iterator.hasNext()) {
                    PoisonComponent poison = iterator.next();
                    poison.durationTicks--;
                    poison.tickCounter++;

                    // Apply damage at the specified interval
                    if (poison.tickCounter >= poison.tickInterval) {
                        CombatSystem.applyDamage(zombie, poison.damagePerTick, true);
                        poison.tickCounter = 0;
                    }

                    // Remove this specific stack if it has expired
                    if (poison.durationTicks <= 0) {
                        iterator.remove();
                    }
                }
            }

            // 2. Process Ice (Freeze & Slow)
            IceComponent ice = zombie.get(IceComponent.class);
            if (ice != null && ice.freezeLevel > 0) {
                if(ice.freezeLevel==2){
                    zombie.get(VelocityComponent.class).velocityPerTick.setX(0);
                    zombie.get(VelocityComponent.class).velocityPerTick.setY(0);
                }
                if(ice.freezeLevel==1){
                    zombie.get(VelocityComponent.class).velocityPerTick.setX(zombie.get(VelocityComponent.class).velocityPerTick.getX());
                    zombie.get(VelocityComponent.class).velocityPerTick.setY(zombie.get(VelocityComponent.class).velocityPerTick.getY());
                }
                if (ice.freezeTicksRemaining > 0) {
                    ice.freezeTicksRemaining--;
                    if (ice.freezeTicksRemaining <= 0) {
                        ice.freezeLevel = 1; // Thaw into a slow/chill state
                    }
                }

                if (ice.slowTicksRemaining > 0) {
                    ice.slowTicksRemaining--;
                    if (ice.slowTicksRemaining <= 0 && ice.freezeTicksRemaining <= 0) {
                        ice.melt(); // Completely clear the debuff
                    }
                }
            }

            // 3. Process Stuns (Butter)
            if (zombie.has(ButterComponent.class)) {
                List<ButterComponent> stuns = zombie.getAll(ButterComponent.class);
                Iterator<ButterComponent> iterator = stuns.iterator();

                while (iterator.hasNext()) {
                    ButterComponent stun = iterator.next();
                    stun.stunTicksRemaining--;

                    if (stun.stunTicksRemaining <= 0) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}
