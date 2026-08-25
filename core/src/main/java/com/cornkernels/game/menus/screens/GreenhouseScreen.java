package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cornkernels.GameManager;
import com.cornkernels.game.menus.model.Garden;
import com.cornkernels.game.menus.model.GardenPot;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.User;

public class GreenhouseScreen extends BaseScreen {

    private User user;
    private Garden garden;

    private Label statusLabel;
    private Table potsTable;
    private Table infoTable;

    private GardenPot selectedPot;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private Drawable lockedDrawable;
    private Drawable emptyDrawable;
    private Drawable growingDrawable;
    private Drawable readyDrawable;

    private Texture potEmptyTexture;
    private Texture potLockedTexture;

    // ===== لیست ۵ گیاه ثابت =====
    private static final String[] AVAILABLE_PLANTS = {
        "SUNFLOWER",
        "PEASHOOTER",
        "WALL_NUT",
        "SNOW_PEA",
        "REPEATER"
    };

    public GreenhouseScreen(GameManager game, User user) {
        super(game);
        this.user = user;

        PlayerProgress progress = user.getProgress();
        garden = progress.getGarden();

        if (garden == null) {
            garden = new Garden();
            progress.setGarden(garden);
        }

        loadPotImages();
        createDrawables();
        loadBackground();
        buildUI();
        updatePotsDisplay();
    }

    private void loadPotImages() {
        try {
            potEmptyTexture = new Texture(Gdx.files.internal("IMAGES/greenhouse/pot_empty.png"));
            System.out.println("✅ pot_empty.png loaded");
        } catch (Exception e) {
            System.out.println("❌ pot_empty.png not found!");
            potEmptyTexture = null;
        }

        try {
            potLockedTexture = new Texture(Gdx.files.internal("IMAGES/greenhouse/pot_locked.png"));
            System.out.println("✅ pot_locked.png loaded");
        } catch (Exception e) {
            System.out.println("❌ pot_locked.png not found!");
            potLockedTexture = null;
        }
    }

    private void createDrawables() {
        if (potLockedTexture != null) {
            lockedDrawable = new TextureRegionDrawable(potLockedTexture);
        } else {
            Pixmap lockedPixmap = new Pixmap(100, 90, Pixmap.Format.RGBA8888);
            lockedPixmap.setColor(new Color(0.15f, 0.15f, 0.15f, 1));
            lockedPixmap.fill();
            lockedPixmap.setColor(Color.DARK_GRAY);
            lockedPixmap.drawRectangle(2, 2, 96, 86);
            lockedDrawable = new TextureRegionDrawable(new Texture(lockedPixmap));
            lockedPixmap.dispose();
        }

        if (potEmptyTexture != null) {
            emptyDrawable = new TextureRegionDrawable(potEmptyTexture);
        } else {
            Pixmap emptyPixmap = new Pixmap(100, 90, Pixmap.Format.RGBA8888);
            emptyPixmap.setColor(new Color(0.4f, 0.25f, 0.1f, 1));
            emptyPixmap.fill();
            emptyPixmap.setColor(new Color(0.5f, 0.35f, 0.15f, 1));
            emptyPixmap.drawRectangle(2, 2, 96, 86);
            emptyDrawable = new TextureRegionDrawable(new Texture(emptyPixmap));
            emptyPixmap.dispose();
        }

        Pixmap growingPixmap = new Pixmap(100, 90, Pixmap.Format.RGBA8888);
        growingPixmap.setColor(new Color(0.4f, 0.3f, 0.05f, 1));
        growingPixmap.fill();
        growingPixmap.setColor(Color.YELLOW);
        growingPixmap.drawRectangle(2, 2, 96, 86);
        growingDrawable = new TextureRegionDrawable(new Texture(growingPixmap));
        growingPixmap.dispose();

        Pixmap readyPixmap = new Pixmap(100, 90, Pixmap.Format.RGBA8888);
        readyPixmap.setColor(new Color(0.05f, 0.3f, 0.05f, 1));
        readyPixmap.fill();
        readyPixmap.setColor(Color.GREEN);
        readyPixmap.drawRectangle(2, 2, 96, 86);
        readyDrawable = new TextureRegionDrawable(new Texture(readyPixmap));
        readyPixmap.dispose();
    }

