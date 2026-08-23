package io.github.some_example_name.screens;

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
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.GardenPot;
import io.github.some_example_name.model.PlayerProgress;
import io.github.some_example_name.model.User;

import java.util.ArrayList;
import java.util.List;

public class ShopScreen extends BaseScreen {  // ✅ تغییر کلیدی

    private User user;
    private Image backgroundImage;

    private Label statusLabel;
    private Table itemsTable;
    private Table infoTable;

    private List<ShopItem> shopItems;
    private ShopItem selectedItem;

    private String[] dailyPlants = {
        "SUNFLOWER", "PEASHOOTER", "WALL_NUT", "POTATO_MINE",
        "CHERRY_BOMB", "SNOW_PEA", "REPEATER", "FIRE_PEASHOOTER",
        "BONK_CHOY", "CACTUS", "STARFRUIT", "MELON_PULT"
    };

    public ShopScreen(Main game, User user) {
        super(game);  // ✅ تغییر کلیدی
        this.user = user;

        initializeShopItems();
        loadBackground();
        // ❌ حذف: setupCurrencyDisplay();
        buildUI();
        updateItemsDisplay();
    }

    private void initializeShopItems() {
        shopItems = new ArrayList<>();

        int randomIndex = (int)(Math.random() * dailyPlants.length);
        String dailyPlant = dailyPlants[randomIndex];
        int originalPrice = getPlantCost(dailyPlant);
        int discountPrice = originalPrice / 2;

        shopItems.add(new ShopItem("DAILY OFFER: " + dailyPlant,
            "50% OFF! Limited time", discountPrice, "Coins",
            "IMAGES/plants/" + dailyPlant.toLowerCase() + ".png", true, 0));

        shopItems.add(new ShopItem("POT", "Unlock a new pot", 200, "Coins",
            "IMAGES/greenhouse/pot_empty.png", false, 0));

        shopItems.add(new ShopItem("PLANT FOOD", "Instantly grow a plant", 3, "Diamonds",
            "IMAGES/shop/plant_food.png", false, 0));

        shopItems.add(new ShopItem("RANDOM SEED", "Get a random plant seed", 1000, "Coins",
            "IMAGES/shop/seed_packet.png", false, 0));

        shopItems.add(new ShopItem("CHOSEN SEED", "Choose a plant seed", 5, "Diamonds",
            "IMAGES/shop/seed_packet.png", false, 0));
    }

    private int getPlantCost(String plantName) {
        switch (plantName) {
            case "SUNFLOWER": return 100;
            case "PEASHOOTER": return 150;
            case "WALL_NUT": return 80;
            case "POTATO_MINE": return 60;
            case "CHERRY_BOMB": return 200;
            case "SNOW_PEA": return 180;
            case "REPEATER": return 250;
            case "FIRE_PEASHOOTER": return 220;
            case "BONK_CHOY": return 170;
            case "CACTUS": return 190;
            case "STARFRUIT": return 160;
            case "MELON_PULT": return 300;
            default: return 100;
        }
    }

