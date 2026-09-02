package com.cornkernels.game.entities.types.sun;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.sun_specific.SunComponent;

public class SunInstance extends Entity {

    private final SunType sunType;
    private float FALL_DURATION_SECONDS = 5f;
    private static float NORMAL_FALL_DURATION =5f;

    public SunInstance(SunType sunType, int lane, int column) {
        this(sunType,lane,column,NORMAL_FALL_DURATION);
    }

    public SunInstance(SunType sunType, float lane, float column, float time) {
        super();
        this.sunType = sunType;
        add(new PositionComponent(new Vec2d(column, lane)));
        add(new SunComponent(sunType, FALL_DURATION_SECONDS));
        add(new PamAnimationComponent());
        FALL_DURATION_SECONDS = time;
        if(this.sunType==SunType.RADIOACTIVE){
            this.get(PamAnimationComponent.class).tint.set(0.8f, 0.2f, 0.8f, 1f);
        }
    }

    public SunType getSunType() {
        return sunType;
    }
}
