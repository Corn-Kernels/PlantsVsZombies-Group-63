package com.cornkernels.game;

import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.*;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.random.RandomGenerator;

public final class GameSession {

    private static final int MAX_CATCHUP_TICKS = 1000;
    private static final float SECONDS_PER_TICK = 0.1f;
    private final Field field;
    private final RandomGenerator random;
    private final MovementSystem movementSystem;
    private final CombatSystem combatSystem;
    private final PlantAttackSystem plantAttackSystem;
    private final SunSystem sunSystem;
    private final LawnMowersSystem lawnMowersSystem;
    private final WaveSystem waveSystem;
    private final GameAttributes gameAttributes;
    private long currentTick = 0;
    private boolean running = true;

    public GameSession(Field field, @NotNull GameAttributes gameAttributes) {
        this.field = field;

        this.random = new Random();
        this.gameAttributes = gameAttributes;
        this.movementSystem = new MovementSystem();
        this.combatSystem = new CombatSystem();
        this.plantAttackSystem = new PlantAttackSystem();
        this.sunSystem = new SunSystem(random);
        this.lawnMowersSystem = new LawnMowersSystem();
        this.waveSystem = new WaveSystem(1, random, gameAttributes.eligibleZombies);
    }

    public void update(float deltaTick) {
        if (!running) {
            return;
        }

        field.update(deltaTick);
        gameAttributes.update(deltaTick);
        waveSystem.update(field, deltaTick);
        movementSystem.update(field.getEntities());
        combatSystem.update(field);
        plantAttackSystem.update(field, deltaTick);
        sunSystem.update(field, deltaTick);
        lawnMowersSystem.update(field);

        if (lawnMowersSystem.isGameFinished()) {
            System.out.println("The zombie ate your brain; LOSER!!!");
            end();
        }

        if (waveSystem.isGameFinished(field)) {
            System.out.println("Dear humanz, zis is not done yet; we will come back to eat your brainz, humanz.");
            end();
        }
    }

    public void end() {
        if (!running) {
            return;
        }
        running = false;
    }

    public void advanceTime(long deltaTicks) {
        currentTick += deltaTicks;
        long ticksToRun = Math.min(deltaTicks, MAX_CATCHUP_TICKS);
        for (long i = 0; i < ticksToRun; i++) {
            update(SECONDS_PER_TICK);
        }
    }

    public void nextTick() {
        currentTick++;
    }

    public void nextSecond() {
        currentTick += 10;
    }


    public Field getField() {
        return field;
    }

    public boolean isRunning() {
        return running;
    }

    public GameAttributes getGameAttributes() {
        return gameAttributes;
    }

    public MovementSystem getMovementSystem() {
        return movementSystem;
    }

    public CombatSystem getCombatSystem() {
        return combatSystem;
    }

    public PlantAttackSystem getPlantAttackSystem() {
        return plantAttackSystem;
    }

    public SunSystem getSunSystem() {
        return sunSystem;
    }

    public WaveSystem getWaveSystem() {
        return waveSystem;
    }

    public LawnMowersSystem getLawnMowersSystem() {
        return lawnMowersSystem;
    }

    public long getTicksPassed() {
        return currentTick;
    }
}
