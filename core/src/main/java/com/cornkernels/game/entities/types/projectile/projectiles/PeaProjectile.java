package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;

public class PeaProjectile extends AbstractProjectile {

    private static final float PEA_SPEED = 1f;

    public PeaProjectile(int damage, Vec2d startPosition) {
        super(damage, new Vec2d(PEA_SPEED, 0), startPosition);

    }
}
