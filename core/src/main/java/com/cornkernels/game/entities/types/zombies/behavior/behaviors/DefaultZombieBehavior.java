package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.DancingComponent;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.random.RandomGenerator;

public class DefaultZombieBehavior implements ZombieBehavior {

    private static int sharedDanceTick = 0;
    private static long lastFrameTime = 0;
    private final int DANCE_INTERVAL = 240; // 4 seconds (assuming 20 TPS)

    private final RandomGenerator rng = RandomGenerator.getDefault();

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        // 1. Advance the global master clock exactly once per game frame
        long now = System.nanoTime();
        if (now - lastFrameTime > 5_000_000) { // 5ms threshold prevents multi-counting
            sharedDanceTick++;
            lastFrameTime = now;
        }

        // 2. Scan the field for an active Piano Zombie
        boolean isPianoActive = false;
        for (ZombieInstance z : field.getActiveZombies()) {
            if (z.isMarkedForRemoval()) continue;

            ZombieDefComponent defComp = z.get(ZombieDefComponent.class);
            if (defComp != null && defComp.def().id.equals("ZombiePiano")) {
                isPianoActive = true;
                break;
            }
        }

        DancingComponent comp = zombie.get(DancingComponent.class);
        if (comp == null) {
            comp = new DancingComponent();
            zombie.add(comp);
        }

        PositionComponent posComp = zombie.get(PositionComponent.class);

        // 3. Actively Sliding Between Lanes
        if (comp.isSwitchingLanes) {
            double currentY = posComp.position.getY();
            double slideSpeed = 0.05; // Adjust this for faster/slower lane switching

            if (Math.abs(currentY - comp.targetY) <= slideSpeed) {
                currentY = comp.targetY; // Snap exactly to lane
                comp.isSwitchingLanes = false;
            } else if (currentY < comp.targetY) {
                currentY += slideSpeed;
            } else {
                currentY -= slideSpeed;
            }

            // Apply Y change without touching X velocity
            posComp.position = new Vec2d(posComp.position.getX(), (float) currentY);
        }
        // 4. Initiate Synchronized Lane Switch
        else if (isPianoActive && state.state == ZombieStateComponent.State.WALKING) {

            int currentBeat = sharedDanceTick / DANCE_INTERVAL;

            // If the global timer hits a multiple of 80, and this zombie hasn't danced this beat yet
            if (sharedDanceTick > 0 && sharedDanceTick % DANCE_INTERVAL == 0 && comp.lastDanceBeat != currentBeat) {
                comp.lastDanceBeat = currentBeat; // Lock out until the next 80-tick cycle

                int currentLane = GridPosition.fromContinuous(posComp.position).lane();
                int newLane = currentLane;

                // Pick a valid adjacent lane
                if (currentLane == 0) {
                    newLane = 1;
                } else if (currentLane == field.getTotalLanes() - 1) {
                    newLane = currentLane - 1;
                } else {
                    newLane = rng.nextBoolean() ? currentLane - 1 : currentLane + 1;
                }

                if (newLane != currentLane) {
                    comp.targetY = newLane;
                    comp.isSwitchingLanes = true;
                }
            }
        }
    }
}
