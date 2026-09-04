package com.cornkernels.game.systems.entity;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GraveBeingEatenComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDeathComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.SunInfectedComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.obstacles.PushableObstacle;
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

    private static float plantDamageMultiplier = 1f;

    public static void setPlantDamageMultiplier(float multiplier) {
        plantDamageMultiplier = multiplier;
    }

    public static void applyDamage(Entity target, int amount, boolean ignoresArmor) {
        applyDamage(target, amount, ignoresArmor, null);
    }

    public static void applyDamage(Entity target, int amount, boolean ignoresArmor, Field field) {
        if (isInvulnerable(target)) return;
        if (target instanceof ZombieInstance) amount = Math.round(amount * plantDamageMultiplier);
        if (handleShields(target, amount)) return;

        int initialRemaining = amount;
        int remaining = ignoresArmor ? amount : handleArmor(target, amount);
        int healthDamage = handleHealthDamage(target, remaining);

        applySunInfection(target, field, initialRemaining, remaining, healthDamage);
    }

    private static boolean isInvulnerable(Entity target) {
        if (target instanceof PlantInstance && target.has(PlantFoodComponent.class)) {
            PlantFoodComponent plantFoodComponent = target.get(PlantFoodComponent.class);
            return plantFoodComponent.isActive();
        }
        return false;
    }

    private static boolean handleShields(Entity target, int amount) {
        if (target.has(OctoedComponent.class)) {
            OctoedComponent octo = target.get(OctoedComponent.class);
            octo.currentHealth -= amount;
            if (octo.currentHealth <= 0) target.removeAll(OctoedComponent.class);
            return true;
        }

        PlantFreezeComponent freezeComp = target.get(PlantFreezeComponent.class);
        if (freezeComp != null && freezeComp.frozenHp > 0) {
            freezeComp.frozenHp -= amount;
            if (freezeComp.frozenHp <= 0) {
                freezeComp.frozenHp = 0;
                freezeComp.freezeLayers = 0;
            }
            return true;
        }
        return false;
    }

    private static int handleArmor(Entity target, int amount) {
        int remaining = amount;
        List<ArmorComponent> destroyedArmors = new ArrayList<>();

        for (ArmorComponent armor : target.getAll(ArmorComponent.class)) {
            if (remaining <= 0) break;
            remaining = armor.absorbDamage(remaining);
            if (armor.currentArmorHealth <= 0) destroyedArmors.add(armor);
        }

        for (ArmorComponent brokenArmor : destroyedArmors) {
            target.remove(ArmorComponent.class, brokenArmor);
        }
        return remaining;
    }

    private static int handleHealthDamage(Entity target, int remaining) {
        HealthComponent health = target.get(HealthComponent.class);
        if (remaining <= 0 || health == null) return 0;

        int healthDamage = Math.min(health.currentHealth, remaining);
        health.adjustHealth(-remaining);

        if (health.isDead()) {
            ZombieStateComponent state = target.get(ZombieStateComponent.class);
            if (state != null && state.state != ZombieStateComponent.State.DEAD) {
                state.changeState(ZombieStateComponent.State.DEAD);
                target.add(new ZombieDeathComponent());
            } else if (state == null) {
                target.markForRemoval();
            }
        }
        return healthDamage;
    }

    private static void applySunInfection(Entity target, Field field, int initialRemaining, int remaining, int healthDamage) {
        if (field == null || !target.has(SunInfectedComponent.class)) return;

        HealthComponent health = target.get(HealthComponent.class);
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

    @Override
    public void update(float delta) {
        processPlantProjectiles();
        processZombieProjectiles();
    }

    private void processPlantProjectiles() {
        List<Entity> validTargets = getValidPlantTargets();

        for (AbstractProjectile projectile : List.copyOf(field.getActiveProjectiles())) {
            if (projectile.isMarkedForRemoval()) continue;
            Vec2d pos = projectile.get(PositionComponent.class).position;

            if (pos.getX() < 0 || pos.getX() > field.getTotalColumns()) {
                projectile.markForRemoval();
                continue;
            }

            for (Entity target : validTargets) {
                if (target.isMarkedForRemoval() || target.has(GraveBeingEatenComponent.class)) continue;
                if (projectile.hit(target, field)) {
                    projectile.markForRemoval();
                    break;
                }
            }
        }
    }

    private List<Entity> getValidPlantTargets() {
        List<Entity> validTargets = new ArrayList<>();
        for (Entity e : field.getEntities()) {
            boolean isLiveZombie = e instanceof ZombieInstance && !e.has(HypnoComponent.class)
                && e.get(ZombieStateComponent.class).state != ZombieStateComponent.State.DEAD;

            if (!e.isMarkedForRemoval() && (isLiveZombie || e instanceof Grave || e instanceof PushableObstacle)) {
                validTargets.add(e);
            } else if (!e.isMarkedForRemoval() && e instanceof PlantInstance) {
                PlantFreezeComponent freezeComp = e.get(PlantFreezeComponent.class);
                if (e.has(OctoedComponent.class) || (freezeComp != null && freezeComp.frozenHp > 0)) {
                    validTargets.add(e);
                }
            }
        }
        return validTargets;
    }

    private void processZombieProjectiles() {
        List<Entity> validTargets = getValidZombieTargets();

        for (AbstractZombieProjectile projectile : List.copyOf(field.getActiveZombieProjectiles())) {
            if (projectile.isMarkedForRemoval()) continue;
            Vec2d pos = projectile.get(PositionComponent.class).position;

            if (pos.getX() < 0 || pos.getX() > field.getTotalColumns()) {
                projectile.markForRemoval();
                continue;
            }

            for (Entity target : validTargets) {
                if (target.isMarkedForRemoval()) continue;
                if (projectile.hit(target, field)) {
                    field.removeZombieProjectile(projectile);
                    projectile.markForRemoval();
                    break;
                }
            }
        }
    }

    private List<Entity> getValidZombieTargets() {
        List<Entity> validTargets = new ArrayList<>();
        for (Entity e : field.getEntities()) {
            if (!e.isMarkedForRemoval() && (e instanceof PlantInstance)) {
                PlantFreezeComponent freezeComp = e.get(PlantFreezeComponent.class);
                if (freezeComp == null || freezeComp.frozenHp <= 0) {
                    validTargets.add(e);
                }
            }
        }
        return validTargets;
    }
}
