package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class SplitPeaBehavior implements PlantAttackBehavior {
    private final AbstractProjectile projectile;
    private final double shotSpacing = 0.3f;

    public SplitPeaBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(origin).lane();

        boolean targetForward = false;
        boolean targetBackward = false;

        for (ZombieInstance z : field.getZombiesInLane(lane)) {
            if (!z.isMarkedForRemoval()) {
                double zX = z.get(PositionComponent.class).position.getX();
                if (zX >= origin.getX()) {
                    targetForward = true;
                } else {
                    targetBackward = true;
                }
            }
        }

        // Shoot 1 forward
        if (targetForward) {
            field.addProjectile(projectile.clone(new Vec2d(origin.getX() + 0.5f, origin.getY())));
        }

        // Shoot 2 backwards
        if (targetBackward) {
            for (int i = 0; i < 2; i++) {
                // FIXED: + i * shotSpacing means the 2nd pea spawns further right (closer to plant), correctly trailing!
                AbstractProjectile backPea = projectile.clone(new Vec2d((float) (origin.getX() - 0.5 + i * shotSpacing), origin.getY()));

                // FIXED: Clean in-place mutation using your Vec2d class
                VelocityComponent velComp = backPea.get(VelocityComponent.class);
                if (velComp != null && velComp.velocityPerTick != null) {
                    velComp.velocityPerTick.setX(-Math.abs(velComp.velocityPerTick.getX()));
                }

                field.addProjectile(backPea);
            }
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        int lane = GridPosition.fromContinuous(self.get(PositionComponent.class).position).lane();
        return field.getZombiesInLane(lane).stream().anyMatch(z -> !z.isMarkedForRemoval());
    }
}
