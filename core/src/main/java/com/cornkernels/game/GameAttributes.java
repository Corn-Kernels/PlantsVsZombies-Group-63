package com.cornkernels.game;

import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.systems.controller.plants.SeedSlot;

import java.util.List;

public class GameAttributes {

    public final List<SeedSlot> seedSlots;
    public final List<ZombieDef> eligibleZombies;

    public GameAttributes(List<SeedSlot> seedSlots, List<ZombieDef> eligibleZombies) {
        this.seedSlots = seedSlots;
        this.eligibleZombies = eligibleZombies;
    }

    public void update(float deltaTick) {
        for (SeedSlot seedSlot : seedSlots) {
            seedSlot.update(deltaTick);
        }
    }

    public boolean slotsContainPlantOfType(PlantDef plantDef) {
        for (SeedSlot seedSlot : seedSlots) {
            if (seedSlot.getPlantDef().equals(plantDef)) return true;
        }
        return false;
    }
}
