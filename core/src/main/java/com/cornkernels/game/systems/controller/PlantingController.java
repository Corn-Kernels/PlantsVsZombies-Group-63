package com.cornkernels.game.systems.controller;

import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.hud.HighlightAnimationSet;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.hud.cursor.CursorToolController;
import com.cornkernels.game.hud.cursor.CursorToolState;
import com.cornkernels.game.hud.cursor.InputSnapshot;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.MapData;

import java.util.HashMap;
import java.util.Map;

public class PlantingController {

    private final CursorToolState toolState = new CursorToolState();

    private final CursorToolController toolController;
    private final Field field;

    private final Map<String, Float> coolDownRemaining = new HashMap<>();
    private final Map<String, Float> coolDownDuration = new HashMap<>();

    private int currentSun;

    public PlantingController(Field field, MapData mapData, GameplayCamera camera, int startingSun) {
        this.field = field;
        this.toolController = new CursorToolController(mapData, camera, toolState);
        this.currentSun = startingSun;
    }

    public void update(float delta, InputSnapshot inputSnapshot) {
        toolController.update(inputSnapshot);
        for (Map.Entry<String, Float> entry : coolDownRemaining.entrySet()) {
            entry.setValue(Math.max(0f, entry.getValue() - delta));
        }
    }

    public void addSun(int amount) {
        currentSun += amount;
    }

    public int getCurrentSun() {
        return currentSun;
    }

    public boolean canAfford(int sunCost) {
        return currentSun >= sunCost;
    }

    public boolean isOnCooldown(String plantTypeId) {
        return coolDownRemaining.getOrDefault(plantTypeId, 0f) > 0f;
    }

    public float getCoolDownFraction(String plantTypeId) {
        float duration = coolDownDuration.getOrDefault(plantTypeId, 1f);
        return coolDownRemaining.getOrDefault(plantTypeId, 0f) / duration;
    }

    public void beginPlantPlacement(String plantTypeId, CursorAttachment thumbnail, HighlightAnimationSet highlightSet) {
        toolState.active = true;
        toolState.attachment = thumbnail;
        toolState.highlightSet = highlightSet;
        toolState.eligibility =
            cell -> cell.getPlant() == null && cell.getObstacle() == null;
        toolState.onConfirm = gridPosition -> {
            field.addPlant(new PlantInstance(PlantDef.getPlantTypeOfName(plantTypeId), gridPosition));
            toolState.active = false; // TODO: Reduce the sun amount and cooldown
        };
    }

    public void beginShovel(CursorAttachment shovelIcon) {
        toolState.active = true;
        toolState.attachment = shovelIcon;
        toolState.highlightSet = null;
        toolState.eligibility = cell -> cell.getPlant() != null;
        toolState.onConfirm = field::removePlantAt;
    }

    public void cancelActiveTool() {
        toolState.active = false;
    }

    public CursorToolState getToolState() {
        return toolState;
    }
}
