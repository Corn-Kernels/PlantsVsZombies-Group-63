package com.cornkernels.game.entities.types.lawnmower;

import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;

public class LawnMower extends Entity {

    private final Rectangle worldBounds;

    public LawnMower(Vec2d position, Rectangle worldBounds) {
        add(new PositionComponent(position));
        add(new PamAnimationComponent());
        this.worldBounds = worldBounds;
    }

    public Rectangle getWorldBounds() {
        return worldBounds;
    }
}
