// KingBehavior.java
package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.KnightedComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.KingComponent;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.ArrayList;
import java.util.List;

public class KingBehavior implements ZombieBehavior {

    private final int promotionIntervalTicks;

    public KingBehavior(float promotionIntervalSeconds) {
        this.promotionIntervalTicks = (int) (promotionIntervalSeconds * 20);
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        KingComponent comp = zombie.get(KingComponent.class);
        if (comp == null) {
            comp = new KingComponent();
            zombie.add(comp);
        }

        if (state.state != ZombieStateComponent.State.ACTION) {
            state.changeState(ZombieStateComponent.State.ACTION);
        }

        zombie.get(VelocityComponent.class).velocityPerTick = new Vec2d(0, 0);

        comp.tickCounter++;

        if (comp.tickCounter >= promotionIntervalTicks) {
            comp.tickCounter = 0;

            GridPosition kingPos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            List<ZombieInstance> validTargets = new ArrayList<>();

            for (ZombieInstance otherZombie : field.getActiveZombies()) {
                if (otherZombie == zombie || otherZombie.isMarkedForRemoval()) continue;

                GridPosition otherPos = GridPosition.fromContinuous(otherZombie.get(PositionComponent.class).position);
                if (kingPos.lane() != otherPos.lane()) continue;

                if (!otherZombie.has(ArmorComponent.class) && !otherZombie.has(KnightedComponent.class)) {
                    validTargets.add(otherZombie);
                }
            }

            if (!validTargets.isEmpty()) {
                ZombieInstance target = validTargets.get(0);
                target.add(new ArmorComponent(ArmorType.SHOULDER));
                target.add(new ArmorComponent(ArmorType.CROWN));
                target.add(new KnightedComponent());
            }
        }
    }
}
