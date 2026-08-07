package com.cornkernels.game.systems.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.game.hud.cursor.CursorToolState;
import com.cornkernels.game.map.MapData;

public class CursorAttachmentRenderSystem extends RenderSystem {

    private final MapData mapData;
    private final CursorToolState state;

    public CursorAttachmentRenderSystem(SpriteBatch batch, MapData mapData, CursorToolState state) {
        super(batch);
        this.mapData = mapData;
        this.state = state;
    }

    @Override
    public void render(float delta) {
        if (!state.active || state.attachment == null) return;

        float drawX;
        float drawY;

        if (state.snappedPosition != null) {
            Rectangle bounds = mapData.cellBounds[state.snappedPosition.lane()][state.snappedPosition.column()];
            drawX = bounds.x + bounds.width / 2f;
            drawY = bounds.y + bounds.height / 2f;
        } else {
            drawX = state.rawCursorWorldX;
            drawY = state.rawCursorWorldY;
        }

        state.attachment.render(batch, drawX, drawY, delta);
    }
}