    private void loadBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/shop_background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("Shop background not found! Using default color.");
        }
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" SHOP", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        infoTable = new Table();
        updateInfoDisplay();
        mainTable.add(infoTable).padBottom(10).row();

        itemsTable = new Table();
        itemsTable.setBackground(skin.getDrawable("white_pixel"));
        itemsTable.setColor(0.2f, 0.15f, 0.1f, 0.5f);
        itemsTable.pad(10);

        ScrollPane scrollPane = new ScrollPane(itemsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(350);
        mainTable.add(scrollPane).width(500).height(350).padBottom(10).row();

        statusLabel = new Label("Select an item to buy", skin);
        statusLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        Table buttonTable = new Table();

        TextButton buyBtn = new TextButton(" Buy Selected", skin, "green");
        TextButton backBtn = new TextButton(" Back", skin, "default");

        buttonTable.add(buyBtn).width(150).height(50).padRight(10);
        buttonTable.add(backBtn).width(100).height(50);

        mainTable.add(buttonTable).row();

        buyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleBuy();
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
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin));
    }

    private void updateItemsDisplay() {
        itemsTable.clear();

        for (ShopItem item : shopItems) {
            Table card = createItemCard(item);
            itemsTable.add(card).width(450).height(80).pad(5).row();
        }
    }

    private Table createItemCard(ShopItem item) {
        Table card = new Table();
        card.setBackground(skin.getDrawable("white_pixel"));
        card.setColor(0.18f, 0.18f, 0.18f, 0.85f);
        card.pad(10);

        Image itemImage;
        try {
            Texture texture = new Texture(Gdx.files.internal(item.imagePath));
            itemImage = new Image(texture);
            itemImage.setSize(50, 50);
        } catch (Exception e) {
            itemImage = new Image();
            itemImage.setSize(50, 50);
            itemImage.setColor(0.3f, 0.3f, 0.3f, 1);
        }

        Label nameLabel = new Label(item.name, skin);
        nameLabel.setFontScale(0.8f);

        Label descLabel = new Label(item.description, skin);
        descLabel.setFontScale(0.6f);
        descLabel.setColor(0.8f, 0.8f, 0.8f, 1);

        String priceText = item.currency.equals("Coins") ? "🪙" : "💎";
        Label priceLabel = new Label(priceText + " " + item.price, skin);
        priceLabel.setFontScale(0.8f);
        priceLabel.setColor(1, 0.8f, 0, 1);

        TextButton selectBtn = new TextButton("Select", skin, "default");
        selectBtn.setWidth(80);
        selectBtn.setHeight(30);

        final ShopItem finalItem = item;
        selectBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectItem(finalItem);
            }
        });

        card.add(itemImage).size(50, 50).padRight(10);

        Table infoTable = new Table();
        infoTable.add(nameLabel).left().row();
        infoTable.add(descLabel).left().row();
        card.add(infoTable).padRight(10);

        Table rightTable = new Table();
        rightTable.add(priceLabel).padRight(10);
        rightTable.add(selectBtn);
        card.add(rightTable);

        return card;
    }

    private void selectItem(ShopItem item) {
        selectedItem = item;
        statusLabel.setText("Selected: " + item.name + " - Price: " + item.price + " " + item.currency);
    }

    private void handleBuy() {
        if (selectedItem == null) {
            showToast("❌ Please select an item first!", 2f, true);
            return;
        }

        PlayerProgress progress = user.getProgress();

        if (selectedItem.currency.equals("Coins")) {
            if (progress.getCoins() < selectedItem.price) {
                showToast("❌ Not enough coins! Need " + selectedItem.price + " coins.", 2f, true);
                return;
            }
        } else {
            if (progress.getDiamonds() < selectedItem.price) {
                showToast("❌ Not enough diamonds! Need " + selectedItem.price + " diamonds.", 2f, true);
                return;
            }
        }

        showConfirmDialog(selectedItem);
    }

    private void showConfirmDialog(ShopItem item) {
        Dialog confirmDialog = new Dialog("Confirm Purchase", skin) {
            @Override
            protected void result(Object object) {
                if ((boolean) object) {
                    completePurchase(item);
                } else {
                    showToast("❌ Purchase cancelled.", 1.5f, true);
                }
            }
        };

        confirmDialog.text("Buy " + item.name + " for " + item.price + " " + item.currency + "?");
        confirmDialog.button("✅ Yes", true);
        confirmDialog.button("❌ No", false);
        confirmDialog.show(stage);
    }

    private void completePurchase(ShopItem item) {
        PlayerProgress progress = user.getProgress();

        if (item.currency.equals("Coins")) {
            progress.deductCoins(item.price);
        } else {
            progress.deductDiamonds(item.price);
        }

        if (item.name.startsWith("DAILY OFFER:")) {
            String plantName = item.name.replace("DAILY OFFER: ", "");
            progress.addSeedPacket(plantName);
            showToast("✅ " + plantName + " seed added! (Daily Offer)", 2f, false);

        } else {
            switch (item.name) {
                case "POT":
                    for (GardenPot pot : user.getProgress().getGarden().getPots()) {
                        if (pot.isLocked()) {
                            pot.setLocked(false);
                            showToast("✅ Pot unlocked successfully!", 2f, false);
                            break;
                        }
                    }
                    break;

                case "PLANT FOOD":
                    progress.setPlantFood(progress.getPlantFood() + 1);
                    showToast("✅ Plant Food added! Total: " + progress.getPlantFood(), 2f, false);
                    break;

                case "RANDOM SEED":
                    String[] plants = {"SUNFLOWER", "PEASHOOTER", "WALL_NUT", "POTATO_MINE",
                        "CHERRY_BOMB", "SNOW_PEA", "REPEATER", "CACTUS"};
                    String randomPlant = plants[(int)(Math.random() * plants.length)];
                    progress.addSeedPacket(randomPlant);
                    showToast("✅ Random seed: " + randomPlant + " added!", 2f, false);
                    break;

                case "CHOSEN SEED":
                    showPlantSelectionDialog();
                    break;
            }
        }

        game.getStorageService().saveUsers();
        updateCurrencyDisplay();
        updateInfoDisplay();
        updateItemsDisplay();
    }

    private void showPlantSelectionDialog() {
        Dialog plantDialog = new Dialog("Choose a Plant", skin);

        Table plantTable = new Table();
        String[] plants = {"SUNFLOWER", "PEASHOOTER", "WALL_NUT", "POTATO_MINE",
            "CHERRY_BOMB", "SNOW_PEA", "REPEATER", "CACTUS"};

        for (String plant : plants) {
            TextButton plantBtn = new TextButton(plant, skin, "default");
            final String selectedPlant = plant;
            plantBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    plantDialog.hide();
                    plantDialog.remove();

                    user.getProgress().addSeedPacket(selectedPlant);
                    game.getStorageService().saveUsers();
                    updateCurrencyDisplay();
                    updateInfoDisplay();
                    showToast("✅ " + selectedPlant + " seed added!", 2f, false);
                }
            });
            plantTable.add(plantBtn).width(150).height(40).pad(5).row();
        }

        plantDialog.getContentTable().add(plantTable);

        TextButton cancelBtn = new TextButton("Cancel", skin, "default");
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                plantDialog.hide();
                plantDialog.remove();
            }
        });
        plantDialog.getContentTable().add(cancelBtn).padTop(10);

        plantDialog.show(stage);
    }

    private static class ShopItem {
        String name;
        String description;
        int price;
        String currency;
        String imagePath;
        boolean isDaily;
        int timeRemaining;

        ShopItem(String name, String description, int price, String currency,
                 String imagePath, boolean isDaily, int timeRemaining) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.currency = currency;
            this.imagePath = imagePath;
            this.isDaily = isDaily;
            this.timeRemaining = timeRemaining;
        }
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
