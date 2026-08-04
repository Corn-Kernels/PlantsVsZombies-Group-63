package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.grid.GridPosition;

import java.util.List;

public class CombatSystem extends EntitySystem {

    private static final double HIT_DISTANCE = 0.5;

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
        for (AbstractProjectile projectile : List.copyOf(field.getActiveProjectiles())) {
            if (projectile.isMarkedForRemoval()) continue;

            var pos = projectile.get(PositionComponent.class).position;

            if (pos.getX() < 0 || pos.getX() > field.getTotalColumns()) {
                projectile.markForRemoval();
                continue;
            }

            int lane = GridPosition.fromContinuous(pos).lane();
            for (ZombieInstance zombie : field.getZombiesInLane(lane)) {
                if (zombie.isMarkedForRemoval()) continue;

                double distance = Math.abs(zombie.get(PositionComponent.class).position.getX() - pos.getX());
                if (distance < HIT_DISTANCE) {
                    if (projectile instanceof LobProjectile) {
                        if (zombie != ((LobProjectile) projectile).target)
                            continue;
                    }
                    if (projectile instanceof HomingProjectile) {
                        if (zombie != ((HomingProjectile) projectile).target)
                            continue;
                    }
                    applyDamage(zombie, projectile.get(DamageComponent.class).amount, false);
                    projectile.markForRemoval();
                    break;
                }
            }
        }
    }
}
