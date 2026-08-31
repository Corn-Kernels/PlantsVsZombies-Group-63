package com.cornkernels.game.entities.types.plants.behavior.plantfoods;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.PuffShroomComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;

public class PuffShroomPlantFoodBehavior implements PlantFoodBehavior {

    private final AbstractProjectile projectile;

    public PuffShroomPlantFoodBehavior(AbstractProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public boolean plantFood(PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0) {

            // 1. Swarm Cascade Trigger (Only fires on the exact starting tick)
            if (pf.timerTicks == pf.normalTime) {
                for (PlantInstance p : field.getActivePlants()) {
                    if (p.isMarkedForRemoval()) continue;

                    PuffShroomComponent puffComp = p.get(PuffShroomComponent.class);
                    if (puffComp != null) {
                        // Reset the lifespan clock for ALL shrooms
                        puffComp.lifeTimer = 0.0f;

                        // Trigger Plant Food for the others, but set to (normalTime - 1)
                        // so they skip this exact block and don't cause an infinite loop!
                        if (p != plant) {
                            PlantFoodComponent targetPf = p.get(PlantFoodComponent.class);
                            if (targetPf != null) {
                                targetPf.timerTicks = targetPf.normalTime - 1;
                            }
                        }
                    }
                }
            }

            // 2. Rapid Fire: Fire a spore every other tick
            if (pf.timerTicks % 2 == 0) {
                Vec2d origin = plant.get(PositionComponent.class).position;
                Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());
                field.addProjectile(projectile.clone(spawnPos));
            }

            pf.timerTicks--;
            return false; // Reject new plant food while active
        }

        return true;
    }
}
