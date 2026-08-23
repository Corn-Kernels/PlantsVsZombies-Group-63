package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.TangleKelpComponent;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.entity.CombatSystem;

import java.util.ArrayList;
import java.util.List;

public class TangleKelpBehavior implements PlantAttackBehavior {
    private final int damage = 99999;
    private final int maxTargets;
    private final float lengthRange = 2.0f; // 2 tiles forward. Width 1 is implicitly handled by lane check.

    public TangleKelpBehavior(int maxTargets) {
        this.maxTargets = maxTargets;
    }

    @Override
    public void execute(Entity self, Field field) {
        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null || hc.currentHealth <= 0) return;

        TangleKelpComponent state = self.get(TangleKelpComponent.class);
        if (state == null) {
            state = new TangleKelpComponent();
            self.add(state);
        }

        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5;
        int lane = GridPosition.fromContinuous(origin).lane();

        if (state.isAttacking) {
            state.attackTimer += (1.0f / 20.0f); // 20 ticks per second

            // Wait 0.5s to simulate the kelp bubbling up and dragging the zombie down
            if (state.attackTimer >= 0.5f) {
                // Re-acquire the leftmost targets exactly when the timer pops to avoid hitting dead/escaped zombies
                List<Entity> validTargets = getLeftmostTargets(field, lane, originX);

                for (Entity target : validTargets) {
                    CombatSystem.applyDamage(target, damage, false);
                }

                // Snap health to 0 so the plant dies alongside the zombie(s)
                hc.currentHealth = 0;
            }
            return;
        }

        // --- Target Acquisition Phase ---
        List<Entity> validTargets = getLeftmostTargets(field, lane, originX);

        if (!validTargets.isEmpty()) {
            state.isAttacking = true;
            // TODO: Trigger Tangle Kelp grab/bubble animation here
        }
    }

    /**
     * Internal helper to always fetch the absolute leftmost zombies within range.
     */
    private List<Entity> getLeftmostTargets(Field field, int lane, double originX) {
        List<Entity> laneTargets = new ArrayList<>();

        for (ZombieInstance z : field.getZombiesInLane(lane)) {
            if (!z.isMarkedForRemoval()) {
                double targetX = z.get(PositionComponent.class).position.getX() + 0.5;

                // Ensure target in the range
                if (targetX >= originX - lengthRange/2 && targetX <= originX + lengthRange/2) {
                    laneTargets.add(z);
                }
            }
        }

        // Sort leftmost to rightmost
        laneTargets.sort((a, b) -> Double.compare(
            a.get(PositionComponent.class).position.getX(),
            b.get(PositionComponent.class).position.getX()
        ));

        // Return up to maxTargets
        if (laneTargets.size() > maxTargets) {
            return laneTargets.subList(0, maxTargets);
        }
        return laneTargets;
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }
}
