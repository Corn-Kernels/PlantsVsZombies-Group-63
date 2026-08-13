package com.cornkernels.engine.renderer.hud.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cornkernels.engine.renderer.hud.HudAnchor;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class CompositeHudView implements HudView {

    private final HudAnchor anchor;
    private final float width;
    private final float height;
    private final List<HudView> childern = new ArrayList<>();
    private BooleanSupplier visibleWhen = () -> true;

    public CompositeHudView(HudAnchor anchor, float width, float height) {
        this.anchor = anchor;
        this.width = width;
        this.height = height;
    }

    public CompositeHudView addChild(HudView child) {
        childern.add(child);
        return this;
    }

    public CompositeHudView visibleWhen(BooleanSupplier visibleWhen) {
        this.visibleWhen = visibleWhen;
        return this;
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
        if (!isVisible()) return;
        for (HudView child : childern) child.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, Rectangle bounds, float delta) {
        if (!isVisible()) return;
        for (HudView child : childern) {
            if (!child.isVisible()) continue;
            Rectangle childBounds = resolveChildBounds(child, bounds);
            child.render(batch, childBounds, delta);
        }
    }

    private @NonNull Rectangle resolveChildBounds(@NonNull HudView child, Rectangle containerBounds) {
        Vector2 anchorPoint = child.getAnchor().resolve(containerBounds, child.getWidth(), child.getHeight());
        return new Rectangle(anchorPoint.x, anchorPoint.y, child.getWidth(), child.getHeight());
    }
}
