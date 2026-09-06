package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.EndurianComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import org.jspecify.annotations.NonNull;

public class EndurianBehavior implements PlantAttackBehavior {
    private final AbstractProjectile invisibleSpikeProjectile;

    public EndurianBehavior(AbstractProjectile invisibleSpikeProjectile) {
        this.invisibleSpikeProjectile = invisibleSpikeProjectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        EndurianComponent state = self.get(EndurianComponent.class);
        if (state == null) {
            state = new EndurianComponent();
            self.add(state);
        }

        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null) return;

        // Initialize health tracking
        if (state.lastHealth == -1) {
            state.lastHealth = hc.currentHealth;
        }

        // Tick up the 1-second interval timer (20 ticks per second)
        state.timeSinceLastShot += (1.0f / 20.0f);

        // Check if we took damage this tick
        if (hc.currentHealth < state.lastHealth) {
            if (state.timeSinceLastShot >= 1.0f) {
                Vec2d origin = self.get(PositionComponent.class).position;

                // Spawn projectile slightly to the right to hit the eating zombie directly in front
                Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());
                field.addProjectile(invisibleSpikeProjectile.clone(spawnPos));

                state.timeSinceLastShot = 0f; // Reset cooldown
            }
        }

        // Update tracked health for the next tick
        state.lastHealth = hc.currentHealth;
    }

    @Override
    public boolean hasTarget(@NonNull Entity self, @NonNull Field field) {
        return true; // Continuously run to evaluate health changes every tick
    }
}
