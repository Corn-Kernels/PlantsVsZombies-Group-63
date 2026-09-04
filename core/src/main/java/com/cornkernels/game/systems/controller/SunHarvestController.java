package com.cornkernels.game.systems.controller;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.engine.utility.InputSnapshot;
import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
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
        Vec2d continuousPos = sun.get(PositionComponent.class).position;

        // Calculate the exact world bounds based on the sun's physical continuous position
        Rectangle origin = mapData.cellBounds[0][0];

        float columnStep = mapData.cellBounds[0].length > 1
            ? mapData.cellBounds[0][1].x - origin.x
            : origin.width;

        float laneStep = mapData.cellBounds.length > 1
            ? mapData.cellBounds[1][0].y - origin.y
            : origin.height;

        float centerX = origin.x + origin.width / 2f + continuousPos.getX() * columnStep;
        float centerY = origin.y + origin.height / 2f + continuousPos.getY() * laneStep;

        float halfWidth = origin.width / 2f;
        float halfHeight = origin.height / 2f;

        return worldX >= centerX - halfWidth && worldX <= centerX + halfWidth
            && worldY >= centerY - halfHeight && worldY <= centerY + halfHeight;
    }
}
