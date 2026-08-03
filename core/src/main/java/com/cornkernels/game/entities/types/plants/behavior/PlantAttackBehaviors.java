package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.DirectShotBehavior;

import java.util.EnumMap;
import java.util.Map;

public class PlantAttackBehaviors {

    private static final Map<PlantDef, PlantAttackBehavior> REGISTRY = new EnumMap<>(PlantDef.class);

    static {
        REGISTRY.put(PlantDef.PEASHOOTER, new DirectShotBehavior(1, 20)); // TODO: LATER, WE HAVE TO READ DAMAGE FROM PLANTDEF ITSELF
        REGISTRY.put(PlantDef.REPEATER, new DirectShotBehavior(2, 20));
        REGISTRY.put(PlantDef.MEGA_GATLING_PEA, new DirectShotBehavior(4, 20));
    }

    private PlantAttackBehaviors() {
    }

    public static PlantAttackBehavior get(PlantDef def) {
        if (!REGISTRY.containsKey(def)) {
            throw new IllegalArgumentException("Unknown PlantDef: " + def);
        }
        return REGISTRY.get(def);
    }
}
