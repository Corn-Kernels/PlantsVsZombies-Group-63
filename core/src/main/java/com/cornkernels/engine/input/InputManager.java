package com.cornkernels.engine.input;

import com.cornkernels.engine.settings.InputSettings;

public class InputManager {

    private static InputManager instance;
    private InputSettings settings;
    private PlayerActions actions;
    private long frameId = 0;

    private InputManager() {
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public void init(InputSettings settings) {
        this.settings = settings;
        actions = new PlayerActions();
        this.actions.applyBinding(settings);
    }

    public PlayerActions getActions() {
        return actions;
    }

    public void rebind(GameAction action, int newKeyCode) {
        action.setKeyCode(newKeyCode);
        actions.exportBinding(settings);
    }

    public void update() {
        frameId++;
        actions.update(frameId);
    }
}



