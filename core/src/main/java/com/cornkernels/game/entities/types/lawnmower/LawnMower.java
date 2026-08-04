package com.cornkernels.game.entities.types.lawnmower;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;

public class LawnMower extends Entity {

    public LawnMower(Vec2d position) {
        add(new PositionComponent(position));
        add(new PamAnimationComponent());
    }
}
