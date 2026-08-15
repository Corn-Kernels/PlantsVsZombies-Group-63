package com.cornkernels.game.hud.cursor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jspecify.annotations.NonNull;

public class RegionCursorAttachment implements CursorAttachment {

    private final TextureRegion region;
    private final float width;
    private final float height;

    public RegionCursorAttachment(TextureRegion region, float width, float height) {
        this.region = region;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(@NonNull SpriteBatch batch, float worldX, float worldY, float delta) {
        batch.draw(region, worldX - width / 2f, worldY - height / 2f, width, height);
    }
}
