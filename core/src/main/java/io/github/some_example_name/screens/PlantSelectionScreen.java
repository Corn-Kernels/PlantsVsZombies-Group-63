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
import io.github.some_example_name.model.Plant;
import io.github.some_example_name.model.PlayerProgress;
import io.github.some_example_name.model.User;
import io.github.some_example_name.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class PlantSelectionScreen implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;
    private String chapterName;
    private Image backgroundImage;
    private List<Plant> allPlants;
    private List<Plant> selectedPlants;
    private List<Plant> ownedPlants;
    private static final int MAX_PLANTS = 8;

    private Table currencyTable;
    private Label coinsLabel;
    private Label diamondsLabel;

    private Table plantListTable;
    private Table selectedTable;
    private Label selectedCountLabel;
    private Label errorLabel;
    private Label statusLabel;
    private ScrollPane plantScroll;

    public PlantSelectionScreen(Main game, User user, String chapterName) {
        this.game = game;
        this.user = user;
        this.chapterName = chapterName;
        this.selectedPlants = new ArrayList<>();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        allPlants = DataLoader.loadAllPlants();
        ownedPlants = getOwnedPlants();

        buildUI();
        updatePlantList();
        updateSelectedList();
    }

    private List<Plant> getOwnedPlants() {
        List<Plant> result = new ArrayList<>();
        PlayerProgress progress = user.getProgress();
        for (Plant plant : allPlants) {
            if (progress.hasPlant(plant.getName().toUpperCase())) {
                boolean isBoosted = checkIfBoosted(plant);
                plant.setBoosted(isBoosted);

                boolean isUpgradable = plant.getSeedPackets() >= plant.getSeedPacketsNeeded();
                plant.setUpgradable(isUpgradable);

                result.add(plant);
            }
        }
        return result;
    }

    private boolean checkIfBoosted(Plant plant) {
        return plant.getLevel() >= 2;
    }

    private void buildUI() {
        loadBackground();

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        setupCurrencyDisplay();

        Label titleLabel = new Label(" SELECT PLANTS - " + chapterName, skin);
        mainTable.add(titleLabel).padBottom(10).row();

        Table infoTable = new Table();
        PlayerProgress progress = user.getProgress();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin)).padRight(20);
        selectedCountLabel = new Label("Selected: 0/" + MAX_PLANTS, skin);
        infoTable.add(selectedCountLabel);
        mainTable.add(infoTable).padBottom(10).row();

        errorLabel = new Label("", skin);
        errorLabel.setColor(1, 0, 0, 1);
        mainTable.add(errorLabel).padBottom(10).row();

        Table splitTable = new Table();

        plantListTable = new Table();
        plantScroll = new ScrollPane(plantListTable, skin);
        plantScroll.setScrollingDisabled(true, false);
        plantScroll.setHeight(350);
        plantScroll.setWidth(350);

        selectedTable = new Table();
        ScrollPane selectedScroll = new ScrollPane(selectedTable, skin);
        selectedScroll.setScrollingDisabled(true, false);
        selectedScroll.setHeight(350);
        selectedScroll.setWidth(200);

        splitTable.add(plantScroll).width(350).padRight(10);
        splitTable.add(selectedScroll).width(200);
        mainTable.add(splitTable).padBottom(10).row();

        statusLabel = new Label("Select plants for battle (max 8)", skin);
        statusLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        Table buttonTable = new Table();

        TextButton startBtn = new TextButton(" START BATTLE", skin, "green");
        TextButton backBtn = new TextButton(" Back", skin, "default");

        buttonTable.add(startBtn).width(180).height(50).padRight(10);
        buttonTable.add(backBtn).width(150).height(50);
        mainTable.add(buttonTable).row();

        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleStart();
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AdventureMenuScreen(game, user));
            }
        });
    }

    private void loadBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/plant_selection_background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("Plant selection background not found! Using default color.");
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

    private void updatePlantList() {
        plantListTable.clear();

        if (ownedPlants.isEmpty()) {
            plantListTable.add(new Label("⚠ No plants owned!", skin)).padTop(20).row();
            return;
        }

        int cols = 2;
        int colCount = 0;
        Table rowTable = new Table();

        for (Plant plant : ownedPlants) {
            if (colCount >= cols) {
                plantListTable.add(rowTable).padBottom(5).row();
                rowTable = new Table();
                colCount = 0;
            }

            Table card = createPlantCard(plant);
            rowTable.add(card).width(150).height(210).pad(5);
            colCount++;
        }

        if (colCount > 0) {
            plantListTable.add(rowTable).padBottom(5).row();
        }
    }

    private Table createPlantCard(Plant plant) {
        Table card = new Table();
        card.setBackground(skin.getDrawable("white_pixel"));

        boolean isSelected = selectedPlants.contains(plant);
        boolean isBoosted = plant.isBoosted();
        boolean isUpgradable = plant.isUpgradable();

        if (isBoosted) {
            card.setColor(new Color(0.8f, 0.6f, 0.1f, 0.9f));
        } else if (isSelected) {
            card.setColor(new Color(0.3f, 0.3f, 0.1f, 0.9f));
        } else {
            card.setColor(new Color(0.18f, 0.18f, 0.18f, 0.85f));
        }

        Image plantImage;
        try {
            Texture texture = new Texture(Gdx.files.internal(plant.getImagePath()));
            plantImage = new Image(texture);
            plantImage.setSize(50, 50);
        } catch (Exception e) {
            plantImage = new Image();
            plantImage.setSize(50, 50);
            plantImage.setColor(0.3f, 0.3f, 0.3f, 1);
        }

        Label nameLabel = new Label(plant.getName(), skin);
        nameLabel.setFontScale(0.7f);

        Label costLabel = new Label("☀ " + plant.getCost(), skin);
        costLabel.setFontScale(0.65f);

        Label levelLabel = new Label("Lv." + plant.getLevel(), skin);
        levelLabel.setFontScale(0.65f);

        Label seedLabel = new Label("Seeds: " + plant.getSeedPackets() + "/" + plant.getSeedPacketsNeeded(), skin);
        seedLabel.setFontScale(0.55f);

        Label statusLabel = new Label(isSelected ? "✅" : (isBoosted ? "⭐" : ""), skin);
        statusLabel.setFontScale(0.8f);

        Table buttonTable = new Table();

        TextButton selectBtn;
        if (isSelected) {
            selectBtn = new TextButton("Remove", skin, "default");
        } else if (selectedPlants.size() >= MAX_PLANTS) {
            selectBtn = new TextButton("Full", skin, "default");
            selectBtn.setDisabled(true);
        } else {
            selectBtn = new TextButton("Select", skin, "green");
        }
        selectBtn.setWidth(70);
        selectBtn.setHeight(25);

        final Plant finalPlant = plant;
        selectBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isSelected) {
                    selectedPlants.remove(finalPlant);
                    updatePlantList();
                    updateSelectedList();
                    errorLabel.setText("");
                } else if (selectedPlants.size() < MAX_PLANTS) {
                    selectedPlants.add(finalPlant);
                    updatePlantList();
                    updateSelectedList();
                    errorLabel.setText("");
                } else {
                    errorLabel.setText(" Max " + MAX_PLANTS + " plants selected!");
                }
            }
        });

        TextButton upgradeBtn = null;
        if (isUpgradable && !isSelected) {
            upgradeBtn = new TextButton("⬆", skin, "default");
            upgradeBtn.setWidth(30);
            upgradeBtn.setHeight(25);
            upgradeBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleUpgrade(finalPlant);
                }
            });
        }

        card.add(plantImage).size(50, 50).padTop(8).row();
        card.add(nameLabel).padTop(4).row();
        card.add(costLabel).row();
        card.add(levelLabel).row();
        card.add(seedLabel).row();
        card.add(statusLabel).padTop(2).row();

        Table bottomRow = new Table();
        bottomRow.add(selectBtn).width(70).height(25);
        if (upgradeBtn != null) {
            bottomRow.add(upgradeBtn).width(30).height(25).padLeft(5);
        }
        card.add(bottomRow).padBottom(8).row();

        return card;
    }

    private void handleUpgrade(Plant plant) {
        PlayerProgress progress = user.getProgress();

        int upgradeCost = 50;

        if (progress.getCoins() < upgradeCost) {
            errorLabel.setText("❌ Not enough coins! Need " + upgradeCost + " coins.");
            return;
        }

        if (plant.getSeedPackets() < plant.getSeedPacketsNeeded()) {
            errorLabel.setText("❌ Not enough seed packets!");
            return;
        }

        progress.deductCoins(upgradeCost);
        plant.setSeedPackets(plant.getSeedPackets() - plant.getSeedPacketsNeeded());
        plant.setLevel(plant.getLevel() + 1);
        plant.setUpgradable(false);

        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        updatePlantList();
        updateSelectedList();

        showToast("✅ " + plant.getName() + " upgraded to Lv." + plant.getLevel() + "!", 2f, false);
    }

    private void updateSelectedList() {
        selectedTable.clear();

        if (selectedPlants.isEmpty()) {
            selectedTable.add(new Label(" No plants selected", skin)).padTop(20).row();
            selectedCountLabel.setText("Selected: 0/" + MAX_PLANTS);
            return;
        }

        selectedCountLabel.setText("Selected: " + selectedPlants.size() + "/" + MAX_PLANTS);

        for (Plant plant : selectedPlants) {
            Table card = new Table();
            card.setBackground(skin.getDrawable("white_pixel"));

            if (plant.isBoosted()) {
                card.setColor(new Color(0.8f, 0.6f, 0.1f, 0.9f));
            } else {
                card.setColor(new Color(0.2f, 0.2f, 0.1f, 0.9f));
            }
            card.pad(5);

            Image plantImage;
            try {
                Texture texture = new Texture(Gdx.files.internal(plant.getImagePath()));
                plantImage = new Image(texture);
                plantImage.setSize(30, 30);
            } catch (Exception e) {
                plantImage = new Image();
                plantImage.setSize(30, 30);
                plantImage.setColor(0.3f, 0.3f, 0.3f, 1);
            }

            Label nameLabel = new Label(plant.getName(), skin);
            nameLabel.setFontScale(0.6f);

            Label costLabel = new Label("☀" + plant.getCost(), skin);
            costLabel.setFontScale(0.5f);

            TextButton removeBtn = new TextButton("✕", skin, "default");
            removeBtn.setWidth(25);
            removeBtn.setHeight(25);

            final Plant finalPlant = plant;
            removeBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedPlants.remove(finalPlant);
                    updatePlantList();
                    updateSelectedList();
                    errorLabel.setText("");
                }
            });

            Table row = new Table();
            row.add(plantImage).size(30, 30).padRight(5);
            row.add(nameLabel).padRight(5);
            row.add(costLabel).padRight(5);
            row.add(removeBtn);

            selectedTable.add(row).padBottom(5).row();
        }

        int remaining = MAX_PLANTS - selectedPlants.size();
        for (int i = 0; i < remaining; i++) {
            Table emptyCard = new Table();
            emptyCard.setBackground(skin.getDrawable("white_pixel"));
            emptyCard.setColor(0.1f, 0.1f, 0.1f, 0.5f);
            emptyCard.add(new Label("⬜", skin)).pad(5);
            selectedTable.add(emptyCard).width(60).height(30).pad(2).row();
        }
    }

    private void handleStart() {
        if (selectedPlants.isEmpty()) {
            errorLabel.setText("❌ Select at least one plant!");
            return;
        }

        PlayerProgress progress = user.getProgress();
        List<String> plantNames = new ArrayList<>();
        for (Plant plant : selectedPlants) {
            plantNames.add(plant.getName().toUpperCase());
        }

        statusLabel.setText("✅ Battle started with " + selectedPlants.size() + " plants!");

        System.out.println("▶ Starting battle in " + chapterName + " with: " + plantNames);
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
    }
}
