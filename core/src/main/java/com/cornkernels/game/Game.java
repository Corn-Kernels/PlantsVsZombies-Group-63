package com.cornkernels.game;

import com.cornkernels.game.entities.attributes.SeedSlot;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.map.Field;

import java.util.List;

public final class Game {

    private GameSession gameSession;
    private boolean isRunning;

    public Game() {
        this.gameSession = null;
        this.isRunning = false;
    }

    public Game(GameSession gameSession) {
        this.gameSession = gameSession;
        this.isRunning = true;
    }

    public void startGame(Field field, List<SeedSlot> seedSlots, List<ZombieDef> eligibleZombies, int initialSun) {
        GameAttributes gameAttributes = new GameAttributes(seedSlots, eligibleZombies);
        gameAttributes.sunAmount = initialSun;
        this.gameSession = new GameSession(field, gameAttributes);
        this.isRunning = true;
    }

    public void endGame() {
        if (gameSession != null) {
            gameSession.end();
        }
        this.gameSession = null;
        this.isRunning = false;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
