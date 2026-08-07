package com.cornkernels.game.hud.cursor;

import com.badlogic.gdx.math.Vector2;
import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.game.map.MapData;
import com.cornkernels.game.map.grid.GridObject;
import com.cornkernels.game.map.grid.GridPosition;

public class CursorToolController {

    private final MapData mapData;
    private final GameplayCamera camera;
    private final CursorToolState state;

    public CursorToolController(MapData mapData, GameplayCamera camera, CursorToolState state) {
        this.mapData = mapData;
        this.camera = camera;
        this.state = state;
    }

    public void update(InputSnapshot snapshot) {
        if (!state.active) return;

        Vector2 worldPoint = camera.screenToWorld(snapshot.cursorScreenX(), snapshot.cursorScreenY());
        state.rawCursorWorldX = worldPoint.x;
        state.rawCursorWorldY = worldPoint.y;

        GridPosition position = mapData.getGridPositionAt(worldPoint.x, worldPoint.y);
        boolean eligible = position != null && isEligible(position);

        state.snappedPosition = eligible ? position : null;

        if (snapshot.confirmPressed() && state.snappedPosition != null) {
            GridPosition confirmedPosition = state.snappedPosition;
            state.onConfirm.accept(confirmedPosition);
        }
    }

    private boolean isEligible(GridPosition position) {
        GridObject cell = mapData.grid[position.lane()][position.lane()];
        return cell != null && state.eligibility.test(cell);
    }
}
