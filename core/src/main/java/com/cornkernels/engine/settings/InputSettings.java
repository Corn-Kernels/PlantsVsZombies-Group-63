package com.cornkernels.engine.settings;

import java.util.LinkedHashMap;
import java.util.Map;

public class InputSettings {

    public Map<String, Integer> keyBindings = new LinkedHashMap<>();
    private GameSettings settings;

    public InputSettings(GameSettings settings) {
        this.settings = settings;
    }

    public void load() {
//        keyBindings.put();
    }

    public void resetToDefault() {
        keyBindings.clear();
    }
}
