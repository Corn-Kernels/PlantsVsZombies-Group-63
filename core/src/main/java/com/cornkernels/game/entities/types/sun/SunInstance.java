package com.cornkernels.game.entities.types.sun;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.sun_specific.SunComponent;

public class SunInstance extends Entity {

    private final SunType sunType;

    // Sky sun constructor (Spawns above the screen and falls slowly to the target lane)
    public SunInstance(SunType sunType, float targetLane, float column) {
        this(sunType, 8.0f, column, targetLane); // Start high at Y=8.0
        this.get(SunComponent.class).state = SunComponent.State.FALLING;
    }

    // Plant sun constructor (Spawns at the plant's position to prepare for the hop)
    public SunInstance(SunType sunType, float startY, float startX, float endY) {
        super();
        this.sunType = sunType;
        add(new PositionComponent(new Vec2d(startX, startY)));
        add(new SunComponent(sunType, endY));
        add(new PamAnimationComponent());

        if (this.sunType == SunType.RADIOACTIVE) {
            this.get(PamAnimationComponent.class).tint.set(0.8f, 0.2f, 0.8f, 1f);
        }
    }

    public SunType getSunType() {
        return sunType;
    }
}
