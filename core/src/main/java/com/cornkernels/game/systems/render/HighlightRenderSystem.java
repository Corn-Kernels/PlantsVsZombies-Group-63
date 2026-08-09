package com.cornkernels.game.systems.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.game.hud.cursor.CursorToolState;
import com.cornkernels.game.map.MapData;
import com.cornkernels.game.map.grid.GridObject;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.Collections;

public class HighlightRenderSystem extends RenderSystem {

    private final PamPlayer pamPlayer;
    private final MapData mapData;
    private final CursorToolState state;
    private float stateTime = 0f;

    public HighlightRenderSystem(SpriteBatch batch, PamPlayer pamPlayer, MapData mapData, CursorToolState state) {
        super(batch);
        this.pamPlayer = pamPlayer;
        this.mapData = mapData;
        this.state = state;
    }

    @Override
    public void render(float delta) {
        if (!state.active || state.highlightSet == null) return;

        stateTime += delta;

        for (int lane = 0; lane < mapData.grid.length; lane++) {
            for (int column = 0; column < mapData.grid[lane].length; column++) {
                GridObject cell = mapData.grid[lane][column];
                if (cell == null || !state.eligibility.test(cell)) continue;

                boolean isHovered = state.snappedPosition != null
                    && state.snappedPosition.lane() == lane
                    && state.snappedPosition.column() == column;

                ClipRef clip = isHovered ? state.highlightSet.hoveredClip() : state.highlightSet.idleClip();
                Rectangle bounds = mapData.cellBounds[lane][column];

                pamPlayer.draw(batch, clip, stateTime, bounds.x + bounds.width / 2f,
                    bounds.y + bounds.height / 2f, true, Collections.emptyMap());
            }
        }
    }
}
