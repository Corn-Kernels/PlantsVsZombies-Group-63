package com.cornkernels.game.systems.controller;

public class PauseController {

    private boolean paused = false;

    public boolean isPaused() {
        return paused;
    }

    public void togglePause() {
        paused = !paused;
    }

    public void resume() {
        paused = false;
    }
}
