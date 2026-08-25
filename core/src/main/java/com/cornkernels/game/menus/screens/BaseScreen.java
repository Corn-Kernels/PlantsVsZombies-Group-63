package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cornkernels.GameManager;
import com.cornkernels.game.menus.model.User;

public abstract class BaseScreen implements Screen {
    protected GameManager game;
    protected Stage stage;
    protected Skin skin;
    protected Image backgroundImage;
    protected Table currencyTable;
    protected Label coinsLabel;
    protected Label diamondsLabel;

    public BaseScreen(GameManager game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        createSimpleSkin();

        loadBackground();
        setupCurrencyDisplay();
    }

    private void createSimpleSkin() {
        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // ===== ساخت Drawable سفید برای white_pixel =====
        Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fill();
        Texture whiteTexture = new Texture(whitePixmap);
        whitePixmap.dispose();
        Drawable whiteDrawable = new TextureRegionDrawable(whiteTexture);

        // ===== اضافه کردن "white_pixel" =====
        skin.add("white_pixel", whiteDrawable);

        // ===== ساخت Drawable خاکستری =====
        Pixmap grayPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        grayPixmap.setColor(Color.LIGHT_GRAY);
        grayPixmap.fill();
        Texture grayTexture = new Texture(grayPixmap);
        grayPixmap.dispose();
        Drawable grayDrawable = new TextureRegionDrawable(grayTexture);

        // ===== LabelStyle =====
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // ===== TextButtonStyle =====
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = grayDrawable;
        buttonStyle.down = whiteDrawable;
        skin.add("default", buttonStyle);
        skin.add("green", buttonStyle);
        skin.add("brown", buttonStyle);

        // ===== TextFieldStyle =====
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = whiteDrawable;
        textFieldStyle.cursor = whiteDrawable;
        textFieldStyle.selection = grayDrawable;
        skin.add("default", textFieldStyle);

        // ===== CheckBoxStyle =====
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkboxOn = whiteDrawable;
        checkBoxStyle.checkboxOff = grayDrawable;
        skin.add("default", checkBoxStyle);

        // ============================================
        // ===== SelectBoxStyle =====
        // ============================================
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = font;
        selectBoxStyle.fontColor = Color.BLACK;
        selectBoxStyle.background = whiteDrawable;

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.selection = grayDrawable;

        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("default", selectBoxStyle);

        // ============================================
        // ===== ScrollPaneStyle =====
        // ============================================
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = whiteDrawable;
        skin.add("default", scrollPaneStyle);

        // ============================================
        // ===== SliderStyle =====
        // ============================================
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = grayDrawable;
        sliderStyle.knob = whiteDrawable;
        sliderStyle.knobBefore = whiteDrawable;
        skin.add("default-horizontal", sliderStyle);

        // ============================================
        // ===== ProgressBarStyle =====
        // ============================================
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = grayDrawable;
        progressBarStyle.knob = whiteDrawable;
        progressBarStyle.knobBefore = whiteDrawable;
        skin.add("default-horizontal", progressBarStyle);

        // ============================================
        // ===== WindowStyle =====
        // ============================================
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = grayDrawable;
        skin.add("default", windowStyle);
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

    private Drawable createColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(texture);
    }

    private void setupCurrencyDisplay() {
        currencyTable = new Table();
        currencyTable.top().right();
        currencyTable.setFillParent(true);
        updateCurrencyDisplay();
        stage.addActor(currencyTable);
    }

    protected void updateCurrencyDisplay() {
        User currentUser = game.getCurrentUser();
        int coins = (currentUser != null) ? currentUser.getProgress().getCoins() : 0;
        int diamonds = (currentUser != null) ? currentUser.getProgress().getDiamonds() : 0;

        currencyTable.clear();

        Drawable bgDrawable = createColorDrawable(new Color(0, 0, 0, 0.5f));

        coinsLabel = new Label("🪙 " + coins, skin);
        diamondsLabel = new Label("💎 " + diamonds, skin);
        coinsLabel.setFontScale(1.2f);
        diamondsLabel.setFontScale(1.2f);

        Label.LabelStyle style = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        style.background = bgDrawable;
        coinsLabel.setStyle(style);
        diamondsLabel.setStyle(style);

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

        Drawable toastBg = createColorDrawable(new Color(0, 0, 0, 0.7f));

        Label.LabelStyle style = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        style.background = toastBg;
        toast.setStyle(style);

        toast.setPosition(
            stage.getWidth() / 2f - toast.getWidth() / 2f,
            stage.getHeight() / 2f + 100
        );

        stage.addActor(toast);

        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                toast.remove();
            }
        }, duration);
    }

    @Override
    public void render(float delta) {}

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
