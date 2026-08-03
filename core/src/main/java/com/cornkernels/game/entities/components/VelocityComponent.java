package com.cornkernels.game.entities.components;

import com.cornkernels.engine.utility.math.Vec2d;

public class VelocityComponent {

    public Vec2d velocityPerTick;

    public VelocityComponent(Vec2d velocityPerTick) {
        this.velocityPerTick = velocityPerTick;
    }
}
