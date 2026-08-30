package com.cornkernels.game.entities.types.projectile.ZombieProjectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.map.Field;

public class OctopusProjectile extends AbstractZombieProjectile {

    private final PlantInstance targetPlant;

    public OctopusProjectile(Vec2d startPosition, PlantInstance targetPlant) {
        super(0, new Vec2d(0, 0), startPosition);
        this.targetPlant = targetPlant;
    }

    public PlantInstance getTargetPlant() {
        return targetPlant;
    }

    @Override
    public boolean hit(Entity target, Field field) {
        // Only evaluate a hit if the CombatSystem is checking against our specific target plant
        if (target != targetPlant) return false;

        Vec2d currentPos = get(PositionComponent.class).position;
        Vec2d targetPos = targetPlant.get(PositionComponent.class).position;

        double targetX = targetPos.getX();

        // Hits when the X coordinate is within +- 0.2 of the target
        if (currentPos.getX() <= targetX + 0.2 && currentPos.getX() >= targetX - 0.2) {

            if (!targetPlant.has(OctoedComponent.class)) {
                targetPlant.add(new OctoedComponent(600)); // Octopus HP
            }
            return true; // Mark for removal
        }
        return false;
    }
}
