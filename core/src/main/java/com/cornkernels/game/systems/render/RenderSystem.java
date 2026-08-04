package com.cornkernels.game.systems.render;

import com.cornkernels.game.map.Field;

public abstract class RenderSystem {

    protected Field field;

    public void registerSystem(Field field) {
        this.field = field;
    }

    public void unregisterSystem(Field field) {
        this.field = null;
    }

    public abstract void render(float delta);
}
