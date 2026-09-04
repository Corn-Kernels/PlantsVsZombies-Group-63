package com.cornkernels.game.entities.types.plants.behavior.behaviors.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.specific_specific.SquashComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.HypnoComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage;
import com.cornkernels.game.entities.types.projectile.projectiles.LineOfDamage;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;

public class SquashBehavior implements PlantAttackBehavior {
    private final int damage;
    private final int maxCrushes;
    private final boolean useLineAoe;
    private final float triggerRange = 1.5f;

    public SquashBehavior(int damage, int maxCrushes, boolean useLineAoe) {
        this.damage = damage;
        this.maxCrushes = maxCrushes;
        this.useLineAoe = useLineAoe;
    }

    @Override
    public void execute(Entity self, Field field) {
        HealthComponent hc = self.get(HealthComponent.class);
        if (hc == null || hc.currentHealth <= 0) return;

        SquashComponent state = self.get(SquashComponent.class);
        if (state == null) {
            state = new SquashComponent();
            state.crushesLeft = maxCrushes;
            self.add(state);
        }

        if (state.isAttacking) {
            state.attackTimer += (1.0f / 20.0f);

            if (state.attackTimer >= 0.5f) {
                Vec2d crushPos = new Vec2d(state.targetCrushX, state.targetCrushY);

                if (useLineAoe) {
                    field.addProjectile(new LineOfDamage(crushPos, 2.5f, 1.0f, damage));
                } else {
                    field.addProjectile(new AreaOfDamage(crushPos, 0.5f, damage));
                }

                state.crushesLeft--;
                if (state.crushesLeft <= 0) {
                    hc.currentHealth = 0;
                    self.markForRemoval();
                } else {
                    self.get(PositionComponent.class).position.setX((float) Math.floor(state.targetCrushX));
                    self.get(PositionComponent.class).position.setY((float) Math.floor(state.targetCrushY));
                    state.isAttacking = false;
                    state.attackTimer = 0f;
                }
            }
            return;
        }

        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5;
        int lane = GridPosition.fromContinuous(origin).lane();

        Entity closestTarget = null;
        double minDistance = Double.MAX_VALUE;

        for (Entity e : getAllValidTargets(field)) {

                if (GridPosition.fromContinuous(e.get(PositionComponent.class).position).lane() == lane) {
                    double targetX = e.get(PositionComponent.class).position.getX() + 0.5;
                    double dist = Math.abs(targetX - originX);

                    if (dist <= triggerRange && dist < minDistance) {
                        minDistance = dist;
                        closestTarget = e;
                    }
                }
        }

        if (closestTarget != null) {
            state.isAttacking = true;
            state.targetCrushX = closestTarget.get(PositionComponent.class).position.getX() + 0.5f;
            state.targetCrushY = closestTarget.get(PositionComponent.class).position.getY();
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        return true;
    }
}
