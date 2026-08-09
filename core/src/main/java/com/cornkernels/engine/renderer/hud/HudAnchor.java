package com.cornkernels.engine.renderer.hud;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class HudAnchor {

    private final Corner corner;
    private final float offsetX;
    private final float offsetY;

    public HudAnchor(Corner corner, float offsetX, float offsetY) {
        this.corner = corner;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Vector2 resolve(Rectangle containerBounds, float elementWidth, float elementHeight) {
        float x;
        float y = switch (corner) {
            case BOTTOM_LEFT -> {
                x = containerBounds.x + offsetX;
                yield containerBounds.y + offsetY;
            }
            case BOTTOM_RIGHT -> {
                x = containerBounds.x + containerBounds.width - offsetX - elementWidth;
                yield containerBounds.y + offsetY;
            }
            case TOP_LEFT -> {
                x = containerBounds.x + offsetX;
                yield containerBounds.y + containerBounds.height - offsetY - elementHeight;
            }
            case TOP_RIGHT -> {
                x = containerBounds.x + containerBounds.width - offsetX - elementWidth;
                yield containerBounds.y + containerBounds.height - offsetY - elementHeight;
            }
            case BOTTOM_CENTER -> {
                x = containerBounds.x + containerBounds.width / 2f + offsetX - elementWidth / 2f;
                yield containerBounds.y + offsetY;
            }
            case TOP_CENTER -> {
                x = containerBounds.x + containerBounds.width / 2f + offsetX - elementWidth / 2f;
                yield containerBounds.y + containerBounds.height - offsetY - elementHeight;
            }
            default -> throw new IllegalStateException("Unknown corner: " + corner);
        };

        return new Vector2(x, y);
    }

    public enum Corner {BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_CENTER, TOP_CENTER}
}
