package com.cornkernels.game.hud.cursor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record InputSnapshot(float cursorScreenX, float cursorScreenY, boolean confirmPressed, boolean pointerDown) {

    @Contract(" -> new")
    public static @NonNull InputSnapshot capture() {
        return new InputSnapshot(
            Gdx.input.getX(),
            Gdx.input.getY(),
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT),
            Gdx.input.isButtonPressed(Input.Buttons.LEFT)
        );
    }

    @Contract(" -> new")
    public @NonNull InputSnapshot withoutConfirm() {
        return new InputSnapshot(cursorScreenX, cursorScreenY, false, pointerDown);
    }
}
