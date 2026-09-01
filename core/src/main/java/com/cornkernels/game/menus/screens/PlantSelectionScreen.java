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
import com.cornkernels.game.menus.model.Plant;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.User;
import com.cornkernels.game.menus.utils.DataLoader;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PlantSelectionScreen extends BaseScreen {

    private static final int MAX_PLANTS = 8;
    private static final int SCROLLBAR_WIDTH = 20;
    private User user;
    private String chapterName;
    private Image backgroundImage;
    private List<Plant> allPlants;
    private List<Plant> selectedPlants;
    private List<Plant> ownedPlants;
    private Table plantListTable;
    private Table selectedTable;
    private Label selectedCountLabel;
    private Label errorLabel;
    private Label statusLabel;
    private ScrollPane plantScroll;
    // ===== Drawable برای کارت‌ها =====
    private Drawable cardDrawable;

    public PlantSelectionScreen(GameManager game, User user, String chapterName) {
        super(game);
        this.user = user;
        this.chapterName = chapterName;
        this.selectedPlants = new ArrayList<>();

        createDrawables();
        allPlants = DataLoader.loadAllPlants();
        ownedPlants = getOwnedPlants();

        loadBackground();
        buildUI();
        updatePlantList();
        updateSelectedList();
    }

    private void createDrawables() {
        Pixmap cardPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        cardPixmap.setColor(new Color(0.18f, 0.18f, 0.18f, 0.85f));
        cardPixmap.fill();
        cardDrawable = new TextureRegionDrawable(new Texture(cardPixmap));
        cardPixmap.dispose();
    }

    private ScrollPane.ScrollPaneStyle createWideScrollPaneStyle() {
        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle(skin.get(ScrollPane.ScrollPaneStyle.class));

        Pixmap trackPixmap = new Pixmap(SCROLLBAR_WIDTH, SCROLLBAR_WIDTH, Pixmap.Format.RGBA8888);
        trackPixmap.setColor(new Color(0.1f, 0.1f, 0.1f, 0.6f));
        trackPixmap.fill();
        Drawable trackDrawable = new TextureRegionDrawable(new Texture(trackPixmap));
        trackPixmap.dispose();

        Pixmap knobPixmap = new Pixmap(SCROLLBAR_WIDTH, SCROLLBAR_WIDTH, Pixmap.Format.RGBA8888);
        knobPixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 0.95f));
        knobPixmap.fill();
        Drawable knobDrawable = new TextureRegionDrawable(new Texture(knobPixmap));
        knobPixmap.dispose();

        style.vScroll = trackDrawable;
        style.vScrollKnob = knobDrawable;
        style.hScroll = trackDrawable;
        style.hScrollKnob = knobDrawable;
        return style;
    }

    private @NonNull List<Plant> getOwnedPlants() {
        List<Plant> result = new ArrayList<>();
        PlayerProgress progress = user.getProgress();
        for (Plant plant : allPlants) {
            if (progress.hasPlant(plant.getName())) {
                // ===== تغییر: استفاده از متدهای جدید =====
                boolean isBoosted = plant.getLevel(progress) >= 2;
                plant.setBoosted(isBoosted);
                result.add(plant);
            }
        }
        return result;
    }

    private boolean checkIfBoosted(@NonNull Plant plant) {
        PlayerProgress progress = user.getProgress();
        return plant.getLevel(progress) >= 2;
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

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" SELECT PLANTS - " + chapterName, skin);
        titleLabel.setFontScale(1.2f);
        mainTable.add(titleLabel).padBottom(10).row();

        Table infoTable = new Table();
        PlayerProgress progress = user.getProgress();

        Label coinsLabel = new Label(" Coins: " + progress.getCoins(), skin);
        coinsLabel.setFontScale(1.1f);
        infoTable.add(coinsLabel).padRight(20);

        Label diamondsLabel = new Label(" Diamonds: " + progress.getDiamonds(), skin);
        diamondsLabel.setFontScale(1.1f);
        infoTable.add(diamondsLabel).padRight(20);

        selectedCountLabel = new Label("Selected: 0/" + MAX_PLANTS, skin);
        selectedCountLabel.setFontScale(1.1f);
        infoTable.add(selectedCountLabel);
        mainTable.add(infoTable).padBottom(10).row();

        errorLabel = new Label("", skin);
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setFontScale(1.1f);
        mainTable.add(errorLabel).padBottom(10).row();

        Table splitTable = new Table();

        ScrollPane.ScrollPaneStyle wideScrollStyle = createWideScrollPaneStyle();

        plantListTable = new Table();
        plantScroll = new ScrollPane(plantListTable, wideScrollStyle);
        plantScroll.setScrollingDisabled(true, false);
        plantScroll.setHeight(350);
        plantScroll.setWidth(350);
        plantScroll.setFadeScrollBars(false);

        selectedTable = new Table();
        ScrollPane selectedScroll = new ScrollPane(selectedTable, wideScrollStyle);
        selectedScroll.setScrollingDisabled(true, false);
        selectedScroll.setHeight(350);
        selectedScroll.setWidth(200);
        selectedScroll.setFadeScrollBars(false);

        splitTable.add(plantScroll).width(350).padRight(10);
        splitTable.add(selectedScroll).width(200);
        mainTable.add(splitTable).padBottom(10).row();

        statusLabel = new Label("Select plants for battle (max 8)", skin);
        statusLabel.setFontScale(1.1f);
        statusLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        Table buttonTable = new Table();

        TextButton startBtn = new TextButton(" START BATTLE", skin, "green");
        TextButton backBtn = new TextButton(" Back", skin, "default");

        startBtn.getLabel().setFontScale(1.2f);
        backBtn.getLabel().setFontScale(1.2f);

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

    private void updatePlantList() {
        plantListTable.clear();

        if (ownedPlants.isEmpty()) {
            plantListTable.add(new Label("No plants owned!", skin)).padTop(20).row();
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
        PlayerProgress progress = user.getProgress();
        Table card = new Table();
        card.setBackground(cardDrawable);

        boolean isSelected = selectedPlants.contains(plant);
        int currentLevel = plant.getLevel(progress);
        boolean isBoosted = currentLevel >= 2;
        int plantSeeds = progress.getPlantSeedCount(plant.getName());
        int neededSeeds = plant.getSeedPacketsNeeded(progress);

        // ===== رنگ‌بندی کارت =====
        if (isBoosted) {
            card.setColor(new Color(0.8f, 0.6f, 0.1f, 0.9f));
        } else if (isSelected) {
            card.setColor(new Color(0.3f, 0.3f, 0.1f, 0.9f));
        } else {
            card.setColor(new Color(0.18f, 0.18f, 0.18f, 0.85f));
        }
        card.pad(5);

        // ===== تصویر گیاه =====
        Image plantImage;
        try {
            String fullPath = "IMAGES/plants/" + plant.getImagePath();
            Texture texture = new Texture(Gdx.files.internal(fullPath));
            plantImage = new Image(texture);
            plantImage.setSize(50, 50);
        } catch (Exception e) {
            plantImage = new Image();
            plantImage.setSize(50, 50);
            plantImage.setColor(0.3f, 0.3f, 0.3f, 1);
        }

        Label nameLabel = new Label(plant.getName(), skin);
        nameLabel.setFontScale(0.7f);

        Label costLabel = new Label(String.valueOf(plant.getCost()), skin);
        costLabel.setFontScale(0.65f);

        Label levelLabel = new Label("Lv." + currentLevel, skin);
        levelLabel.setFontScale(0.65f);

        Label seedLabel = new Label("Seeds: " + plantSeeds + "/" + neededSeeds, skin);
        seedLabel.setFontScale(0.55f);

        Label statusLabel = new Label(isSelected ? "Selected" : (isBoosted ? "Boosted" : ""), skin);
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
        selectBtn.getLabel().setFontScale(0.8f);

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
        if (currentLevel < plant.getMaxLevel()) {
            boolean hasEnoughSeeds = plantSeeds >= neededSeeds;
            if (hasEnoughSeeds) {
                upgradeBtn = new TextButton("Up", skin, "green");
                upgradeBtn.setWidth(30);
                upgradeBtn.setHeight(25);
                upgradeBtn.getLabel().setFontScale(0.8f);
                upgradeBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        handleUpgrade(finalPlant);
                    }
                });
            } else {
                upgradeBtn = new TextButton("Up", skin, "default");
                upgradeBtn.setWidth(30);
                upgradeBtn.setHeight(25);
                upgradeBtn.getLabel().setFontScale(0.8f);
                upgradeBtn.setDisabled(true);
            }
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
        int upgradeCost = plant.getUpgradeCost(progress);
        int plantSeeds = progress.getPlantSeedCount(plant.getName());
        int neededSeeds = plant.getSeedPacketsNeeded(progress);
        int currentLevel = plant.getLevel(progress);

        if (progress.getCoins() < upgradeCost) {
            errorLabel.setText("Not enough coins! Need " + upgradeCost + " coins.");
            return;
        }

        if (plantSeeds < neededSeeds) {
            errorLabel.setText("Not enough seed packets! Need " + neededSeeds + " seeds.");
            return;
        }

        if (currentLevel >= plant.getMaxLevel()) {
            errorLabel.setText("Already at max level!");
            return;
        }

        progress.deductCoins(upgradeCost);
        plant.performUpgrade(progress);

        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        updatePlantList();
        updateSelectedList();

        showToast(plant.getName() + " upgraded to Lv." + plant.getLevel(progress) + "!", 2f, false);
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
            card.setBackground(cardDrawable);

            if (plant.isBoosted()) {
                card.setColor(new Color(0.8f, 0.6f, 0.1f, 0.9f));
            } else {
                card.setColor(new Color(0.2f, 0.2f, 0.1f, 0.9f));
            }
            card.pad(5);

            Image plantImage;
            try {
                String fullPath = "IMAGES/plants/" + plant.getImagePath();
                Texture texture = new Texture(Gdx.files.internal(fullPath));
                plantImage = new Image(texture);
                plantImage.setSize(30, 30);
            } catch (Exception e) {
                plantImage = new Image();
                plantImage.setSize(30, 30);
                plantImage.setColor(0.3f, 0.3f, 0.3f, 1);
            }

            Label nameLabel = new Label(plant.getName(), skin);
            nameLabel.setFontScale(0.6f);

            Label costLabel = new Label(String.valueOf(plant.getCost()), skin);
            costLabel.setFontScale(0.5f);

            TextButton removeBtn = new TextButton("X", skin, "default");
            removeBtn.setWidth(25);
            removeBtn.setHeight(25);
            removeBtn.getLabel().setFontScale(0.8f);

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
            emptyCard.setBackground(cardDrawable);
            emptyCard.setColor(0.1f, 0.1f, 0.1f, 0.5f);
            emptyCard.add(new Label("Empty", skin)).pad(5);
            selectedTable.add(emptyCard).width(60).height(30).pad(2).row();
        }
    }

    private void handleStart() {
        if (selectedPlants.isEmpty()) {
            errorLabel.setText("Select at least one plant!");
            return;
        }

        PlayerProgress progress = user.getProgress();
        List<String> plantNames = new ArrayList<>();
        for (Plant plant : selectedPlants) {
            plantNames.add(plant.getName().toUpperCase());
        }

        statusLabel.setText("Battle started with " + selectedPlants.size() + " plants!");

        System.out.println("▶ Starting battle in " + chapterName + " with: " + plantNames);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
