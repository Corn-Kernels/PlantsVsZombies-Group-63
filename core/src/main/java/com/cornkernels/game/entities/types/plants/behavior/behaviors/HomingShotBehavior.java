package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.HypnoHomingProjectile;
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

    private List<Entity> getAvailableTargets(Field field) {
        // Use the universal targeting logic to get all generally valid targets on the board
        List<Entity> availableTargets = getAllValidTargets(field);

        // If this plant fires HypnoHomingProjectiles, filter out any zombie already being targeted by one in-flight
        if (projectile instanceof HypnoHomingProjectile) {
            List<Entity> alreadyTargeted = new ArrayList<>();
            for (Entity e : field.getEntities()) {
                if (e instanceof HypnoHomingProjectile hypnoProj && hypnoProj.target != null) {
                    alreadyTargeted.add(hypnoProj.target);
                }
            }
            availableTargets.removeAll(alreadyTargeted);
        }

        return availableTargets;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        List<Entity> availableTargets = getAvailableTargets(field);

        if (availableTargets.isEmpty()) {
            return;
        }

        Entity closestTarget = availableTargets.get(0);
        double minX = closestTarget.get(PositionComponent.class).position.getX();

        for (Entity target : availableTargets) {
            double currentX = target.get(PositionComponent.class).position.getX();
            if (currentX < minX) {
                minX = currentX;
                closestTarget = target;
            }
        }

        for (int i = 0; i < shotCount; i++) {
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5 + i * shotSpacing), origin.getY());
            AbstractProjectile spawned = projectile.clone(spawnPos);

            if (spawned instanceof LobProjectile lobProj) {
                lobProj.target = closestTarget;
            }
            if (spawned instanceof HomingProjectile homingProj) {
                homingProj.target = closestTarget;
            }
            field.addProjectile(spawned);
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        // The plant only considers itself as having a target if there's an unclaimed valid target available
        return !getAvailableTargets(field).isEmpty();
    }
}