    private void loadBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/greenhouse_background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("Greenhouse background not found! Using default color.");
        }
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" GREENHOUSE", skin);
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(10).row();

        infoTable = new Table();
        updateInfoDisplay();
        mainTable.add(infoTable).padBottom(10).row();

        potsTable = new Table();
        potsTable.pad(10);

        ScrollPane scrollPane = new ScrollPane(potsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(350);
        mainTable.add(scrollPane).width(560).height(350).padBottom(10).row();

        statusLabel = new Label("Select a pot to interact", skin);
        statusLabel.setFontScale(1.2f);
        statusLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        Table buttonTable = new Table();

        TextButton plantBtn = new TextButton(" Plant", skin, "default");
        TextButton harvestBtn = new TextButton(" Harvest", skin, "green");
        TextButton speedBtn = new TextButton(" Speed Up", skin, "default");
        TextButton buyPotBtn = new TextButton(" Buy Pot (200)", skin, "default");
        TextButton backBtn = new TextButton(" Back", skin, "default");

        buttonTable.add(plantBtn).width(120).height(40).padRight(5);
        buttonTable.add(harvestBtn).width(120).height(40).padRight(5);
        buttonTable.add(speedBtn).width(140).height(40).padRight(5);
        buttonTable.add(buyPotBtn).width(130).height(40).padRight(5);
        buttonTable.add(backBtn).width(100).height(40);

        mainTable.add(buttonTable).row();

        plantBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handlePlant();
            }
        });

        harvestBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleHarvest();
            }
        });

        speedBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleSpeedUp();
            }
        });

        buyPotBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleBuyPot();
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameMenuScreen(game, user));
            }
        });
    }

    private void updateInfoDisplay() {
        infoTable.clear();
        PlayerProgress progress = user.getProgress();
        int unlockedPots = garden.getUnlockedPotCount();
        int totalPots = garden.getTotalPotCount();

        Label potsLabel = new Label(" Pots: " + unlockedPots + "/" + totalPots, skin);
        potsLabel.setFontScale(1.2f);
        infoTable.add(potsLabel).padRight(20);

        Label coinsLabel = new Label(" Coins: " + progress.getCoins(), skin);
        coinsLabel.setFontScale(1.2f);
        infoTable.add(coinsLabel).padRight(20);

        Label diamondsLabel = new Label(" Diamonds: " + progress.getDiamonds(), skin);
        diamondsLabel.setFontScale(1.2f);
        infoTable.add(diamondsLabel);
    }

    private void updatePotsDisplay() {
        potsTable.clear();

        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 4; col++) {
                GardenPot pot = garden.getPot(row, col);
                Table potCard;
                if (pot != null) {
                    potCard = createPotCard(pot, row, col);
                } else {
                    potCard = createEmptyPotCard(row, col);
                }
                potsTable.add(potCard).width(105).height(95).pad(5);
            }
            potsTable.row();
        }
    }

    private Table createEmptyPotCard(int row, int col) {
        Table card = new Table();
        card.setBackground(lockedDrawable);
        card.pad(3);

        Label label = new Label("🔒", skin);
        label.setFontScale(2.5f);
        card.add(label).center().row();

        Label posLabel = new Label(row + "," + col, skin);
        posLabel.setFontScale(0.6f);
        posLabel.setColor(0.7f, 0.7f, 0.7f, 1);
        card.add(posLabel).bottom().right().pad(2);

        return card;
    }

    private Table createPotCard(GardenPot pot, int row, int col) {
        Table card = new Table();
        card.pad(5);

        if (pot.isLocked()) {
            card.setBackground(lockedDrawable);
        } else if (pot.isEmpty()) {
            card.setBackground(emptyDrawable);
        } else if (pot.isReady()) {
            card.setBackground(readyDrawable);
        } else {
            card.setBackground(growingDrawable);
        }

        // ===== نمایش اسم گیاه =====
        if (!pot.isEmpty() && !pot.isLocked()) {
            String plantName = pot.getPlantType();
            if (plantName != null) {
                String displayName = plantName;
                if (displayName.length() > 8) {
                    displayName = displayName.substring(0, 8) + "..";
                }
                Label plantLabel = new Label(displayName, skin);
                plantLabel.setFontScale(1.0f);
                plantLabel.setColor(1, 1, 1, 1);
                card.add(plantLabel).center().row();
            }
        } else if (!pot.isLocked() && pot.isEmpty()) {
            Label emptyLabel = new Label("⬜", skin);
            emptyLabel.setFontScale(2.5f);
            card.add(emptyLabel).center().row();
        }

        // ===== وضعیت (زمان بر حسب ثانیه یا READY) =====
        if (!pot.isEmpty() && !pot.isLocked()) {
            if (pot.isReady()) {
                Label readyLabel = new Label("✅ READY", skin);
                readyLabel.setFontScale(0.9f);
                readyLabel.setColor(0, 1, 0, 1);
                card.add(readyLabel).center().row();
            } else {
                long seconds = pot.getSecondsRemaining();
                if (seconds > 0) {
                    Label timeLabel = new Label(seconds + "s left", skin);
                    timeLabel.setFontScale(0.9f);
                    timeLabel.setColor(1, 1, 0.5f, 1);
                    card.add(timeLabel).center().row();
                } else {
                    Label timeLabel = new Label("Soon", skin);
                    timeLabel.setFontScale(0.9f);
                    timeLabel.setColor(1, 1, 0.5f, 1);
                    card.add(timeLabel).center().row();
                }
            }
        }

        // ===== جایزه =====
        if (pot.isReady()) {
            int reward = 5 + (int)(Math.random() * 15);
            Label rewardLabel = new Label("💎" + reward, skin);
            rewardLabel.setFontScale(0.9f);
            rewardLabel.setColor(1, 0.8f, 0, 1);
            card.add(rewardLabel).center().row();
        }

        // ===== موقعیت =====
        Label posLabel = new Label(row + "," + col, skin);
        posLabel.setFontScale(0.5f);
        posLabel.setColor(0.5f, 0.5f, 0.5f, 1);
        card.add(posLabel).bottom().right().pad(2);

        card.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectPot(row, col);
            }
        });

        return card;
    }

    private String getShortStatus(GardenPot pot) {
        if (pot.isLocked()) return "🔒";
        if (pot.isEmpty()) return "⬜";
        if (pot.isReady()) return "✅ READY";
        long seconds = pot.getSecondsRemaining();
        if (seconds > 0) return seconds + "s left";
        return "⏳ soon";
    }

    private void selectPot(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        selectedPot = garden.getPot(row, col);

        if (selectedPot == null) {
            statusLabel.setText("Invalid pot!");
            return;
        }

        String status = getShortStatus(selectedPot);
        statusLabel.setText("Selected: (" + row + "," + col + ") - " + status);
        updatePotsDisplay();
    }

    private void handlePlant() {
        if (selectedPot == null || selectedPot.isLocked() || !selectedPot.isEmpty()) {
            showToast("❌ Select an empty, unlocked pot first!", 2f, true);
            return;
        }

        String plantType;
        if (Math.random() < 0.5) {
            plantType = "MARIGOLD";
        } else {
            int randomIndex = (int)(Math.random() * AVAILABLE_PLANTS.length);
            plantType = AVAILABLE_PLANTS[randomIndex];
        }

        selectedPot.plant(plantType);
        game.getStorageService().saveUsers();
        updatePotsDisplay();
        updateInfoDisplay();

        String displayName = plantType.length() > 8 ? plantType.substring(0, 8) + ".." : plantType;
        showToast("✅ Planted " + displayName + " in (" + selectedRow + "," + selectedCol + ")", 2f, false);
    }

    private void handleHarvest() {
        if (selectedPot == null || selectedPot.isLocked() || !selectedPot.isReady()) {
            showToast("❌ Select a ready pot to harvest!", 2f, true);
            return;
        }

        PlayerProgress progress = user.getProgress();
        String plantType = selectedPot.getPlantType();

        int coinsReward = 500;

        if (!plantType.equals("MARIGOLD")) {
            progress.addSeedPacket(plantType);
            showToast("🌱 +1 " + plantType + " seed packet!", 2f, false);
        }

        progress.addCoins(coinsReward);
        selectedPot.clear();

        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        updatePotsDisplay();
        updateInfoDisplay();

        showToast("✅ Harvested! +" + coinsReward + " coins" + (!plantType.equals("MARIGOLD") ? " + seed packet" : ""), 2f, false);
    }

    private void handleSpeedUp() {
        if (selectedPot == null || selectedPot.isLocked() || selectedPot.isEmpty() || selectedPot.isReady()) {
            showToast("❌ Select a growing pot to speed up!", 2f, true);
            return;
        }

        PlayerProgress progress = user.getProgress();

        long seconds = selectedPot.getSecondsRemaining();
        if (seconds <= 0) {
            showToast("❌ Plant is almost ready! Just wait a bit.", 2f, true);
            return;
        }

        // ===== ۱ الماس به ازای هر ۵ ثانیه =====
        int diamondsNeeded = (int)Math.ceil(seconds / 5.0);
        if (diamondsNeeded < 1) diamondsNeeded = 1;

        if (progress.getDiamonds() < diamondsNeeded) {
            showToast("❌ Not enough diamonds! Need " + diamondsNeeded + " 💎", 2f, true);
            return;
        }

        progress.deductDiamonds(diamondsNeeded);
        selectedPot.forceReady();

        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        updatePotsDisplay();
        updateInfoDisplay();

        showToast("✅ Growth accelerated! Plant is now ready. (Cost: " + diamondsNeeded + " 💎)", 2f, false);
    }

    private void handleBuyPot() {
        PlayerProgress progress = user.getProgress();
        int cost = 200;

        int unlockedPots = garden.getUnlockedPotCount();
        int totalPots = garden.getTotalPotCount();

        if (unlockedPots >= totalPots) {
            showToast("❌ All pots are already unlocked!", 2f, true);
            return;
        }

        if (progress.getCoins() < cost) {
            showToast("❌ Not enough coins! Need " + cost + " coins.", 2f, true);
            return;
        }

        for (GardenPot pot : garden.getPots()) {
            if (pot.isLocked()) {
                progress.deductCoins(cost);
                pot.setLocked(false);
                game.getStorageService().saveUsers();
                updateCurrencyDisplay();
                updatePotsDisplay();
                updateInfoDisplay();
                showToast("✅ Pot unlocked successfully!", 2f, false);
                return;
            }
        }
    }

    @Override
    public void render(float delta) {
        if (garden != null) {
            for (GardenPot pot : garden.getPots()) {
                pot.checkReady();
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (potEmptyTexture != null) potEmptyTexture.dispose();
        if (potLockedTexture != null) potLockedTexture.dispose();
    }
}
