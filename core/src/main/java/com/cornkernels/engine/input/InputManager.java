package com.cornkernels.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.cornkernels.engine.settings.GameSettings;

public class InputManager {

    private static InputManager instance;
    private final IntMap<GameAction> keyBindings = new IntMap<>();
    private final IntMap<GameAction> mouseBindings = new IntMap<>();
    private final ObjectMap<GameAction, Boolean> pressedStates = new ObjectMap<>();
    private final ObjectMap<GameAction, Boolean> justPressedStates = new ObjectMap<>();
    private final ObjectMap<GameAction, Boolean> justReleasedStates = new ObjectMap<>();
    private final ObjectMap<GameAction, Boolean> previousPressedStates = new ObjectMap<>();
    private GameSettings settings;

    private InputManager() {
    }

    public static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public void init(GameSettings settings) {
        this.settings = settings;
        resetStates();
        setupDefaultBindings();
    }

    private void resetStates() {
        for (GameAction action : GameAction.values()) {
            pressedStates.put(action, false);
            justPressedStates.put(action, false);
            justReleasedStates.put(action, false);
            previousPressedStates.put(action, false);
        }
    }

    private void setupDefaultBindings() {
    }

    public void bindKey(int keycode, GameAction action) {
        keyBindings.put(keycode, action);
    }

    public void bindMouse(int button, GameAction action) {
        mouseBindings.put(button, action);
    }

    public boolean isPressed(GameAction action) {
        return pressedStates.get(action, false);
    }

    public boolean isPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    public boolean isJustPressed(GameAction action) {
        return justPressedStates.get(action, false);
    }

    public boolean isJustPressed(int keycode) {
        return Gdx.input.isKeyJustPressed(keycode);
    }

    public boolean isReleased(GameAction action) {
        return justReleasedStates.get(action, false);
    }

    public int getKeyFor(GameAction action) {
        for (IntMap.Entry<GameAction> entry : keyBindings.entries()) {
            if (entry.value == action) return entry.key;
        }
        return -1;
    }

    public void resetToDefaults() {
        keyBindings.clear();
        mouseBindings.clear();
        setupDefaultBindings();
    }

    public void update() {

        for (GameAction action : GameAction.values()) {
            previousPressedStates.put(action, pressedStates.get(action, false));
        }

        for (GameAction action : GameAction.values()) {
            pressedStates.put(action, false);
            justPressedStates.put(action, false);
            justReleasedStates.put(action, false);
        }

        for (IntMap.Entry<GameAction> entry : keyBindings.entries()) {
            int keycode = entry.key;
            GameAction action = entry.value;

            if (Gdx.input.isKeyPressed(keycode)) {
                pressedStates.put(action, true);
            }
            if (Gdx.input.isKeyJustPressed(keycode)) {
                justPressedStates.put(action, true);
            }
        }

        for (IntMap.Entry<GameAction> entry : mouseBindings.entries()) {
            int button = entry.key;
            GameAction action = entry.value;

            if (Gdx.input.isButtonPressed(button)) {
                pressedStates.put(action, true);
            }
            if (Gdx.input.isButtonJustPressed(button)) {
                justPressedStates.put(action, true);
            }
        }

        for (GameAction action : GameAction.values()) {
            boolean wasPressed = previousPressedStates.get(action, false);
            boolean isPressedNow = pressedStates.get(action, false);

            if (wasPressed && !isPressedNow) {
                justReleasedStates.put(action, true);
            }
        }
    }

    public enum GameAction {
    }
}



