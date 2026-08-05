package com.cornkernels.game.systems.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;

public class PamRenderSystem extends RenderSystem {

    private final PamPlayer pamPlayer;
    private final SpriteBatch batch;

    public PamRenderSystem(PamPlayer pamPlayer, SpriteBatch batch) {
        super(batch);
        this.pamPlayer = pamPlayer;
        this.batch = batch;
    }

    @Override
    public void render(float delta) {
        List<Entity> renderableEntities = field.getEntitiesWith(PositionComponent.class, PamAnimationComponent.class);
        for (Entity entity : renderableEntities) {
            PositionComponent positionComponent = entity.get(PositionComponent.class);
            PamAnimationComponent anim = entity.get(PamAnimationComponent.class);

            anim.stateTime += delta;

            pamPlayer.draw(
                batch,
                anim.currentClip,
                anim.stateTime,
                positionComponent.position.getX(),
                positionComponent.position.getY(),
                anim.isLooping,
                anim.visibilityMap);
        }
    }
}
