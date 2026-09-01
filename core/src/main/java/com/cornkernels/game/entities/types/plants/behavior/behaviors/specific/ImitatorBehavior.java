package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.ExplosionTimerComponent;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class ImitatorBehavior implements PlantAttackBehavior {

    private final PlantDef targetDef;
    private final float transformationTimeSeconds;

    public ImitatorBehavior(PlantDef targetDef, float transformationTimeSeconds) {
        this.targetDef = targetDef;
        this.transformationTimeSeconds = transformationTimeSeconds;
    }

    @Override
    public void execute(Entity self, Field field) {
        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null || hc.currentHealth <= 0) return;

        ExplosionTimerComponent timer = self.get(ExplosionTimerComponent.class);
        if (timer == null) {
            timer = new ExplosionTimerComponent();
            self.add(timer);
        }

        timer.timeElapsed += (1.0f / 20.0f);

        if (timer.timeElapsed >= transformationTimeSeconds) {
            GridPosition pos = GridPosition.fromContinuous(self.get(PositionComponent.class).position);

            hc.currentHealth = 0;
            self.markForRemoval();

        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }
}
