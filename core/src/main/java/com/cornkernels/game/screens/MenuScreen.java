package com.cornkernels.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.cornkernels.GameManager;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class MenuScreen implements Screen {

    private final GameManager gameManager;
    private final SpriteBatch batch;

    @Contract(pure = true)
    public MenuScreen(@NonNull GameManager gameManager) {
        this.gameManager = gameManager;
        batch = gameManager.batch;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        batch.begin();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
