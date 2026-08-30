package com.cornkernels.game.entities.components.zombie_specific.debuffs;

public class IceComponent {
    public int freezeLevel = 0; // 0 = Normal, 1 = Chilled (Slow), 2 = Frozen Solid
    public int freezeTicksRemaining = 0;
    public int slowTicksRemaining = 0;

    public void applyChill(int durationTicks) {
        if (freezeLevel < 1) freezeLevel = 1;
        this.slowTicksRemaining = Math.max(this.slowTicksRemaining, durationTicks);
    }

    public void applyFreeze(int durationTicks, int residualSlowTicks) {
        this.freezeLevel = 2;
        this.freezeTicksRemaining = Math.max(this.freezeTicksRemaining, durationTicks);
        this.slowTicksRemaining = Math.max(this.slowTicksRemaining, residualSlowTicks);
    }

    public void melt() {
        this.freezeLevel = 0;
        this.freezeTicksRemaining = 0;
        this.slowTicksRemaining = 0;
    }
}
