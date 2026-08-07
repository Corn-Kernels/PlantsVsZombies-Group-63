package com.cornkernels.game.hud.cursor;

import com.cornkernels.game.hud.HighlightAnimationSet;
import com.cornkernels.game.map.grid.GridObject;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class CursorToolState {
    public boolean active = false;
    public CursorAttachment attachment;
    public HighlightAnimationSet highlightSet;
    public Predicate<GridObject> eligibility;
    public Consumer<GridPosition> onConfirm;

    public GridPosition snappedPosition;
    public float rawCursorWorldX;
    public float rawCursorWorldY;
}
