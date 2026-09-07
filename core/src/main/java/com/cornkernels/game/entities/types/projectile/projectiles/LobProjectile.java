package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.ButterComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class LobProjectile extends AbstractProjectile {

    private final static float LOB_SPEED = 1f;
    private static final double HIT_DISTANCE = 0.6f; // Standard collision threshold[cite: 19]

    public Entity target;
    public Vec2d startPosition;
    public float areaOfEffect;
    public int aoeDamage;
    public boolean butter = false;
    public boolean fiery;
    public int chillDurationTicks;
    public int stunDurationTicks;

    public LobProjectile(int damage, Vec2d startPosition, Entity target, float radius, int aoeDamage) {
        this(damage, startPosition, target, radius, aoeDamage, false, 0, 0);
    }

    public LobProjectile(int damage, Vec2d startPosition, Entity target, float radius, int aoeDamage, boolean fiery, int chillDurationTicks, int stunDurationTicks) {
        super(damage, new Vec2d(LOB_SPEED, 0), startPosition);
        this.target = target;
        this.areaOfEffect = radius;
        this.aoeDamage = aoeDamage;
        this.fiery = fiery;
        this.chillDurationTicks = chillDurationTicks;
        this.stunDurationTicks = stunDurationTicks;
        this.startPosition = startPosition;
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        boolean isHit = this.get(PositionComponent.class).position.getX() > target.get(PositionComponent.class).position.getX() - 0.3f;

        if (this.target != null && target == this.target && isHit) {

            // Umbrella Zombie Deflection Logic[cite: 18]
            if (target instanceof ZombieInstance && target.get(ZombieDefComponent.class).def().id.equals("ZombieLostCityJane")) {
                Vec2d currentPos = this.get(PositionComponent.class).position;

                // Switch spawn location to the exact location of the hit[cite: 18]
                this.startPosition = new Vec2d(currentPos.getX(), currentPos.getY());

                // Clear the target so the projectile ignores all future collisions and flies right forever[cite: 18]
                this.target = null;

                return false; // Return false so CombatSystem does NOT destroy the projectile[cite: 18]
            }

            int directDamage = this.get(DamageComponent.class).amount;

            // Apply direct impact debuffs[cite: 18]
            if (fiery && target.has(IceComponent.class)) target.get(IceComponent.class).melt();
            if (chillDurationTicks > 0 && target.has(IceComponent.class))
                target.get(IceComponent.class).applyChill(chillDurationTicks);
            if (stunDurationTicks > 0) target.add(new ButterComponent(stunDurationTicks));

            if (areaOfEffect > 0) {
                CombatSystem.applyDamage(target, directDamage - aoeDamage, false);
                field.addProjectile(new AreaOfDamage(this.get(PositionComponent.class).position, areaOfEffect, aoeDamage, fiery, chillDurationTicks));
            } else {
                CombatSystem.applyDamage(target, directDamage, false);
            }

            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new LobProjectile(damage, newPosition, this.target, this.areaOfEffect, this.aoeDamage, this.fiery, this.chillDurationTicks, this.stunDurationTicks);
    }
}
