package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.plant_specific.*;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.ChomperComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GrowthComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.InstaTrapComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.SunShroomComponent;
import com.cornkernels.game.entities.types.plants.PlantDef;
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

            if (plant.has(OctoedComponent.class)) {
                continue;
            }
            if (plant.get(PlantFreezeComponent.class).freezeLayers >= 3) {
                continue;
            }

            PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
            if (freezeComp != null && freezeComp.frozenHp > 0) {
                continue;
            }

            PlantAttackComponent attack = plant.get(PlantAttackComponent.class);
            if (attack == null) continue;

            attack.cooldownRemaining = Math.max(0, attack.cooldownRemaining - deltaTick);

            ChomperComponent chomperComp = plant.get(ChomperComponent.class);
            boolean wasDigesting = chomperComp != null && chomperComp.isDigesting;
            boolean hadBiteCooldown = chomperComp != null && chomperComp.biteCooldownTicks > 0;

            InstaTrapComponent trapComp = plant.get(InstaTrapComponent.class);
            boolean wasArmed = trapComp != null && trapComp.isArmed;

            GrowthComponent growthComp = plant.get(GrowthComponent.class);
            int stageBefore = growthComp != null ? growthComp.stage : 0;

            SunShroomComponent shroomComp = plant.get(SunShroomComponent.class);
            int shroomStageBefore = shroomComp != null ? shroomComp.currentStage : 0;

            // --- PLANT FOOD BYPASS LOGIC ---
            PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
            boolean isPlantFoodActive = (pf != null && pf.timerTicks > 0);

            boolean hasTarget;
            if (isPlantFoodActive) {
                pf.behavior.plantFood(plant, field);
                updateAttackAnimation(plant, true);
                pf.timerTicks--;
                continue;
            } else {
                hasTarget = attack.behavior.hasTarget(plant, field);
            }

            updateAttackAnimation(plant, hasTarget);

            PlantDef def = plant.get(PlantDefComponent.class).def();
            PamAnimationComponent anim = plant.get(PamAnimationComponent.class);

            if (growthComp != null && growthComp.stage != stageBefore) {
                PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle_stage" + growthComp.stage + "_");
            }
            if (shroomComp != null && shroomComp.currentStage != shroomStageBefore) {
                PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle_stage" + shroomComp.currentStage);
            }

            if (attack.cooldownRemaining > 0) continue;
            if (!hasTarget) continue;

            attack.behavior.execute(plant, field);
            attack.cooldownRemaining = attack.actionInterval;

            if (chomperComp != null) {
                if (!wasDigesting && chomperComp.isDigesting) {
                    PlantAnimationLocator.applyClip(pamPlayer, anim, def, "special_idle");
                } else if (wasDigesting && !chomperComp.isDigesting) {
                    PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle");
                } else if (!hadBiteCooldown && chomperComp.biteCooldownTicks > 0) {
                    PlantAnimationLocator.tryPlayOneShotClip(pamPlayer, anim, def, "bite_end", "special");
                }
            } else if (trapComp != null) {
                if (!wasArmed && trapComp.isArmed) {
                    PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle");
                }
            } else if (growthComp == null && shroomComp == null && attack.actionInterval > 0f) {
                PlantAnimationLocator.tryPlayOneShotClip(pamPlayer, anim, def, "attack", "special");
            }
        }
    }

    private void updateAttackAnimation(@NonNull PlantInstance plant, boolean hasTarget) {
        PlantStateComponent stateComp = plant.get(PlantStateComponent.class);
        if (stateComp == null || stateComp.state == PlantStateComponent.State.DEAD) return;

        PlantAttackComponent attack = plant.get(PlantAttackComponent.class);

        PlantStateComponent.State desired = hasTarget
            ? PlantStateComponent.State.SHOOTING
            : PlantStateComponent.State.IDLE;
        if (stateComp.state == desired) return;
        stateComp.state = desired;

        GrowthComponent growth = plant.get(GrowthComponent.class);
        String clipName;
        if (growth == null) {
            clipName = desired == PlantStateComponent.State.SHOOTING ? "attack" : "idle";
        } else if (desired == PlantStateComponent.State.SHOOTING) {
            clipName = "attack_stage" + growth.stage;
        } else {
            clipName = "idle_stage" + growth.stage + "_";
        }
        PlantAnimationLocator.applyClip(pamPlayer, plant.get(PamAnimationComponent.class),
            plant.get(PlantDefComponent.class).def(), clipName);
    }
}
