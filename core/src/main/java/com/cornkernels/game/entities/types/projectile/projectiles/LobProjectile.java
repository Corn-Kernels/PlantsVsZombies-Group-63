package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class LobProjectile extends AbstractProjectile {

    private final static float LOB_SPEED = 1f;
    public Entity target;
    public Vec2d startPosition;
    public float areaOfEffect;
    public int aoeDamage;

    public LobProjectile(int damage, Vec2d startPosition, Entity target, float radius, int aoeDamage) {
        super(damage, new Vec2d(LOB_SPEED, 0), startPosition);
        this.target = target;
        this.startPosition = startPosition;
        this.areaOfEffect = radius;
        this.aoeDamage = aoeDamage;
    }

    @Override
    public boolean hit(Entity target, Field field) {
        if (target == this.target && super.hit(target, field)) {

            int directDamage = this.get(DamageComponent.class).amount;

            if (areaOfEffect > 0) {
                // AoE Lobber (e.g., Melon-pult)
                // We subtract the AoE damage from the direct hit to prevent the primary target
                // from taking massive double damage when the AreaOfDamage resolves next tick.
                CombatSystem.applyDamage(target, directDamage - aoeDamage, false);

                field.addProjectile(new AreaOfDamage(this.get(PositionComponent.class).position, areaOfEffect, aoeDamage));
            } else {
                // Standard Lobber (e.g., Cabbage-pult)
                CombatSystem.applyDamage(target, directDamage, false);
            }

            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new LobProjectile(damage, newPosition, this.target, this.areaOfEffect, this.aoeDamage);
    }
}
