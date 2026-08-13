package com.cornkernels.engine.renderer.hud.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.engine.renderer.hud.HudAnchor;
import com.cornkernels.engine.utility.renderer.TextRenderer;

import java.util.function.Supplier;

public class TextView implements HudView {

    private final Supplier<String> textSupplier;
    private final TextRenderer textRenderer;
    private final HudAnchor anchor;
    private final float width;
    private final float height;

    public TextView(
        Supplier<String> textSupplier,
        TextRenderer textRenderer,
        HudAnchor anchor,
        float width,
        float height) {
        this.textSupplier = textSupplier;
        this.textRenderer = textRenderer;
        this.anchor = anchor;
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
    public void render(SpriteBatch batch, Rectangle bounds, float delta) {
        textRenderer.draw(batch, bounds, textSupplier.get());
    }
}
