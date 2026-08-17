package com.cornkernels.game.systems.controller;

import com.badlogic.gdx.math.Vector2;
import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.engine.utility.InputSnapshot;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.controller.plants.PlantingController;
import com.cornkernels.game.systems.entity.SunSystem;
import org.jspecify.annotations.NonNull;

public class SunHarvestController {

    private final Field field;
    private final MapData mapData;
    private final GameplayCamera camera;
    private final PlantingController plantingController;

    public SunHarvestController(Field field, MapData mapData, GameplayCamera camera,
                                PlantingController plantingController) {
        this.field = field;
        this.mapData = mapData;
        this.camera = camera;
        this.plantingController = plantingController;
    }

    public boolean update(@NonNull InputSnapshot snapshot) {
        if (!snapshot.confirmPressed()) return false;

        Vector2 worldPoint = camera.screenToWorld(snapshot.cursorScreenX(), snapshot.cursorScreenY());
        GridPosition clicked = mapData.getGridPositionAt(worldPoint.x, worldPoint.y);
        if (clicked == null) return false;

        for (SunInstance sun : field.getActiveSuns()) {
            if (sun.isMarkedForRemoval()) continue;
            GridPosition sunPosition = GridPosition.fromContinuous(sun.get(PositionComponent.class).position);
            if (sunPosition.equals(clicked)) {
                return SunSystem.tryHarvest(sun, field, plantingController);
            }
        }
        return false;
    }
}
