package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.ExplosionTimerComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.LineOfDamage;
import com.cornkernels.game.map.Field;
import org.jspecify.annotations.NonNull;

public class LineExplosiveBehavior implements PlantAttackBehavior {
    private final float length;
    private final float width;
    private final int damage;
    private final float waitTimeSeconds;
    private final boolean fiery;

    public LineExplosiveBehavior(float length, float width, int damage, float waitTimeSeconds, boolean fiery) {
        this.length = length;
        this.width = width;
        this.damage = damage;
        this.waitTimeSeconds = waitTimeSeconds;
        this.fiery = fiery;
    }

    @Override
    public void execute(Entity self, Field field) {
        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null || hc.currentHealth <= 0) return;

        ExplosionTimerComponent timer = self.get(ExplosionTimerComponent.class);
        if (timer == null) {
            timer = new ExplosionTimerComponent();
            self.add(timer);
        }

        // Tick timer (Assuming 20 ticks per second)
        timer.timeElapsed += (1.0f / 20.0f);

        if (timer.timeElapsed >= waitTimeSeconds) {
            // Spawn the piercing line explosion with the fiery property
            field.addProjectile(new LineOfDamage(self.get(PositionComponent.class).position, length, width, damage, fiery));

            // Snap health exactly to 0
            hc.currentHealth = 0;
            self.markForRemoval();
        }
    }

    @Override
    public boolean hasTarget(@NonNull Entity self, @NonNull Field field) {
        return true; // Always true so it ticks the timer every game cycle
    }
}
