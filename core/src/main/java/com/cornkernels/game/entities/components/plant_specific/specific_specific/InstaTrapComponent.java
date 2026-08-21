package com.cornkernels.game.entities.components.plant_specific.specific_specific;

public class InstaTrapComponent {
    public float armTimeElapsed = 0f;
    public boolean isArmed = false;

    // Tracks the "real" health before it arms, since its maxHealth is basically infinite
    public int fakeUnarmedHealth = 300;
}
