package com.cornkernels.engine.renderer.hud.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.engine.renderer.hud.HudAnchor;
import org.jspecify.annotations.NonNull;

public class IconView implements HudView {

    private final HudAnchor anchor;
    private final float width;
    private final float height;
    private final TextureRegion icon;

    public IconView(HudAnchor anchor, TextureRegion icon, float width, float height) {
        this.anchor = anchor;
        this.icon = icon;
        this.width = width;
        this.height = height;
    }

    @Override
    public HudAnchor getAnchor() {
        return anchor;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void update(float delta) {
        return;
    }

    @Override
    public void render(@NonNull SpriteBatch batch, @NonNull Rectangle bounds, float delta) {
        batch.draw(icon, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
