package com.cornkernels.game.entities.types.zombies;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.*;
import com.cornkernels.game.entities.components.zombie_specific.ZombieBehaviorComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehaviors;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jetbrains.annotations.NotNull;

public class ZombieInstance extends Entity {

    private static final float DARK_KING_FRONT_OFFSET = 3f;

    public ZombieInstance(@NotNull ZombieDef def, Vec2d position) {
        super();

        if (def == ZombieDef.DARK_KING) {
            position = new Vec2d(position.getX() - DARK_KING_FRONT_OFFSET, position.getY());
        }

        add(new ZombieDefComponent(def));
        add(new PositionComponent(position));
        add(new VelocityComponent(new Vec2d(-def.baseSpeed, 0f)));
        add(new ZombieStateComponent());

        PamAnimationComponent pamAnim = new PamAnimationComponent();
        add(pamAnim);

        add(new IceComponent());

        HealthComponent health = new HealthComponent();
        health.maxHealth = def.baseHp;
        health.currentHealth = def.baseHp;
        add(health);

        for (ArmorType armorType : def.armors) {
            add(new ArmorComponent(armorType));
        }

        ZombieAnimationLocator.updateArmorVisibility(this);

        // The redundant health listener that was prematurely hiding the arm has been removed

        ZombieBehavior behavior = ZombieBehaviors.get(def);
        if (behavior != null) {
            add(new ZombieBehaviorComponent(behavior));
        }
    }
}
