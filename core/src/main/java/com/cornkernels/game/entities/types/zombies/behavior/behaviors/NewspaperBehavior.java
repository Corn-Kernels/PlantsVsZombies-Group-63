package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieBehaviorComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.EnragedComponent;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;

public class NewspaperBehavior implements ZombieBehavior {

    @Override
    public void update(ZombieInstance zombie, Field field) {
        boolean hasNewspaper = false;

        // Check if the newspaper armor still exists and has health remaining[cite: 12]
        for (ArmorComponent armor : zombie.getAll(ArmorComponent.class)) {
            if (armor.currentArmorHealth > 0) {
                hasNewspaper = true;
                break;
            }
        }

        // check if has newspaper -> false -> enrage -> destroy behavior
        if (!hasNewspaper) {

            // 2. Tag the zombie so the ZombieSystem knows to apply higher bite damage
            zombie.add(new EnragedComponent());

            // 3. Destroy this behavior component so this update loop ceases to run[cite: 8]
            zombie.removeAll(ZombieBehaviorComponent.class);
        }
    }
}
