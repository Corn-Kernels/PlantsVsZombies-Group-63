package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;

public class CitronBehavior implements PlantAttackBehavior {
    private final AbstractProjectile projectile;

    public CitronBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());

        // Fires its massive damage projectile when explicitly called by an external system
        field.addProjectile(projectile.clone(spawnPos));
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        // Return false so the standard PlantAttackComponent auto-cycle ignores it.
        // Your external ChargeComponent/System will be responsible for calling execute().
        return false;
    }
}
