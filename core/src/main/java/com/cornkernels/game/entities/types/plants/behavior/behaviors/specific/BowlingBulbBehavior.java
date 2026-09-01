package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.BowlingBulbComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.BowlingProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class BowlingBulbBehavior implements PlantAttackBehavior {
    private final int[] damages; // Index 0: Cyan, 1: Blue, 2: Orange
    private final float regenTimeSeconds;

    public BowlingBulbBehavior(int[] damages, float regenTimeSeconds) {
        this.damages = damages;
        this.regenTimeSeconds = regenTimeSeconds;
    }

    @Override
    public void execute(Entity self, Field field) {
        BowlingBulbComponent state = self.get(BowlingBulbComponent.class);
        if (state == null) {
            state = new BowlingBulbComponent();
            self.add(state);
        }

        float dt = 1.0f / 20.0f; // 20 ticks per sec

        // 1. Handle Bulb Regeneration
        if (state.bulbsReady < 3) {
            state.regenTimer += dt;
            if (state.regenTimer >= regenTimeSeconds) {
                state.bulbsReady++;
                state.regenTimer = 0f;
                // TODO: Update PAM Animation to visually add a bulb back
            }
        }

        // 2. Handle Firing Cooldown
        if (state.attackCooldown > 0) {
            state.attackCooldown -= dt;
            return;
        }

        // 3. Fire logic
        if (state.bulbsReady > 0 && hasRealTarget(self, field)) {
            Vec2d origin = self.get(PositionComponent.class).position;
            Vec2d spawnPos = new Vec2d((float) (origin.getX() + 0.5), origin.getY());

            // bulbsReady = 3 (fires Orange, damages[2])
            // bulbsReady = 2 (fires Blue, damages[1])
            // bulbsReady = 1 (fires Cyan, damages[0])
            int fireDamage = damages[state.bulbsReady - 1];

            field.addProjectile(new BowlingProjectile(fireDamage, spawnPos));

            state.bulbsReady--;
            state.attackCooldown = 1.0f; // 1-second delay between shots so it doesn't fire them all instantly
            // TODO: Update PAM Animation to remove the front bulb
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }

    private boolean hasRealTarget(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        int lane = GridPosition.fromContinuous(origin).lane();

        for (Entity e : field.getEntities()) {
            if ((e instanceof ZombieInstance || e instanceof Grave) && !e.isMarkedForRemoval()) {
                if (GridPosition.fromContinuous(e.get(PositionComponent.class).position).lane() == lane) {
                    if (e.get(PositionComponent.class).position.getX() >= origin.getX()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
