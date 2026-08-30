package com.cornkernels.game.entities.components.zombie_specific;

import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import org.jetbrains.annotations.NotNull;

public class ZombieBehaviorComponent {
    public final ZombieBehavior behavior;

    public ZombieBehaviorComponent(@NotNull ZombieBehavior behavior) {
        this.behavior = behavior;
    }
}
