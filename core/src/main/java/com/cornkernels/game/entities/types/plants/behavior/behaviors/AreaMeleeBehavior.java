package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.GrowthComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class AreaMeleeBehavior implements PlantAttackBehavior {
    private final int[] damages;
    private final float[] ranges;
    private final float[] hpThresholds;
    private final boolean usesGrowth;

    public AreaMeleeBehavior(int[] damages, float[] ranges, float[] hpThresholds) {
        this.damages = damages;
        this.ranges = ranges;
        this.hpThresholds = hpThresholds;
        this.usesGrowth = hpThresholds.length > 0;
    }

    @Override
    public void execute(Entity self, Field field) {
        int stage = getStage(self);
        int index = stage - 1; // Adjust the 1-based stage to a 0-based array index

        float currentRange = ranges[index];
        int currentDamage = damages[index];

        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5;
        double originY = origin.getY();

        for (Entity target : field.getEntities()) {
            if ((target instanceof ZombieInstance || target instanceof Grave) && !target.isMarkedForRemoval()) {
                Vec2d targetPos = target.get(PositionComponent.class).position;
                double targetX = targetPos.getX() + 0.5;
                double targetY = targetPos.getY();

                if (Math.abs(targetX - originX) <= currentRange && Math.abs(targetY - originY) <= currentRange) {
                    CombatSystem.applyDamage(target, currentDamage, false);
                }
            }
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        int stage = getStage(self);
        int index = stage - 1; // Adjust the 1-based stage to a 0-based array index

        float currentRange = ranges[index];

        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5;
        double originY = origin.getY();

        for (Entity target : field.getEntities()) {
            if ((target instanceof ZombieInstance || target instanceof Grave) && !target.isMarkedForRemoval()) {
                Vec2d targetPos = target.get(PositionComponent.class).position;
                double targetX = targetPos.getX() + 0.5;
                double targetY = targetPos.getY();

                if (Math.abs(targetX - originX) <= currentRange && Math.abs(targetY - originY) <= currentRange) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getStage(Entity self) {
        if (!usesGrowth) return 1;

        GrowthComponent growth = self.get(GrowthComponent.class);
        if (growth == null) {
            growth = new GrowthComponent();
            self.add(growth);
        }

        HealthComponent hc = self.get(HealthComponent.class);
        if (hc != null) {
            float hpPercent = (float) hc.currentHealth / hc.maxHealth;

            int newStage = 1;
            // If it drops below threshold[0], it becomes stage 2. If it drops below threshold[1], it becomes stage 3.
            for (int i = 0; i < hpThresholds.length; i++) {
                if (hpPercent <= hpThresholds[i]) {
                    newStage = i + 2;
                }
            }

            if (newStage != growth.stage) {
                growth.stage = newStage;
                // TODO: Update your PamAnimationComponent here to show Kiwibeast getting bigger!
            }
        }
        return growth.stage;
    }
}
