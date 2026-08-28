package com.cornkernels.game.entities.types.zombies.behavior;

import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.behavior.behaviors.CustomMeleeBehavior;
import com.cornkernels.game.entities.types.zombies.behavior.behaviors.TombRaiserBehavior;
import com.cornkernels.game.entities.types.zombies.behavior.behaviors.KingBehavior;
import com.cornkernels.game.entities.types.zombies.behavior.behaviors.DodoBehavior;
import java.util.EnumMap;
import java.util.Map;

public class ZombieBehaviors {

    private static final Map<ZombieDef, ZombieBehavior> REGISTRY = new EnumMap<>(ZombieDef.class);

    static {
        register(ZombieDef.GARGANTUAR, new CustomMeleeBehavior(40, 10, 10000));
        register(ZombieDef.EXPLORER, new CustomMeleeBehavior(1, 0, 10000));
        // King Zombie: Promotes a peasant every 3.5 seconds
        register(ZombieDef.DARK_KING, new KingBehavior(3.5f));
        // Cooldown 7s, Windup 1s, Recovery 0.5s
        register(ZombieDef.TOMB_RAISER, new TombRaiserBehavior(7.0f, 1.0f, 0.5f));
        // Dodo: Walks for 12 seconds, then flies over plants for 12 seconds at half speed
        register(ZombieDef.ICE_AGE_DODO, new DodoBehavior(12.0f, 12.0f, 0.5f));
    }

    private static void register(ZombieDef def, ZombieBehavior behavior) {
        REGISTRY.put(def, behavior);
    }

    public static ZombieBehavior get(ZombieDef def) {
        return REGISTRY.get(def);
    }
}
