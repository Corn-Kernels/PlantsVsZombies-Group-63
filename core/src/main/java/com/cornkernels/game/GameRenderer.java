package com.cornkernels.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.game.hud.cursor.CursorToolState;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.systems.render.*;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameRenderer {

    private final Field field;
    private final SpriteBatch batch;
    private final List<RenderSystem> systems = new ArrayList<>();

    public GameRenderer(
        SpriteBatch batch,
        PamPlayer pamPlayer,
        MapData mapData,
        Field field,
        CursorToolState toolState) {
        this.field = field;
        this.batch = batch;


        addSystem(new BackgroundRenderSystem(batch, mapData));
        addSystem(new PamRenderSystem(batch, pamPlayer, mapData));
        addSystem(new HighlightRenderSystem(batch, pamPlayer, mapData, toolState));
        addSystem(new CursorAttachmentRenderSystem(batch, mapData, toolState));
    }

    private void addSystem(@NonNull RenderSystem system) {
        system.registerSystem(field);
        systems.add(system);
    }

    public void render(float delta) {
        for (RenderSystem system : systems) {
            system.render(delta);
        }
    }
}
