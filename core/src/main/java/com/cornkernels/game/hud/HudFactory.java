package com.cornkernels.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
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
import com.cornkernels.game.menus.model.PlayerProgress;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public class HudFactory implements Disposable {

    private static final float BUTTON_SIZE = 50f;

    private static final float SEED_PACKET_WIDTH = 200f;
    private static final float PLANT_CURSOR_ATTACHMENT_WIDTH = 80f;
    private static final float SEED_SLOT_PADDING = 6f;
    private static final float MENU_PACKET_WIDTH = 150f;
    private static final float MENU_PADDING = 12f;
    private static final float MENU_MARGIN = 28f;
    private static final int MENU_VISIBLE_ROWS = 4;
    private static final int REWARD_BG_INSET = 20;
    private static final int PAUSE_BG_INSET = 14;
    private static final int MAX_TRAY_SLOTS = 8;
    private static final int BOOST_COST_GEMS = 2;
    private static final float READY_SET_PLANT_FADE_IN = 0.3f;
    private static final float READY_SET_PLANT_HOLD = 0.6f;
    private static final float READY_SET_PLANT_FADE_OUT = 0.3f;
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
    private final PlayerProgress playerProgress;
    private final float hudWidth;
    private final float hudHeight;

    public HudFactory(
        PamPlayer pamPlayer,
        PlantingController plantingController,
        PauseController pauseController,
        PlayerProgress playerProgress,
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
        this.playerProgress = playerProgress;
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
        placeButton(button, hudWidth - 20f - BUTTON_SIZE - 10f - BUTTON_SIZE - 10f - BUTTON_SIZE,
            hudHeight - 20f - BUTTON_SIZE);
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

    public @NonNull Group createPlantFoodCounter() {
        float belowSunOffset = 90f;

        Image background = new Image(drawable(alwaysLoadedAtlas, "background_3slice"));
        background.setColor(0.12f, 0.12f, 0.12f, 0.92f);
        background.setBounds(70f, hudHeight - 20f - 50f - belowSunOffset, 180f, 50f);

        Image icon = new Image(drawable(ingameSkinAtlas, "image_ui_hud_eventbutton_event_icon_potw_up"));
        icon.setScaling(Scaling.stretch);
        icon.setBounds(50f, hudHeight - 14f - 70f - belowSunOffset, 70f, 70f);

        Label count = new Label("", new Label.LabelStyle(counterFont, Color.WHITE)) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText(String.valueOf(playerProgress.getPlantFood()));
            }
        };
        count.setAlignment(Align.center);
        count.setBounds(150f, hudHeight - 14f - 60f - belowSunOffset, 90f, 60f);

        Group group = new Group();
        group.addActor(background);
        group.addActor(icon);
        group.addActor(count);

        TextureRegion leafRegion = new TextureRegion(alwaysLoadedAtlas.findRegion("leaf_backdrop"));
        CursorAttachment leafCursor = new RegionCursorAttachment(leafRegion, 69f, 74f);

        group.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerProgress.getPlantFood() <= 0) return;
                plantingController.togglePlantFoodTool(leafCursor,
                    () -> playerProgress.setPlantFood(playerProgress.getPlantFood() - 1));
            }
        });

        return group;
    }

    public @NonNull ImageButton createAddSunButton() {
        ImageButton button = createPlusButton(() -> plantingController.addSun(100));
        placeButton(button, 220f + 15f, hudHeight - 74f);
        return button;
    }

    public @NonNull ImageButton createAddPlantFoodButton() {
        ImageButton button = createPlusButton(
            () -> playerProgress.setPlantFood(Math.clamp(playerProgress.getPlantFood() + 1, 0, 4)));
        float belowSunOffset = 90f;
        placeButton(button, 250f + 15f, hudHeight - 74f - belowSunOffset);
        return button;
    }

    private @NonNull ImageButton createPlusButton(@NonNull Runnable onClick) {
        Drawable up = drawable(ingameSkinAtlas, "image_ui_hud_ingame_coin_buy");
        Drawable down = drawable(ingameSkinAtlas, "image_ui_hud_ingame_coin_buy_down");

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = up;
        style.imageDown = down;

        ImageButton button = new ImageButton(style);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClick.run();
            }
        });
        return button;
    }

    public @NonNull Group createCoinCounter() {
        return createCurrencyCounter("image_ui_coins_stack_2", 20f, playerProgress::getCoins);
    }

    public @NonNull Group createGemCounter() {
        return createCurrencyCounter("image_ui_gems_stack_1", 20f + 50f + 14f, playerProgress::getDiamonds);
    }

    private @NonNull Group createCurrencyCounter(String iconRegion, float backgroundY,
                                                 @NonNull IntSupplier valueSupplier) {
        Image background = new Image(drawable(alwaysLoadedAtlas, "background_3slice"));
        background.setBounds(70f, backgroundY, 150f, 50f);

        Image icon = new Image(drawable(ingameSkinAtlas, iconRegion));
        icon.setScaling(Scaling.stretch);
        icon.setBounds(50f, backgroundY - 7f, 84f, 64f);

        Label count = new Label("", new Label.LabelStyle(counterFont, Color.WHITE)) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText(String.valueOf(valueSupplier.getAsInt()));
            }
        };
        count.setAlignment(Align.center);
        count.setBounds(140f, backgroundY + 10f, 80f, 30f);

        Group group = new Group();
        group.setTouchable(Touchable.disabled);
        group.addActor(background);
        group.addActor(icon);
        group.addActor(count);
        return group;
    }

    public @NonNull SeedPacket createSeedPacket(@NonNull SeedSlot slot) {
        SeedPacket packet = new SeedPacket(
            slot,
            drawable(seedPacketsAtlas, "modernday"),
            drawable(seedPacketsAtlas, "boost"),
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
        tray.createSeedSlots(Math.min(MAX_TRAY_SLOTS, slots.size()));

        NinePatch patch = new NinePatch(alwaysLoadedAtlas.findRegion("reward3_bg"),
            REWARD_BG_INSET, REWARD_BG_INSET, REWARD_BG_INSET, REWARD_BG_INSET);
        PlantSelectionMenu menu = new PlantSelectionMenu(
            new NinePatchDrawable(patch),
            this::createSeedPacket,
            MENU_PACKET_WIDTH,
            MENU_PADDING,
            MENU_MARGIN);
        menu.setSeedSlots(slots);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.vScrollKnob = new TextureRegionDrawable(new TextureRegion(whitePixel));
        ScrollPane menuScrollPane = new ScrollPane(menu, scrollStyle);
        menuScrollPane.setScrollingDisabled(true, false);
        menuScrollPane.setFadeScrollBars(false);

        float packetHeight = MENU_PACKET_WIDTH / SeedPacket.ASPECT;
        float visibleContentHeight = MENU_VISIBLE_ROWS * packetHeight + (MENU_VISIBLE_ROWS - 1) * MENU_PADDING;
        float scrollHeight = Math.min(menu.getHeight(), visibleContentHeight + 2f * MENU_MARGIN);
        menuScrollPane.setSize(menu.getWidth(), scrollHeight);

        return new SeedChooser(tray, menu, menuScrollPane, seedBank,
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

    /**
     * Beside the plant selection menu: spends {@value BOOST_COST_GEMS} gems to boost whichever
     * plant is currently "pending" (first-clicked but not yet confirmed into a seed slot). Disabled
     * (greyed out) whenever nothing is pending, that plant is already boosted, or the player can't
     * afford it.
     */
    public @NonNull TextButton createBoostButton(@NonNull SeedChooser seedChooser) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = drawable(alwaysLoadedAtlas, "GreenButton");
        style.down = drawable(alwaysLoadedAtlas, "GreenButton_Down");
        style.font = confirmButtonFont;

        TextButton button = new TextButton("Boost", style) {
            @Override
            public void act(float delta) {
                super.act(delta);
                SeedSlot pending = seedChooser.getPendingSlot();
                boolean canBoost = pending != null && !pending.isBoosted()
                    && playerProgress.getDiamonds() >= BOOST_COST_GEMS;
                setDisabled(!canBoost);
                setColor(canBoost ? Color.WHITE : new Color(0.55f, 0.55f, 0.55f, 1f));
            }
        };
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SeedSlot pending = seedChooser.getPendingSlot();
                if (pending == null || pending.isBoosted()) return;
                if (!playerProgress.deductDiamonds(BOOST_COST_GEMS)) return;
                pending.setBoosted(true);
            }
        });
        return button;
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

        float waveArrowWidth = 16f;
        float waveArrowHeight = 17.6f;
        List<Image> waveArrows = new ArrayList<>();
        for (float waveStartProgress : waveSystem.getWaveStartProgress()) {
            float centerX = barX + trackInset + trackWidth * waveStartProgress;
            Image waveArrow = new Image(drawable(alwaysLoadedAtlas, "Arrow_Down_Orange"));
            waveArrow.setBounds(centerX - waveArrowWidth / 2f, barY + barHeight - waveArrowHeight,
                waveArrowWidth, waveArrowHeight);
            waveArrows.add(waveArrow);
        }

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
        for (Image waveArrow : waveArrows) {
            group.addActor(waveArrow);
        }
        group.addActor(flag);
        group.addActor(zombieHead);
        group.addActor(waveLabel);
        return group;
    }

    public @NonNull Label createReadySetPlantBanner() {
        Label.LabelStyle style = new Label.LabelStyle(counterFont, Color.WHITE);
        Label banner = new Label("", style);
        banner.setAlignment(Align.center);
        banner.setSize(hudWidth, 80f);
        banner.setPosition(0, hudHeight / 2f - 40f);
        banner.getColor().a = 0f;
        banner.setVisible(false);
        banner.setTouchable(Touchable.disabled);
        return banner;
    }

    public void playReadySetPlant(@NonNull Label banner, Runnable onComplete) {
        banner.clearActions();
        banner.setVisible(true);
        banner.addAction(Actions.sequence(
            flashText(banner, "Ready", Color.WHITE),
            flashText(banner, "Set", Color.WHITE),
            flashText(banner, "Plant!", Color.WHITE),
            Actions.run(() -> {
                banner.setVisible(false);
                if (onComplete != null) onComplete.run();
            })
        ));
    }


    public @NonNull Label createErrorBanner() {
        Label.LabelStyle style = new Label.LabelStyle(counterFont, Color.WHITE);
        Label banner = new Label("", style);
        banner.setAlignment(Align.center);
        banner.setSize(hudWidth, 60f);
        banner.setPosition(0, hudHeight / 2f - 30f);
        banner.getColor().a = 0f;
        banner.setVisible(false);
        banner.setTouchable(Touchable.disabled);
        return banner;
    }

    public void showErrorMessage(@NonNull Label banner, String message) {
        banner.clearActions();
        banner.setVisible(true);
        banner.addAction(Actions.sequence(
            flashText(banner, message, Color.RED),
            Actions.run(() -> banner.setVisible(false))
        ));
    }

    private @NonNull Action flashText(@NonNull Label banner, String text, @NonNull Color color) {
        return Actions.sequence(
            Actions.run(() -> banner.setColor(color.r, color.g, color.b, 0f)),
            Actions.run(() -> banner.setText(text)),
            Actions.fadeIn(READY_SET_PLANT_FADE_IN),
            Actions.delay(READY_SET_PLANT_HOLD),
            Actions.fadeOut(READY_SET_PLANT_FADE_OUT)
        );
    }

    public @NonNull Label createWaveStartBanner(@NonNull WaveSystem waveSystem, @NonNull BooleanSupplier visibleWhen) {
        Label.LabelStyle style = new Label.LabelStyle(counterFont, Color.WHITE);
        Label banner = new Label("", style) {
            private int lastAnnouncedWave = 0;

            @Override
            public void act(float delta) {
                super.act(delta);
                if (!visibleWhen.getAsBoolean()) return;

                int currentWave = waveSystem.getWaveNumber();
                if (currentWave > lastAnnouncedWave) {
                    lastAnnouncedWave = currentWave;
                    setText("Wave " + currentWave + " has started!");
                    clearActions();
                    setVisible(true);
                    getColor().a = 0f;
                    addAction(Actions.sequence(
                        Actions.fadeIn(0.3f),
                        Actions.delay(1.5f),
                        Actions.fadeOut(0.5f)
                    ));
                }
            }
        };
        banner.setAlignment(Align.center);
        banner.setSize(hudWidth, 60f);
        banner.setPosition(0, hudHeight / 2f - 30f);
        banner.getColor().a = 0f;
        banner.setVisible(false);
        banner.setTouchable(Touchable.disabled);
        return banner;
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
