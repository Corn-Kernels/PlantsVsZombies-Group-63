package com.cornkernels.game.map;

import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.game.entities.types.lawnmower.LawnMower;

public class LawnMowerSlot {
    private final int lane;
    private final Rectangle bounds;
    private LawnMower lawnMower;

    public LawnMowerSlot(int lane, Rectangle bounds) {
        this.lane = lane;
        this.bounds = bounds;
    }

    public int getLane() {
        return lane;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void provideLawnMower(LawnMower lawnMower) {
        this.lawnMower = lawnMower;
    }

    public LawnMower getLawnMower() {
        return lawnMower;
    }

    public void removeLawnMower() {
        this.lawnMower = null;
    }
}
