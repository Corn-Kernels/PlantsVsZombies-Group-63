package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raeleus.tenpatch.TenPatch;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;

public abstract class BaseScreen implements Screen {
    protected Main game;
    protected Stage stage;
    protected Skin skin;
    protected Image backgroundImage;
    protected Table currencyTable;
    protected Label coinsLabel;
    protected Label diamondsLabel;

    public BaseScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TenPatch.setDefaultDrawable("tenpatch");
        skin = new Skin(Gdx.files.internal("skin/pvz2_skin.json"));
        loadBackground();
        setupCurrencyDisplay();
    }
    private void loadBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println(" Background not found! Using default color.");
        }
    }
    // ===== سکه و الماس (همیشه در همه منوها) =====
    private void setupCurrencyDisplay() {
        currencyTable = new Table();
        currencyTable.top().right();  // گوشه بالا سمت راست
        currencyTable.setFillParent(true);

        updateCurrencyDisplay();
        stage.addActor(currencyTable);
    }
    protected void updateCurrencyDisplay() {
        User currentUser = game.getCurrentUser();
        int coins = (currentUser != null) ? currentUser.getProgress().getCoins() : 0;
        int diamonds = (currentUser != null) ? currentUser.getProgress().getDiamonds() : 0;

        currencyTable.clear();

        coinsLabel = new Label("🪙 " + coins, skin);
        diamondsLabel = new Label("💎 " + diamonds, skin);
        coinsLabel.setFontScale(1.2f);
        diamondsLabel.setFontScale(1.2f);

        coinsLabel.setBackground(skin.getDrawable("white_pixel"));
        diamondsLabel.setBackground(skin.getDrawable("white_pixel"));
        coinsLabel.getBackground().setMinWidth(80);
        coinsLabel.getBackground().setMinHeight(30);
        diamondsLabel.getBackground().setMinWidth(80);
        diamondsLabel.getBackground().setMinHeight(30);
        coinsLabel.setColor(0, 0, 0, 0.5f);
        diamondsLabel.setColor(0, 0, 0, 0.5f);

        currencyTable.add(coinsLabel).padTop(10).padRight(10);
        currencyTable.add(diamondsLabel).padTop(10).padRight(20);
        currencyTable.row();
    }
    protected void showToast(String message, float duration, boolean isError) {
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
        toast.setBackground(skin.getDrawable("white_pixel"));
        toast.getBackground().setMinWidth(400);
        toast.getBackground().setMinHeight(50);
        toast.setColor(0, 0, 0, 0.7f);

        stage.addActor(toast);

        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                toast.remove();
            }
        }, duration);
    }
    @Override
    public void render(float delta) {
        // هر Screen خودش render رو پیاده‌سازی میکنه
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
