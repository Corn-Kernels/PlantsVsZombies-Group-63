package com.cornkernels.game.systems.controller;

import com.badlogic.gdx.math.Rectangle;
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

        for (SunInstance sun : field.getActiveSuns()) {
            if (sun.isMarkedForRemoval()) continue;
            if (containsPoint(sun, worldPoint.x, worldPoint.y)) {
                return SunSystem.tryHarvest(sun, field, plantingController);
            }
        }
        return false;
    }

    private boolean containsPoint(@NonNull SunInstance sun, float worldX, float worldY) {
        GridPosition landingPosition = GridPosition.fromContinuous(sun.get(PositionComponent.class).position);
        int lane = landingPosition.lane();
        int column = landingPosition.column();
        if (lane < 0 || lane >= mapData.cellBounds.length) return false;
        Rectangle[] row = mapData.cellBounds[lane];
        if (column < 0 || column >= row.length) return false;

        Rectangle landedBounds = row[column];
        float landedY = landedBounds.y + landedBounds.height / 2f;
        float currentY = SunSystem.currentDrawY(sun, landedY, landedBounds.height, mapData);

        float halfWidth = landedBounds.width / 2f;
        float halfHeight = landedBounds.height / 2f;
        float centerX = landedBounds.x + halfWidth;

        return worldX >= centerX - halfWidth && worldX <= centerX + halfWidth
            && worldY >= currentY - halfHeight && worldY <= currentY + halfHeight;
    }
}
