package com.cornkernels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.GameManager;
import com.cornkernels.engine.camera.Camera;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.GameSession;
import com.cornkernels.game.map.*;
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

    private MapLoader mapLoader;

    private float accumulator = 0f;

    public GameplayScreen(GameManager gameManager, GameAttributes gameAttributes) {
        this.gameManager = gameManager;
        this.batch = gameManager.batch;
        this.gameAttributes = gameAttributes;
    }

    @Override
    public void show() {
        FileHandle assetsFolder = Gdx.files.internal("");
        textures = new TextureBank("768", assetsFolder);
        pamPlayer = new PamPlayer(textures, assetsFolder);

        mapLoader = new MapLoader();
        MapData mapData = mapLoader.load(new MapDefinition(1,
            "maps/default.tmx", MapSkin.DELAY_LOAD_BACKGROUND_FRONTLAWN_BIGBRAINZ));

        Rectangle worldBounds = mapData.getWorldBounds();
        camera = new Camera(worldBounds.width, worldBounds.height);
        camera.setWorldBounds(worldBounds);
        camera.resizeViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameSession = new GameSession(new Field(5, 10), gameAttributes, batch, pamPlayer, mapData);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        batch.setProjectionMatrix(camera.getCombined());
        batch.begin();
        gameSession.render(delta);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.resizeViewport(width, height);
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
