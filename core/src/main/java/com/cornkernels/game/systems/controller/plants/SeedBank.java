package com.cornkernels.game.systems.controller.plants;

import com.cornkernels.game.hud.HighlightAnimationSet;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SeedBank {

    private final List<SeedSlot> slots;
    private final PlantingController plantingController;

    public SeedBank(List<SeedSlot> slots, PlantingController plantingController) {
        this.slots = slots;
        this.plantingController = plantingController;
    }

    public List<SeedSlot> getSlots() {
        return slots;
    }

    public void update(float delta) {
        for (SeedSlot slot : slots) slot.update(delta);
    }

    public boolean canSelect(@NonNull SeedSlot slot) {
        return slot.isReady() && plantingController.canAfford(slot.getPlantDef().getCost());
    }

    public void select(SeedSlot slot, CursorAttachment thumbnail, HighlightAnimationSet highlightSet) {
        if (!canSelect(slot)) return;
        plantingController.beginPlantPlacement(
            slot.getPlantDef().getId(),
            slot.getPlantDef().getCost(),
            thumbnail,
            highlightSet,
            slot::startCooldown
        );
    }
}
