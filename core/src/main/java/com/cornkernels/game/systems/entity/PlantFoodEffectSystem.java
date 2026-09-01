package com.cornkernels.game.systems.entity;

import com.cornkernels.game.entities.types.effects.PlantFoodEffect;

public class PlantFoodEffectSystem extends EntitySystem {

    @Override
    public void update(float delta) {
        for (PlantFoodEffect effect : field.getActiveEffects()) {
            effect.tickEnding(delta);
        }
    }
}
