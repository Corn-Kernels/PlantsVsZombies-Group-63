package com.cornkernels.game.entities.types.obstacles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;

public class AbstractObstacle extends Entity {
    protected final Vec2d position;

    public AbstractObstacle(Vec2d position) {
        this.position = position;
    }
}
