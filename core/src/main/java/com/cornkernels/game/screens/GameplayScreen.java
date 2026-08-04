package com.cornkernels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.GameManager;
import com.cornkernels.engine.camera.Camera;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.GameSession;
import com.cornkernels.game.map.Field;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;

public class GameplayScreen implements Screen {

    private static final float TICK_RATE = 1f / 30f;

    private final GameManager gameManager;
    private final SpriteBatch batch;
    private Camera camera;
    private GameSession gameSession;
    private GameAttributes gameAttributes;

    private TextureBank textures;
    private PamPlayer pamPlayer;

    private float accumulator = 0f;

    public GameplayScreen(GameManager gameManager, GameAttributes gameAttributes) {
        this.gameManager = gameManager;
        this.batch = gameManager.batch;
        this.gameAttributes = gameAttributes;
    }

    @Override
    public void show() {
        camera = new Camera();
        textures = new TextureBank("768", Gdx.files.internal("assets"));
        pamPlayer = new PamPlayer(textures, Gdx.files.internal("assets"));
        gameSession = new GameSession(new Field(5, 10), gameAttributes, batch, pamPlayer);
    }

    @Override
    public void render(float delta) {
        camera.update();

        // Logic Tick Based Update
        accumulator += delta;
        while (accumulator >= TICK_RATE) {
            gameSession.update(TICK_RATE);
            accumulator -= TICK_RATE;
        }

        // Render deltaTime Based Update
        renderGame(delta);
    }

    private void renderGame(float delta) {
        textures.update();

        batch.begin();
        gameSession.render(delta);
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
        batch.dispose();
    }
}
