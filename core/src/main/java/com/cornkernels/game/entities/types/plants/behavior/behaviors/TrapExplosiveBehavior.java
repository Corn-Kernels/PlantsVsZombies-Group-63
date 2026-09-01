package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.InstaTrapComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage;
import com.cornkernels.game.map.Field;

public class TrapExplosiveBehavior implements PlantAttackBehavior {
    private final float radius;
    private final int damage;
    private final float armTimeSeconds;

    public TrapExplosiveBehavior(float radius, int damage, float armTimeSeconds) {
        this.radius = radius;
        this.damage = damage;
        this.armTimeSeconds = armTimeSeconds;
    }

    @Override
    public void execute(Entity self, Field field) {
        InstaTrapComponent state = self.get(InstaTrapComponent.class);
        if (state == null) {
            state = new InstaTrapComponent();
            self.add(state);
        }

        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null) return;

        // Tick up arm time if not armed
        if (!state.isArmed) {
            state.armTimeElapsed += (1.0f / 20.0f);
            if (state.armTimeElapsed >= armTimeSeconds) {
                state.isArmed = true;
            }
        }

        // Check if HP was reduced (zombie is eating it)
        int damageTaken = hc.maxHealth - hc.currentHealth;
        if (damageTaken > 0) {

            if (state.isArmed || armTimeSeconds == 0) {
                // EXPLODE!
                field.addProjectile(new AreaOfDamage(self.get(PositionComponent.class).position, radius, damage));

                hc.currentHealth = 0; // Die immediately after triggering
                self.markForRemoval();
            } else {
                // Plant is being eaten before it armed!
                state.fakeUnarmedHealth -= damageTaken;

                if (state.fakeUnarmedHealth <= 0) {
                    hc.currentHealth = 0; // It got eaten completely, die without exploding
                    self.markForRemoval();
                } else {
                    hc.currentHealth = hc.maxHealth; // Heal it back up so we can detect the next bite
                }
            }
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true; // Continuously evaluate timers and HP reduction
    }
}
