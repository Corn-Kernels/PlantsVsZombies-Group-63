package com.cornkernels.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cornkernels.engine.renderer.hud.HudAnchor;
import com.cornkernels.engine.renderer.hud.element.HudButton;
import com.cornkernels.engine.renderer.hud.element.HudGroup;
import com.cornkernels.engine.renderer.hud.view.CompositeHudView;
import com.cornkernels.engine.renderer.hud.view.IconView;
import com.cornkernels.engine.renderer.hud.view.TextView;
import com.cornkernels.engine.utility.renderer.TextRenderer;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.hud.cursor.PamCursorAttachment;
import com.cornkernels.game.systems.controller.PauseController;
import com.cornkernels.game.systems.controller.plants.PlantingController;
import com.cornkernels.game.systems.controller.plants.SeedSlot;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;

public class HudFactory {

    private final TextureAtlas alwaysLoadedAtlas;
    private final TextureAtlas seedPacketsAtlas;

    private final PamPlayer pamPlayer;
    private final PlantingController plantingController;
    private final PauseController pauseController;

    public HudFactory(
        PamPlayer pamPlayer,
        PlantingController plantingController,
        PauseController pauseController) {
        this.alwaysLoadedAtlas = new TextureAtlas(Gdx.files.internal("ui/atlases/UI_AlwaysLoaded.atlas"));
        this.seedPacketsAtlas = new TextureAtlas(Gdx.files.internal("ui/atlases/ui_seedpackets.atlas"));
        this.pamPlayer = pamPlayer;
        this.plantingController = plantingController;
        this.pauseController = pauseController;
    }

    @Contract("_,_ -> new")
    private @NonNull TextureRegion region(@NonNull TextureAtlas atlas, String name) {
        TextureRegion region = atlas.findRegion(name);
        if (region == null) throw new IllegalStateException("Texture region " + name + " not found");
        return new TextureRegion(region);
    }

    public HudButton createPauseButton() {
        TextureRegion pause_button = region(alwaysLoadedAtlas, "pause_button");
        TextureRegion pause_button_down = region(alwaysLoadedAtlas, "pause_button_down");
        return HudButton.builder(new HudAnchor(HudAnchor.Corner.TOP_RIGHT, 20, 20),
                50, 50)
            .icon(() -> pauseController.isPaused() ? pause_button_down : pause_button).hoverIcon(pause_button_down)
            .onClick(pauseController::togglePause)
            .build();
    }

    public HudButton createShovelButton() {
        TextureRegion shovel_button = region(alwaysLoadedAtlas, "shovel_button");
        TextureRegion shovel_button_down = region(alwaysLoadedAtlas, "shovel_button_down");
        String removalCursorPath = "768/INITIAL/ZEN_GARDEN/CURSORS/REMOVAL_CURSOR/REMOVAL_CURSOR.PAM";
        pamPlayer.loadSync(removalCursorPath);
        CursorAttachment shovelCursor = new PamCursorAttachment(pamPlayer,
            pamPlayer.getClip(removalCursorPath, "idle"), 0.4f);
        return HudButton.builder(new HudAnchor(HudAnchor.Corner.TOP_RIGHT, 70, 70),
                50, 50)
            .icon(shovel_button).hoverIcon(shovel_button_down).onClick(()
                -> plantingController.toggleShovel(shovelCursor))
            .build();
    }

    public CompositeHudView createSunCounter(PlantingController plantingController, TextRenderer textRenderer) {
        TextureRegion sunTexture = region(alwaysLoadedAtlas, "sun");
        TextureRegion background = region(alwaysLoadedAtlas, "background_3slice");

        return new CompositeHudView(new HudAnchor(HudAnchor.Corner.TOP_LEFT, 70, 20), 120, 180)
            .addChild(new IconView(new HudAnchor(HudAnchor.Corner.TOP_LEFT, 70, 20), background, 150, 50))
            .addChild(new IconView(new HudAnchor(HudAnchor.Corner.TOP_LEFT, 50, 14), sunTexture, 70, 70))
            .addChild(new TextView(() -> String.valueOf(plantingController.getCurrentSun()), textRenderer,
                new HudAnchor(HudAnchor.Corner.TOP_LEFT, 150, 14), 60, 60));
    }

    public HudGroup createSeedSlots(List<SeedSlot> seedSlots) {
        return null;
    }

}
