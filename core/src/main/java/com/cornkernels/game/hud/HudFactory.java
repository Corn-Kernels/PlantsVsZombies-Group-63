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
import com.cornkernels.game.systems.controller.GameSpeedController;
import com.cornkernels.game.systems.controller.PauseController;
import com.cornkernels.game.systems.controller.plants.PlantingController;
import com.cornkernels.game.systems.controller.plants.SeedBank;
import com.cornkernels.game.systems.controller.plants.SeedSlot;
import com.cornkernels.game.systems.entity.WaveSystem;
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
    private static final int PAUSE_BG_INSET = 14;

    private final TextureAtlas alwaysLoadedAtlas;
    private final TextureAtlas seedPacketsAtlas;
    private final TextureAtlas pauseMenuAtlas;
    private final TextureAtlas ingameSkinAtlas;
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
        this.pauseMenuAtlas = new TextureAtlas(Gdx.files.internal("ui/atlases/pause_menu.atlas"));
        this.ingameSkinAtlas = new TextureAtlas(Gdx.files.internal("ui/pvz2_skin.atlas"));
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
                // Set both imageUp AND imageOver to the same drawable — ImageButton renders
                // imageOver whenever the cursor is hovering the button, regardless of imageUp, so
                // leaving imageOver fixed on "down" made the icon look permanently paused while
                // hovered (which is right where the cursor sits immediately after clicking it),
                // masking the real state until the mouse moved away.
                Drawable current = pauseController.isPaused() ? down : up;
                style.imageUp = current;
                style.imageOver = current;
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

    public @NonNull ImageButton createSpeedToggleButton(@NonNull GameSpeedController speedController,
                                                        BooleanSupplier visibleWhen) {
        Drawable up = drawable(ingameSkinAtlas, "image_ui_hud_ingame_2x");
        Drawable selected = drawable(ingameSkinAtlas, "image_ui_hud_ingame_2x_selected");

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = up;
        style.imageOver = up;

        ImageButton button = new ImageButton(style) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setVisible(visibleWhen.getAsBoolean());
                Drawable current = speedController.isFastForward() ? selected : up;
                style.imageUp = current;
                style.imageOver = current;
            }
        };
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speedController.toggleFastForward();
            }
        });
        // Directly left of the pause button, same row.
        placeButton(button, hudWidth - 20f - BUTTON_SIZE - 10f - BUTTON_SIZE, hudHeight - 20f - BUTTON_SIZE);
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
        return createTextButton("Let's Go!", "GreenButton", "GreenButton_Down", onConfirm);
    }

    public @NonNull PauseMenu createPauseMenu(@NonNull Runnable onRestart, @NonNull Runnable onExit) {
        NinePatch patch = new NinePatch(alwaysLoadedAtlas.findRegion("reward1_bg"),
            PAUSE_BG_INSET, PAUSE_BG_INSET, PAUSE_BG_INSET, PAUSE_BG_INSET);
        Image background = new Image(new NinePatchDrawable(patch));

        Image windowTopper = new Image(drawable(pauseMenuAtlas, "windowtopper"));

        Label title = new Label("Game Paused", new Label.LabelStyle(counterFont, Color.WHITE));
        title.setAlignment(Align.center);

        TextButton resumeButton = createTextButton("Resume", "GreenButton", "GreenButton_Down", pauseController::resume);
        TextButton restartButton = createTextButton("Restart", "BlueButton", "BlueButton_Down", onRestart);
        TextButton exitButton = createTextButton("Exit Level", "BrownButton", "BrownButton_Down", onExit);

        return new PauseMenu(background, windowTopper, title,
            List.of(resumeButton, restartButton, exitButton), pauseController);
    }

    public @NonNull EndGameMenu createEndGameMenu(@NonNull Runnable onRestart, @NonNull Runnable onExit,
                                                  @NonNull BooleanSupplier visibleWhen,
                                                  @NonNull BooleanSupplier wonWhen) {
        NinePatch patch = new NinePatch(alwaysLoadedAtlas.findRegion("reward1_bg"),
            PAUSE_BG_INSET, PAUSE_BG_INSET, PAUSE_BG_INSET, PAUSE_BG_INSET);
        Image background = new Image(new NinePatchDrawable(patch));

        Label title = new Label("", new Label.LabelStyle(counterFont, Color.WHITE));
        title.setAlignment(Align.center);

        TextButton restartButton = createTextButton("Play Level Again", "GreenButton", "GreenButton_Down", onRestart);

        TextButton.TextButtonStyle exitStyle = new TextButton.TextButtonStyle();
        exitStyle.up = drawable(alwaysLoadedAtlas, "BrownButton");
        exitStyle.down = drawable(alwaysLoadedAtlas, "BrownButton_Down");
        exitStyle.font = confirmButtonFont;
        TextButton exitButton = new TextButton("Exit Level", exitStyle) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText(wonWhen.getAsBoolean() ? "Back to Levels" : "Exit Level");
            }
        };
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onExit.run();
            }
        });

        return new EndGameMenu(background, title, List.of(restartButton, exitButton), visibleWhen, wonWhen);
    }

    // Classic PvZ flag-meter: a track that fills as the level's zombie budget is spawned in, a
    // zombie-head icon riding the leading edge of the fill, and a flag marking the final wave.
    public @NonNull Group createLevelProgressBar(@NonNull WaveSystem waveSystem, BooleanSupplier visibleWhen) {
        float barWidth = 273f;
        float barHeight = 33f;
        float trackInset = 10f;
        float trackWidth = barWidth - trackInset * 2f;
        float margin = 20f;

        float barX = hudWidth - margin - barWidth;
        float barY = margin;

        Image track = new Image(drawable(alwaysLoadedAtlas, "progress_meter"));
        track.setBounds(barX, barY, barWidth, barHeight);

        Image fill = new Image(drawable(alwaysLoadedAtlas, "progress_meter_fill")) {
            @Override
            public void act(float delta) {
                super.act(delta);
                float progress = waveSystem.getProgress();
                setBounds(barX + trackInset, barY + trackInset / 2f, trackWidth * progress, barHeight - trackInset);
            }
        };
        fill.setScaling(Scaling.stretch);

        float flagWidth = 29f;
        float flagHeight = 38f;
        Image flag = new Image(drawable(alwaysLoadedAtlas, "progress_meter_flag_pole"));
        flag.setBounds(barX + barWidth - trackInset - flagWidth, barY + (barHeight - flagHeight) / 2f,
            flagWidth, flagHeight);

        float headWidth = 42f;
        float headHeight = 45f;
        Image zombieHead = new Image(drawable(alwaysLoadedAtlas, "progress_meter_zombiehead")) {
            @Override
            public void act(float delta) {
                super.act(delta);
                float progress = waveSystem.getProgress();
                float centerX = barX + trackInset + trackWidth * progress;
                float x = Math.max(barX - headWidth / 2f, Math.min(centerX - headWidth / 2f, barX + barWidth - headWidth / 2f));
                setBounds(x, barY + (barHeight - headHeight) / 2f, headWidth, headHeight);
            }
        };

        Label waveLabel = new Label("", new Label.LabelStyle(confirmButtonFont, Color.WHITE)) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText("Wave " + Math.max(1, waveSystem.getWaveNumber()) + "/" + waveSystem.getTotalWaves());
            }
        };
        waveLabel.setAlignment(Align.center);
        waveLabel.setBounds(barX, barY + barHeight + 4f, barWidth, 24f);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                super.act(delta);
                setVisible(visibleWhen.getAsBoolean());
            }
        };
        group.setTouchable(Touchable.disabled);
        group.addActor(track);
        group.addActor(fill);
        group.addActor(flag);
        group.addActor(zombieHead);
        group.addActor(waveLabel);
        return group;
    }

    private @NonNull TextButton createTextButton(String text, String upRegion, String downRegion, Runnable onClick) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = drawable(alwaysLoadedAtlas, upRegion);
        style.down = drawable(alwaysLoadedAtlas, downRegion);
        style.font = confirmButtonFont;
        TextButton button = new TextButton(text, style);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClick.run();
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
        ingameSkinAtlas.dispose();
        counterFont.dispose();
        priceFont.dispose();
        whitePixel.dispose();
    }
}
