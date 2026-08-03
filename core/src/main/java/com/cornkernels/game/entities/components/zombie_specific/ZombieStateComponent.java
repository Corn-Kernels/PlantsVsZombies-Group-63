package com.cornkernels.game.entities.components.zombie_specific;

import com.cornkernels.game.entities.Entity;

public class ZombieStateComponent {

    public State state = State.WALKING;
    public Entity targetPlant;
    public long ticksUntilNextBite;

    public ZombieStateComponent() {

    }

    public enum State {WALKING, EATING, DEAD}
}
