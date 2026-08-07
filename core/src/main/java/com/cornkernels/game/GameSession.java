package com.cornkernels.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.hud.HighlightAnimationSet;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.hud.cursor.CursorToolController;
import com.cornkernels.game.hud.cursor.CursorToolState;
import com.cornkernels.game.hud.cursor.InputSnapshot;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.MapData;
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

    private final Field field;

    private final CursorToolState toolState = new CursorToolState();
    private final CursorToolController toolController;

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
        this.simulation = new GameSimulation(field, gameAttributes);
        this.toolController = new CursorToolController(mapData, camera, toolState);
        this.renderer = new GameRenderer(batch, pamPlayer, mapData, field, toolState);

        phase = GameSession.LevelPhase.INTRO_PAN_RIGHT;
    }

    public void beginPlantPlacement(String plantTypeId, CursorAttachment thumbnail, HighlightAnimationSet highlightSet) {
        toolState.active = true;
        toolState.attachment = thumbnail;
        toolState.highlightSet = highlightSet;
        toolState.eligibility =
            cell -> cell.getPlant() == null && cell.getObstacle() == null;
        toolState.onConfirm = gridPosition -> {
            field.addPlant(new PlantInstance(PlantDef.getPlantTypeOfName(plantTypeId), gridPosition));
            toolState.active = false;
        };
    } // TODO: MOVE THIS METHOD TO HUD RELATED CLASSES WHEN SYSTEM IS WRITTEN

    public void beginShovel(CursorAttachment shovelIcon) {
        toolState.active = true;
        toolState.attachment = shovelIcon;
        toolState.highlightSet = null;
        toolState.eligibility = cell -> cell.getPlant() != null;
        toolState.onConfirm = field::removePlantAt;
    } // TODO: MOVE THIS METHOD TO HUD RELATED CLASSES WHEN SYSTEM IS WRITTEN

    public void update(float deltaTick, InputSnapshot inputSnapshot) {
        if (phase != GameSession.LevelPhase.PLAYING) {
            return;
        }
        toolController.update(inputSnapshot);
        simulation.update(deltaTick);
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

    public boolean isPlaying() {
        return phase == GameSession.LevelPhase.PLAYING;
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

    public enum LevelPhase {INTRO_PAN_RIGHT, SEED_SELECTION, INTRO_PAN_LEFT, PLAYING, ENDED}

    public interface OnGamePhaseChangedListener {
        void onGamePhaseChanged(LevelPhase phase);
    }
}
