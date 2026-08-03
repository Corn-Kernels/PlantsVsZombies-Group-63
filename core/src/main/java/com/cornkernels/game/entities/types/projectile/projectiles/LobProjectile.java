package com.cornkernels.game.entities.types.projectile.projectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;

public class LobProjectile extends AbstractProjectile {

    private final static float LOB_SPEED = 1f;
    public Entity target;
    public float areaOfEffect = 0f;

    public LobProjectile(int damage, Vec2d startPosition, Entity target) {
        super(damage, new Vec2d(LOB_SPEED, 0), startPosition);
        this.target = target;
        add(new PositionComponent(startPosition));
        //if a projectile has AOE its area of effect should be changed to the correct number
    }
}
