package com.cornkernels.game.entities.types.plants.behavior;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.obstacles.PushableObstacle;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public interface PlantAttackBehavior {

    void execute(Entity self, Field field);

    default boolean hasTarget(@NotNull Entity self, @NotNull Field field) {
        List<Entity> entities = getAllValidTargets(field);

        for (Entity entity : entities) {
            if (entity.has(PositionComponent.class)) {
                PositionComponent positionComponent = entity.get(PositionComponent.class);
                if (positionComponent.position.getY() == self.get(PositionComponent.class).position.getY()) {
                    return true;
                }
            }
        }

        return false;
    }

    default boolean isValidTarget(Entity e) {
        if (e == null || e.isMarkedForRemoval()) return false;

        // 1. Zombies (Not Hypnotized)
        if (e instanceof ZombieInstance z) {
            return !z.has(HypnoComponent.class);
        }

        // 2. Obstacles
        if (e instanceof Grave || e instanceof PushableObstacle) {
            return true;
        }

        // 3. Debuffed Plants (Octopus or Ice Block)
        if (e instanceof PlantInstance p) {
            PlantFreezeComponent freeze = p.get(PlantFreezeComponent.class);
            boolean isFrozenBlock = (freeze != null && freeze.freezeLayers >= 3);
            return p.has(OctoedComponent.class) || isFrozenBlock;
        }

        return false;
    }

    default List<Entity> getAllValidTargets(@NonNull Field field) {
        List<Entity> targets = new ArrayList<>();
        for (Entity e : field.getEntities()) {
            if (isValidTarget(e)) {
                targets.add(e);
            }
        }
        return targets;
    }
}
