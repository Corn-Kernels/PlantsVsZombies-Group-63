package com.cornkernels.engine.renderer.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public interface HudElement {
    HudAnchor getAnchor();

    float getWidth();

    float getHeight();

    void update(float delta);

    void render(SpriteBatch batch, Rectangle bounds, boolean hovered, float delta);

    boolean handleClick(float hudX, float hudY, Rectangle bounds);

    default boolean isInteractive() {
        return true;
    }

    default void onHoverEnter() {
    }

    default void onHoverExit() {
    }
}
