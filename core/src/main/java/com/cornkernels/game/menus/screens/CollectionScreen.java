package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cornkernels.GameManager;
import com.cornkernels.game.menus.model.Plant;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.User;
import com.cornkernels.game.menus.model.Zombie;
import com.cornkernels.game.menus.utils.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class CollectionScreen extends BaseScreen {

    private User user;
    private Image backgroundImage;
    private Table contentTable;
    private Table detailTable;
    private Label detailLabel;
    private Image detailImage;
    private List<Plant> allPlants;
    private List<Zombie> allZombies;

    private String currentFilter = "ALL";
    private String currentCategoryFilter = "ALL";
    private List<Plant> filteredPlants;

    private Drawable darkDrawable;
    private Drawable darkerDrawable;
    private Drawable cardDrawable;

    public CollectionScreen(GameManager game, User user) {
        super(game);
        this.user = user;

        allPlants = DataLoader.loadAllPlants();
        allZombies = DataLoader.loadAllZombies();

        // ===== زامبی‌های All Chapters رو دیده‌شده کن =====
        for (Zombie z : allZombies) {
            if (z.getChapter().equals("All Chapters")) {
                z.setSeen(true);
            }
        }

        filteredPlants = new ArrayList<>(allPlants);

        createDrawables();
        loadBackground();
        buildUI();
        showPlantsTab();
    }

    private void createDrawables() {
        Pixmap darkPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        darkPixmap.setColor(Color.DARK_GRAY);
        darkPixmap.fill();
        darkDrawable = new TextureRegionDrawable(new Texture(darkPixmap));
        darkPixmap.dispose();

        Pixmap darkerPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        darkerPixmap.setColor(Color.BLACK);
        darkerPixmap.fill();
        darkerDrawable = new TextureRegionDrawable(new Texture(darkerPixmap));
        darkerPixmap.dispose();

        Pixmap cardPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        cardPixmap.setColor(new Color(0.18f, 0.18f, 0.18f, 0.85f));
        cardPixmap.fill();
        cardDrawable = new TextureRegionDrawable(new Texture(cardPixmap));
        cardPixmap.dispose();
    }

    private void loadBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/collection_background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("Collection background not found! Using default color.");
        }
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" COLLECTION", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        ButtonGroup<TextButton> tabGroup = new ButtonGroup<>();
        tabGroup.setMaxCheckCount(1);

        TextButton plantsTab = new TextButton("  Plants", skin, "default");
        TextButton zombiesTab = new TextButton("  Zombies", skin, "default");
        plantsTab.setChecked(true);

        Table tabTable = new Table();
        tabTable.add(plantsTab).width(150).height(40).padRight(10);
        tabTable.add(zombiesTab).width(150).height(40);
        mainTable.add(tabTable).padBottom(10).row();

        Table filterTable = new Table();
        filterTable.add(new Label("Filter:", skin)).padRight(10);

        SelectBox<String> filterSelect = new SelectBox<>(skin);
        filterSelect.setItems("ALL", "UNLOCKED", "LOCKED", "UPGRADABLE");
        filterSelect.setSelected("ALL");
        filterTable.add(filterSelect).width(150).padRight(20);

        SelectBox<String> categorySelect = new SelectBox<>(skin);
        categorySelect.setItems("ALL", "Sun Producer", "Shooter", "Homing", "Strike-through",
            "Lobber", "Explosive", "Melee", "Wall-nut", "Modifier");
        categorySelect.setSelected("ALL");
        filterTable.add(categorySelect).width(150);

        mainTable.add(filterTable).padBottom(10).row();

        contentTable = new Table();
        mainTable.add(contentTable).padBottom(10).row();

        detailTable = new Table();
        detailTable.setBackground(darkDrawable);
        detailTable.setWidth(500);
        detailTable.setHeight(220);

        detailImage = new Image();
        detailImage.setSize(80, 80);

        detailLabel = new Label(" Select an item to see details", skin);
        detailLabel.setWrap(true);
        detailLabel.setWidth(380);
        detailLabel.setColor(1, 1, 0.8f, 1);

        Table detailInnerTable = new Table();
        detailInnerTable.add(detailImage).size(80, 80).padRight(15);
        detailInnerTable.add(detailLabel).width(380);

        detailTable.add(detailInnerTable).pad(10);
        mainTable.add(detailTable).width(500).height(220).padBottom(10).row();

        TextButton backBtn = new TextButton(" Back", skin, "default");
        mainTable.add(backBtn).width(150).height(50).row();

        plantsTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                plantsTab.setChecked(true);
                zombiesTab.setChecked(false);
                showPlantsTab();
            }
        });

        zombiesTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                zombiesTab.setChecked(true);
                plantsTab.setChecked(false);
                showZombiesTab();
            }
        });

        filterSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentFilter = filterSelect.getSelected();
                applyFilters();
            }
        });

        categorySelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentCategoryFilter = categorySelect.getSelected();
                applyFilters();
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameMenuScreen(game, user));
            }
        });
    }

    private void applyFilters() {
        filteredPlants.clear();
        for (Plant plant : allPlants) {
            boolean passFilter = true;
            boolean passCategory = true;

            switch (currentFilter) {
                case "UNLOCKED":
                    passFilter = plant.isUnlocked();
                    break;
                case "LOCKED":
                    passFilter = !plant.isUnlocked();
                    break;
                case "UPGRADABLE":
                    passFilter = plant.isUnlocked() && plant.canUpgrade();
                    break;
                default:
                    passFilter = true;
                    break;
            }

            if (!currentCategoryFilter.equals("ALL")) {
                passCategory = plant.getCategory().equals(currentCategoryFilter);
            }

            if (passFilter && passCategory) {
                filteredPlants.add(plant);
            }
        }
        showPlantsTab();
    }

    private void showPlantsTab() {
        contentTable.clear();

        Table plantsTable = new Table();

        int cols = 3;
        int colCount = 0;
        Table rowTable = new Table();

        for (Plant plant : filteredPlants) {
            if (colCount >= cols) {
                plantsTable.add(rowTable).padBottom(5).row();
                rowTable = new Table();
                colCount = 0;
            }

            Table card = createPlantCard(plant);
            rowTable.add(card).width(160).height(230).pad(5);
            colCount++;
        }

        if (colCount > 0) {
            plantsTable.add(rowTable).padBottom(5).row();
        }

        ScrollPane scrollPane = new ScrollPane(plantsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setOverscroll(false, true);

        contentTable.add(scrollPane).width(650).height(380);
    }

    private void showZombiesTab() {
        contentTable.clear();

        Table zombiesTable = new Table();

        int cols = 3;
        int colCount = 0;
        Table rowTable = new Table();

        for (Zombie zombie : allZombies) {
            if (colCount >= cols) {
                zombiesTable.add(rowTable).padBottom(5).row();
                rowTable = new Table();
                colCount = 0;
            }

            Table card = createZombieCard(zombie);
            rowTable.add(card).width(140).height(190).pad(5);
            colCount++;
        }

        if (colCount > 0) {
            zombiesTable.add(rowTable).padBottom(5).row();
        }

        ScrollPane scrollPane = new ScrollPane(zombiesTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setOverscroll(false, true);

        contentTable.add(scrollPane).width(650).height(380);
    }

    private Table createPlantCard(Plant plant) {
        Table card = new Table();
        card.setBackground(cardDrawable);
        card.pad(5);

        Image plantImage;
        try {
            String fullPath = "IMAGES/plants/" + plant.getImagePath();
            Texture texture = new Texture(Gdx.files.internal(fullPath));
            plantImage = new Image(texture);
            plantImage.setSize(96, 96);

            if (!plant.isUnlocked()) {
                plantImage.setColor(0.3f, 0.3f, 0.3f, 0.5f);
            }
        } catch (Exception e) {
            plantImage = new Image();
            plantImage.setSize(96, 96);
            if (plant.isUnlocked()) {
                plantImage.setColor(0.3f, 0.3f, 0.3f, 1);
            } else {
                plantImage.setColor(0.05f, 0.05f, 0.05f, 1);
            }
        }

        Label nameLabel = new Label(plant.getName(), skin);
        nameLabel.setFontScale(0.75f);

        Label levelLabel = new Label("Lv." + plant.getLevel(), skin);
        levelLabel.setFontScale(0.65f);

        Label costLabel = new Label("☀ " + plant.getCost(), skin);
        costLabel.setFontScale(0.65f);

        Label seedLabel = new Label("Seeds: " + plant.getSeedPackets() + "/" + plant.getSeedPacketsNeeded(), skin);
        seedLabel.setFontScale(0.6f);

        Label statusLabel = new Label(plant.isUnlocked() ? "✅" : "🔒", skin);
        statusLabel.setFontScale(0.8f);

        Table buttonTable = new Table();

        if (plant.isUnlocked() && plant.getLevel() < plant.getMaxLevel()) {
            boolean canUpgrade = plant.canUpgrade();
            int upgradeCost = plant.getUpgradeCost();

            TextButton upgradeBtn;
            if (canUpgrade) {
                upgradeBtn = new TextButton("⬆ Lv." + (plant.getLevel() + 1) + " (" + upgradeCost + ")", skin, "green");
            } else {
                upgradeBtn = new TextButton("Need " + plant.getSeedPacketsNeeded() + " seeds", skin, "default");
                upgradeBtn.setDisabled(true);
            }
            upgradeBtn.setWidth(100);
            upgradeBtn.setHeight(25);

            final Plant finalPlant = plant;
            upgradeBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (canUpgrade) {
                        handleUpgrade(finalPlant);
                    }
                }
            });
            buttonTable.add(upgradeBtn).padRight(5);
        }

        if (!plant.isUnlocked()) {
            TextButton buyBtn = new TextButton("Buy 100", skin, "default");
            buyBtn.setWidth(80);
            buyBtn.setHeight(25);

            final Plant finalPlant = plant;
            buyBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    buyPlant(finalPlant);
                }
            });
            buttonTable.add(buyBtn);
        }

        card.add(plantImage).size(96, 96).padTop(8).row();

        Table infoTable = new Table();
        infoTable.add(nameLabel).row();
        infoTable.add(levelLabel).row();
        infoTable.add(costLabel).row();
        infoTable.add(seedLabel).row();
        infoTable.add(statusLabel).row();

        card.add(infoTable).padTop(4).row();
        card.add(buttonTable).padBottom(8).row();

        card.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPlantDetail(plant);
            }
        });

        return card;
    }

    private Table createZombieCard(Zombie zombie) {
        Table card = new Table();
        card.pad(5);

        if (zombie.isSeen()) {
            card.setBackground(cardDrawable);
        } else {
            card.setBackground(darkerDrawable);
        }

        Image zombieImage;
        if (zombie.isSeen()) {
            try {
                String fullPath = "IMAGES/zombies/" + zombie.getImagePath();
                Texture texture = new Texture(Gdx.files.internal(fullPath));
                zombieImage = new Image(texture);
                zombieImage.setSize(96, 96);
            } catch (Exception e) {
                zombieImage = new Image();
                zombieImage.setSize(96, 96);
                zombieImage.setColor(0.3f, 0.3f, 0.3f, 1);
            }
        } else {
            zombieImage = new Image();
            zombieImage.setSize(96, 96);
            zombieImage.setColor(0.05f, 0.05f, 0.05f, 1);
        }

        Label nameLabel = new Label(zombie.isSeen() ? zombie.getName() : "???", skin);
        nameLabel.setFontScale(0.75f);

        Label statusLabel = new Label(zombie.isSeen() ? "🧟" : "❓", skin);
        statusLabel.setFontScale(0.8f);

        Label hpLabel = new Label(zombie.isSeen() ? "HP: " + zombie.getHitpoints() : "", skin);
        hpLabel.setFontScale(0.6f);

        card.add(zombieImage).size(96, 96).padTop(8).row();

        Table infoTable = new Table();
        infoTable.add(nameLabel).row();
        if (zombie.isSeen()) {
            infoTable.add(hpLabel).row();
        }
        infoTable.add(statusLabel).row();

        card.add(infoTable).padTop(4).padBottom(8).row();

        card.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showZombieDetail(zombie);
            }
        });

        return card;
    }

    private void handleUpgrade(Plant plant) {
        PlayerProgress progress = user.getProgress();
        int cost = plant.getUpgradeCost();

        if (progress.getCoins() < cost) {
            showToast("❌ Not enough coins! Need " + cost + " coins.", 2f, true);
            return;
        }

        if (plant.getSeedPackets() < plant.getSeedPacketsNeeded()) {
            showToast("❌ Not enough seeds! Need " + plant.getSeedPacketsNeeded() + " seeds.", 2f, true);
            return;
        }

        if (plant.getLevel() >= plant.getMaxLevel()) {
            showToast("⭐ Already at max level!", 2f, false);
            return;
        }

        progress.deductCoins(cost);
        plant.performUpgrade();

        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        applyFilters();

        showToast("✅ " + plant.getName() + " upgraded to Lv." + plant.getLevel() + "!", 2f, false);
    }

    private void buyPlant(Plant plant) {
        PlayerProgress progress = user.getProgress();
        int cost = 100;

        if (progress.getCoins() >= cost) {
            progress.deductCoins(cost);
            plant.setUnlocked(true);
            progress.addPlant(plant.getName().toUpperCase());
            game.getStorageService().saveUsers();
            updateCurrencyDisplay();

            applyFilters();
            showToast("✅ " + plant.getName() + " purchased successfully!", 2f, false);
        } else {
            showToast("❌ Not enough coins! Need " + cost + " coins.", 2f, true);
        }
    }

    private void showPlantDetail(Plant plant) {
        try {
            String fullPath = "IMAGES/plants/" + plant.getImagePath();
            Texture texture = new Texture(Gdx.files.internal(fullPath));
            detailImage.setDrawable(new Image(texture).getDrawable());
            detailImage.setSize(80, 80);
        } catch (Exception e) {
            detailImage.setColor(0.3f, 0.3f, 0.3f, 1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("🌱 ").append(plant.getName()).append("\n");
        sb.append("Category: ").append(plant.getCategory()).append("\n");
        sb.append("Cost: ").append(plant.getCost()).append(" sun\n");
        sb.append("HP: ").append(plant.getBaseHp()).append("\n");
        sb.append("Damage: ").append(plant.getDamage()).append("\n");
        sb.append("Recharge: ").append(plant.getRecharge()).append("s\n");
        sb.append("Level: ").append(plant.getLevel()).append("/").append(plant.getMaxLevel()).append("\n");
        sb.append("Seeds: ").append(plant.getSeedPackets()).append("/").append(plant.getSeedPacketsNeeded()).append("\n");
        sb.append("Status: ").append(plant.isUnlocked() ? "✅ Unlocked" : "🔒 Locked").append("\n");

        if (plant.getTags().length > 0) {
            sb.append("Tags: ");
            for (String tag : plant.getTags()) {
                sb.append(tag).append(" ");
            }
            sb.append("\n");
        }

        sb.append("\nAbility: ").append(plant.getBaseAbility()).append("\n");
        sb.append("Plant Food: ").append(plant.getPlantFoodEffect());

        detailLabel.setText(sb.toString());
    }

    private void showZombieDetail(Zombie zombie) {
        if (!zombie.isSeen()) {
            detailImage.setColor(0.05f, 0.05f, 0.05f, 1);
            detailLabel.setText("❓ This zombie hasn't been discovered yet!\n\nPlay more levels to find it.");
            return;
        }

        try {
            String fullPath = "IMAGES/zombies/" + zombie.getImagePath();
            Texture texture = new Texture(Gdx.files.internal(fullPath));
            detailImage.setDrawable(new Image(texture).getDrawable());
            detailImage.setSize(80, 80);
        } catch (Exception e) {
            detailImage.setColor(0.3f, 0.3f, 0.3f, 1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("🧟 ").append(zombie.getName()).append("\n");
        sb.append("Chapter: ").append(zombie.getChapter()).append("\n");
        sb.append("HP: ").append(zombie.getHitpoints()).append("\n");
        sb.append("Speed: ").append(String.format("%.2f", zombie.getSpeed())).append("\n");
        sb.append("Damage: ").append(zombie.getEatDPS()).append("\n");

        if (zombie.getArmorTypes().length > 0) {
            sb.append("Armor: ");
            for (String armor : zombie.getArmorTypes()) {
                sb.append(armor).append(" ");
            }
            sb.append("\n");
        }

        sb.append("\n").append(zombie.getDescription());

        detailLabel.setText(sb.toString());
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
