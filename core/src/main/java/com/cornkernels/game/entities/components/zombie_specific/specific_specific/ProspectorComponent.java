package com.cornkernels.game.entities.components.zombie_specific.specific_specific;


public class ProspectorComponent {
    public enum Phase {
        PRE_JUMP,
        JUMPING,
        LANDED_STUN,
        WALKING_BACKWARDS
    }

    public int tickCounter = 0;
    public boolean dynamiteDefused = false;
    public Phase phase = Phase.PRE_JUMP;
}
