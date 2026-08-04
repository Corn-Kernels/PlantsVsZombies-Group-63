package com.cornkernels.engine.input;

public enum GameActions {
    LEFT("Left");

    private final String name;

    GameActions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
