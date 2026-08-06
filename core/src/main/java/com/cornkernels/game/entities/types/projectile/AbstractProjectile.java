package com.cornkernels.game.entities.types.projectile;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;

public class AbstractProjectile extends Entity {

    public AbstractProjectile(int damage, Vec2d velocity, Vec2d Position) {
        super();
        add(new DamageComponent(damage));
        add(new VelocityComponent(velocity));
        add(new PositionComponent(Position));
        add(new PamAnimationComponent());
    }

    public AbstractProjectile clone(Vec2d newPosition) {
        return null;
    }

    /*
     *  this function is called when a projectile overlaps an enemy to decide if it should be damaged or not
     *  this is overwritten by each projectile type to ensure correct behavior
     * this shouldnt be used by itself but is written here to help determine if the target is correct
     * this is just to doubly insure targets are valid
     */
    public boolean hit(Entity target){
        if(target instanceof ZombieInstance||target instanceof Grave)
            return true;
        return false;
    }
}
