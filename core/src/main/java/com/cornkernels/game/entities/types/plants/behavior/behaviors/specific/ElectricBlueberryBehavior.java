package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.LightningCloudProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElectricBlueberryBehavior implements PlantAttackBehavior {
    private final int damage;
    private final boolean priorityTargeting;
    private final Random random = new Random();

    public ElectricBlueberryBehavior(int damage, boolean priorityTargeting) {
        this.damage = damage;
        this.priorityTargeting = priorityTargeting;
    }

    @Override
    public void execute(Entity self, Field field) {
        List<Entity> validTargets = new ArrayList<>();
        for (Entity e : field.getEntities()) {
            if ((e instanceof ZombieInstance || e instanceof Grave) && !e.isMarkedForRemoval()) {
                validTargets.add(e);
            }
        }

        if (validTargets.isEmpty()) return;

        Entity chosenTarget = null;

        // Level 3+ Logic: Seek out the target with the highest base HP
        if (priorityTargeting) {
            int maxHpFound = -1;
            for (Entity e : validTargets) {
                HealthComponent hc = e.get(HealthComponent.class);
                if (hc != null && hc.maxHealth > maxHpFound) {
                    maxHpFound = hc.maxHealth;
                    chosenTarget = e;
                }
            }
        }

        // Level 1-2 Logic (or fallback): Pick a completely random target
        if (chosenTarget == null) {
            chosenTarget = validTargets.get(random.nextInt(validTargets.size()));
        }

        Vec2d origin = self.get(PositionComponent.class).position;
        Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());

        field.addProjectile(new LightningCloudProjectile(damage, spawnPos, chosenTarget));
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        // Only fires if there is at least one valid enemy on the board
        for (Entity e : field.getEntities()) {
            if ((e instanceof Grave || e instanceof ZombieInstance) && !e.isMarkedForRemoval()) {
                return true;
            }
        }
        return false;
    }
}
