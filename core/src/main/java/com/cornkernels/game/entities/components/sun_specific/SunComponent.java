package com.cornkernels.game.entities.components.sun_specific;

import com.cornkernels.game.entities.types.sun.SunType;

public class SunComponent {

    public SunType type;
    public State state = State.FALLING;
    public float endY;
    public float velocityX = 0f;
    public float velocityY = 0f;

    public SunComponent(SunType type, float endY) {
        this.type = type;
        this.endY = endY;
    }

    public enum State {FALLING, SUN_FLOWER, LANDED}
}
