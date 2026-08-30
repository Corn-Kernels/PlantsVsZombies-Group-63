package com.cornkernels.game.entities.types.plants.behavior.plantfoods.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;

public class RepeaterPlantFoodBehavior implements PlantFoodBehavior {

    private final AbstractProjectile normalProjectile;
    private final AbstractProjectile bigProjectile;

    public RepeaterPlantFoodBehavior(AbstractProjectile normalProjectile, AbstractProjectile bigProjectile) {
        this.normalProjectile = normalProjectile;
        this.bigProjectile = bigProjectile;
    }

    @Override
    public boolean plantFood(PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0&&pf.timerTicks%2==0) {
            Vec2d origin = plant.get(PositionComponent.class).position;
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());

            // 1. First 60 ticks: Rapid fire normal peas
            if (pf.timerTicks > 20) {
                field.addProjectile(normalProjectile.clone(spawnPos));
            }
            // 2. Ticks 20 down to 2: Do nothing (Dramatic pause)
            // 3. Tick 1: Fire the massive finishing pea
            else if (pf.timerTicks == 1) {
                field.addProjectile(bigProjectile.clone(spawnPos));
            }

            pf.timerTicks--;
            return false; // Reject new plant food while active
        }

        return true;
    }
}
