package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BowlingProjectile extends AbstractProjectile {
    private final List<Entity> hitTargets = new ArrayList<>();
    private final Random random = new Random();
    private int bouncesLeft = 20;

    public BowlingProjectile(int damage, Vec2d startPosition) {
        // Moves faster than a normal pea
        super(damage, new Vec2d(2.0f, 0), startPosition);
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        // Prevent multi-hitting the same zombie instantly in the same frame
        if (super.hit(target, field) && !hitTargets.contains(target)) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            hitTargets.add(target);
            bouncesLeft--;

            if (bouncesLeft <= 0) {
                return true; // Destroy after 3 bounces
            } else {
                // Deflect diagonally up or down
                VelocityComponent vel = this.get(VelocityComponent.class);
                if (vel != null) {
                    float bounceY = random.nextBoolean() ? 1.5f : -1.5f;
                    vel.velocityPerTick.setY(bounceY);
                }
                return false; // Keep surviving
            }
        }
        return false;
    }

    @Override
    public BowlingProjectile clone(Vec2d newPosition) {
        return new BowlingProjectile(this.get(DamageComponent.class).amount, newPosition);
    }
}
