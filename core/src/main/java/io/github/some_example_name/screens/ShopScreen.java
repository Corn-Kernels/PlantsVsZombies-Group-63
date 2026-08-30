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
import io.github.some_example_name.Main;
import io.github.some_example_name.model.GardenPot;
import io.github.some_example_name.model.PlayerProgress;
import io.github.some_example_name.model.User;

import java.util.ArrayList;
import java.util.List;

public class ShopScreen extends BaseScreen {

    private User user;
    private Image backgroundImage;

    private Label statusLabel;
    private Table itemsTable;
    private Table infoTable;

    private List<ShopItem> shopItems;
    private ShopItem selectedItem;
    private float dailyOfferTimeRemaining = 10f; // 10 ثانیه
    private boolean dailyOfferExpired = false;

    private String[] dailyPlants = {
        "SUNFLOWER", "PEASHOOTER", "WALL_NUT", "POTATO_MINE",
        "CHERRY_BOMB", "SNOW_PEA", "REPEATER", "FIRE_PEASHOOTER",
        "BONK_CHOY", "CACTUS", "STARFRUIT", "MELON_PULT"
    };

    public ShopScreen(Main game, User user) {
        super(game);
        this.user = user;

        initializeShopItems();
        createBackground();
        buildUI();
        updateItemsDisplay();
    }

    // ===== ساخت پس‌زمینه ساده با رنگ =====
    private void createBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/shop_background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("Shop background not found! Creating simple background.");
            // ===== ساخت پس‌زمینه ساده با Pixmap =====
            Pixmap pixmap = new Pixmap(800, 600, Pixmap.Format.RGBA8888);

            // گرادینت آبی تیره تا آبی روشن
            for (int y = 0; y < 600; y++) {
                float ratio = y / 600f;
                int r = (int)(20 + 30 * ratio);
                int g = (int)(40 + 80 * ratio);
                int b = (int)(120 + 80 * ratio);
                pixmap.setColor(r/255f, g/255f, b/255f, 1);
                pixmap.drawLine(0, y, 800, y);
            }

            // اضافه کردن طرح شطرنجی ساده
            pixmap.setColor(0.2f, 0.3f, 0.5f, 0.3f);
            for (int x = 0; x < 800; x += 80) {
                for (int y = 0; y < 600; y += 80) {
                    if ((x/80 + y/80) % 2 == 0) {
                        pixmap.fillRectangle(x, y, 80, 80);
                    }
                }
            }

