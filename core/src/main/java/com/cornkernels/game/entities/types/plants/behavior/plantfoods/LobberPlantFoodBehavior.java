package com.cornkernels.game.entities.types.plants.behavior.plantfoods;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantAttackComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantFoodBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.LobShotBehavior;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.specific.KernelPultBehavior;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.LobProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;

import java.util.ArrayList;
import java.util.List;

public class LobberPlantFoodBehavior implements PlantFoodBehavior {

    @Override
    public boolean plantFood(PlantInstance plant, Field field) {
        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
        if (pf == null) return false;

        if (pf.timerTicks > 0) {

            if (pf.timerTicks == 10) {
                // 1. Grab the projectile dynamically from the plant's attack logic
                PlantAttackComponent attack = plant.get(PlantAttackComponent.class);
                AbstractProjectile projectileToClone = null;

                if (attack != null) {
                    if (attack.behavior instanceof LobShotBehavior lobBehavior) {
                        projectileToClone = lobBehavior.getProjectile();
                    }
                    else if (attack.behavior instanceof KernelPultBehavior kernelPultBehavior){
                        projectileToClone = kernelPultBehavior.getSpecialProjectile();
                    }
                    // Add an 'else if' here for KernelPultBehavior if it doesn't extend LobShotBehavior
                }

                if (projectileToClone != null) {
                    Vec2d origin = plant.get(PositionComponent.class).position;
                    Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());

                    List<Entity> targets = getValidTargets(field);

                    for (Entity target : targets) {
                        AbstractProjectile spawned = projectileToClone.clone(spawnPos);

                        if (spawned instanceof LobProjectile lobProj) {
                            lobProj.target = target;
                        }

                        VelocityComponent vel = spawned.get(VelocityComponent.class);
                        if (vel != null) {
                            vel.velocityPerTick = new Vec2d(
                                vel.velocityPerTick.getX() * 3.5f,
                                vel.velocityPerTick.getY() * 3.5f
                            );
                        }

                        field.addProjectile(spawned);
                    }
                }
            }

            pf.timerTicks--;
            return false;
        }

        return true;
    }

    private List<Entity> getValidTargets(Field field) {
        List<Entity> targets = new ArrayList<>();

        for (Entity e : field.getEntities()) {
            if (e.isMarkedForRemoval()) continue;

            if (e instanceof ZombieInstance || e instanceof Grave) {
                targets.add(e);
            } else if (e instanceof PlantInstance p) {
                boolean isTargetable = false;

                //TODO: fix this
//                if (p.has(OctoedComponent.class)) {
//                    isTargetable = true;
//                } else {
//                    PlantFreezeComponent freeze = p.get(PlantFreezeComponent.class);
//                    if (freeze != null && freeze.frozenHp > 0) {
//                        isTargetable = true;
//                    }
//                }

                if (isTargetable) {
                    targets.add(p);
                }
            }
        }

        return targets;
    }
}
