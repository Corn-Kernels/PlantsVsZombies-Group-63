package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.plant_specific.MintComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantDefComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.types.plants.PlantCategory;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.map.Field;
import org.jetbrains.annotations.NotNull;

public class MintBehavior implements PlantAttackBehavior {

    private final float lifespanSeconds;

    public MintBehavior(float lifespanSeconds) {
        this.lifespanSeconds = lifespanSeconds;
    }

    @Override
    public void execute(Entity self, Field field) {
        MintComponent comp = self.get(MintComponent.class);
        if (comp == null) {
            comp = new MintComponent();
            self.add(comp);
        }

        // 1. Manage Lifespan
        float dt = 1.0f / 20.0f; // 20 ticks per second
        comp.lifeTimer += dt;

        if (comp.lifeTimer >= lifespanSeconds) {
            self.markForRemoval();
            return;
        }

        // 2. Execute Plant Food Activation (Exactly Once)
        if (!comp.hasActivated) {
            comp.hasActivated = true;

            PlantDefComponent myDefComp = self.get(PlantDefComponent.class);
            if (myDefComp == null) return;

            PlantCategory targetCategory = myDefComp.def().getCategory();

            for (PlantInstance p : field.getActivePlants()) {
                if (p.isMarkedForRemoval() || p == self) continue;

                PlantDefComponent targetDefComp = p.get(PlantDefComponent.class);
                if (targetDefComp != null && targetDefComp.def().getCategory() == targetCategory) {

                    PlantFoodComponent targetPf = p.get(PlantFoodComponent.class);
                    if (targetPf != null) {
                        // Instantly start or refresh their plant food timer
                        targetPf.activate();
                    }
                }
            }
        }
    }

    @Override
    public boolean hasTarget(@NotNull Entity self, @NotNull Field field) {
        // Mints act immediately and independently of zombie presence
        return true;
    }
}
