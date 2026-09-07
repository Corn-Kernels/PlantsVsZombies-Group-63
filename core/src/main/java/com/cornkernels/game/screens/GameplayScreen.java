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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cornkernels.GameManager;
import com.cornkernels.engine.renderer.camera.Camera;
import com.cornkernels.engine.utility.InputSnapshot;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.GameSession;
import com.cornkernels.game.hud.EndGameMenu;
import com.cornkernels.game.hud.HudFactory;
import com.cornkernels.game.hud.PauseMenu;
import com.cornkernels.game.hud.seeds.SeedChooser;
import com.cornkernels.game.hud.seeds.SeedSelectionBar;
import com.cornkernels.game.levels.LevelDef;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.map.data.MapDefinition;
import com.cornkernels.game.map.data.MapLoader;
import com.cornkernels.game.map.data.MapSkin;
import com.cornkernels.game.menus.model.NewsItem;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.Zombie;
import com.cornkernels.game.menus.screens.AdventureMenuScreen;
import com.cornkernels.game.menus.utils.DataLoader;
import com.cornkernels.game.systems.controller.plants.SeedBank;
import com.cornkernels.game.systems.controller.plants.SeedSlot;
import com.cornkernels.game.systems.entity.CombatSystem;
import com.cornkernels.game.utility.ZombieCollectionLocator;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;
import pvz.libpvz.textures.TextureBank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameplayScreen implements Screen {

    private static final float TICK_RATE = 1f / 30f;
    private static final float INTRO_PAN_DURATION = 3f;
    private static final float RETURN_PAN_DURATION = 1.5f;
    private static final int COINS_PER_LAWN_MOWER = 5;
    private static final int GEMS_FOR_ALL_LAWN_MOWERS = 1;
    private static final int SCORE_PER_LAWN_MOWER = 100;
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
        MapSkin skin = MapSkin.forChapter(gameAttributes.levelDef.chapter);
        MapData mapData = mapLoader.load(new MapDefinition(1, "maps/default.tmx", skin));

        Rectangle worldBounds = mapData.getWorldBounds();
        camera = new Camera(worldBounds.height);
        camera.resizeViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setWorldBounds(worldBounds);

        float hudWidth = Gdx.graphics.getWidth();
        float hudHeight = Gdx.graphics.getHeight();
        hudStage = new Stage(new FitViewport(hudWidth, hudHeight), batch);
        Gdx.input.setInputProcessor(hudStage);

        int totalLanes = mapData.cellBounds.length;
        int totalColumns = mapData.cellBounds[0].length;
        gameSession = new GameSession(new Field(totalLanes, totalColumns, mapData.lawnMowerSlots),
            gameAttributes, batch, pamPlayer, mapData, camera);

        PlayerProgress progress = gameManager.getCurrentUser().getProgress();

        int difficulty = Math.max(1, progress.getDifficultyLevel());
        CombatSystem.setPlantDamageMultiplier(1f / difficulty);

        gameSession.getSpeedController().setFastForwardSpeed(progress.getGameSpeed());

        List<Zombie> zombieCatalog = DataLoader.loadAllZombies();
        gameSession.getWaveSystem().setOnZombieSpawnedListener(zombieDef -> {
            String menuAlias = ZombieCollectionLocator.menuAlias(zombieDef);
            if (menuAlias == null || progress.getSeenZombies().contains(menuAlias)) {
                return;
            }

            progress.addSeenZombie(menuAlias);
            Zombie catalogEntry = DataLoader.getZombieByAlias(zombieCatalog, menuAlias);
            String displayName = catalogEntry != null ? catalogEntry.getName() : menuAlias;
            progress.addNews(new NewsItem(
                "zombie_" + System.currentTimeMillis(),
                "New Zombie Discovered: " + displayName,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                "You have encountered " + displayName + "! Study its weaknesses to defeat it.",
                "ZOMBIE"
            ));
        });

        hudFactory = new HudFactory(pamPlayer, gameSession.getPlantingController(),
            gameSession.getPauseController(), gameManager.getCurrentUser().getProgress(), hudWidth, hudHeight);

        hudStage.addActor(hudFactory.createSunCounter());
        hudStage.addActor(hudFactory.createPlantFoodCounter());
        hudStage.addActor(hudFactory.createAddSunButton());
        hudStage.addActor(hudFactory.createAddPlantFoodButton());
        hudStage.addActor(hudFactory.createCoinCounter());
        hudStage.addActor(hudFactory.createGemCounter());
        hudStage.addActor(hudFactory.createLevelProgressBar(gameSession.getWaveSystem(),
            () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING));

        Label readySetPlantBanner = hudFactory.createReadySetPlantBanner();
        hudStage.addActor(readySetPlantBanner);

        hudStage.addActor(hudFactory.createWaveStartBanner(gameSession.getWaveSystem(),
            () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING));

        Label errorBanner = hudFactory.createErrorBanner();
        hudStage.addActor(errorBanner);

        SeedBank seedBank = new SeedBank(gameAttributes.seedSlots, gameSession.getPlantingController());
        seedChooser = hudFactory.createSeedChooser(gameAttributes.seedSlots, seedBank,
            () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING);
        seedChooser.setOnInsufficientSun(() -> hudFactory.showErrorMessage(errorBanner, "Not enough sun!"));

        SeedSelectionBar seedTray = seedChooser.getTray();
        seedTray.setPosition((hudWidth - seedTray.getWidth()) / 2f,
            hudHeight - 20f - seedTray.getHeight());
        seedTray.setVisible(false);
        hudStage.addActor(seedTray);

        hudStage.addActor(hudFactory.createShovelButton(
            () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING));
        hudStage.addActor(hudFactory.createPauseButton());
        hudStage.addActor(hudFactory.createSpeedToggleButton(gameSession.getSpeedController(),
            () -> gameSession.getPhase() == GameSession.LevelPhase.PLAYING));

        EndGameMenu endGameMenu = hudFactory.createEndGameMenu(this::restartLevel, this::exitLevel,
            () -> gameSession.getPhase() == GameSession.LevelPhase.ENDED, gameSession::hasWon);
        endGameMenu.setPosition((hudWidth - endGameMenu.getWidth()) / 2f, (hudHeight - endGameMenu.getHeight()) / 2f);
        hudStage.addActor(endGameMenu);

        ScrollPane plantMenu = seedChooser.getMenuContainer();
        plantMenu.setPosition((hudWidth - plantMenu.getWidth()) / 2f,
            (hudHeight - plantMenu.getHeight()) / 2f);
        plantMenu.setVisible(false);
        hudStage.addActor(plantMenu);

        TextButton boostButton = hudFactory.createBoostButton(seedChooser);
        boostButton.setSize(140f, 50f);
        boostButton.setPosition(plantMenu.getX() + plantMenu.getWidth() + 20f,
            plantMenu.getY() + (plantMenu.getHeight() - boostButton.getHeight()) / 2f);
        boostButton.setVisible(false);
        hudStage.addActor(boostButton);

        TextButton confirmButton = hudFactory.createConfirmButton(() -> {
            if (!seedChooser.getChosenSlots().isEmpty()) {
                gameSession.changeLevelPhase(GameSession.LevelPhase.INTRO_PAN_LEFT);
            }
        });
        confirmButton.setSize(160f, 50f);
        confirmButton.setPosition((hudWidth - 160f) / 2f, plantMenu.getY() - 60f);
        confirmButton.setVisible(false);
        hudStage.addActor(confirmButton);

        PauseMenu pauseMenu = hudFactory.createPauseMenu(this::restartLevel, this::exitLevel);
        pauseMenu.setPosition((hudWidth - pauseMenu.getWidth()) / 2f, (hudHeight - pauseMenu.getHeight()) / 2f);
        hudStage.addActor(pauseMenu);

        camera.panToRightEdge(INTRO_PAN_DURATION, () -> {
            gameSession.changeLevelPhase(GameSession.LevelPhase.SEED_SELECTION);
        });

        gameSession.addPhaseChangeListener(phase -> {
            if (phase == GameSession.LevelPhase.INTRO_PAN_LEFT) {
                camera.panToLeftEdge(RETURN_PAN_DURATION,
                    () -> gameSession.changeLevelPhase(GameSession.LevelPhase.READY_SET_PLANT));
            }
            if (phase == GameSession.LevelPhase.READY_SET_PLANT) {
                hudFactory.playReadySetPlant(readySetPlantBanner,
                    () -> gameSession.changeLevelPhase(GameSession.LevelPhase.PLAYING));
            }
            if (phase == GameSession.LevelPhase.ENDED && gameSession.hasWon()) {
                onLevelWon();
            }
            boolean choosing = phase == GameSession.LevelPhase.SEED_SELECTION;
            boolean playing = phase == GameSession.LevelPhase.PLAYING;
            seedTray.setVisible(choosing || playing);
            plantMenu.setVisible(choosing);
            boostButton.setVisible(choosing);
            confirmButton.setVisible(choosing);
        });
    }

    private void onLevelWon() {
        PlayerProgress progress = gameManager.getCurrentUser().getProgress();
        progress.incrementCompletedLevels();

        Field field = gameSession.getField();
        int lawnMowersRemaining = field.getActiveLawnMowers().size();
        int totalLawnMowers = field.getTotalLawnMowerCount();
        if (lawnMowersRemaining > 0) {
            progress.addCoins(lawnMowersRemaining * COINS_PER_LAWN_MOWER);
            progress.addHighScore(lawnMowersRemaining * SCORE_PER_LAWN_MOWER);
            if (totalLawnMowers > 0 && lawnMowersRemaining == totalLawnMowers) {
                progress.addDiamonds(GEMS_FOR_ALL_LAWN_MOWERS);
            }
        }

        LevelDef levelDef = gameAttributes.levelDef;
        LevelDef nextChapterFirstLevel = LevelDef.of(levelDef.chapter + 1, 1);
        if (levelDef.isLastLevelInChapter() && nextChapterFirstLevel != null) {
            progress.addUnlockedChapter("Chapter " + (levelDef.chapter + 1));
        }

        gameManager.getStorageService().saveUsers();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        InputSnapshot rawInput = InputSnapshot.capture();
        hudStage.act(delta);

        boolean hudConsumedClick = rawInput.confirmPressed() && isPointerOverHud(rawInput);
        InputSnapshot worldInput = hudConsumedClick ? rawInput.withoutConfirm() : rawInput;

        boolean worldShouldRun = gameSession.getPhase() != GameSession.LevelPhase.ENDED
            && !gameSession.getPauseController().isPaused();
        if (worldShouldRun) {
            camera.update(delta);
            gameSession.updateInput(delta, worldInput);

            float speedMultiplier = gameSession.getSpeedController().getSpeedMultiplier();

            // Logic Tick Based Update
            accumulator += delta * speedMultiplier;
            while (accumulator >= TICK_RATE) {
                gameSession.update(TICK_RATE);
                accumulator -= TICK_RATE;
            }

            renderGame(delta * speedMultiplier);
        } else {
            renderGame(0f);
        }
    }

    private void restartLevel() {
        for (SeedSlot slot : gameAttributes.seedSlots) {
            slot.setCooldown(0f);
        }
        gameManager.setScreen(new GameplayScreen(gameManager, gameAttributes));
    }

    private void exitLevel() {
        gameManager.getStorageService().saveUsers();
        gameManager.setScreen(new AdventureMenuScreen(gameManager, gameManager.getCurrentUser()));
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
