package com.cornkernels.game.entities.types.obstacles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;

public class AbstractObstacle extends Entity {

    public AbstractObstacle(Vec2d position) {
        add(new PositionComponent(position));
        add(new PamAnimationComponent());
    }
}
