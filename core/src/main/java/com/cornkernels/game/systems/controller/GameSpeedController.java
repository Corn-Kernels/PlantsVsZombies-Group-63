package com.cornkernels.game.systems.controller;

public class GameSpeedController {

    private static final float NORMAL_SPEED = 1f;
    private static final float FAST_FORWARD_SPEED = 2f;

    private boolean fastForward = false;

    public boolean isFastForward() {
        return fastForward;
    }

    public void toggleFastForward() {
        fastForward = !fastForward;
    }

    public float getSpeedMultiplier() {
        return fastForward ? FAST_FORWARD_SPEED : NORMAL_SPEED;
    }
}
