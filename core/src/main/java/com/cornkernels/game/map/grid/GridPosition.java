package com.cornkernels.game.map.grid;

import com.cornkernels.engine.utility.math.Vec2d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record GridPosition(int lane, int column) {

    @Contract("_ -> new")
    public static @NotNull GridPosition fromContinuous(@NotNull Vec2d position) {
        int lane = Math.round(position.getY());
        int column = Math.round(position.getX());
        return new GridPosition(lane, column);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Vec2d toVec2d(@NotNull GridPosition gridPosition) {
        return new Vec2d(gridPosition.lane, gridPosition.column);
    }
}
