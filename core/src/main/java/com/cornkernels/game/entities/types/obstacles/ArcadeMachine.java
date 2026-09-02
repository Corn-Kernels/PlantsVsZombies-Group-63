package com.cornkernels.game.entities.types.obstacles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class ArcadeMachine extends PushableObstacle {

    private final int spawnIntervalTicks;
    private int tickCounter = 0;

    public ArcadeMachine(Vec2d position, int maxHp, float spawnIntervalSeconds) {
        super(position, maxHp);
        this.spawnIntervalTicks = (int) (spawnIntervalSeconds * 20);
        add(new PamAnimationComponent());
    }

    @Override
    public void update(Field field) {
        HealthComponent hp = this.get(HealthComponent.class);
        if (hp.currentHealth <= 0 || this.isMarkedForRemoval()) {
            return;
        }

        tickCounter++;
        if (tickCounter >= spawnIntervalTicks) {
            tickCounter = 0;
            Vec2d currentPos = this.get(PositionComponent.class).position;
            field.addZombie(new ZombieInstance(ZombieDef.DEFAULT, currentPos));
        }
    }
}
