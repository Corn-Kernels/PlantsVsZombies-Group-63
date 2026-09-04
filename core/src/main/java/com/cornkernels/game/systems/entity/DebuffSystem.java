package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.PamAnimationComponent;
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
            processPoison(zombie);
            processTint(zombie);
        }
    }

    private void processPoison(ZombieInstance zombie) {
        if (!zombie.has(PoisonComponent.class)) return;

        List<PoisonComponent> poisons = zombie.getAll(PoisonComponent.class);
        Iterator<PoisonComponent> iterator = poisons.iterator();

        while (iterator.hasNext()) {
            PoisonComponent poison = iterator.next();
            poison.durationTicks--;
            poison.tickCounter++;

            if (poison.tickCounter >= poison.tickInterval) {
                CombatSystem.applyDamage(zombie, poison.damagePerTick, true);
                poison.tickCounter = 0;
            }

            if (poison.durationTicks <= 0) {
                iterator.remove();
            }
        }
    }

    private void processTint(ZombieInstance zombie) {
        PamAnimationComponent pam = zombie.get(PamAnimationComponent.class);
        if (pam == null) return;

        float r = 0f, g = 0f, b = 0f;
        int activeCount = 0;

        if (zombie.has(HypnoComponent.class)) {
            r += 1.0f;
            g += 0.4f;
            b += 0.7f;
            activeCount++;
        }
        if (zombie.has(PoisonComponent.class)) {
            r += 0.8f;
            g += 0.2f;
            b += 1.0f;
            activeCount++;
        }
        if (zombie.has(SunInfectedComponent.class)) {
            r += 1.0f;
            g += 1.0f;
            b += 0.4f;
            activeCount++;
        }

        IceComponent ice = zombie.get(IceComponent.class);
        if (ice != null && ice.freezeLevel > 0) {
            if (ice.freezeLevel == 2) {
                r += 0.2f;
                g += 0.5f;
                b += 1.0f;
            } else {
                r += 0.6f;
                g += 0.8f;
                b += 1.0f;
            }
            activeCount++;
        }

        if (activeCount == 0) pam.tint.set(1.0f, 1.0f, 1.0f, 1.0f);
        else pam.tint.set(r / activeCount, g / activeCount, b / activeCount, 1.0f);
    }
}
