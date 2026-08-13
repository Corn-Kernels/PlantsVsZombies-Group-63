package com.cornkernels.engine.renderer.hud.element;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.engine.renderer.hud.HudAnchor;

import java.util.function.BooleanSupplier;

public class ConditionalHudElement implements HudElement {

    private final HudElement delegate;
    private final BooleanSupplier condition;

    public ConditionalHudElement(HudElement delegate, BooleanSupplier condition) {
        this.delegate = delegate;
        this.condition = condition;
    }

    @Override
    public HudAnchor getAnchor() {
        return delegate.getAnchor();
    }

    @Override
    public float getWidth() {
        return delegate.getWidth();
    }

    @Override
    public float getHeight() {
        return delegate.getHeight();
    }

    @Override
    public void update(float delta) {
        if (condition.getAsBoolean()) delegate.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, Rectangle bounds, boolean hovered, boolean pressed, float delta) {
        if (condition.getAsBoolean()) delegate.render(batch, bounds, hovered, pressed, delta);
    }

    @Override
    public boolean handleClick(float hudX, float hudY, Rectangle bounds) {
        return condition.getAsBoolean() && delegate.handleClick(hudX, hudY, bounds);
    }

    @Override
    public boolean isInteractive() {
        return condition.getAsBoolean() && delegate.isInteractive();
    }

    @Override
    public void onHoverEnter() {
        delegate.onHoverEnter();
    }

    @Override
    public void onHoverExit() {
        delegate.onHoverExit();
    }
}
