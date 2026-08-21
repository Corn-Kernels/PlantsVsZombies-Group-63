package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.ExplosionTimerComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.GrapeshotProjectile;
import com.cornkernels.game.map.Field;

public class GrapeshotBehavior implements PlantAttackBehavior {
    private final float aoeRadius;
    private final int aoeDamage;
    private final int grapeDamage;
    private final float waitTimeSeconds;
    private final int bounces;
    private final int pierce = 5;
    private final float grapeLifetime = 4.0f; // 4 seconds of screen chaos

    public GrapeshotBehavior(float aoeRadius, int aoeDamage, int grapeDamage, float waitTimeSeconds, int bounces) {
        this.aoeRadius = aoeRadius;
        this.aoeDamage = aoeDamage;
        this.grapeDamage = grapeDamage;
        this.waitTimeSeconds = waitTimeSeconds;
        this.bounces = bounces;
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

        timer.timeElapsed += (1.0f / 20.0f);

        if (timer.timeElapsed >= waitTimeSeconds) {
            Vec2d origin = self.get(PositionComponent.class).position;

            // 1. Spawn the main explosion
            // TODO: field.addProjectile(new AreaOfDamage(origin, aoeRadius, aoeDamage));

            // 2. Calculate speeds and directions
            float speed = 2.0f;
            float diagSpeed = (float) (speed * 0.7071f); // cos(45) and sin(45)

            Vec2d[] velocities = {
                new Vec2d(-speed, 0),             // Left
                new Vec2d(speed, 0),              // Right
                new Vec2d(0, -speed),             // Up
                new Vec2d(0, speed),              // Down
                new Vec2d(-diagSpeed, -diagSpeed),// Top-Left
                new Vec2d(speed, -diagSpeed),     // Top-Right
                new Vec2d(-diagSpeed, diagSpeed), // Bottom-Left
                new Vec2d(speed, diagSpeed)       // Bottom-Right
            };

            // 3. Spawn the 8 bouncing grapes
            for (Vec2d vel : velocities) {
                Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());
                field.addProjectile(new GrapeshotProjectile(grapeDamage, spawnPos, vel, pierce, bounces, grapeLifetime));
            }

            // Snap health exactly to 0
            hc.currentHealth = 0;
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }
}
