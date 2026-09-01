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

    public ZombieInstance(@NotNull ZombieDef def, Vec2d position) {
        super();

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

        // Add a listener to monitor health and handle the arm-loss visual state
        health.addListener(new HealthComponent.OnHealthChangedListener() {
            @Override
            public void OnHealthChanged(int currentHealth, int maxHealth, int delta) {
                // If health drops to 50% or below, strip the arm
                if (currentHealth <= (maxHealth / 2)) {
                    ZombieAnimationLocator.applyArmLossVisibility(ZombieInstance.this);
                }
            }

            @Override
            public void onMaxHealthChanged(int maxHealth, int delta) {
            }
        });

        ZombieBehavior behavior = ZombieBehaviors.get(def);
        if (behavior != null) {
            add(new ZombieBehaviorComponent(behavior));
        }
    }
}
