package com.cornkernels.game.entities.components.zombie_specific;

public class ZombieDeathComponent {

    public static final float FADE_DURATION = 0.6f;
    public boolean deathClipStarted = false;
    public float deathClipDuration = 0f;
    public float elapsed = 0f;

    public boolean isFading() {
        return elapsed >= deathClipDuration;
    }

    public float getAlpha() {
        if (!isFading()) return 1f;
        float fadeElapsed = elapsed - deathClipDuration;
        return Math.max(0f, 1f - fadeElapsed / FADE_DURATION);
    }

    public boolean isFadeComplete() {
        return elapsed >= deathClipDuration + FADE_DURATION;
    }
}
