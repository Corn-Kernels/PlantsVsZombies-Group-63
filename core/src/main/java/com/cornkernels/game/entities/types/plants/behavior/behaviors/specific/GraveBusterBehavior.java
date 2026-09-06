package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GraveBeingEatenComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GraveBusterComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class GraveBusterBehavior implements PlantAttackBehavior {
    private final int maxTicks;
    private final int explosionDamage;
    private final float explosionRadius = 1.5f;

    public GraveBusterBehavior(float eatTimeSeconds, int explosionDamage) {
        this.maxTicks = (int) (eatTimeSeconds * 20); // 20 ticks per second
        this.explosionDamage = explosionDamage;
    }

    @Override
    public void execute(Entity self, Field field) {
        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null || hc.currentHealth <= 0) return;

        GraveBusterComponent state = self.get(GraveBusterComponent.class);
        if (state == null) {
            state = new GraveBusterComponent();
            self.add(state);
        }

        Vec2d origin = self.get(PositionComponent.class).position;
        Entity targetGrave = null;

        for (Entity e : field.getEntities()) {
            if (e instanceof Grave && !e.isMarkedForRemoval()) {
                Vec2d gravePos = e.get(PositionComponent.class).position;
                if (Math.abs(gravePos.getX() - origin.getX()) < 0.1 &&
                    Math.abs(gravePos.getY() - origin.getY()) < 0.1) {

                    targetGrave = e;
                    if (!e.has(GraveBeingEatenComponent.class)) {
                        e.add(new GraveBeingEatenComponent());
                    }
                    break;
                }
            }
        }

        // If the grave is missing (e.g., destroyed by a Cherry Bomb), kill the Buster
        if (targetGrave == null) {
            hc.currentHealth = 0;
            return;
        }

        state.ticksAlive++;

        if (state.ticksAlive >= maxTicks) {
            // Destroy the grave
            CombatSystem.applyDamage(targetGrave, 99999, false);

            // Level 4 Explosion Logic
            if (explosionDamage > 0) {
                field.addProjectile(new AreaOfDamage(origin, explosionRadius, explosionDamage));
            }

            // Mark plant for removal by depleting health
            hc.currentHealth = 0;
        }
    }

    @Override
    public boolean hasTarget(@NonNull Entity self, @NonNull Field field) {
        return true;
    }
}
