package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;

import java.util.ArrayList;
import java.util.List;

public class HomingShotBehavior implements PlantAttackBehavior {

    private final int shotCount;
    private final double shotSpacing = 0.3f;
    private final AbstractProjectile projectile;

    public HomingShotBehavior(int shotCount, AbstractProjectile projectile) {
        this.shotCount = shotCount;
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        List<Entity> laneTargets = new ArrayList<>();

        for (Entity e : field.getEntities()) {
            if ((e instanceof ZombieInstance || e instanceof Grave) && !e.isMarkedForRemoval()) {
                if (e instanceof ZombieInstance && e.has(HypnoComponent.class)) continue;
                laneTargets.add(e);
            }
        }

        if (laneTargets.isEmpty()) {
            return;
        }

        Entity closestTarget = laneTargets.get(0);
        double minX = closestTarget.get(PositionComponent.class).position.getX();

        for (Entity target : laneTargets) {
            double currentX = target.get(PositionComponent.class).position.getX();
            if (currentX < minX) {
                minX = currentX;
                closestTarget = target;
            }
        }

        for (int i = 0; i < shotCount; i++) {
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5 + i * shotSpacing), origin.getY());
            AbstractProjectile spawned = projectile.clone(spawnPos);
            if (spawned instanceof LobProjectile) {
                ((LobProjectile) spawned).target = closestTarget;
            }
            if (spawned instanceof HomingProjectile) {
                ((HomingProjectile) spawned).target = closestTarget;
            }
            field.addProjectile(spawned);
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        for (Entity e : field.getEntities()) {
            if ((e instanceof Grave || e instanceof ZombieInstance) && !e.isMarkedForRemoval()) {
                if (e instanceof ZombieInstance && e.has(HypnoComponent.class)) continue;
                return true;
            }
        }
        return false;
    }
}
