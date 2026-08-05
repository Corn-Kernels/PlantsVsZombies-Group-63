package com.cornkernels.game.systems.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.game.map.Field;

public abstract class RenderSystem {

    protected final SpriteBatch batch;
    protected Field field;

    public RenderSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    public void registerSystem(Field field) {
        this.field = field;
    }

    public void unregisterSystem(Field field) {
        this.field = null;
    }

    public abstract void render(float delta);
}
