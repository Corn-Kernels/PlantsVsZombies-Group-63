package com.cornkernels.game.systems.controller.plants;


import com.cornkernels.game.entities.types.plants.PlantDef;

public class SeedSlot {

    private final PlantDef plantDef;
    private float rechargeRemaining;
    private boolean boosted;

    public SeedSlot(PlantDef plantDef) {
        this.plantDef = plantDef;
        this.rechargeRemaining = 0f;
        this.boosted = false;
    }

    public PlantDef getPlantDef() {
        return plantDef;
    }

    public boolean isBoosted() {
        return boosted;
    }

    public void setBoosted(boolean boosted) {
        this.boosted = boosted;
    }

    public boolean isReady() {
        return rechargeRemaining <= 0f;
    }

    public float getRechargeRemaining() {
        return rechargeRemaining;
    }

    public float getRechargeProgress() {
        float total = plantDef.getRecharge();
        if (total <= 0f) return 1f;
        return 1f - Math.clamp(rechargeRemaining / total, 0f, 1f);
    }

    public void update(float deltaTick) {
        if (rechargeRemaining > 0f) {
            rechargeRemaining = Math.max(0f, rechargeRemaining - deltaTick);
        }
    }

    public void startCooldown() {
        rechargeRemaining = plantDef.getRecharge();
    }

    public void setCooldown(float cooldown) {
        this.rechargeRemaining = cooldown;
    }
}
