package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.map.Field;

public class SunBehavior implements PlantAttackBehavior {
    @Override
    public void execute(Entity self, Field field) {
        Vec2d SunSpawnPos = self.get(PositionComponent.class).position;
        field.addSun(new SunInstance(SunType.NORMAL, (int) SunSpawnPos.getY(), (int) SunSpawnPos.getX(), 0));
        //check sun.java
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;//you always make sun when you can duh;
    }
}
