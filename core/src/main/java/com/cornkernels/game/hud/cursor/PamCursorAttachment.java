package com.cornkernels.game.hud.cursor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.jetbrains.annotations.NotNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.Collections;

public class PamCursorAttachment implements CursorAttachment {
    private final PamPlayer pamPlayer;
    private final ClipRef clip;
    private float stateTime = 0f;

    public PamCursorAttachment(@NotNull PamPlayer pamPlayer, @NotNull ClipRef clip) {
        this.pamPlayer = pamPlayer;
        this.clip = clip;
    }

    @Override
    public void render(SpriteBatch batch, float worldX, float worldY, float delta) {
        stateTime += delta;
        pamPlayer.draw(batch, clip, stateTime, worldX, worldY, true, Collections.emptyMap());
    }
}
