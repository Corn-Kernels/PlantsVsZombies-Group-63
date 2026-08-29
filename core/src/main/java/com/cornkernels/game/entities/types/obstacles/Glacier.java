package com.cornkernels.game.entities.types.obstacles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class Glacier extends PushableObstacle {

    private boolean hasSpawnedZombie = false;
    private final ZombieDef containedZombie;

    public Glacier(Vec2d position,ZombieDef containedZombie) {
        super(position, 600);
        this.containedZombie = containedZombie;
    }

    @Override
    public void update(Field field) {
        HealthComponent hp = this.get(HealthComponent.class);

        if (!hasSpawnedZombie && (hp.currentHealth <= 600 || this.isMarkedForRemoval())) {
            this.hasSpawnedZombie = true;

            Vec2d currentPos = this.get(PositionComponent.class).position;
            GridPosition gridPos = GridPosition.fromContinuous(currentPos);

            // Spawn the freed zombie exactly where the glacier broke
            field.addZombie(new ZombieInstance(containedZombie, currentPos));
            this.markForRemoval();
        }
    }
}
