package com.cornkernels.game;

import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.render.RenderSystem;

import java.util.ArrayList;
import java.util.List;

public class GameRenderer {

    private final Field field;
    private final List<RenderSystem> systems = new ArrayList<>();

    public GameRenderer(Field field) {
        this.field = field;
    }

    public void addSystem(RenderSystem system) {
        system.registerSystem(field);
        systems.add(system);
    }

    public void render(float delta) {
        for (RenderSystem system : systems) {
            system.render(delta);
        }
    }

}
