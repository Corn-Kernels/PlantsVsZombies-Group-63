package com.cornkernels.game.entities.types.effects;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;

/**
 * Purely visual overlay played on top of a plant while its Plant Food is active (the shared
 * "sparkle glow" PAM, not the plant's own clip). Spawned at the plant's position when Plant Food
 * activates; once Plant Food ends it's told to play its outro ({@link #startEnding}) and gets
 * removed once that finishes, ticked by {@link com.cornkernels.game.systems.entity.PlantFoodEffectSystem}.
 */
public class PlantFoodEffect extends Entity {

    private boolean ending = false;
    private float outroDuration = 0f;
    private float outroElapsed = 0f;

    public PlantFoodEffect(Vec2d position) {
        add(new PositionComponent(new Vec2d(position.getX(), position.getY())));
        add(new PamAnimationComponent());
    }

    public boolean isEnding() {
        return ending;
    }

    public void startEnding(float outroDuration) {
        this.ending = true;
        this.outroDuration = outroDuration;
        this.outroElapsed = 0f;
    }

    public void tickEnding(float deltaTick) {
        if (!ending || isMarkedForRemoval()) return;
        outroElapsed += deltaTick;
        if (outroElapsed >= outroDuration) {
            markForRemoval();
        }
    }
}
