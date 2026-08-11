package com.cornkernels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.GameManager;
import com.cornkernels.engine.renderer.camera.Camera;
import com.cornkernels.engine.renderer.hud.HudCamera;
import com.cornkernels.engine.renderer.hud.HudSystem;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.GameSession;
import com.cornkernels.game.hud.HudFactory;
import com.cornkernels.game.hud.cursor.InputSnapshot;
import com.cornkernels.game.map.*;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;

public class GameplayScreen implements Screen {

    private static final float TICK_RATE = 1f / 30f;
    private static final float INTRO_PAN_DURATION = 3f;
    private static final float RETURN_PAN_DURATION = 1.5f;

    private final GameManager gameManager;
    private final SpriteBatch batch;
    private final GameAttributes gameAttributes;
    private GameSession gameSession;
    private TextureBank textures;
    private PamPlayer pamPlayer;

    private Camera camera;

    private HudCamera hudCamera;
    private HudSystem hudSystem;
    private HudFactory hudFactory;

    private MapLoader mapLoader;

    private float accumulator = 0f;

    @Contract(pure = true)
    public GameplayScreen(@NonNull GameManager gameManager, GameAttributes gameAttributes) {
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
        camera = new Camera(worldBounds.height);
        camera.resizeViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setWorldBounds(worldBounds);

        hudCamera = new HudCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hudSystem = new HudSystem(hudCamera);
        gameSession = new GameSession(new Field(5, 10), gameAttributes, batch, pamPlayer, mapData, camera);

        hudFactory = new HudFactory(pamPlayer, gameSession.getPlantingController(), gameSession.getPauseController());
        hudSystem.addElement(hudFactory.createPauseButton());
        hudSystem.addElement(hudFactory.createShovelButton(), () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING);

        camera.panToRightEdge(INTRO_PAN_DURATION, () -> {
            gameSession.changeLevelPhase(GameSession.LevelPhase.INTRO_PAN_LEFT);
        });

        gameSession.addPhaseChangeListener(phase -> {
            if (phase == GameSession.LevelPhase.INTRO_PAN_LEFT) {
                camera.panToLeftEdge(RETURN_PAN_DURATION, () -> gameSession.changeLevelPhase(GameSession.LevelPhase.PLAYING));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        InputSnapshot rawInput = InputSnapshot.capture();
        boolean hudConsumedClick = hudSystem.update(delta, rawInput);
        InputSnapshot worldInput = hudConsumedClick ? rawInput.withoutConfirm() : rawInput;

        if (!gameSession.getPauseController().isPaused()) {
            camera.update(delta);

            // Logic Tick Based Update
            accumulator += delta;
            while (accumulator >= TICK_RATE) {
                gameSession.update(TICK_RATE, worldInput);
                accumulator -= TICK_RATE;
            }

            // Render deltaTime Based Update
            renderGame(delta);
        } else {
            renderGame(0f);
        }


    }

    private void renderGame(float delta) {
        textures.update();

        camera.applyViewport();
        batch.setProjectionMatrix(camera.getCombined());
        batch.begin();
        gameSession.render(delta);
        batch.end();

        hudCamera.applyViewport();
        batch.setProjectionMatrix(hudCamera.getCombined());
        batch.begin();
        hudSystem.render(batch, delta);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        camera.resizeViewport(width, height);
        hudCamera.resize(width, height);

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
