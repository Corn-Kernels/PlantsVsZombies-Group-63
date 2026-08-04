package com.cornkernels.game.entities.components;

import pvz.libpvz.pam.ClipRef;

import java.util.HashMap;
import java.util.Map;

public class PamAnimationComponent {
    public ClipRef currentClip;
    public float stateTime = 0f;
    public boolean isLooping = true;

    public Map<String, Boolean> visibilityMap = new HashMap<>();
}
