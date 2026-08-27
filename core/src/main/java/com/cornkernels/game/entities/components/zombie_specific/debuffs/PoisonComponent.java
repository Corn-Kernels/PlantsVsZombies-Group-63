package com.cornkernels.game.entities.components.zombie_specific.debuffs;

public class PoisonComponent {
    public int damagePerTick;
    public int tickInterval; // How many engine ticks between each damage application
    public int durationTicks; // Total lifetime of this poison stack

    public int tickCounter = 0;

    public PoisonComponent(int damagePerTick, int durationTicks, int tickInterval) {
        this.damagePerTick = damagePerTick;
        this.durationTicks = durationTicks;
        this.tickInterval = tickInterval;
    }
}
