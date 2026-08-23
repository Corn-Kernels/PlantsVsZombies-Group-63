package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GraveBeingEatenComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;

import java.util.ArrayList;
import java.util.List;

public class CombatSystem extends EntitySystem {

    public static void applyDamage(Entity target, int amount, boolean ignoresArmor) {
        int remaining = amount;

        if (!ignoresArmor) {
            for (ArmorComponent armor : target.getAll(ArmorComponent.class)) {
                if (remaining <= 0) break;
                remaining = armor.absorbDamage(remaining);
            }
        }

        if (remaining > 0) {
            HealthComponent health = target.get(HealthComponent.class);
            if (health == null) return;
            health.adjustHealth(-remaining);

            if (health.isDead()) {
                target.markForRemoval();
                ZombieStateComponent state = target.get(ZombieStateComponent.class);
                if (state != null) state.state = ZombieStateComponent.State.DEAD;
            }
        }
    }

    @Override
    public void update(float delta) {

        // 1. Pre-filter potential targets ONCE outside the projectile loop
        List<Entity> validTargets = new ArrayList<>();
        for (Entity e : field.getEntities()) {
            if (!e.isMarkedForRemoval() && (e instanceof ZombieInstance || e instanceof com.cornkernels.game.entities.types.obstacles.Grave)) {
                validTargets.add(e);
            }
        }

        // 2. Iterate through active projectiles
        for (AbstractProjectile projectile : List.copyOf(field.getActiveProjectiles())) {
            if (projectile.isMarkedForRemoval()) continue;

            var pos = projectile.get(PositionComponent.class).position;

            // Clean up out-of-bounds projectiles
            if (pos.getX() < 0 || pos.getX() > field.getTotalColumns()) {
                projectile.markForRemoval();
                continue;
            }

            // 3. Test collisions against our pre-filtered targets
            for (Entity target : validTargets) {
                // Must re-check removal here! A previous projectile in this exact same frame
                // might have just killed this target, and we don't want to hit a corpse.
                if (target.isMarkedForRemoval()) continue;
                // graves that are being eaten shouldnt get hit
                if (target.has(GraveBeingEatenComponent.class))continue;
                // Checking all projectiles since each one has a different hitbox logic
                if (!projectile.hit(target,field)) {
                    continue;
                }

                // If hit() returns true, mark the projectile for removal and stop checking targets
                // (Note: AreaOfDamage and LineOfDamage return false so they can pierce/hit multiple)
                projectile.markForRemoval();
                break;
            }
        }
    }
}
