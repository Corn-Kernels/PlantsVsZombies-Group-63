package com.cornkernels.game.entities.components.plant_specific.specific_specific;

public class SquashComponent {
    public int crushesLeft = -1; // -1 means uninitialized
    public boolean isAttacking = false;
    public float attackTimer = 0f;

    // Stores the exact grid coordinate we decided to crush so it doesn't follow a fast zombie out of bounds
    public float targetCrushX = 0f;
    public float targetCrushY = 0f;
}
