package com.cornkernels.game.entities.types.projectile.ZombieProjectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.CombatSystem;

public class JugglerReflectedProjectile extends AbstractZombieProjectile {

    public JugglerReflectedProjectile(int damage, Vec2d startPosition, Vec2d velocity, PamAnimationComponent animToCopy) {
        super(damage, velocity, startPosition);

        // Remove the default empty PamAnimationComponent added by the superclass
        this.removeAll(PamAnimationComponent.class);

        // Apply the exact animation data from the stolen plant projectile
        if (animToCopy != null) {
            this.add(animToCopy);
        }
    }

    @Override
    public boolean hit(Entity target, Field field) {
        if (super.hit(target, field)) {
            DamageComponent dmg = this.get(DamageComponent.class);
            if (dmg != null) {
                // Apply the stolen damage to the plant!
                CombatSystem.applyDamage(target, dmg.amount, false, field);
            }
            return true; // Mark for removal
        }
        return false;
    }
}
