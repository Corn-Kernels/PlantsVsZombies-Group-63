package com.cornkernels.game.entities.types.effects;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import org.jspecify.annotations.NonNull;


public class PlantFoodEffect extends Entity {

    private boolean ending = false;
    private float outroDuration = 0f;
    private float outroElapsed = 0f;

    public PlantFoodEffect(@NonNull Vec2d position) {
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
