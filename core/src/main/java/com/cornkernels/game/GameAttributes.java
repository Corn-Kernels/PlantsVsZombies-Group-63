package com.cornkernels.game;

import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.controller.plants.SeedSlot;

import java.util.List;

public class GameAttributes {

    public final List<SeedSlot> seedSlots;
    public final List<ZombieDef> eligibleZombies;
    public int sunAmount = 0;

    public GameAttributes(List<SeedSlot> seedSlots, List<ZombieDef> eligibleZombies) {
        this.seedSlots = seedSlots;
        this.eligibleZombies = eligibleZombies;
    }

    public void adjustSunAmount(int sunAmount) {
        this.sunAmount += sunAmount;
    }

    public void update(float deltaTick) {
        for (SeedSlot seedSlot : seedSlots) {
            seedSlot.update(deltaTick);
        }
    }

    public PlantInstance getPlantInstance(SeedSlot seedSlot, GridPosition position) {
        if (!seedSlots.contains(seedSlot)) return null;
        if (!seedSlot.isReady()) return null;
        if (sunAmount < seedSlot.getPlantDef().getCost()) return null;

        sunAmount -= seedSlot.getPlantDef().getCost();
        seedSlot.startCooldown();
        return new PlantInstance(seedSlot.getPlantDef(), position);
    }

    public boolean slotsContainPlantOfType(PlantDef plantDef) {
        for (SeedSlot seedSlot : seedSlots) {
            if (seedSlot.getPlantDef().equals(plantDef)) return true;
        }
        return false;
    }
}
