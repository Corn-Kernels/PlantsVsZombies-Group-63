package com.cornkernels.game.entities.types.projectile.ZombieProjectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.map.Field;

public class SnowballProjectile extends AbstractZombieProjectile {

    public SnowballProjectile(Vec2d startPosition) {
        // Moves left at 0.15 speed, 0 damage (it strictly applies debuffs)
        super(0, new Vec2d(-0.15f, 0), startPosition);
    }

    @Override
    public boolean hit(Entity target, Field field) {
        if (super.hit(target, field)) {
            PlantFreezeComponent freeze = target.get(PlantFreezeComponent.class);
            if (freeze == null) {
                freeze = new PlantFreezeComponent();
                target.add(freeze);
            }

            freeze.freezeLayers++;
            if (freeze.freezeLayers >= 3) {
                freeze.frozenHp = 600;
            }

            return true; // Destroy snowball on impact
        }
        return false;
    }
}
