package com.cornkernels.game.hud.cursor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface CursorAttachment {
    void render(SpriteBatch batch, float worldX, float worldY, float delta);
}
