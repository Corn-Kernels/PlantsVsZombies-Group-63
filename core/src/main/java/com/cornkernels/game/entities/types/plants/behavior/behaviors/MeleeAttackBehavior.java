package com.cornkernels.game.entities.types.plants.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehavior;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.entity.CombatSystem;

import java.util.ArrayList;
import java.util.List;

    public class MeleeAttackBehavior implements PlantAttackBehavior {
    private final float frontRange;
    private final float backRange;
    private final int pierce;
    private final int damage;

    public MeleeAttackBehavior(float frontRange, float backRange, int pierce, int damage) {
        this.frontRange = frontRange;
        this.backRange = backRange;
        this.pierce = pierce;
        this.damage = damage;
    }

    @Override
    public void execute(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5; // Calculate from center of the plant
        int lane = GridPosition.fromContinuous(origin).lane();

        List<Entity> laneTargets = new ArrayList<>();

        // Get Zombies
        for (ZombieInstance z : field.getZombiesInLane(lane)) {
            if (!z.isMarkedForRemoval()) {
                laneTargets.add(z);
            }
        }

        // Get Graves
        for (Entity e : field.getEntities()) {
            if (e instanceof Grave && !e.isMarkedForRemoval()) {
                if (GridPosition.fromContinuous(e.get(PositionComponent.class).position).lane() == lane) {
                    laneTargets.add(e);
                }
            }
        }

        List<Entity> backTargets = new ArrayList<>();
        List<Entity> frontTargets = new ArrayList<>();

        // Split targets based on location
        for (Entity target : laneTargets) {
            double targetX = target.get(PositionComponent.class).position.getX() + 0.5;
            if (targetX <= originX && originX - targetX <= backRange) {
                backTargets.add(target);
            } else if (targetX > originX && targetX - originX <= frontRange) {
                frontTargets.add(target);
            }
        }

        // PRIORITY 1: Back Targets (Sort rightmost to leftmost so we hit the closest one behind)
        if (!backTargets.isEmpty()) {
            backTargets.sort((a, b) -> Double.compare(
                b.get(PositionComponent.class).position.getX(),
                a.get(PositionComponent.class).position.getX()
            ));

            int hitCount = 0;
            for (Entity target : backTargets) {
                CombatSystem.applyDamage(target, damage, false);
                hitCount++;
                if (hitCount >= pierce) break;
            }
            return; // Since we attack back first, we exit after hitting them
        }

        // PRIORITY 2: Front Targets (Sort leftmost to rightmost so we hit the closest one in front)
        if (!frontTargets.isEmpty()) {
            frontTargets.sort((a, b) -> Double.compare(
                a.get(PositionComponent.class).position.getX(),
                b.get(PositionComponent.class).position.getX()
            ));

            int hitCount = 0;
            for (Entity target : frontTargets) {
                CombatSystem.applyDamage(target, damage, false);
                hitCount++;
                if (hitCount >= pierce) break;
            }
        }
    }

    @Override
    public boolean hasTarget(Entity self, Field field) {
        Vec2d origin = self.get(PositionComponent.class).position;
        double originX = origin.getX() + 0.5;
        int lane = GridPosition.fromContinuous(origin).lane();

        for (ZombieInstance z : field.getZombiesInLane(lane)) {
            if (!z.isMarkedForRemoval()) {
                double targetX = z.get(PositionComponent.class).position.getX() + 0.5;
                if (targetX <= originX && originX - targetX <= backRange) return true;
                if (targetX > originX && targetX - originX <= frontRange) return true;
            }
        }
        for (Entity e : field.getEntities()) {
            if (e instanceof Grave && !e.isMarkedForRemoval()) {
                if (GridPosition.fromContinuous(e.get(PositionComponent.class).position).lane() == lane) {
                    double targetX = e.get(PositionComponent.class).position.getX() + 0.5;
                    if (targetX <= originX && originX - targetX <= backRange) return true;
                    if (targetX > originX && targetX - originX <= frontRange) return true;
                }
            }
        }
        return false;
    }
}
