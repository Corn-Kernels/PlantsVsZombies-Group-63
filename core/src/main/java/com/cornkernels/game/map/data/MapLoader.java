package com.cornkernels.game.map.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.game.map.grid.GridObject;
import com.cornkernels.game.map.grid.GridPosition;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class MapLoader {

    private static final String BACKGROUND_LAYER = "BackgroundObjects";
    private static final String GRID_LAYER = "GridObjects";
    private static final String LAWNMOWER_LAYER = "LawnMowerObjects";

    private final TmxMapLoader tmxMapLoader = new TmxMapLoader();

    private static float laneRowHeight(@NonNull Map<GridPosition, Rectangle> cellRects) {
        float height = 0f;
        for (Rectangle rect : cellRects.values()) {
            height = Math.max(height, rect.height);
        }
        return height;
    }

    private static void shiftDownOneLane(@NonNull Collection<Rectangle> rects, float laneShift) {
        for (Rectangle rect : rects) {
            rect.y -= laneShift;
        }
    }

    public MapData load(@NonNull MapDefinition mapDef) {
        TiledMap tiledMap = tmxMapLoader.load(mapDef.getLayoutTmxPath());
        float mapHeightPx = tiledMap.getProperties().get("height", Integer.class)
            * tiledMap.getProperties().get("tileheight", Integer.class);

        Map<GridPosition, Rectangle> cellRects = parsRectLayer(tiledMap, GRID_LAYER, mapHeightPx);
        float laneShift = laneRowHeight(cellRects);
        shiftDownOneLane(cellRects.values(), laneShift);

        GridObject[][] grid = buildGrid(cellRects);
        Rectangle[][] cellBounds = buildBoundsArray(cellRects, grid.length, grid[0].length);

        List<LawnMowerSlot> lawnMowerSlots = parseLawnMowerSlots(tiledMap, mapHeightPx);
        for (LawnMowerSlot slot : lawnMowerSlots) {
            slot.getBounds().y -= laneShift;
        }
        Map<String, Rectangle> backgroundRegions = parseBackgroundRegions(tiledMap, mapHeightPx);
        Map<String, TextureRegion> backgroundTextures = resolveSkin(mapDef.getSkin(), backgroundRegions.keySet());
        tiledMap.dispose();
        return new MapData(grid, cellBounds, lawnMowerSlots, backgroundRegions, backgroundTextures);
    }

    private @NonNull Map<GridPosition, Rectangle> parsRectLayer(@NonNull TiledMap map, String layerName, float mapHeightPx) {
        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) {
            throw new IllegalStateException("Missing object layer '" + layerName + "' in tmx.");
        }

        Map<GridPosition, Rectangle> result = new HashMap<>();
        for (MapObject obj : layer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;

            MapProperties props = obj.getProperties();
            Integer lane = props.get("lane", Integer.class);
            if (lane == null)
                throw new IllegalStateException("Missing lane property in '" + obj.getName() + "' in tmx.");
            Integer column = props.get("column", Integer.class);
            if (column == null) throw new IllegalStateException("Missing column in '" + obj.getName() + "' in tmx.");
            Rectangle tiledRect = ((RectangleMapObject) obj).getRectangle();

            Rectangle worldRect = new Rectangle(
                tiledRect.x,
                mapHeightPx - tiledRect.y - tiledRect.height,
                tiledRect.width,
                tiledRect.height
            );

            result.put(new GridPosition(lane, column), worldRect);
        }
        return result;
    }

    private GridObject @NonNull [][] buildGrid(@NonNull Map<GridPosition, Rectangle> cellRects) {
        int maxLane = 0;
        int maxColumn = 0;
        for (GridPosition pos : cellRects.keySet()) {
            maxLane = Math.max(maxLane, pos.lane());
            maxColumn = Math.max(maxColumn, pos.column());
        }
        GridObject[][] grid = new GridObject[maxLane + 1][maxColumn + 1];
        for (GridPosition pos : cellRects.keySet()) {
            grid[pos.lane()][pos.column()] = new GridObject(pos);
        }
        return grid;
    }

    private Rectangle @NonNull [][] buildBoundsArray(@NonNull Map<GridPosition, Rectangle> cellRects, int lanes, int columns) {
        Rectangle[][] bounds = new Rectangle[lanes][columns];
        for (Map.Entry<GridPosition, Rectangle> entry : cellRects.entrySet()) {
            bounds[entry.getKey().lane()][entry.getKey().column()] = entry.getValue();
        }
        return bounds;
    }

    private @NonNull List<LawnMowerSlot> parseLawnMowerSlots(@NonNull TiledMap map, float mapHeightPx) {
        MapLayer layer = map.getLayers().get(LAWNMOWER_LAYER);
        List<LawnMowerSlot> slots = new ArrayList<>();
        if (layer == null) return slots;

        for (MapObject obj : layer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            int lane = obj.getProperties().get("lane", Integer.class);
            Rectangle r = ((RectangleMapObject) obj).getRectangle();
            Rectangle worldRect = new Rectangle(r.x, mapHeightPx - r.y - r.height, r.width, r.height);
            slots.add(new LawnMowerSlot(lane, worldRect));
        }
        return slots;
    }

    private @NonNull Map<String, Rectangle> parseBackgroundRegions(@NonNull TiledMap map, float mapHeightPx) {
        Map<String, Rectangle> regions = new HashMap<>();
        MapLayer layer = map.getLayers().get(BACKGROUND_LAYER);
        if (layer == null) return regions;
        for (MapObject obj : layer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;
            String part = obj.getProperties().get("part", String.class);
            if (part == null) throw new IllegalStateException(
                "Missing part property in '" + obj.getName() + "' in map.");
            Rectangle r = ((RectangleMapObject) obj).getRectangle();
            regions.put(part, new Rectangle(r.x, mapHeightPx - r.y - r.height, r.width, r.height));
        }
        return regions;
    }

    private @NonNull Map<String, TextureRegion> resolveSkin(@NonNull MapSkin skin, @NonNull Set<String> partNames) {
        TextureAtlas atlas = new TextureAtlas(skin.getAtlasPath());

        Map<String, TextureRegion> textures = new HashMap<>();
        for (String part : partNames) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(part);
            if (region == null) {
                Gdx.app.error("MapLoader", "Could not find texture atlas region: " + part);
                continue;
            }
            textures.put(part, new TextureRegion(region));
        }
        return textures;
    }
}
