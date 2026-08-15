package com.cornkernels.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.cornkernels.engine.utility.FontLoader;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.hud.cursor.PamCursorAttachment;
import com.cornkernels.game.hud.cursor.RegionCursorAttachment;
import com.cornkernels.game.hud.seeds.PlantSelectionMenu;
import com.cornkernels.game.hud.seeds.SeedChooser;
import com.cornkernels.game.hud.seeds.SeedPacket;
import com.cornkernels.game.hud.seeds.SeedSelectionBar;
import com.cornkernels.game.systems.controller.PauseController;
import com.cornkernels.game.systems.controller.plants.PlantingController;
import com.cornkernels.game.systems.controller.plants.SeedBank;
import com.cornkernels.game.systems.controller.plants.SeedSlot;
import com.cornkernels.game.utility.UIEntityTextureFinder;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;
import java.util.function.BooleanSupplier;

public class HudFactory implements Disposable {

    private static final float BUTTON_SIZE = 50f;

    private static final float SEED_PACKET_WIDTH = 200f;
    private static final float PLANT_CURSOR_ATTACHMENT_WIDTH = 80f;
    private static final float SEED_SLOT_PADDING = 6f;
    private static final float MENU_PACKET_WIDTH = 150f;
    private static final float MENU_PADDING = 12f;
    private static final float MENU_MARGIN = 28f;
    private static final int REWARD_BG_INSET = 20;

    private final TextureAtlas alwaysLoadedAtlas;
    private final TextureAtlas seedPacketsAtlas;
    private final UIEntityTextureFinder plantTextureFinder;
    private final BitmapFont counterFont;
    private final BitmapFont priceFont;
    private final BitmapFont confirmButtonFont;
    private final Texture whitePixel;

    private final PamPlayer pamPlayer;
    private final PlantingController plantingController;
    private final PauseController pauseController;

    private final float hudWidth;
    private final float hudHeight;

    public HudFactory(
        PamPlayer pamPlayer,
        PlantingController plantingController,
        PauseController pauseController,
        float hudWidth,
        float hudHeight) {
        this.alwaysLoadedAtlas = new TextureAtlas(Gdx.files.internal("ui/atlases/UI_AlwaysLoaded.atlas"));
        this.seedPacketsAtlas = new TextureAtlas(Gdx.files.internal("ui/atlases/ui_seedpackets.atlas"));
        this.plantTextureFinder = new UIEntityTextureFinder(seedPacketsAtlas);
        this.counterFont = FontLoader.generate(Gdx.files.internal("ui/FBUSV8C5EI.TTF"), 45, Color.WHITE);
        this.priceFont = FontLoader.generate(Gdx.files.internal("ui/FBUSV8C5EI.TTF"), 45, Color.WHITE);
        this.confirmButtonFont = FontLoader.generate(Gdx.files.internal("ui/FBUSV8C5EI.TTF"), 25, Color.WHITE);
        this.whitePixel = createWhitePixel();
        this.pamPlayer = pamPlayer;
        this.plantingController = plantingController;
        this.pauseController = pauseController;
        this.hudWidth = hudWidth;
        this.hudHeight = hudHeight;
    }

    @Contract(" -> new")
    private static @NonNull Texture createWhitePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Contract("_,_ -> new")
    private @NonNull Drawable drawable(@NonNull TextureAtlas atlas, String name) {
        TextureRegion region = atlas.findRegion(name);
        if (region == null) throw new IllegalStateException("Texture region " + name + " not found");
        return new TextureRegionDrawable(new TextureRegion(region));
    }

