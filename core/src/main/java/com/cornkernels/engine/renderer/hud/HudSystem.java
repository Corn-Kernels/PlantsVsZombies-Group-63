package com.cornkernels.engine.renderer.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cornkernels.game.hud.cursor.InputSnapshot;
import com.cornkernels.game.utility.DebugTextures;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HudSystem {

    private static final boolean DEBUG_HUD_BOUNDS = false;
    private final TextureRegion debugPixelTexture;

    private final List<HudElement> elements = new ArrayList<>();
    private final HudCamera hudCamera;
    private HudElement hoveredElement;

    public HudSystem(HudCamera hudCamera) {
        this.hudCamera = hudCamera;
        this.debugPixelTexture = new TextureRegion(DebugTextures.createWhitePixel());
    }

    public void addElement(HudElement element) {
        elements.add(element);
    }

    public boolean update(float delta, @NonNull InputSnapshot inputSnapshot) {
        Vector2 hudPoint = hudCamera.screenToHud(inputSnapshot.cursorScreenX(), inputSnapshot.cursorScreenY());
        HudElement topmost = resolveTopmostAt(hudPoint.x, hudPoint.y);

        if (topmost != hoveredElement) {
            if (hoveredElement != null) hoveredElement.onHoverExit();
            if (topmost != null) topmost.onHoverEnter();
            hoveredElement = topmost;
        }

        for (HudElement element : elements) element.update(delta);

        if (!inputSnapshot.confirmPressed() || topmost == null) return false;
        topmost.handleClick(hudPoint.x, hudPoint.y, resolveBounds(topmost));
        return true;
    }

    private @Nullable HudElement resolveTopmostAt(float x, float y) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            HudElement element = elements.get(i);
            if (!element.isInteractive()) continue;
            Rectangle bounds = resolveBounds(element);
            if (bounds.contains(x, y)) return element;
        }
        return null;
    }

    public void render(SpriteBatch batch, float delta) {
        Rectangle virtualBounds = hudCamera.getVirtualBounds();
        for (HudElement element : elements) {
            Rectangle bounds = resolveBounds(element, virtualBounds);
            element.render(batch, bounds, element == hoveredElement, delta);
        }

        if (DEBUG_HUD_BOUNDS) {
            for (HudElement element : elements) {
                Rectangle bounds = resolveBounds(element);
                batch.setColor(1f, 0f, 0f, 0.3f);
                batch.draw(debugPixelTexture, bounds.x, bounds.y, bounds.width, bounds.height);
            }
            batch.setColor(Color.WHITE);
        }
    }

    private @NonNull Rectangle resolveBounds(@NonNull HudElement element, Rectangle virtualBounds) {
        Vector2 anchorPoint = element.getAnchor().resolve(
            virtualBounds, element.getWidth(), element.getHeight());
        return new Rectangle(anchorPoint.x, anchorPoint.y, element.getWidth(), element.getHeight());
    }

    private @NonNull Rectangle resolveBounds(@NonNull HudElement element) {
        Vector2 anchorPoint = element.getAnchor().resolve(
            hudCamera.getVirtualBounds(), element.getWidth(), element.getHeight());
        return new Rectangle(anchorPoint.x, anchorPoint.y, element.getWidth(), element.getHeight());
    }

    public List<HudElement> getElements() {
        return elements;
    }
}
