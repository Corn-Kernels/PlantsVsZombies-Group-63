package com.cornkernels.game.entities.components.plant_specific;

public class PlantStateComponent {

    public State state = State.IDLE;

    public enum State {IDLE, SHOOTING, DEAD}
}
