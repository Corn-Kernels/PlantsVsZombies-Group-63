package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.plant_specific.*;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.utility.PlantAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

public class PlantAttackSystem extends EntitySystem {

    private final PamPlayer pamPlayer;

    public PlantAttackSystem(PamPlayer pamPlayer) {
        this.pamPlayer = pamPlayer;
    }

    @Override
    public void update(float deltaTick) {
        for (PlantInstance plant : field.getActivePlants()) {
            if (plant.isMarkedForRemoval()) continue;

            // If the plant is covered in an octopus, it cannot attack!
            if (plant.has(OctoedComponent.class)) {continue;}

            PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
            if (freezeComp != null && freezeComp.frozenHp > 0) {
                continue;
            }

            PlantAttackComponent attack = plant.get(PlantAttackComponent.class);
            if (attack == null) continue;

            attack.cooldownRemaining = Math.max(0, attack.cooldownRemaining - deltaTick);

            boolean hasTarget = attack.behavior.hasTarget(plant, field);
            updateAttackAnimation(plant, hasTarget);

            if (attack.cooldownRemaining > 0) continue;
            if (!hasTarget) continue;

            attack.behavior.execute(plant, field);
            attack.cooldownRemaining = attack.actionInterval;
        }
    }

    private void updateAttackAnimation(@NonNull PlantInstance plant, boolean hasTarget) {
        PlantStateComponent stateComp = plant.get(PlantStateComponent.class);
        if (stateComp == null || stateComp.state == PlantStateComponent.State.DEAD) return;

        PlantStateComponent.State desired = hasTarget
            ? PlantStateComponent.State.SHOOTING
            : PlantStateComponent.State.IDLE;
        if (stateComp.state == desired) return;

        stateComp.state = desired;
        PlantAnimationLocator.applyClip(pamPlayer, plant.get(PamAnimationComponent.class),
            plant.get(PlantDefComponent.class).def(),
            desired == PlantStateComponent.State.SHOOTING ? "attack" : "idle");
    }
}
