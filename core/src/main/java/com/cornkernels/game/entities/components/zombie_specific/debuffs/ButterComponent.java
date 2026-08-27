package com.cornkernels.game.entities.components.zombie_specific.debuffs;

public class ButterComponent {
    public int stunTicksRemaining;

    public ButterComponent(int durationTicks) {
        this.stunTicksRemaining = durationTicks;
    }
}
