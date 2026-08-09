package com.cornkernels.engine.renderer.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class HudButton implements HudElement {

    private final HudAnchor anchor;
    private final Supplier<TextureRegion> iconSupplier;
    private final Supplier<TextureRegion> hovredIconSupplier;
    private final Runnable onClick;
    private final Runnable hoverEnterAction;
    private final Runnable hoverExitAction;
    private final BooleanSupplier enabled;
    private final BooleanSupplier visible;
    private final Color hoverTint;

    private final float width;
    private final float height;

    @Contract(pure = true)
    protected HudButton(@NonNull Builder builder) {
        this.anchor = builder.anchor;
        this.width = builder.width;
        this.height = builder.height;
        this.iconSupplier = builder.iconSupplier;
        this.onClick = builder.onClick;
        this.enabled = builder.enabled;
        this.visible = builder.visible;
        this.hoverEnterAction = builder.hoverEnterAction;
        this.hoverExitAction = builder.hoverExitAction;
        this.hovredIconSupplier = builder.hovredIconSupplier;
        this.hoverTint = builder.hoverTint;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NonNull Builder builder(HudAnchor anchor, float width, float height) {
        return new Builder(anchor, width, height);
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
    }

    @Override
    public void render(SpriteBatch batch, Rectangle bounds, boolean hovered, float delta) {
        if (!visible.getAsBoolean()) return;

        TextureRegion icon = iconSupplier.get();
        if (icon != null) {
            if (!enabled.getAsBoolean()) {
                batch.setColor(0.5f, 0.5f, 0.5f, 1f);
            }
            TextureRegion hoveredIcon = hovredIconSupplier.get();

            if (hovered && hoveredIcon != null) {
                batch.draw(hoveredIcon, bounds.x, bounds.y, bounds.width, bounds.height);
            } else if (hovered && hoverTint != null) {
                batch.setColor(hoverTint);
                batch.draw(icon, bounds.x, bounds.y, bounds.width, bounds.height);
            } else {
                batch.draw(icon, bounds.x, bounds.y, bounds.width, bounds.height);
            }
            batch.setColor(Color.WHITE);
        }
        renderOverlay(batch, bounds, delta);
    }

    protected void renderOverlay(SpriteBatch batch, Rectangle bounds, float delta) {
    }

    @Override
    public boolean handleClick(float hudX, float hudY, Rectangle bounds) {
        if (!visible.getAsBoolean() || !enabled.getAsBoolean()) return false;
        if (!bounds.contains(hudX, hudY)) return false;
        onClick.run();
        return true;
    }

    @Override
    public boolean isInteractive() {
        return visible.getAsBoolean();
    }

    @Override
    public void onHoverEnter() {
        if (hoverEnterAction != null) hoverEnterAction.run();
    }

    @Override
    public void onHoverExit() {
        if (hoverExitAction != null) hoverExitAction.run();
    }

    public static class Builder {

        private final HudAnchor anchor;
        private final float width;
        private final float height;
        private Supplier<TextureRegion> iconSupplier = () -> null;
        private Supplier<TextureRegion> hovredIconSupplier = () -> null;
        private Runnable onClick = () -> {
        };
        private Runnable hoverEnterAction = () -> {
        };
        private Runnable hoverExitAction = () -> {
        };
        private BooleanSupplier enabled = () -> true;
        private BooleanSupplier visible = () -> true;
        private Color hoverTint;

        private Builder(HudAnchor anchor, float width, float height) {
            this.anchor = anchor;
            this.width = width;
            this.height = height;
        }

        public Builder hoverTint(Color tint) {
            this.hoverTint = tint;
            return this;
        }

        public Builder icon(TextureRegion staticIcon) {
            this.iconSupplier = () -> staticIcon;
            return this;
        }

        public Builder icon(Supplier<TextureRegion> dynamicIcon) {
            this.iconSupplier = dynamicIcon;
            return this;
        }

        public Builder hoverIcon(TextureRegion hoveredIcon) {
            this.hovredIconSupplier = () -> hoveredIcon;
            return this;
        }

        public Builder hoverIcon(Supplier<TextureRegion> hoveredIcon) {
            this.hovredIconSupplier = hoveredIcon;
            return this;
        }

        public Builder onClick(Runnable onClick) {
            this.onClick = onClick;
            return this;
        }

        public Builder hoverEnterAction(Runnable hoverEnterAction) {
            this.hoverEnterAction = hoverEnterAction;
            return this;
        }

        public Builder hoverExitAction(Runnable hoverExitAction) {
            this.hoverExitAction = hoverExitAction;
            return this;
        }

        public Builder enabledWhen(BooleanSupplier enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder visibleWhen(BooleanSupplier visible) {
            this.visible = visible;
            return this;
        }

        public HudButton build() {
            return new HudButton(this);
        }
    }
}
