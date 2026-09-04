package com.cornkernels.game.entities.types.obstacles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PamAnimationComponent;

public class Redirector extends AbstractObstacle {

    public final Direction direction;

    public Redirector(Vec2d position, Direction direction) {
        super(position);
        this.direction = direction;
        add(new PamAnimationComponent());
    }

    public enum Direction {
        UP, DOWN
    }
}
