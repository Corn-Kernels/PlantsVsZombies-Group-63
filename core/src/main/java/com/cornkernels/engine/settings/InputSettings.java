package com.cornkernels.engine.settings;

import java.util.LinkedHashMap;
import java.util.Map;

public class InputSettings {

    private final GameSettings settings;
    public Map<String, Integer> keyBindings = new LinkedHashMap<>();

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
