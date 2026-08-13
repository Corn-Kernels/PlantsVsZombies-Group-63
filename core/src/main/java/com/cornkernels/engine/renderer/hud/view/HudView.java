package com.cornkernels.engine.renderer.hud.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.engine.renderer.hud.HudAnchor;

public interface HudView {
    HudAnchor getAnchor();

    float getWidth();

    float getHeight();

    void update(float delta);

    void render(SpriteBatch batch, Rectangle bounds, float delta);

    default boolean isVisible() {
        return true;
    }
}
