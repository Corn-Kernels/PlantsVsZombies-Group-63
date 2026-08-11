package com.cornkernels.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cornkernels.engine.renderer.hud.HudAnchor;
import com.cornkernels.engine.renderer.hud.elements.HudButton;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.hud.cursor.PamCursorAttachment;
import com.cornkernels.game.systems.controller.PauseController;
import com.cornkernels.game.systems.controller.PlantingController;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

public class HudFactory {

    private final TextureAtlas atlas;
    private final PamPlayer pamPlayer;
    private final PlantingController plantingController;
    private final PauseController pauseController;

    public HudFactory(
        PamPlayer pamPlayer,
        PlantingController plantingController,
        PauseController pauseController) {
        this.atlas = new TextureAtlas(Gdx.files.internal("ui/atlases/UI_AlwaysLoaded.atlas"));
        this.pamPlayer = pamPlayer;
        this.plantingController = plantingController;
        this.pauseController = pauseController;
    }

    @Contract("_ -> new")
    private @NonNull TextureRegion region(String name) {
        TextureRegion region = atlas.findRegion(name);
        if (region == null) throw new IllegalStateException("Texture region " + name + " not found");
        return new TextureRegion(region);
    }

    public HudButton createPauseButton() {
        TextureRegion pause_button = region("pause_button");
        TextureRegion pause_button_down = region("pause_button_down");
        return HudButton.builder(new HudAnchor(HudAnchor.Corner.TOP_RIGHT, 20, 20),
                50, 50)
            .icon(() -> pauseController.isPaused() ? pause_button_down : pause_button).hoverIcon(pause_button_down)
            .onClick(pauseController::togglePause)
            .build();
    }

    public HudButton createShovelButton() {
        TextureRegion shovel_button = region("shovel_button");
        TextureRegion shovel_button_down = region("shovel_button_down");
        String removalCursorPath = "768/INITIAL/ZEN_GARDEN/CURSORS/REMOVAL_CURSOR/REMOVAL_CURSOR.PAM";
        pamPlayer.loadSync(removalCursorPath);
        CursorAttachment shovelCursor = new PamCursorAttachment(pamPlayer,
            pamPlayer.getClip(removalCursorPath, "idle"), 0.4f);
        return HudButton.builder(new HudAnchor(HudAnchor.Corner.TOP_RIGHT, 70, 70),
                50, 50)
            .icon(shovel_button).hoverIcon(shovel_button_down).onClick(()
                -> plantingController.beginShovel(shovelCursor))
            .build();
    }
}
