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

    public boolean canAfford(@NonNull SeedSlot slot) {
        return plantingController.canAfford(slot.getPlantDef().getCost());
    }

    public void cancelSelection() {
        plantingController.cancelActiveTool();
    }

    public void select(SeedSlot slot, CursorAttachment thumbnail, HighlightAnimationSet highlightSet) {
        select(slot, thumbnail, highlightSet, null);
    }

    public void select(SeedSlot slot, CursorAttachment thumbnail, HighlightAnimationSet highlightSet,
                       Runnable onPlaced) {
        if (!canSelect(slot)) return;
        plantingController.beginPlantPlacement(
            slot,
            thumbnail,
            highlightSet,
            () -> {
                slot.startCooldown();
                if (onPlaced != null) onPlaced.run();
            }
        );
    }
}
