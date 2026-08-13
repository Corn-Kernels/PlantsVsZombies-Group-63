package com.cornkernels.engine.renderer.hud.element;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cornkernels.engine.renderer.hud.HudAnchor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HudGroup {

    private final HudAnchor anchor;
    private final float width;
    private final float height;
    private final TextureRegion panelBackground;

    private final List<HudElement> children = new ArrayList<>();
    private boolean open = false;
    private HudElement hoveredChild;
    private HudElement pressedChild;

    public HudGroup(HudAnchor anchor, float width, float height, @Nullable TextureRegion panelBackground) {
        this.anchor = anchor;
        this.width = width;
        this.height = height;
        this.panelBackground = panelBackground;
    }

    public void update(float delta) {
        if (!open) return;
        for (HudElement child : children) child.update(delta);
    }

    public void render(SpriteBatch batch, Rectangle panelBounds, float delta) {
        if (!open) return;

        if (panelBackground != null) {
            batch.draw(panelBackground, panelBounds.x, panelBounds.y, panelBounds.width, panelBounds.height);
        }

        for (HudElement child : children) {
            Rectangle childBounds = resolveChildBounds(child, panelBounds);
            boolean isPressed = child == pressedChild && child == hoveredChild;
            child.render(batch, childBounds, child == hoveredChild, isPressed, delta);
        }
    }

    public void updateHover(float hudX, float hudY, Rectangle panelBounds) {
        HudElement topmost = null;
        for (int i = children.size() - 1; i >= 0; i--) {
            HudElement child = children.get(i);
            if (!child.isInteractive()) continue;
            if (resolveChildBounds(child, panelBounds).contains(hudX, hudY)) {
                topmost = child;
                break;
            }
        }
        if (topmost != hoveredChild) {
            if (hoveredChild != null) hoveredChild.onHoverExit();
            if (topmost != null) topmost.onHoverEnter();
            hoveredChild = topmost;
        }
    }

    public boolean handleClick(float hudX, float hudY, Rectangle panelBounds) {
        for (int i = children.size() - 1; i >= 0; i--) {
            HudElement child = children.get(i);
            if (!child.isInteractive()) continue;
            Rectangle childBounds = resolveChildBounds(child, panelBounds);
            if (child.handleClick(hudX, hudY, childBounds)) {
                pressedChild = child;
                return true;
            }
        }
        return true;
    }

    private @NonNull Rectangle resolveChildBounds(@NonNull HudElement child, Rectangle panelBounds) {
        Vector2 anchorPoint = child.getAnchor().resolve(panelBounds, child.getWidth(), child.getHeight());
        return new Rectangle(anchorPoint.x, anchorPoint.y, child.getWidth(), child.getHeight());
    }


    public HudGroup addChild(HudElement hudElement) {
        children.add(hudElement);
        return this;
    }

    public void open() {
        open = true;
    }

    public void close() {
        open = false;
        hoveredChild = null;
    }

    public boolean isOpen() {
        return open;
    }

    public HudAnchor getAnchor() {
        return anchor;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
