package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.ChomperComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChomperBehavior implements PlantAttackBehavior {
    private final float frontRange;
    private final int digestTicks;
    private final int biteCooldownTicks;
    private final int chompDamage;
    private final int heavyThreshold = 3000;

    public ChomperBehavior(float digestSeconds, float biteCooldownSeconds, float frontRange, int chompDamage) {
        this.digestTicks = (int) (digestSeconds * 20);
        this.biteCooldownTicks = (int) (biteCooldownSeconds * 20);
        this.frontRange = frontRange;
        this.chompDamage = chompDamage;
    }

    @Override
    public void execute(Entity self, Field field) {
        ChomperComponent state = self.get(ChomperComponent.class);

        // Self-initialize the component if it hasn't been added yet
        if (state == null) {
            state = new ChomperComponent();
            self.add(state);
        }

        // --- Timer Logic ---
        if (state.isDigesting) {
            state.digestTimerTicks--;
            if (state.digestTimerTicks <= 0) {
                state.isDigesting = false;
            }
            return; // Can't attack while full
        }

        if (state.biteCooldownTicks > 0) {
            state.biteCooldownTicks--;
            return; // Can't attack while waiting on standard bite cooldown
        }

        // --- Attack Logic ---
        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5;
        int lane = GridPosition.fromContinuous(origin).lane();

        List<Entity> frontTargets = new ArrayList<>();
        for (ZombieInstance z : field.getZombiesInLane(lane)) {
            if (!z.isMarkedForRemoval()) {
                double targetX = z.get(PositionComponent.class).position.getX() + 0.5;
                if (targetX > originX && targetX - originX <= frontRange) {
                    frontTargets.add(z);
                }
            }
        }

        if (!frontTargets.isEmpty()) {
            // Sort leftmost to rightmost so we hit the closest one in front
            frontTargets.sort(Comparator.comparingDouble(a -> a.get(PositionComponent.class).position.getX()));

            Entity target = frontTargets.get(0);
            HealthComponent hc = target.get(HealthComponent.class);

            if (hc != null && hc.maxHealth >= heavyThreshold) {
                // Chomp heavy target (Gargantuars, Mecha-Football, etc.)
                CombatSystem.applyDamage(target, chompDamage, false);
                state.biteCooldownTicks = this.biteCooldownTicks;
            } else {
                // Swallow normal target
                CombatSystem.applyDamage(target, 99999, false); // Massive damage to ensure death and process armor properly
                state.isDigesting = true;
                state.digestTimerTicks = this.digestTicks;
            }
        }
    }

    @Override
    public boolean hasTarget(@NonNull Entity self, @NonNull Field field) {
        return true;
    }
}
