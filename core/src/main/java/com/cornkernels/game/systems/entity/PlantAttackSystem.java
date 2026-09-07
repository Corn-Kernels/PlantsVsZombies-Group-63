package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.*;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.ChomperComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GrowthComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.InstaTrapComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.SunShroomComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.effects.PlantFoodEffect;
import com.cornkernels.game.entities.types.plants.PlantCategory;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.utility.PlantAnimationLocator;
import com.cornkernels.game.utility.PlantFoodEffectAnimationLocator;
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
            if (handleDebuffs(plant)) continue;

            PlantDef def = plant.get(PlantDefComponent.class).def();
            PamAnimationComponent anim = plant.get(PamAnimationComponent.class);

            if (handlePlantFood(plant, def, anim)) continue;

            handleStandardAttack(plant, def, anim, deltaTick);
        }
    }

    private boolean handleDebuffs(PlantInstance plant) {
        SheepedComponent sheep = plant.get(SheepedComponent.class);
        if (sheep != null) {
            boolean wizardDead = sheep.wizard == null || sheep.wizard.isMarkedForRemoval()
                || (sheep.wizard.has(ZombieStateComponent.class) && sheep.wizard.get(ZombieStateComponent.class).state == ZombieStateComponent.State.DEAD);

            if (wizardDead) plant.removeAll(SheepedComponent.class);
            else return true;
        }

        if (plant.has(OctoedComponent.class)) return true;

        PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
        return freezeComp != null && (freezeComp.freezeLayers >= 3 || freezeComp.frozenHp > 0);
    }

    private boolean handlePlantFood(PlantInstance plant, PlantDef def, PamAnimationComponent anim) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf != null && pf.timerTicks > 0) {
            if (pf.timerTicks == pf.normalTime) {
                PlantAnimationLocator.applyPlantFoodActiveClip(pamPlayer, anim, def);
                spawnPlantFoodEffect(plant, pf);
            }
            pf.behavior.plantFood(plant, field);
            pf.timerTicks--;

            if (pf.timerTicks <= 0) {
                applyPlantFoodEndAnimation(plant, def, anim);
                endPlantFoodEffect(pf);
            }
            return true;
        }
        return false;
    }

    private void handleStandardAttack(PlantInstance plant, PlantDef def, PamAnimationComponent anim, float deltaTick) {
        PlantAttackComponent attack = plant.get(PlantAttackComponent.class);
        if (attack == null) return;

        attack.cooldownRemaining = Math.max(0, attack.cooldownRemaining - deltaTick);

        ChomperComponent chomperComp = plant.get(ChomperComponent.class);
        InstaTrapComponent trapComp = plant.get(InstaTrapComponent.class);
        GrowthComponent growthComp = plant.get(GrowthComponent.class);
        SunShroomComponent shroomComp = plant.get(SunShroomComponent.class);

        boolean wasDigesting = chomperComp != null && chomperComp.isDigesting;
        boolean hadBiteCooldown = chomperComp != null && chomperComp.biteCooldownTicks > 0;
        boolean wasArmed = trapComp != null && trapComp.isArmed;
        int stageBefore = growthComp != null ? growthComp.stage : 0;
        int shroomStageBefore = shroomComp != null ? shroomComp.currentStage : 0;

        boolean hasTarget = attack.behavior.hasTarget(plant, field);
        updateAttackAnimation(plant, hasTarget);
        updateGrowthAnimations(plant, def, anim, growthComp, stageBefore, shroomComp, shroomStageBefore);

        if (attack.cooldownRemaining <= 0 && hasTarget) {
            attack.behavior.execute(plant, field);
            attack.cooldownRemaining = attack.actionInterval;
            applyActionAnimations(plant, def, anim, chomperComp, wasDigesting, hadBiteCooldown, trapComp, wasArmed, growthComp, shroomComp, attack);
        }
    }

    private void updateGrowthAnimations(PlantInstance plant, PlantDef def, PamAnimationComponent anim, GrowthComponent growth, int stageBefore, SunShroomComponent shroom, int shroomStageBefore) {
        if (growth != null && growth.stage != stageBefore) {
            PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle_stage" + growth.stage + "_");
        }
        if (shroom != null && shroom.currentStage != shroomStageBefore) {
            PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle_stage" + shroom.currentStage);
        }
    }

    private void applyActionAnimations(PlantInstance plant, PlantDef def, PamAnimationComponent anim, ChomperComponent chomper, boolean wasDigesting, boolean hadBiteCooldown, InstaTrapComponent trap, boolean wasArmed, GrowthComponent growth, SunShroomComponent shroom, PlantAttackComponent attack) {
        if (chomper != null) {
            if (!wasDigesting && chomper.isDigesting) {
                PlantAnimationLocator.applyClip(pamPlayer, anim, def, "special_idle");
            } else if (wasDigesting && !chomper.isDigesting) {
                PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle");
            } else if (!hadBiteCooldown && chomper.biteCooldownTicks > 0) {
                PlantAnimationLocator.tryPlayOneShotClip(pamPlayer, anim, def, "bite_end", "special");
            }
        } else if (trap != null) {
            if (!wasArmed && trap.isArmed) {
                PlantAnimationLocator.applyClip(pamPlayer, anim, def, "idle");
            }
        } else if (growth == null && shroom == null && attack.actionInterval > 0f) {
            PlantAnimationLocator.tryPlayOneShotClip(pamPlayer, anim, def, "attack", "special");
        }
    }

    private void applyPlantFoodEndAnimation(@NonNull PlantInstance plant, @NonNull PlantDef def, @NonNull PamAnimationComponent anim) {
        if (def.getCategory() == PlantCategory.WALL_NUT) {
            HealthComponent health = plant.get(HealthComponent.class);
            if (health != null && health.maxHealth > 0) {
                float fraction = (float) health.currentHealth / health.maxHealth;
                PlantAnimationLocator.applyDamageStageClip(pamPlayer, anim, def, fraction);
                return;
            }
        }
        PlantAnimationLocator.applyPlantFoodEndClip(pamPlayer, anim, def);
    }

    private void spawnPlantFoodEffect(@NonNull PlantInstance plant, @NonNull PlantFoodComponent pf) {
        PositionComponent posComp = plant.get(PositionComponent.class);
        if (posComp == null) return;

        PlantFoodEffect effect = new PlantFoodEffect(posComp.position);
        PlantFoodEffectAnimationLocator.playOn(pamPlayer, effect.get(PamAnimationComponent.class));
        field.addEffect(effect);
        pf.effectEntity = effect;
    }

    private void endPlantFoodEffect(@NonNull PlantFoodComponent pf) {
        PlantFoodEffect effect = pf.effectEntity;
        pf.effectEntity = null;
        if (effect == null || effect.isMarkedForRemoval()) return;

        float outroDuration = PlantFoodEffectAnimationLocator.playOff(pamPlayer, effect.get(PamAnimationComponent.class));
        effect.startEnding(outroDuration);
    }

    private void updateAttackAnimation(@NonNull PlantInstance plant, boolean hasTarget) {
        PlantStateComponent stateComp = plant.get(PlantStateComponent.class);
        if (stateComp == null || stateComp.state == PlantStateComponent.State.DEAD) return;

        PlantStateComponent.State desired = hasTarget ? PlantStateComponent.State.SHOOTING : PlantStateComponent.State.IDLE;
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
        PlantAnimationLocator.applyClip(pamPlayer, plant.get(PamAnimationComponent.class), plant.get(PlantDefComponent.class).def(), clipName);
    }
}
