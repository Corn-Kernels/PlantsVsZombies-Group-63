package com.cornkernels.game.entities.components;

import com.badlogic.gdx.graphics.Color;
import pvz.libpvz.pam.ClipRef;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class PamAnimationComponent{
    public ClipRef currentClip;
    public float stateTime = 0f;
    public boolean isLooping = true;

    public boolean flipX = false;
    public Color tint = new Color(Color.WHITE);

    public Deque<ClipRef> upcomingClips = new ArrayDeque<>();
    public Map<String, Boolean> visibilityMap = new HashMap<>();
}
