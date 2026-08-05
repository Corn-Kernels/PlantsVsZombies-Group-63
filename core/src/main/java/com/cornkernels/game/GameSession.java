package com.cornkernels.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.MapData;
import org.jetbrains.annotations.NotNull;
import pvz.libpvz.pam.PamPlayer;

public final class GameSession {

    private final GameSimulation simulation;
    private final GameRenderer renderer;
    private final GameAttributes gameAttributes;

    private boolean running = true;

    public GameSession(
        Field field,
        @NotNull GameAttributes gameAttributes,
        SpriteBatch batch,
        PamPlayer pamPlayer,
        MapData mapData) {
        this.gameAttributes = gameAttributes;
        this.simulation = new GameSimulation(field, gameAttributes);
        this.renderer = new GameRenderer(batch, pamPlayer, mapData, field);
    }

    public void update(float deltaTick) {
        if (!running) {
            return;
        }
        simulation.update(deltaTick);
    }

    public void render(float delta) {
        renderer.render(delta);
    }

    public void end() {
        if (!running) {
            return;
        }
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public GameAttributes getGameAttributes() {
        return gameAttributes;
    }
}
