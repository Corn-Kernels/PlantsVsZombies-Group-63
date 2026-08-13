package com.cornkernels.engine.renderer.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cornkernels.engine.renderer.hud.element.ConditionalHudElement;
import com.cornkernels.engine.renderer.hud.element.HudElement;
import com.cornkernels.engine.renderer.hud.element.HudGroup;
import com.cornkernels.engine.renderer.hud.view.HudView;
import com.cornkernels.game.utility.DebugTextures;
import com.cornkernels.engine.utility.InputSnapshot;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class HudSystem {

    private static final boolean DEBUG_HUD_BOUNDS = false;
    private final TextureRegion debugPixelTexture;

    private final List<HudElement> elements = new ArrayList<>();
    private final List<HudGroup> menus = new ArrayList<>();
    private final List<HudView> views = new ArrayList<>();
    private final HudCamera hudCamera;
    private HudElement hoveredElement;
    private HudElement pressedElement;

    public HudSystem(HudCamera hudCamera) {
        this.hudCamera = hudCamera;
        this.debugPixelTexture = new TextureRegion(DebugTextures.createWhitePixel());
    }

    public void addElement(HudElement element) {
        elements.add(element);
    }

    public void addElement(HudElement element, BooleanSupplier visibleWhen) {
        elements.add(new ConditionalHudElement(element, visibleWhen));
    }

    public void addView(HudView view) {
        views.add(view);
    }

    public void addMenu(HudGroup menu) {
        menus.add(menu);
    }

    public boolean update(float delta, @NonNull InputSnapshot inputSnapshot) {
        HudGroup activeModal = findOpenMenu();
        Vector2 hudPoint = hudCamera.screenToHud(inputSnapshot.cursorScreenX(), inputSnapshot.cursorScreenY());

        for (HudView view : views) {
            if (view.isVisible()) view.update(delta);
        }

        if (activeModal != null) {
            if (hoveredElement != null) {
                hoveredElement.onHoverExit();
                hoveredElement = null;
            }

            Rectangle panelBounds = resolveMenuBounds(activeModal);
            activeModal.updateHover(hudPoint.x, hudPoint.y, panelBounds);
            activeModal.update(delta);

            if (inputSnapshot.confirmPressed()) {
                activeModal.handleClick(hudPoint.x, hudPoint.y, panelBounds);
            }
            return true;
        }

        HudElement topmost = resolveTopmostAt(hudPoint.x, hudPoint.y);
        if (topmost != hoveredElement) {
            if (hoveredElement != null) hoveredElement.onHoverExit();
            if (topmost != null) topmost.onHoverEnter();
            hoveredElement = topmost;
        }

        if (inputSnapshot.pointerDown()) {
            if (pressedElement == null && topmost != null) {
                pressedElement = topmost;
            }
        } else {
            pressedElement = null;
        }

        for (HudElement element : elements) element.update(delta);

        if (!inputSnapshot.confirmPressed() || topmost == null) return false;
        topmost.handleClick(hudPoint.x, hudPoint.y, resolveBounds(topmost));
        return true;
    }

    public void render(SpriteBatch batch, float delta) {
        Rectangle virtualBounds = hudCamera.getVirtualBounds();

        for (HudView view : views) {
            if (view.isVisible()) view.render(batch, resolveViewBounds(view), delta);
        }

        for (HudElement element : elements) {
            Rectangle bounds = resolveBounds(element, virtualBounds);
            boolean isPressed = element == pressedElement && element == hoveredElement;
            element.render(batch, bounds, element == hoveredElement, isPressed, delta);
        }

        HudGroup activeModal = findOpenMenu();
        if (activeModal != null) {
            renderBackdrop(batch);
            activeModal.render(batch, resolveMenuBounds(activeModal), delta);
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

    private @NonNull Rectangle resolveViewBounds(@NonNull HudView view) {
        Vector2 anchorPoint = view.getAnchor().resolve(hudCamera.getVirtualBounds(), view.getWidth(), view.getHeight());
        return new Rectangle(anchorPoint.x, anchorPoint.y, view.getWidth(), view.getHeight());
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

    private @Nullable HudGroup findOpenMenu() {
        for (HudGroup menu : menus) if (menu.isOpen()) return menu;
        return null;
    }

    private @NonNull Rectangle resolveMenuBounds(@NonNull HudGroup menu) {
        Vector2 anchorPoint = menu.getAnchor().resolve(hudCamera.getVirtualBounds(), menu.getWidth(), menu.getHeight());
        return new Rectangle(anchorPoint.x, anchorPoint.y, menu.getWidth(), menu.getHeight());
    }

    private void renderBackdrop(@NonNull SpriteBatch batch) {
        Rectangle bounds = hudCamera.getVirtualBounds();
        batch.setColor(0f, 0f, 0f, 0.55f);
        batch.draw(debugPixelTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
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
