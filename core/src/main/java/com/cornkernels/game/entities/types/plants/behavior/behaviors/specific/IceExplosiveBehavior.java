package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.ExplosionTimerComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.AreaOfIceDamage;
import com.cornkernels.game.map.Field;

public class IceExplosiveBehavior implements PlantAttackBehavior {
    private final float radius;
    private final int damage;
    private final float waitTimeSeconds;
    private final int freezeDurationTicks;
    private final int residualChillTicks;

    public IceExplosiveBehavior(float radius, int damage, float waitTimeSeconds, int freezeDurationTicks, int residualChillTicks) {
        this.radius = radius;
        this.damage = damage;
        this.waitTimeSeconds = waitTimeSeconds;
        this.freezeDurationTicks = freezeDurationTicks;
        this.residualChillTicks = residualChillTicks;
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
            // Spawn the radial ice explosion
            field.addProjectile(new AreaOfIceDamage(self.get(PositionComponent.class).position, radius, damage, freezeDurationTicks, residualChillTicks));

            // Snap health exactly to 0 so the standard entity removal system destroys the plant
            hc.currentHealth = 0;
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true; // Always true so it ticks the timer every game cycle
    }
}
