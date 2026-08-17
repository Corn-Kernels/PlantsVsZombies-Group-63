package com.cornkernels.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.engine.utility.InputSnapshot;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.systems.controller.PauseController;
import com.cornkernels.game.systems.controller.SunHarvestController;
import com.cornkernels.game.systems.controller.plants.PlantingController;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayList;
import java.util.List;

public final class GameSession {

    private final List<OnGamePhaseChangedListener> phaseChangedListeners = new ArrayList<>();
    private final GameSimulation simulation;
    private final GameRenderer renderer;
    private final GameAttributes gameAttributes;
    private final PauseController pauseController;
    private final PlantingController plantingController;
    private final SunHarvestController sunHarvestController;
    private final Field field;
    private LevelPhase phase;

    public GameSession(
        Field field,
        @NotNull GameAttributes gameAttributes,
        SpriteBatch batch,
        PamPlayer pamPlayer,
        MapData mapData,
        GameplayCamera camera) {
        this.field = field;
        this.gameAttributes = gameAttributes;
        this.pauseController = new PauseController();
        this.plantingController = new PlantingController(field, mapData, camera, 1000, pamPlayer);
        this.sunHarvestController = new SunHarvestController(field, mapData, camera, plantingController);
        this.simulation = new GameSimulation(field, gameAttributes);
        this.renderer = new GameRenderer(batch, pamPlayer, mapData, field, plantingController.getToolState());

        phase = GameSession.LevelPhase.INTRO_PAN_RIGHT;
    }

    public void update(float deltaTick) {
        if (phase != GameSession.LevelPhase.PLAYING) {
            return;
        }
        if (!pauseController.isPaused()) {
            simulation.update(deltaTick);
        }
    }

    public void updateInput(float delta, InputSnapshot inputSnapshot) {
        if (phase != GameSession.LevelPhase.PLAYING) {
            return;
        }
        if (!pauseController.isPaused()) {
            boolean harvested = sunHarvestController.update(inputSnapshot);
            plantingController.update(delta, harvested ? inputSnapshot.withoutConfirm() : inputSnapshot);
        }
    }

    public void render(float delta) {
        renderer.render(delta);
    }

    public void end() {
        if (phase == LevelPhase.ENDED) {
            return;
        }
        phase = LevelPhase.ENDED;
        notifyPhaseChangeListeners();
    }

    public GameAttributes getGameAttributes() {
        return gameAttributes;
    }

    public void changeLevelPhase(LevelPhase phase) {
        this.phase = phase;
        notifyPhaseChangeListeners();
    }

    public LevelPhase getPhase() {
        return phase;
    }

    public void addPhaseChangeListener(@NonNull OnGamePhaseChangedListener listener) {
        if (!phaseChangedListeners.contains(listener)) {
            phaseChangedListeners.add(listener);
        }
    }

    public void removePhaseChangeListener(@NonNull OnGamePhaseChangedListener listener) {
        phaseChangedListeners.remove(listener);
    }

    private void notifyPhaseChangeListeners() {
        for (OnGamePhaseChangedListener listener : phaseChangedListeners) {
            listener.onGamePhaseChanged(phase);
        }
    }

    public PauseController getPauseController() {
        return pauseController;
    }

    public PlantingController getPlantingController() {
        return plantingController;
    }

    public enum LevelPhase {INTRO_PAN_RIGHT, SEED_SELECTION, INTRO_PAN_LEFT, PLAYING, ENDED}

    public interface OnGamePhaseChangedListener {
        void onGamePhaseChanged(LevelPhase phase);
    }
}
