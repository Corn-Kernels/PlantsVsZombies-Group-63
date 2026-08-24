package com.cornkernels.game.entities.types.zombies;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.*;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import org.jetbrains.annotations.NotNull;

public class ZombieInstance extends Entity {

    public ZombieInstance(@NotNull ZombieDef def, Vec2d position) {
        super();

        add(new ZombieDefComponent(def));
        add(new PositionComponent(position));
        add(new VelocityComponent(new Vec2d(-def.baseSpeed, 0f)));
        add(new ZombieStateComponent());
        add(new PamAnimationComponent());

        HealthComponent health = new HealthComponent();
        health.maxHealth = def.baseHp;
        health.currentHealth = def.baseHp;
        add(health);

        for (ArmorType armorType : def.armors) {
            add(new ArmorComponent(armorType));
        }
    }
}
