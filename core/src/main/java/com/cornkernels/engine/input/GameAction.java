package com.cornkernels.engine.input;

import com.badlogic.gdx.Gdx;

public class GameAction {

    private final String name;
    private long lastPolledFrame = -1;
    private int keyCode;

    private boolean currentlyPressed;
    private boolean previouslyPressed;

    public GameAction(String name, int defaultKeyCode) {
        this.name = name;
        this.keyCode = defaultKeyCode;
    }

    void poll(long frameId) {
        if (frameId == lastPolledFrame) return;
        lastPolledFrame = frameId;
        previouslyPressed = currentlyPressed;
        currentlyPressed = Gdx.input.isKeyPressed(keyCode);
    }

    public boolean isPressed() {
        return currentlyPressed;
    }

    public boolean wasPressed() {
        return currentlyPressed && !previouslyPressed;
    }

    public boolean wasReleased() {
        return !currentlyPressed && previouslyPressed;
    }

    public String getName() {
        return name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
