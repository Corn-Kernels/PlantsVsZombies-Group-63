package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.Garden;
import io.github.some_example_name.model.GardenPot;
import io.github.some_example_name.model.PlayerProgress;
import io.github.some_example_name.model.User;

import java.util.List;

public class GreenhouseScreen implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;
    private Image backgroundImage;
    private Garden garden;

    private Table currencyTable;
    private Label coinsLabel;
    private Label diamondsLabel;

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
    private Texture potReadyBorderTexture;
    private Texture potGrowingBorderTexture;

    public GreenhouseScreen(Main game, User user) {
        this.game = game;
        this.user = user;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        skin.add("default", buttonStyle);
        skin.add("green", buttonStyle);

        PlayerProgress progress = user.getProgress();
        garden = progress.getGarden();

        loadPotImages();
        createDrawables();
        buildUI();
        updatePotsDisplay();
    }

    private void loadPotImages() {
        try {
            potEmptyTexture = new Texture(Gdx.files.internal("IMAGES/greenhouse/pot_empty.png"));
            potLockedTexture = new Texture(Gdx.files.internal("IMAGES/greenhouse/pot_locked.png"));
            potReadyBorderTexture = new Texture(Gdx.files.internal("IMAGES/greenhouse/pot_ready_border.png"));
            potGrowingBorderTexture = new Texture(Gdx.files.internal("IMAGES/greenhouse/pot_growing_border.png"));
        } catch (Exception e) {
            System.out.println("Pot images not found! Using default colors.");
        }
    }

    private void createDrawables() {
        if (potLockedTexture != null) {
            lockedDrawable = new TextureRegionDrawable(potLockedTexture);
        } else {
            Pixmap lockedPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            lockedPixmap.setColor(new Color(0.1f, 0.1f, 0.1f, 0.9f));
            lockedPixmap.fill();
            lockedDrawable = new TextureRegionDrawable(new Texture(lockedPixmap));
            lockedPixmap.dispose();
        }

        if (potEmptyTexture != null) {
            emptyDrawable = new TextureRegionDrawable(potEmptyTexture);
        } else {
            Pixmap emptyPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            emptyPixmap.setColor(new Color(0.3f, 0.2f, 0.1f, 0.8f));
            emptyPixmap.fill();
            emptyDrawable = new TextureRegionDrawable(new Texture(emptyPixmap));
            emptyPixmap.dispose();
        }

        if (potGrowingBorderTexture != null) {
            growingDrawable = new TextureRegionDrawable(potGrowingBorderTexture);
        } else {
            Pixmap growingPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            growingPixmap.setColor(new Color(0.3f, 0.3f, 0.1f, 0.8f));
            growingPixmap.fill();
            growingDrawable = new TextureRegionDrawable(new Texture(growingPixmap));
            growingPixmap.dispose();
        }

        if (potReadyBorderTexture != null) {
            readyDrawable = new TextureRegionDrawable(potReadyBorderTexture);
        } else {
            Pixmap readyPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            readyPixmap.setColor(new Color(0.1f, 0.6f, 0.1f, 0.8f));
            readyPixmap.fill();
            readyDrawable = new TextureRegionDrawable(new Texture(readyPixmap));
            readyPixmap.dispose();
        }
    }

    private void buildUI() {
        loadBackground();

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        setupCurrencyDisplay();

        Label titleLabel = new Label(" GREENHOUSE", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        infoTable = new Table();
        updateInfoDisplay();
        mainTable.add(infoTable).padBottom(10).row();

        potsTable = new Table();
        potsTable.setBackground(skin.getDrawable("white_pixel"));
        potsTable.setColor(0.2f, 0.15f, 0.1f, 0.5f);
        potsTable.pad(10);

        ScrollPane scrollPane = new ScrollPane(potsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(350);
        mainTable.add(scrollPane).width(500).height(350).padBottom(10).row();

        statusLabel = new Label("Select a pot to interact", skin);
        statusLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        Table buttonTable = new Table();

        TextButton plantBtn = new TextButton(" Plant", skin, "default");
        TextButton harvestBtn = new TextButton(" Harvest", skin, "green");
        TextButton speedBtn = new TextButton(" Speed Up (💎5)", skin, "default");
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

    private void setupCurrencyDisplay() {
        currencyTable = new Table();
        currencyTable.top().right();
        currencyTable.setFillParent(true);
        updateCurrencyDisplay();
        stage.addActor(currencyTable);
    }

    private void updateCurrencyDisplay() {
        User currentUser = game.getCurrentUser();
        int coins = (currentUser != null) ? currentUser.getProgress().getCoins() : 0;
        int diamonds = (currentUser != null) ? currentUser.getProgress().getDiamonds() : 0;

        currencyTable.clear();

        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(new Color(0, 0, 0, 0.5f));
        bgPixmap.fill();
        Drawable bgDrawable = new TextureRegionDrawable(new Texture(bgPixmap));
        bgPixmap.dispose();

        coinsLabel = new Label("🪙 " + coins, skin);
        diamondsLabel = new Label("💎 " + diamonds, skin);
        coinsLabel.setFontScale(1.2f);
        diamondsLabel.setFontScale(1.2f);

        Label.LabelStyle style = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        style.background = bgDrawable;

        coinsLabel.setStyle(style);
        diamondsLabel.setStyle(style);

        currencyTable.add(coinsLabel).padTop(10).padRight(10).width(80).height(30);
        currencyTable.add(diamondsLabel).padTop(10).padRight(20).width(80).height(30);
        currencyTable.row();
    }

    private void updateInfoDisplay() {
        infoTable.clear();
        PlayerProgress progress = user.getProgress();
        int unlockedPots = garden.getUnlockedPotCount();

        infoTable.add(new Label(" Pots: " + unlockedPots + "/20", skin)).padRight(20);
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin));
    }

    private void updatePotsDisplay() {
        potsTable.clear();

        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 4; col++) {
                GardenPot pot = garden.getPot(row, col);
                Table potCard = createPotCard(pot, row, col);
                potsTable.add(potCard).width(85).height(75).pad(3);
            }
            potsTable.row();
        }
    }

    private Table createPotCard(GardenPot pot, int row, int col) {
        Table card = new Table();
        card.setBackground(skin.getDrawable("white_pixel"));

        if (pot.isLocked()) {
            card.setBackground(lockedDrawable);
        } else if (pot.isEmpty()) {
            card.setBackground(emptyDrawable);
        } else if (pot.isReady()) {
            card.setBackground(readyDrawable);
        } else {
            card.setBackground(growingDrawable);
        }

        Image plantImage = null;
        if (!pot.isEmpty() && !pot.isLocked()) {
            String plantName = pot.getPlantType();
            if (plantName != null) {
                try {
                    String imagePath = "IMAGES/plants/" + plantName.toLowerCase() + ".png";
                    Texture texture = new Texture(Gdx.files.internal(imagePath));
                    plantImage = new Image(texture);
                    plantImage.setSize(40, 40);
                } catch (Exception e) {
                }
            }
        }

        String statusText = getShortStatus(pot);
        Label statusLabel = new Label(statusText, skin);
        statusLabel.setFontScale(0.5f);
        statusLabel.setColor(1, 1, 1, 1);

        Label rewardLabel = null;
        if (pot.isReady()) {
            int reward = 5 + (int)(Math.random() * 15);
            rewardLabel = new Label("💎" + reward, skin);
            rewardLabel.setFontScale(0.6f);
            rewardLabel.setColor(1, 0.8f, 0, 1);
        }

        Label posLabel = new Label(row + "," + col, skin);
        posLabel.setFontScale(0.4f);
        posLabel.setColor(0.7f, 0.7f, 0.7f, 1);

        card.add(posLabel).top().left().pad(2).row();

        if (plantImage != null) {
            card.add(plantImage).size(40, 40).pad(2).row();
        } else {
            card.add(new Label("", skin)).pad(2).row();
        }

        card.add(statusLabel).center().pad(2).row();

        if (rewardLabel != null) {
            card.add(rewardLabel).center().pad(2).row();
        }

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
        long hours = pot.getHoursRemaining();
        if (hours > 0) return hours + "h left";
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

        String status = selectedPot.getStatusDisplay();
        statusLabel.setText("Selected: (" + row + "," + col + ") - " + status);
        updatePotsDisplay();
    }

    private void handlePlant() {
        if (selectedPot == null || selectedPot.isLocked() || !selectedPot.isEmpty()) {
            showToast("❌ Select an empty, unlocked pot first!", 2f, true);
            return;
        }

        String plantType = "MARIGOLD";

        selectedPot.plant(plantType);
        game.getStorageService().saveUsers();
        updatePotsDisplay();
        updateInfoDisplay();
        showToast("✅ Planted " + plantType + " in (" + selectedRow + "," + selectedCol + ")", 2f, false);
    }

    private void handleHarvest() {
        if (selectedPot == null || selectedPot.isLocked() || !selectedPot.isReady()) {
            showToast("❌ Select a ready pot to harvest!", 2f, true);
            return;
        }

        PlayerProgress progress = user.getProgress();

        int coinsReward = 30 + (int)(Math.random() * 70);
        int diamondsReward = 5 + (int)(Math.random() * 15);

        progress.addCoins(coinsReward);
        progress.addDiamonds(diamondsReward);

        selectedPot.clear();
        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        updatePotsDisplay();
        updateInfoDisplay();

        showToast("✅ Harvested! +" + coinsReward + " coins, +" + diamondsReward + " diamonds", 2f, false);
    }

    private void handleSpeedUp() {
        if (selectedPot == null || selectedPot.isLocked() || selectedPot.isEmpty() || selectedPot.isReady()) {
            showToast("❌ Select a growing pot to speed up!", 2f, true);
            return;
        }

        PlayerProgress progress = user.getProgress();
        int cost = 5;

        if (progress.getDiamonds() < cost) {
            showToast("❌ Not enough diamonds! Need " + cost + " 💎", 2f, true);
            return;
        }

        progress.deductDiamonds(cost);

        selectedPot.plant(selectedPot.getPlantType());
        selectedPot.checkReady();

        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        updatePotsDisplay();
        updateInfoDisplay();

        showToast("✅ Growth accelerated! Plant is now ready.", 2f, false);
    }

    private void handleBuyPot() {
        PlayerProgress progress = user.getProgress();
        int cost = 200;

        int unlockedPots = garden.getUnlockedPotCount();
        if (unlockedPots >= 20) {
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

    private void showToast(String message, float duration, boolean isError) {
        Label toast = new Label(message, skin);
        toast.setAlignment(Align.center);
        if (isError) {
            toast.setColor(1, 0.2f, 0.2f, 1);
        } else {
            toast.setColor(0.2f, 1, 0.2f, 1);
        }
        toast.setFontScale(1.2f);
        toast.setPosition(
            stage.getWidth() / 2f - toast.getWidth() / 2f,
            stage.getHeight() / 2f + 100
        );

        Pixmap toastPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        toastPixmap.setColor(new Color(0, 0, 0, 0.7f));
        toastPixmap.fill();
        Drawable toastBg = new TextureRegionDrawable(new Texture(toastPixmap));
        toastPixmap.dispose();

        Label.LabelStyle style = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        style.background = toastBg;
        toast.setStyle(style);

        stage.addActor(toast);

        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                toast.remove();
            }
        }, duration);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        if (potEmptyTexture != null) potEmptyTexture.dispose();
        if (potLockedTexture != null) potLockedTexture.dispose();
        if (potReadyBorderTexture != null) potReadyBorderTexture.dispose();
        if (potGrowingBorderTexture != null) potGrowingBorderTexture.dispose();
    }
}
