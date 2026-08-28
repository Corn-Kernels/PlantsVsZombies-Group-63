package com.cornkernels.game.entities.components.sun_specific;

import com.cornkernels.game.entities.types.sun.SunType;

public class SunComponent {

    public final float fallDuration;
    public SunType type;
    public State state = State.FALLING;
    public float fallElapsed = 0f;

    public SunComponent(SunType type, float fallDuration) {
        this.type = type;
        this.fallDuration = fallDuration;
    }

    public float fallProgress() {
        if (fallDuration <= 0f) return 1f;
        return Math.clamp(fallElapsed / fallDuration, 0f, 1f);
    }

    public enum State {FALLING, LANDED}
}
