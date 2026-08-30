package com.cornkernels.game.entities.types.zombies.behavior;

import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;

public interface ZombieBehavior {
    /**
     * Evaluates custom abilities, triggers state changes (like shifting to ACTION),
     * or spawns entities based on internal timers.
     */
    void update(ZombieInstance zombie, Field field);
}
