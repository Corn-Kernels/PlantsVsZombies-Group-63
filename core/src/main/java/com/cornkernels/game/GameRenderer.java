package com.cornkernels.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.MapData;
import com.cornkernels.game.systems.render.BackgroundRenderSystem;
import com.cornkernels.game.systems.render.PamRenderSystem;
import com.cornkernels.game.systems.render.RenderSystem;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameRenderer {

    private final Field field;
    private final SpriteBatch batch;
    private final List<RenderSystem> systems = new ArrayList<>();

    public GameRenderer(SpriteBatch batch, PamPlayer pamPlayer, MapData mapData, Field field) {
        this.field = field;
        this.batch = batch;

        addSystem(new PamRenderSystem(pamPlayer, batch));
        addSystem(new BackgroundRenderSystem(batch, mapData));
    }

    private void addSystem(RenderSystem system) {
        system.registerSystem(field);
        systems.add(system);
    }

    public void render(float delta) {
        for (RenderSystem system : systems) {
            system.render(delta);
        }
    }
}
