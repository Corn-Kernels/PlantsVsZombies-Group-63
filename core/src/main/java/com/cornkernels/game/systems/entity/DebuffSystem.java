package com.cornkernels.game.systems.entity;

import com.badlogic.gdx.graphics.Color;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.ButterComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.PoisonComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.SunInfectedComponent;
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

            // 2. Process PamAnimation Tinting
            PamAnimationComponent pam = zombie.get(PamAnimationComponent.class);
            if (pam != null) {
                float r = 0f, g = 0f, b = 0f;
                int activeCount = 0;

                if (zombie.has(ButterComponent.class)) {
                    r += 1.0f; g += 1.0f; b += 0.4f; // Yellow
                    activeCount++;
                }
                if (zombie.has(HypnoComponent.class)) {
                    r += 0.8f; g += 0.2f; b += 1.0f; // Purple
                    activeCount++;
                }
                if (zombie.has(PoisonComponent.class)) {
                    r += 0.4f; g += 1.0f; b += 0.4f; // Green
                    activeCount++;
                }
                if (zombie.has(SunInfectedComponent.class)) {
                    r += 1.0f; g += 0.4f; b += 0.4f; // Red
                    activeCount++;
                }

                IceComponent ice = zombie.get(IceComponent.class);
                if (ice != null && ice.freezeLevel > 0) {
                    if (ice.freezeLevel == 2) {
                        r += 0.2f; g += 0.5f; b += 1.0f; // Bluer (Frozen Solid)
                    } else {
                        r += 0.6f; g += 0.8f; b += 1.0f; // Light Blue (Chilled)
                    }
                    activeCount++;
                }

                if (activeCount == 0) {
                    pam.tint.set(1.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    pam.tint.set(r / activeCount, g / activeCount, b / activeCount, 1.0f);
                }
            }
        }
    }
}
