package com.cornkernels.engine.input;

import com.badlogic.gdx.Input;
import com.cornkernels.engine.settings.InputSettings;

import java.util.List;

public class PlayerActions {

    public GameAction left = new GameAction(GameActions.LEFT.getName(), Input.Keys.A);

    private final List<GameAction> all = List.of(
        left
    );

    void update(long frameId) {
        for (GameAction gameAction : all) {
            gameAction.poll(frameId);
        }
    }

    public void applyBinding(InputSettings settings) {
        for (GameAction action : all) {
            Integer bound = settings.keyBindings.get(action.getName());
            if (bound != null) {
                action.setKeyCode(bound);
            }
        }
    }

    public void exportBinding(InputSettings settings) {
        settings.keyBindings.clear();
        for (GameAction action : all) {
            settings.keyBindings.put(action.getName(), action.getKeyCode());
        }
    }

    public List<GameAction> getAll() {
        return all;
    }
}
