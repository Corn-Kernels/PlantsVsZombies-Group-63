package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GrowthComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;

public class PeaPodBehavior implements PlantAttackBehavior {
    private final AbstractProjectile projectile;
    private final double shotSpacing = 0.3f;

    public PeaPodBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void execute(Entity self, Field field) {
        GrowthComponent peaPodState = self.get(GrowthComponent.class);

        // Self-initialize if it hasn't been added yet
        if (peaPodState == null) {
            peaPodState = new GrowthComponent();
            self.add(peaPodState);
        }

        Vec2d origin = self.get(PositionComponent.class).position;

        for (int i = 0; i < peaPodState.stage; i++) {
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5 + i * shotSpacing), origin.getY());
            field.addProjectile(projectile.clone(spawnPos));
        }
    }
}