    public @NonNull ImageButton createPauseButton() {
        Drawable up = drawable(alwaysLoadedAtlas, "pause_button");
        Drawable down = drawable(alwaysLoadedAtlas, "pause_button_down");

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = up;
        style.imageOver = down;

        ImageButton button = new ImageButton(style) {
            @Override
            public void act(float delta) {
                super.act(delta);
                style.imageUp = pauseController.isPaused() ? down : up;
            }
        };
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseController.togglePause();
            }
        });
        placeButton(button, hudWidth - 20f - BUTTON_SIZE, hudHeight - 20f - BUTTON_SIZE);
        return button;
    }

    public @NonNull ImageButton createShovelButton(BooleanSupplier visibleWhen) {
        Drawable up = drawable(alwaysLoadedAtlas, "shovel_button");
        Drawable down = drawable(alwaysLoadedAtlas, "shovel_button_down");

        String removalCursorPath = "768/INITIAL/ZEN_GARDEN/CURSORS/REMOVAL_CURSOR/REMOVAL_CURSOR.PAM";
        pamPlayer.loadSync(removalCursorPath);
        CursorAttachment shovelCursor = new PamCursorAttachment(pamPlayer,
            pamPlayer.getClip(removalCursorPath, "idle"), 0.4f);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = up;
        style.imageOver = down;

        ImageButton button = new ImageButton(style) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setVisible(visibleWhen.getAsBoolean());
            }
        };
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                plantingController.toggleShovel(shovelCursor);
            }
        });
        placeButton(button, hudWidth - 70f - BUTTON_SIZE, hudHeight - 70f - BUTTON_SIZE);
        return button;
    }

    public @NonNull Group createSunCounter() {
        Image background = new Image(drawable(alwaysLoadedAtlas, "background_3slice"));
        background.setBounds(70f, hudHeight - 20f - 50f, 150f, 50f);

        Image sun = new Image(drawable(alwaysLoadedAtlas, "sun"));
        sun.setScaling(Scaling.stretch);
        sun.setBounds(50f, hudHeight - 14f - 70f, 70f, 70f);

        Label count = new Label("", new Label.LabelStyle(counterFont, Color.WHITE)) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText(String.valueOf(plantingController.getCurrentSun()));
            }
        };
        count.setAlignment(Align.center);
        count.setBounds(150f, hudHeight - 14f - 60f, 60f, 60f);

        Group group = new Group();
        group.setTouchable(Touchable.disabled);
        group.addActor(background);
        group.addActor(sun);
        group.addActor(count);
        return group;
    }


    public @NonNull SeedPacket createSeedPacket(@NonNull SeedSlot slot) {
        SeedPacket packet = new SeedPacket(
            slot,
            drawable(seedPacketsAtlas, "modernday"),
            plantTextureFinder.getPlantUITextureOf(slot.getPlantDef()),
            drawable(seedPacketsAtlas, "price_tab"),
            priceFont,
            whitePixel);
        packet.setAvailableWhen(() -> plantingController.canAfford(slot.getPlantDef().getCost()));
        return packet;
    }

    public @NonNull SeedChooser createSeedChooser(@NonNull List<SeedSlot> slots,
                                                  @NonNull SeedBank seedBank,
                                                  @NonNull BooleanSupplier placementMode) {
        SeedSelectionBar tray = new SeedSelectionBar(
            drawable(seedPacketsAtlas, "empty_packet"), SEED_PACKET_WIDTH, SEED_SLOT_PADDING);
        tray.createSeedSlots(slots.size());

        NinePatch patch = new NinePatch(alwaysLoadedAtlas.findRegion("reward3_bg"),
            REWARD_BG_INSET, REWARD_BG_INSET, REWARD_BG_INSET, REWARD_BG_INSET);
        PlantSelectionMenu menu = new PlantSelectionMenu(
            new NinePatchDrawable(patch),
            this::createSeedPacket,
            MENU_PACKET_WIDTH,
            MENU_PADDING,
            MENU_MARGIN);
        menu.setSeedSlots(slots);

        return new SeedChooser(tray, menu, seedBank,
            this::createSeedPacket, this::createThumbnail, placementMode);
    }

    private @NonNull CursorAttachment createThumbnail(@NonNull SeedSlot slot) {
        return new RegionCursorAttachment(
            plantTextureFinder.getPlantUITextureOf(slot.getPlantDef()),
            PLANT_CURSOR_ATTACHMENT_WIDTH, PLANT_CURSOR_ATTACHMENT_WIDTH);
    }

    public @NonNull TextButton createConfirmButton(@NonNull Runnable onConfirm) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = drawable(alwaysLoadedAtlas, "GreenButton");
        style.down = drawable(alwaysLoadedAtlas, "GreenButton_Down");
        style.font = confirmButtonFont;
        TextButton button = new TextButton("Let's Go!", style);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onConfirm.run();
            }
        });
        return button;
    }

    private void placeButton(@NonNull ImageButton button, float x, float y) {
        button.setSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setPosition(x, y);
        button.getImageCell().grow();
        button.getImage().setScaling(Scaling.stretch);
    }

    @Override
    public void dispose() {
        alwaysLoadedAtlas.dispose();
        seedPacketsAtlas.dispose();
        counterFont.dispose();
        priceFont.dispose();
        whitePixel.dispose();
    }
}
