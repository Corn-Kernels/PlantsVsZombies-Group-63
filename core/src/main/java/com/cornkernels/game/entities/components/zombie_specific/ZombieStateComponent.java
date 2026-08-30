package com.cornkernels.game.entities.components.zombie_specific;

import com.cornkernels.game.entities.Entity;

public class ZombieStateComponent {

    public State state = State.WALKING;
    public Entity targetPlant;
    public long ticksUntilNextBite;
    public int stateTicks = 0; // Tracks consecutive ticks spent in the current state

    public ZombieStateComponent() {
    }

    /**
     * Safely changes the state and resets the state timer to 0 if the state is actually new.
     */
    public void changeState(State newState) {
        if (this.state != newState) {
            this.state = newState;
            this.stateTicks = 0;
        }
    }

    public enum State {WALKING, EATING, ACTION, DEAD}
}
