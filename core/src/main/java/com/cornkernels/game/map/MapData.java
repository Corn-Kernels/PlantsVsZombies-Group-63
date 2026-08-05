package com.cornkernels.game.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.game.map.grid.GridObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MapData {
    public final GridObject[][] grid;
    public final Rectangle[][] cellBounds;
    public final List<LawnMowerSlot> lawnMowerSlots;
    public final Map<String, Rectangle> backgroundRegions;
    public final Map<String, TextureRegion> backgroundTextures;

    private final Rectangle worldBounds;

    public MapData(
        GridObject[][] grid,
        Rectangle[][] cellBounds,
        List<LawnMowerSlot> lawnMowerSlots,
        Map<String, Rectangle> backgroundRegions,
        Map<String, TextureRegion> backgroundTextures) {
        this.grid = grid;
        this.cellBounds = cellBounds;
        this.lawnMowerSlots = lawnMowerSlots;
        this.backgroundRegions = backgroundRegions;
        this.backgroundTextures = backgroundTextures;

        this.worldBounds = computeWorldBounds(backgroundRegions.values());
    }

    private Rectangle computeWorldBounds(Collection<Rectangle> rectangles) {
        Rectangle bounds = null;
        for (Rectangle rectangle : rectangles) {
            if (bounds == null) {
                bounds = new Rectangle(rectangle);
            } else {
                bounds.merge(rectangle);
            }
        }
        if (bounds == null) {
            for (Rectangle[] row : cellBounds) {
                for (Rectangle rectangle : row) {
                    if (rectangle == null) continue;
                    if (bounds == null) {
                        bounds = new Rectangle(rectangle);
                    } else {
                        bounds.merge(rectangle);
                    }
                }
            }
        }
        if (bounds == null) {
            throw new IllegalStateException(
                "MapData has no backgroundRegions or cellBounds to compute world bounds from");
        }
        return bounds;
    }

    public Rectangle getWorldBounds() {
        return worldBounds;
    }
}
