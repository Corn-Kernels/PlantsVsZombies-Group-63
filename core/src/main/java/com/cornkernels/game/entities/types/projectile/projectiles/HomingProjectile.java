package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class HomingProjectile extends AbstractProjectile {

    private final static float HOMING_SPEED = 0.25f;
    private final static int TIMEOUT_SECONDS = 150;

    public Entity target;
    private float elapsedTime = 0f;

    public HomingProjectile(int damage, Vec2d startPosition, Entity target) {
        super(damage, new Vec2d(HOMING_SPEED, 0), startPosition);
        this.target = target;
    }

    protected HomingProjectile(int damage, Vec2d startPosition, Entity target, float elapsedTime) {
        super(damage, new Vec2d(HOMING_SPEED, 0), startPosition);
        this.target = target;
        this.elapsedTime = elapsedTime;
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        if (target == this.target){
            elapsedTime ++;
            if (elapsedTime >= TIMEOUT_SECONDS) {
                PositionComponent myPos = this.get(PositionComponent.class);
                PositionComponent targetPos = this.target.get(PositionComponent.class);

                if (myPos != null && targetPos != null) {
                    myPos.position.setX(targetPos.position.getX());
                    myPos.position.setY(targetPos.position.getY());
                }
            }
        }
        if (target == this.target && super.hit(target, field)) {
            CombatSystem.applyDamage(target, this.get(DamageComponent.class).amount, false);
            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        int damage = this.get(DamageComponent.class).amount;
        return new HomingProjectile(damage, newPosition, this.target, this.elapsedTime);
    }
}
