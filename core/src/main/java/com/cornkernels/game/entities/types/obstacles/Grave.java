package com.cornkernels.game.entities.types.obstacles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;

public class Grave extends AbstractObstacle {

    public Grave(Vec2d position) {
        super(position);
        add(new HealthComponent());
    }
}