            Texture bgTexture2 = new Texture(pixmap);
            pixmap.dispose();
            backgroundImage = new Image(bgTexture2);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        }
    }

    private void initializeShopItems() {
        shopItems = new ArrayList<>();

        int randomIndex = (int)(Math.random() * dailyPlants.length);
        String dailyPlant = dailyPlants[randomIndex];
        int originalPrice = getPlantCost(dailyPlant);
        int discountPrice = originalPrice / 2;
        createDailyOffer();
        //shopItems.add(new ShopItem("DAILY OFFER: " + dailyPlant,
            //"50% OFF! Limited time", discountPrice, "Coins",
            //"IMAGES/plants/" + dailyPlant.toLowerCase() + ".png", true, 0));

        shopItems.add(new ShopItem("POT", "Unlock a new pot", 200, "Coins",
            "IMAGES/shop/pot.png", false, 0));

        shopItems.add(new ShopItem("PLANT FOOD", "Instantly grow a plant", 3, "Diamonds",
            "IMAGES/shop/plant_food.png", false, 0));

        shopItems.add(new ShopItem("RANDOM SEED", "Get a random plant seed", 1000, "Coins",
            "IMAGES/shop/seed_packet.png", false, 0));

        shopItems.add(new ShopItem("CHOSEN SEED", "Choose a plant seed", 5, "Diamonds",
            "IMAGES/shop/seed_packet.png", false, 0));
    }
    private void createDailyOffer() {
        int randomIndex = (int)(Math.random() * dailyPlants.length);
        String dailyPlant = dailyPlants[randomIndex];
        int originalPrice = getPlantCost(dailyPlant);
        int discountPrice = originalPrice / 2;

        // ===== حذف کالای روزانه قدیمی =====
        shopItems.removeIf(item -> item.isDaily);

        // ===== اضافه کردن کالای جدید =====
        shopItems.add(new ShopItem("DAILY OFFER: " + dailyPlant,
            "50% OFF! Limited time", discountPrice, "Coins",
            "IMAGES/plants/" + dailyPlant.toLowerCase() + ".png", true, 0));
        //shopItems.add(0, dailyItem); // بذار اول لیست

        // ===== ریست تایمر =====
        dailyOfferTimeRemaining = 10f;
        dailyOfferExpired = false;
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

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // ===== عنوان =====
        Label titleLabel = new Label(" SHOP", skin);
        titleLabel.setFontScale(2f);
        titleLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(titleLabel).padBottom(15).row();

        // ===== اطلاعات سکه و الماس =====
        infoTable = new Table();
        updateInfoDisplay();
        mainTable.add(infoTable).padBottom(10).row();

        // ===== لیست آیتم‌ها =====
        itemsTable = new Table();
        itemsTable.pad(10);

        ScrollPane scrollPane = new ScrollPane(itemsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(350);
        scrollPane.setFadeScrollBars(false);
        mainTable.add(scrollPane).width(550).height(350).padBottom(10).row();

        // ===== وضعیت =====
        statusLabel = new Label("Select an item to buy", skin);
        statusLabel.setFontScale(1.2f);
        statusLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        // ===== دکمه‌ها =====
        Table buttonTable = new Table();

        TextButton buyBtn = new TextButton(" Buy Selected", skin, "green");
        TextButton backBtn = new TextButton(" Back", skin, "default");

        buyBtn.getLabel().setFontScale(1.2f);
        backBtn.getLabel().setFontScale(1.2f);

        buttonTable.add(buyBtn).width(160).height(50).padRight(10);
        buttonTable.add(backBtn).width(120).height(50);

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

        Label coinsLabel = new Label("Coins: " + progress.getCoins(), skin);
        coinsLabel.setFontScale(1.3f);
        coinsLabel.setColor(1, 1, 0.2f, 1);
        infoTable.add(coinsLabel).padRight(30);

        Label diamondsLabel = new Label("Diamonds: " + progress.getDiamonds(), skin);
        diamondsLabel.setFontScale(1.3f);
        diamondsLabel.setColor(0.3f, 0.8f, 1, 1);
        infoTable.add(diamondsLabel);
    }

    private void updateItemsDisplay() {
        itemsTable.clear();

        for (ShopItem item : shopItems) {
            Table card = createItemCard(item);
            itemsTable.add(card).width(500).height(90).pad(6).row();
        }
    }

    private Table createItemCard(ShopItem item) {
        Table card = new Table();

        // ===== کارت با رنگ تیره و حاشیه =====
        card.setColor(new Color(0.15f, 0.15f, 0.25f, 0.85f));
        card.pad(10);

        // ===== حاشیه دور کارت =====
        Pixmap borderPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        borderPixmap.setColor(new Color(0.3f, 0.4f, 0.6f, 0.5f));
        borderPixmap.fill();
        Drawable borderDrawable = new TextureRegionDrawable(new Texture(borderPixmap));
        borderPixmap.dispose();
        card.setBackground(borderDrawable);

        // ===== تصویر (با placeholder اگه پیدا نشه) =====
        Image itemImage;
        try {
            Texture texture = new Texture(Gdx.files.internal(item.imagePath));
            itemImage = new Image(texture);
            itemImage.setSize(55, 55);
        } catch (Exception e) {
            itemImage = new Image();
            itemImage.setSize(55, 55);
            if (item.name.contains("DAILY")) {
                itemImage.setColor(1, 0.8f, 0, 1); // طلایی
            } else if (item.name.equals("POT")) {
                itemImage.setColor(0.6f, 0.4f, 0.2f, 1); // قهوه‌ای
            } else if (item.name.equals("PLANT FOOD")) {
                itemImage.setColor(0.2f, 0.8f, 0.2f, 1); // سبز
            } else {
                itemImage.setColor(0.5f, 0.5f, 0.8f, 1); // آبی
            }
        }

        // ===== نام و توضیحات =====
        Label nameLabel = new Label(item.name, skin);
        nameLabel.setFontScale(1.1f);
        nameLabel.setColor(1, 1, 1, 1);

        Label descLabel = new Label(item.description, skin);
        descLabel.setFontScale(0.8f);
        descLabel.setColor(0.7f, 0.7f, 0.7f, 1);

        // ===== قیمت =====
        //String priceText = item.currency.equals("Coins") ? "🪙" : "💎";
        Label priceLabel = new Label(item.price + " " + item.currency, skin);
        priceLabel.setFontScale(1.1f);
        priceLabel.setColor(1, 0.8f, 0, 1);

        // ===== دکمه انتخاب =====
        TextButton selectBtn = new TextButton("Select", skin, "default");
        selectBtn.setWidth(90);
        selectBtn.setHeight(35);
        selectBtn.getLabel().setFontScale(0.9f);

        Table topRow = new Table();
        topRow.setFillParent(true);
        // ===== نشان روزانه =====
        if (item.isDaily) {
            Label dailyLabel = new Label("⭐ DAILY", skin);
            dailyLabel.setFontScale(0.6f);
            dailyLabel.setColor(1, 0.8f, 0, 1);
            topRow.add(dailyLabel).left();

            if (!dailyOfferExpired) {
                int secondsLeft = (int)Math.ceil(Math.max(0, dailyOfferTimeRemaining));
                Label timeLabel = new Label(secondsLeft + "s left", skin);
                timeLabel.setFontScale(1.0f);
                timeLabel.setColor(1, 0.5f, 0.5f, 1);
                topRow.add(timeLabel).right();
                item.timeLabel = timeLabel;
            } else {
                Label expiredLabel = new Label(" EXPIRED", skin);
                expiredLabel.setFontScale(0.6f);
                expiredLabel.setColor(1, 0.2f, 0.2f, 1);
                topRow.add(expiredLabel).right();
            }
        }

        final ShopItem finalItem = item;
        selectBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (item.isDaily && dailyOfferExpired) {
                    showToast(" Daily offer expired!", 2f, true);
                    return;
                }
                selectItem(finalItem);
            }
        });

        // ===== چینش المان‌ها =====
        card.add(topRow).colspan(3).fillX().padBottom(5).row();
        card.add(itemImage).size(55, 55).padRight(15);

        Table infoTable = new Table();
        infoTable.add(nameLabel).left().row();
        infoTable.add(descLabel).left().row();
        card.add(infoTable).padRight(15).expandX().left();

        Table rightTable = new Table();
        rightTable.add(priceLabel).padRight(15);
        if (item.isDaily && dailyOfferExpired) {
            selectBtn.setDisabled(true);
            selectBtn.setColor(0.5f, 0.5f, 0.5f, 1);
        }
        rightTable.add(selectBtn);
        card.add(rightTable);

        return card;
    }

    private void selectItem(ShopItem item) {
        selectedItem = item;
        statusLabel.setText("Selected: " + item.name + " - Price: " + item.price + " " + item.currency);
        statusLabel.setColor(0.3f, 1, 0.3f, 1);
    }

    private void handleBuy() {
        if (selectedItem == null) {
            showToast("❌ Please select an item first!", 2f, true);
            return;
        }
        if (selectedItem.isDaily && dailyOfferExpired) {
            showToast("❌ Daily offer expired! Can't buy.", 2f, true);
            return;
        }

        PlayerProgress progress = user.getProgress();

        // ===== بررسی موجودی =====
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

        // ===== اجرای خرید =====
        if (item.name.startsWith("DAILY OFFER:")) {
            String plantName = item.name.replace("DAILY OFFER: ", "");
            progress.addPlantSeed(plantName, 1);
            int totalSeeds = progress.getPlantSeedCount(plantName);
            showToast("✅ " + plantName + " seed added! (Daily Offer) (Total: " + totalSeeds + ")", 2f, false);
            dailyOfferExpired = true;
        } else {
            switch (item.name) {
                case "POT":
                    boolean potUnlocked = false;
                    for (GardenPot pot : user.getProgress().getGarden().getPots()) {
                        if (pot.isLocked()) {
                            pot.setLocked(false);
                            potUnlocked = true;
                            showToast("✅ Pot unlocked successfully!", 2f, false);
                            break;
                        }
                    }
                    if (!potUnlocked) {
                        showToast("❌ All pots are already unlocked!", 2f, true);
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
                    progress.addPlantSeed(randomPlant, 1);
                    int totalSeeds = progress.getPlantSeedCount(randomPlant);
                    showToast("✅ Random seed: " + randomPlant + " added! (Total: " + totalSeeds + ")", 2f, false);
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
            "CHERRY_BOMB", "SNOW_PEA", "REPEATER", "CACTUS", "BONK_CHOY",
            "FIRE_PEASHOOTER", "STARFRUIT", "MELON_PULT"};

        int cols = 3;
        int colCount = 0;
        Table rowTable = new Table();
        for (String plant : plants) {
            if (colCount >= cols) {
                plantTable.add(rowTable).padBottom(5).row();
                rowTable = new Table();
                colCount = 0;
            }

            TextButton plantBtn = new TextButton(plant, skin, "default");
            plantBtn.getLabel().setFontScale(0.8f);
            plantBtn.setWidth(120);
            plantBtn.setHeight(35);

            final String selectedPlant = plant;
            plantBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    plantDialog.hide();
                    plantDialog.remove();

                    user.getProgress().addPlantSeed(selectedPlant, 1);
                    int totalSeeds = user.getProgress().getPlantSeedCount(selectedPlant);
                    showToast("✅ " + selectedPlant + " seed added! (Total: " + totalSeeds + ")", 2f, false);
                    game.getStorageService().saveUsers();
                    updateCurrencyDisplay();
                    updateInfoDisplay();
                    updateItemsDisplay();
                }
            });
            rowTable.add(plantBtn).width(120).height(35).pad(4);
            colCount++;
        }

        if (colCount > 0) {
            plantTable.add(rowTable).padBottom(5).row();
        }

        plantDialog.getContentTable().add(plantTable);

        TextButton cancelBtn = new TextButton("Cancel", skin, "default");
        cancelBtn.getLabel().setFontScale(0.9f);
        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                plantDialog.hide();
                plantDialog.remove();
                user.getProgress().addDiamonds(5);
                showToast("❌ Purchase cancelled. Diamonds returned.", 2f, true);
                updateCurrencyDisplay();
                updateInfoDisplay();
            }
        });
        plantDialog.getContentTable().add(cancelBtn).padTop(10);

        plantDialog.show(stage);
    }
    private void updateDailyOfferTimer(float delta) {
        if (!dailyOfferExpired && dailyOfferTimeRemaining > 0) {
            dailyOfferTimeRemaining -= delta;

            for (ShopItem item : shopItems) {
                if (item.isDaily && item.timeLabel != null) {
                    int secondsLeft = (int)Math.ceil(Math.max(0, dailyOfferTimeRemaining));
                    item.timeLabel.setText(secondsLeft + "s left");
                }
            }

            if (dailyOfferTimeRemaining <= 0) {
                dailyOfferExpired = true;
                dailyOfferTimeRemaining = 0;
                updateItemsDisplay();
                showToast(" Daily offer expired!", 2f, true);
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        createDailyOffer();
                        updateItemsDisplay();
                        showToast(" New daily offer available!", 2f, false);
                    }
                }, 3f);
            }
        }
    }

    private static class ShopItem {
        String name;
        String description;
        int price;
        String currency;
        String imagePath;
        boolean isDaily;
        int timeRemaining;
        Label timeLabel;

        ShopItem(String name, String description, int price, String currency,
                 String imagePath, boolean isDaily, int timeRemaining) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.currency = currency;
            this.imagePath = imagePath;
            this.isDaily = isDaily;
            this.timeRemaining = timeRemaining;
            this.timeLabel = null;
        }
    }

    @Override
    public void render(float delta) {
        updateDailyOfferTimer(delta);
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
