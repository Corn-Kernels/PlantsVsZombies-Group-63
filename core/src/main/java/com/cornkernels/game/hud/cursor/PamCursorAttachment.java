package com.cornkernels.game.hud.cursor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import org.jetbrains.annotations.NotNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.Collections;
import java.util.Map;

public class PamCursorAttachment implements CursorAttachment {
    private final PamPlayer pamPlayer;
    private final ClipRef clip;
    private final Map<String, Boolean> visibilityMap;
    private final float scale;
    private float stateTime = 0f;

    public PamCursorAttachment(@NotNull PamPlayer pamPlayer, @NotNull ClipRef clip) {
        this(pamPlayer, clip, Collections.emptyMap(), 1f);
    }

    public PamCursorAttachment(@NotNull PamPlayer pamPlayer, @NotNull ClipRef clip, float scale) {
        this(pamPlayer, clip, Collections.emptyMap(), scale);
    }

    public PamCursorAttachment(@NotNull PamPlayer pamPlayer, @NotNull ClipRef clip,
                               @NotNull Map<String, Boolean> visibilityMap, float scale) {
        this.pamPlayer = pamPlayer;
        this.clip = clip;
        this.visibilityMap = visibilityMap;
        this.scale = scale;
    }

    @Override
    public void render(SpriteBatch batch, float worldX, float worldY, float delta) {
        stateTime += delta;

        if (scale == 1f) {
            pamPlayer.draw(batch, clip, stateTime, worldX, worldY, true, Collections.emptyMap());
            return;
        }

        Matrix4 originalTransform = batch.getTransformMatrix().cpy();
        Matrix4 scaledTransform = originalTransform.cpy()
            .translate(worldX, worldY, 0)
            .scale(scale, scale, 1)
            .translate(-worldX, -worldY, 0);

        batch.setTransformMatrix(scaledTransform);
        pamPlayer.draw(batch, clip, stateTime, worldX, worldY, true, visibilityMap);
        batch.setTransformMatrix(originalTransform);

    }
}
