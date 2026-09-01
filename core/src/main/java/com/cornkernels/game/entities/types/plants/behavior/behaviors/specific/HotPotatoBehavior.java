package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.HotPotatoComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class HotPotatoBehavior implements PlantAttackBehavior {
    private final float meltRadius;
    private final int totalDamage;

    public HotPotatoBehavior(float meltRadius, int totalDamage) {
        this.meltRadius = meltRadius;
        this.totalDamage = totalDamage;
    }

    @Override
    public void execute(Entity self, Field field) {
        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null || hc.currentHealth <= 0) return;

        HotPotatoComponent state = self.get(HotPotatoComponent.class);
        if (state == null) {
            state = new HotPotatoComponent();
            self.add(state);
        }

        state.ticksAlive++;

        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5;
        double originY = origin.getY();

        // 1. Melt Ice on Plants (Checks a square radius bounding box)
        for (PlantInstance plant : field.getActivePlants()) {
            if (plant == self || plant.isMarkedForRemoval()) continue;

            Vec2d targetPos = plant.get(PositionComponent.class).position;
            double targetX = targetPos.getX() + 0.5;
            double targetY = targetPos.getY();

            if (Math.abs(targetX - originX) <= meltRadius && Math.abs(targetY - originY) <= meltRadius) {
                PlantFreezeComponent freezeComp = plant.get(PlantFreezeComponent.class);
                if (freezeComp != null && freezeComp.frozenHp > 0) {
                    freezeComp.frozenHp = 0;
                    freezeComp.freezeLayers = 0;
                    // TODO: Update PAM animation to remove the ice block visuals
                }
            }
        }

        // 2. Deal slow damage to zombies in the radius over the 30 ticks
        int damagePerTick = totalDamage / state.maxTicks;
        if (damagePerTick > 0) {
            for (Entity target : field.getEntities()) {
                if ((target instanceof ZombieInstance || target instanceof Grave) && !target.isMarkedForRemoval()) {
                    Vec2d targetPos = target.get(PositionComponent.class).position;
                    double targetX = targetPos.getX() + 0.5;
                    double targetY = targetPos.getY();

                    if (Math.abs(targetX - originX) <= meltRadius && Math.abs(targetY - originY) <= meltRadius) {
                        CombatSystem.applyDamage(target, damagePerTick, false);
                    }
                }
            }
        }

        // 3. Die after 1.5 seconds (30 ticks)
        if (state.ticksAlive >= state.maxTicks) {
            hc.currentHealth = 0;
            self.markForRemoval();

        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true; // Instantly runs upon placement and ticks every frame
    }
}
