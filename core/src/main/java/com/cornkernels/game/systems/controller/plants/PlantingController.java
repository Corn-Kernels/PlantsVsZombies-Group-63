package com.cornkernels.game.systems.controller.plants;

import com.badlogic.gdx.Gdx;
import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.engine.utility.InputSnapshot;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.hud.HighlightAnimationSet;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.hud.cursor.CursorToolController;
import com.cornkernels.game.hud.cursor.CursorToolState;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.utility.PlantAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

public class PlantingController {

    private final CursorToolState toolState = new CursorToolState();

    private final CursorToolController toolController;
    private final Field field;
    private final PamPlayer pamPlayer;

    private int currentSun;

    public PlantingController(Field field, MapData mapData, GameplayCamera camera, int startingSun,
                              PamPlayer pamPlayer) {
        this.field = field;
        this.toolController = new CursorToolController(mapData, camera, toolState);
        this.pamPlayer = pamPlayer;
        this.currentSun = startingSun;
    }

    public void update(float delta, InputSnapshot inputSnapshot) {
        toolController.update(inputSnapshot);
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

    public void beginPlantPlacement(int plantTypeId, int sunCost, CursorAttachment thumbnail,
                                    HighlightAnimationSet highlightSet, Runnable onPlanted) {
        toolState.active = true;
        toolState.attachment = thumbnail;
        toolState.highlightSet = highlightSet;
        toolState.eligibility =
            cell -> cell.getPlant() == null && cell.getObstacle() == null;
        toolState.onConfirm = gridPosition -> {
            PlantDef plantDef = PlantDef.getPlantTypeOfId(String.valueOf(plantTypeId));
            if (plantDef != null) {
                PlantInstance plant = new PlantInstance(plantDef, gridPosition);
                applyIdleAnimation(plant, plantDef);
                field.addPlant(plant);
                currentSun -= sunCost;
                toolState.active = false;
                if (onPlanted != null) onPlanted.run();
            } else {
                throw new NullPointerException("plantDef is null.");
            }
        };
    }

    private void applyIdleAnimation(@NonNull PlantInstance plant, @NonNull PlantDef plantDef) {
        String pamPath = PlantAnimationLocator.findPamPath(plantDef);
        if (pamPath == null) {
            Gdx.app.error("PlantingController", "No PAM animation found for " + plantDef);
            return;
        }
        PlantAnimationLocator.applySpawnAnimation(pamPlayer, plant.get(PamAnimationComponent.class), pamPath);
    }

    public void toggleShovel(CursorAttachment shovelIcon) {
        if (toolState.active) {
            toolState.active = false;
            toolState.attachment = null;
            toolState.highlightSet = null;
            toolState.eligibility = null;
            toolState.onConfirm = null;
        } else {
            toolState.active = true;
            toolState.attachment = shovelIcon;
            toolState.highlightSet = null;
            toolState.eligibility = cell -> cell.getPlant() != null;
            toolState.onConfirm = field::removePlantAt;
        }

    }

    public void cancelActiveTool() {
        toolState.active = false;
    }

    public CursorToolState getToolState() {
        return toolState;
    }
}
