package com.cornkernels.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.GameManager;
import com.cornkernels.engine.camera.Camera;
import com.cornkernels.game.Game;
import com.cornkernels.game.GameSession;

public class GameplayScreen implements Screen {

    private final GameManager gameManager;
    private final SpriteBatch batch;
    private Camera camera;
    private Game game;
    private GameSession gameSession;

    public GameplayScreen(GameManager gameManager) {
        this.gameManager = gameManager;
        this.batch = gameManager.batch;
    }

    @Override
    public void show() {

        camera = new Camera();
        game = new Game();
        gameSession = game.getGameSession();
    }

    @Override
    public void render(float delta) {
        camera.update();
        gameSession.update(delta);
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
        batch.dispose();
    }
}
