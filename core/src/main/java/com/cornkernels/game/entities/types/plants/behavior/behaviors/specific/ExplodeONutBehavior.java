package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.map.Field;

public class ExplodeONutBehavior implements PlantAttackBehavior {
    private final int explosionDamage;
    private final float radius = 1.5f;

    public ExplodeONutBehavior(int explosionDamage) {
        this.explosionDamage = explosionDamage;
    }

    @Override
    public void execute(Entity self, Field field) {
        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null) return;

        // Since Explode-o-nut's base health is doubled in PlantDef,
        // its "real" death happens when it drops to exactly 50% maxHealth.
        int realDeathThreshold = hc.maxHealth / 2;

        if (hc.currentHealth <= realDeathThreshold && hc.currentHealth > 0) {

            // TODO: Spawn AreaOfDamage using the 'radius' (1.5f) and 'explosionDamage'
            // Example: field.addProjectile(new AreaOfDamage(self.get(PositionComponent.class).position, radius, explosionDamage));

            // Snap health exactly to 0 so the standard entity removal system destroys the plant safely
            hc.currentHealth = 0;
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true; // Continuously evaluate the health threshold
    }
}
