package com.cornkernels.game.systems.entity;

import com.cornkernels.game.map.Field;

public abstract class EntitySystem {

    protected Field field;

    public void registerSystem(Field field) {
        this.field = field;
    }

    public void unregisterSystem(Field field) {
        this.field = null;
    }

    public abstract void update(float delta);
}
