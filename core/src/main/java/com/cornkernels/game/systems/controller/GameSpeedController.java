package com.cornkernels.game.systems.controller;

public class GameSpeedController {

    private static final float NORMAL_SPEED = 1f;

    private float fastForwardSpeed = 2f;

    private boolean fastForward = false;

    public void setFastForwardSpeed(float fastForwardSpeed) {
        this.fastForwardSpeed = fastForwardSpeed;
    }

    public boolean isFastForward() {
        return fastForward;
    }

    public void toggleFastForward() {
        fastForward = !fastForward;
    }

    public float getSpeedMultiplier() {
        return fastForward ? fastForwardSpeed : NORMAL_SPEED;
    }
}
