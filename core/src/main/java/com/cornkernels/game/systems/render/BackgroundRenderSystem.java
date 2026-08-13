package com.cornkernels.game.systems.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.game.map.data.MapData;

import java.util.Map;

public class BackgroundRenderSystem extends RenderSystem {

    private final MapData mapData;

    public BackgroundRenderSystem(SpriteBatch batch, MapData mapData) {
        super(batch);
        this.mapData = mapData;
    }

    @Override
    public void render(float delta) {
        for (Map.Entry<String, Rectangle> entry : mapData.backgroundRegions.entrySet()) {
            TextureRegion tex = mapData.backgroundTextures.get(entry.getKey());
            if (tex == null) continue;
            Rectangle rect = entry.getValue();
            batch.draw(tex, rect.x, rect.y, rect.width, rect.height);
        }
    }
}
