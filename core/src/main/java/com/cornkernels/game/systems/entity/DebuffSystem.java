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


        }
    }
}
