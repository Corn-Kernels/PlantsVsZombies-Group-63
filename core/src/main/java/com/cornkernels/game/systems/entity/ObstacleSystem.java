package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.types.obstacles.AbstractObstacle;
import com.cornkernels.game.entities.types.obstacles.PushableObstacle;

public class ObstacleSystem extends EntitySystem {

    @Override
    public void update(float delta) {
        for (AbstractObstacle obstacle : field.getActiveObstacles()) {
            if (obstacle instanceof PushableObstacle pushable) {
                pushable.update(field);
            }
        }
    }
}
