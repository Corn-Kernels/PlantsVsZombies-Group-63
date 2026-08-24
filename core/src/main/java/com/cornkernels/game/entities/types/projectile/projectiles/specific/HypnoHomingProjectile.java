package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.types.projectile.projectiles.HomingProjectile;
import com.cornkernels.game.map.Field;
import org.jspecify.annotations.NonNull;

public class HypnoHomingProjectile extends HomingProjectile {

    public HypnoHomingProjectile(int damage, Vec2d startPosition, Entity target) {
        super(damage, startPosition, target);
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        // TODO: Implement hypnosis logic here instead of standard damage
        return super.hit(target, field);
    }

    @Override
    public HypnoHomingProjectile clone(Vec2d newPosition) {
        return new HypnoHomingProjectile(this.get(com.cornkernels.game.entities.components.DamageComponent.class).amount, newPosition, this.target);
    }
}
