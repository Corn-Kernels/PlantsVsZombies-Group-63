package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GraveBeingEatenComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.SunInfectedComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;

import java.util.ArrayList;
import java.util.List;

public class CombatSystem extends EntitySystem {

    public static void applyDamage(Entity target, int amount, boolean ignoresArmor) {
        applyDamage(target, amount, ignoresArmor, null);
    }

    public static void applyDamage(Entity target, int amount, boolean ignoresArmor, Field field) {
        if (target.has(OctoedComponent.class)) {
            OctoedComponent octo = target.get(OctoedComponent.class);
            octo.currentHealth -= amount;
            if (octo.currentHealth <= 0) {
                target.removeAll(OctoedComponent.class); // Octopus destroyed, plant freed!
            }
            return; // Exit early so the plant's health is completely shielded
        }

        int remaining = amount;
        int initialRemaining = remaining;

        if (!ignoresArmor) {
            for (ArmorComponent armor : target.getAll(ArmorComponent.class)) {
                if (remaining <= 0) break;
                remaining = armor.absorbDamage(remaining);
            }
        }

        int healthDamage = 0;
        HealthComponent health = target.get(HealthComponent.class);

        if (remaining > 0 && health != null) {
            healthDamage = Math.min(health.currentHealth, remaining);
            health.adjustHealth(-remaining);

            if (health.isDead()) {
                target.markForRemoval();
                ZombieStateComponent state = target.get(ZombieStateComponent.class);
                if (state != null) state.state = ZombieStateComponent.State.DEAD;
            }
        }

        if (field != null && target.has(SunInfectedComponent.class)) {
            int totalDamageDealt = (initialRemaining - remaining) + healthDamage;

            if (health != null && health.isDead()) {
                for (ArmorComponent armor : target.getAll(ArmorComponent.class)) {
                    totalDamageDealt += armor.currentArmorHealth;
                }
            }

            if (totalDamageDealt > 0) {
                for (SunInfectedComponent infection : target.getAll(SunInfectedComponent.class)) {
                    infection.accumulatedDamage += totalDamageDealt;

                    while (infection.accumulatedDamage >= 25) {
                        infection.accumulatedDamage -= 25;

                        Vec2d pos = target.get(PositionComponent.class).position;
                        field.addSun(new SunInstance(SunType.SMALL, (int) pos.getY(), (int) pos.getX(), 0));
                    }
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        List<Entity> validTargets = new ArrayList<>();
        for (Entity e : field.getEntities()) {
            if (!e.isMarkedForRemoval() && (e instanceof ZombieInstance || e instanceof Grave)) {
                validTargets.add(e);
            }
            if (!e.isMarkedForRemoval() && e instanceof PlantInstance && e.has(OctoedComponent.class)){
                validTargets.add(e);
            }
        }

        for (AbstractProjectile projectile : List.copyOf(field.getActiveProjectiles())) {
            if (projectile.isMarkedForRemoval()) continue;

            Vec2d pos = projectile.get(PositionComponent.class).position;

            if (pos.getX() < 0 || pos.getX() > field.getTotalColumns()) {
                projectile.markForRemoval();
                continue;
            }

            for (Entity target : validTargets) {
                if (target.isMarkedForRemoval()) continue;
                if (target.has(GraveBeingEatenComponent.class)) continue;

                if (!projectile.hit(target, field)) {
                    continue;
                }

                projectile.markForRemoval();
                break;
            }
        }
        validTargets=new ArrayList<>();
        for (Entity e : field.getEntities()) {
            if (!e.isMarkedForRemoval() && (e instanceof PlantInstance)) {
                validTargets.add(e);
            }
        }

        for (AbstractZombieProjectile projectile : List.copyOf(field.getActiveZombieProjectiles())) {
            if (projectile.isMarkedForRemoval()) continue;

            Vec2d pos = projectile.get(PositionComponent.class).position;

            if (pos.getX() < 0 || pos.getX() > field.getTotalColumns()) {
                projectile.markForRemoval();
                continue;
            }

            for (Entity target : validTargets) {
                if (target.isMarkedForRemoval()) continue;

                if (!projectile.hit(target, field)) {
                    continue;
                }

                projectile.markForRemoval();
                break;
            }
        }

    }
}
