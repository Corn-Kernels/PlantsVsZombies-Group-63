package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;


import java.util.ArrayList;
import java.util.List;

public class HomingShotBehavior implements PlantAttackBehavior {

    private final int shotCount;
    private final int damagePerShot;
    private final double shotSpacing = 0.3f;

    public HomingShotBehavior(int shotCount, int damagePerShot) {
        this.shotCount = shotCount;
        this.damagePerShot = damagePerShot;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        List<Entity> laneTargets = new ArrayList<>();

        for (Entity e : field.getEntities()) {
            if (e instanceof ZombieInstance || e instanceof Grave) {
                Vec2d targetPos = e.get(PositionComponent.class).position;

                laneTargets.add(e);//removed condition for lane and horizontal position
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
            field.addProjectile(new LobProjectile(damagePerShot, spawnPos, closestTarget));
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        boolean flag = false;
        for (Entity e : field.getEntities()) {
            if (e instanceof Grave || e instanceof ZombieInstance) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
