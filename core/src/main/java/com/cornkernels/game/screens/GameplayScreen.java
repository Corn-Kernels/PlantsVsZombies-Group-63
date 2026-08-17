package com.cornkernels.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cornkernels.GameManager;
import com.cornkernels.engine.renderer.camera.Camera;
import com.cornkernels.engine.utility.InputSnapshot;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.GameSession;
import com.cornkernels.game.hud.HudFactory;
import com.cornkernels.game.hud.seeds.PlantSelectionMenu;
import com.cornkernels.game.hud.seeds.SeedChooser;
import com.cornkernels.game.hud.seeds.SeedSelectionBar;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.map.data.MapDefinition;
import com.cornkernels.game.map.data.MapLoader;
import com.cornkernels.game.map.data.MapSkin;
import com.cornkernels.game.systems.controller.plants.SeedBank;
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

    private Stage hudStage;
    private HudFactory hudFactory;
    private SeedChooser seedChooser;

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

        float hudWidth = Gdx.graphics.getWidth();
        float hudHeight = Gdx.graphics.getHeight();
        hudStage = new Stage(new FitViewport(hudWidth, hudHeight), batch);
        Gdx.input.setInputProcessor(hudStage);

        gameSession = new GameSession(new Field(5, 10), gameAttributes, batch, pamPlayer, mapData, camera);

        hudFactory = new HudFactory(pamPlayer, gameSession.getPlantingController(),
            gameSession.getPauseController(), hudWidth, hudHeight);

        hudStage.addActor(hudFactory.createSunCounter());

        SeedBank seedBank = new SeedBank(gameAttributes.seedSlots, gameSession.getPlantingController());
        seedChooser = hudFactory.createSeedChooser(gameAttributes.seedSlots, seedBank,
            () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING);

        SeedSelectionBar seedTray = seedChooser.getTray();
        seedTray.setPosition((hudWidth - seedTray.getWidth()) / 2f,
            hudHeight - 20f - seedTray.getHeight());
        seedTray.setVisible(false);
        hudStage.addActor(seedTray);

        hudStage.addActor(hudFactory.createShovelButton(
            () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING));
        hudStage.addActor(hudFactory.createPauseButton());

        PlantSelectionMenu plantMenu = seedChooser.getMenu();
        plantMenu.setPosition((hudWidth - plantMenu.getWidth()) / 2f,
            (hudHeight - plantMenu.getHeight()) / 2f);
        plantMenu.setVisible(false);
        hudStage.addActor(plantMenu);

        TextButton confirmButton = hudFactory.createConfirmButton(() -> {
            if (!seedChooser.getChosenSlots().isEmpty()) {
                gameSession.changeLevelPhase(GameSession.LevelPhase.INTRO_PAN_LEFT);
            }
        });
        confirmButton.setSize(160f, 50f);
        confirmButton.setPosition((hudWidth - 160f) / 2f, plantMenu.getY() - 60f);
        confirmButton.setVisible(false);
        hudStage.addActor(confirmButton);

        camera.panToRightEdge(INTRO_PAN_DURATION, () -> {
            gameSession.changeLevelPhase(GameSession.LevelPhase.SEED_SELECTION);
        });

        gameSession.addPhaseChangeListener(phase -> {
            if (phase == GameSession.LevelPhase.INTRO_PAN_LEFT) {
                camera.panToLeftEdge(RETURN_PAN_DURATION, () -> gameSession.changeLevelPhase(GameSession.LevelPhase.PLAYING));
            }
            boolean choosing = phase == GameSession.LevelPhase.SEED_SELECTION;
            boolean playing = phase == GameSession.LevelPhase.PLAYING;
            seedTray.setVisible(choosing || playing);
            plantMenu.setVisible(choosing);
            confirmButton.setVisible(choosing);
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        InputSnapshot rawInput = InputSnapshot.capture();
        hudStage.act(delta);

        boolean hudConsumedClick = rawInput.confirmPressed() && isPointerOverHud(rawInput);
        InputSnapshot worldInput = hudConsumedClick ? rawInput.withoutConfirm() : rawInput;

        if (!gameSession.getPauseController().isPaused()) {
            camera.update(delta);
            gameSession.updateInput(delta, worldInput);

            // Logic Tick Based Update
            accumulator += delta;
            while (accumulator >= TICK_RATE) {
                gameSession.update(TICK_RATE);
                accumulator -= TICK_RATE;
            }

            // Render deltaTime Based Update
            renderGame(delta);
        } else {
            renderGame(0f);
        }
    }

    private boolean isPointerOverHud(@NonNull InputSnapshot input) {
        Vector2 stageCoords = hudStage.screenToStageCoordinates(
            new Vector2(input.cursorScreenX(), input.cursorScreenY()));
        Actor hit = hudStage.hit(stageCoords.x, stageCoords.y, true);
        return hit != null;
    }

    private void renderGame(float delta) {
        textures.update();

        camera.applyViewport();
        batch.setProjectionMatrix(camera.getCombined());
        batch.begin();
        gameSession.render(delta);
        batch.end();

        hudStage.getViewport().apply();
        hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.resizeViewport(width, height);
        hudStage.getViewport().update(width, height, true);
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
        if (Gdx.input.getInputProcessor() == hudStage) {
            Gdx.input.setInputProcessor(null);
        }
        hudStage.dispose();
        hudFactory.dispose();
        batch.dispose();
    }

}
