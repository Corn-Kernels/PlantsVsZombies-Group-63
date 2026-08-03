package com.cornkernels.game.entities.types.sun;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.sun_specific.SunComponent;

public class SunInstance extends Entity {

    private final SunType sunType;
    private float FALL_DURATION_SECONDS = 5f;

    public SunInstance(SunType sunType, int lane, int column) {
        super();
        this.sunType = sunType;
        add(new PositionComponent(new Vec2d(column, lane)));
        add(new SunComponent(sunType, FALL_DURATION_SECONDS));
    }

    public SunInstance(SunType sunType, int lane, int column, float time) {//added this for sunProducing plants
        super();
        this.sunType = sunType;
        add(new PositionComponent(new Vec2d(column, lane)));
        add(new SunComponent(sunType, FALL_DURATION_SECONDS));
        FALL_DURATION_SECONDS = time;
    }

    public SunType getSunType() {
        return sunType;
    }
}
