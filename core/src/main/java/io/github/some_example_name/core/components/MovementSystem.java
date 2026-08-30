package io.github.some_example_name.core.systems;

import io.github.some_example_name.core.Entity;
import io.github.some_example_name.core.System;
import io.github.some_example_name.core.components.PositionComponent;
import io.github.some_example_name.core.components.VelocityComponent;
import java.util.List;

public class MovementSystem extends System{
    @Override
    public void update(float delta,List<Entity>entities){
        for(Entity entity:entities){
            if (entity.hasComponent(PositionComponent.class) && entity.hasComponent(VelocityComponent.class)) {
                PositionComponent pos = entity.getComponent(PositionComponent.class);
                VelocityComponent vel = entity.getComponent(VelocityComponent.class);
                pos.x += vel.vx * delta;
                pos.y += vel.vy * delta;
            }
        }
    }
}
