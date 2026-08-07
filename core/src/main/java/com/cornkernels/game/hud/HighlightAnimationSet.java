package com.cornkernels.game.hud;

import pvz.libpvz.pam.ClipRef;

public class HighlightAnimationSet {
    public final ClipRef idleClip;
    public final ClipRef hoveredClip;

    public HighlightAnimationSet(ClipRef idleClip, ClipRef hoveredClip) {
        this.idleClip = idleClip;
        this.hoveredClip = hoveredClip;
    }
}
