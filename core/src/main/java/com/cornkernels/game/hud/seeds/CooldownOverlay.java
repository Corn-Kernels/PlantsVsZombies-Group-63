package com.cornkernels.game.hud.seeds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;


public class CooldownOverlay extends Actor {

    private static final Color DIM = new Color(0.15f, 0.15f, 0.2f, 0.55f);

    private final Texture whitePixel;
    private float cooldownRatio;

    public CooldownOverlay(Texture whitePixel) {
        this.whitePixel = whitePixel;
        setTouchable(Touchable.disabled);
    }

    public float getCooldownRatio() {
        return cooldownRatio;
    }

    public void setCooldownRatio(float cooldownRatio) {
        this.cooldownRatio = Math.clamp(cooldownRatio, 0f, 1f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (cooldownRatio <= 0f) return;

        float bandHeight = getHeight() * cooldownRatio;
        float bandY = getY() + getHeight() - bandHeight;

        Color previous = batch.getColor().cpy();
        batch.setColor(DIM.r, DIM.g, DIM.b, DIM.a * parentAlpha);
        batch.draw(whitePixel, getX(), bandY, getWidth(), bandHeight);
        batch.setColor(previous);
    }
}
