package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

        loadSkin();
        loadBackground();
        setupCurrencyDisplay();
    }

    private void loadSkin() {
        try {
            skin = new Skin(Gdx.files.internal("skin/pvz2_skin_backup.json"));
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/pvz2_skin.atlas"));
            skin.addRegions(atlas);
            BitmapFont font = new BitmapFont();
            skin.add("default-font", font);
            System.out.println(" Skin loaded from backup.json successfully!");
            return;
        } catch (Exception e) {
            System.out.println(" backup.json failed: " + e.getMessage());
        }


        try {
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/pvz2_skin.atlas"));
            skin = new Skin(atlas);
            BitmapFont font = new BitmapFont();
            skin.add("default-font", font);

            setupStyles(font, atlas);
            System.out.println(" Skin loaded from atlas successfully!");
            return;
        } catch (Exception e) {
            System.out.println("⚠ Atlas failed: " + e.getMessage());
        }


        System.out.println(" Using simple skin...");
        createSimpleSkin();
    }

    private void setupStyles(BitmapFont font, TextureAtlas atlas) {
        TextButton.TextButtonStyle defaultStyle = new TextButton.TextButtonStyle();
        defaultStyle.font = font;
        defaultStyle.fontColor = Color.WHITE;

        String[] buttonNames = {
            "image_ui_generic_greenbutton",
            "image_ui_generic_bluebutton",
            "image_ui_generic_brownbutton",
            "image_ui_generic_buttons_coin_buy_normal"
        };

        for (String name : buttonNames) {
            if (atlas.findRegion(name) != null) {
                defaultStyle.up = new TextureRegionDrawable(atlas.findRegion(name));
                System.out.println(" Found button: " + name);
                break;
            }
        }

        String[] downNames = {
            "image_ui_generic_greenbutton_down",
            "image_ui_generic_bluebutton_down",
            "image_ui_generic_brownbutton_down",
            "image_ui_generic_buttons_coin_buy_selected"
        };

        for (String name : downNames) {
            if (atlas.findRegion(name) != null) {
                defaultStyle.down = new TextureRegionDrawable(atlas.findRegion(name));
                break;
            }
        }

        skin.add("default", defaultStyle);
        skin.add("green", defaultStyle);
        skin.add("brown", defaultStyle);

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = font;
        selectBoxStyle.fontColor = Color.BLACK;

        if (atlas.findRegion("image_ui_generic_content_well") != null) {
            selectBoxStyle.background = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_content_well"));
        } else {
            Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            whitePixmap.setColor(Color.WHITE);
            whitePixmap.fill();
            Texture whiteTexture = new Texture(whitePixmap);
            whitePixmap.dispose();
            selectBoxStyle.background = new TextureRegionDrawable(whiteTexture);
        }

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.BLACK;

        if (atlas.findRegion("image_ui_generic_bluebutton") != null) {
            listStyle.selection = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_bluebutton"));
        } else {
            Pixmap grayPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            grayPixmap.setColor(Color.LIGHT_GRAY);
            grayPixmap.fill();
            Texture grayTexture = new Texture(grayPixmap);
            grayPixmap.dispose();
            listStyle.selection = new TextureRegionDrawable(grayTexture);
        }

        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("default", selectBoxStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;

        if (atlas.findRegion("image_ui_generic_content_well") != null) {
            textFieldStyle.background = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_content_well"));
        } else {
            Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            whitePixmap.setColor(Color.WHITE);
            whitePixmap.fill();
            Texture whiteTexture = new Texture(whitePixmap);
            whitePixmap.dispose();
            textFieldStyle.background = new TextureRegionDrawable(whiteTexture);
        }

        Pixmap grayPixmap2 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        grayPixmap2.setColor(Color.LIGHT_GRAY);
        grayPixmap2.fill();
        Texture grayTexture2 = new Texture(grayPixmap2);
        grayPixmap2.dispose();
        Drawable grayDrawable2 = new TextureRegionDrawable(grayTexture2);

        textFieldStyle.cursor = grayDrawable2;
        textFieldStyle.selection = grayDrawable2;
        skin.add("default", textFieldStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = Color.WHITE;

        if (atlas.findRegion("checkbox_on") != null) {
            checkBoxStyle.checkboxOn = new TextureRegionDrawable(atlas.findRegion("checkbox_on"));
        }
        if (atlas.findRegion("checkbox_off") != null) {
            checkBoxStyle.checkboxOff = new TextureRegionDrawable(atlas.findRegion("checkbox_off"));
        }
        skin.add("default", checkBoxStyle);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = grayDrawable2;
        skin.add("default", scrollPaneStyle);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();


        if (atlas.findRegion("image_ui_generic_audio_bar") != null) {
            sliderStyle.background = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_audio_bar"));
        } else if (atlas.findRegion("image_ui_hud_ingame_progress_meter") != null) {
            sliderStyle.background = new TextureRegionDrawable(atlas.findRegion("image_ui_hud_ingame_progress_meter"));
        } else {
            Pixmap bgPixmap = new Pixmap(1, 12, Pixmap.Format.RGBA8888);
            bgPixmap.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
            bgPixmap.fill();
            Texture bgTexture = new Texture(bgPixmap);
            bgPixmap.dispose();
            sliderStyle.background = new TextureRegionDrawable(bgTexture);
        }


        if (atlas.findRegion("image_ui_generic_audio_fill") != null) {
            sliderStyle.knobBefore = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_audio_fill"));
        } else if (atlas.findRegion("image_ui_hud_ingame_progress_meter_fill") != null) {
            sliderStyle.knobBefore = new TextureRegionDrawable(atlas.findRegion("image_ui_hud_ingame_progress_meter_fill"));
        } else {
            Pixmap filledPixmap = new Pixmap(1, 12, Pixmap.Format.RGBA8888);
            filledPixmap.setColor(new Color(0.2f, 0.8f, 0.2f, 1));
            filledPixmap.fill();
            Texture filledTexture = new Texture(filledPixmap);
            filledPixmap.dispose();
            sliderStyle.knobBefore = new TextureRegionDrawable(filledTexture);
        }


        if (atlas.findRegion("image_ui_generic_bluebutton") != null) {

            Pixmap knobPixmap = new Pixmap(22, 22, Pixmap.Format.RGBA8888);
            knobPixmap.setColor(Color.WHITE);
            knobPixmap.fillCircle(11, 11, 10);
            knobPixmap.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
            knobPixmap.drawCircle(11, 11, 10);
            Texture knobTexture = new Texture(knobPixmap);
            knobPixmap.dispose();
            sliderStyle.knob = new TextureRegionDrawable(knobTexture);
        } else {
            Pixmap knobPixmap = new Pixmap(22, 22, Pixmap.Format.RGBA8888);
            knobPixmap.setColor(Color.WHITE);
            knobPixmap.fillCircle(11, 11, 10);
            knobPixmap.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
            knobPixmap.drawCircle(11, 11, 10);
            Texture knobTexture = new Texture(knobPixmap);
            knobPixmap.dispose();
            sliderStyle.knob = new TextureRegionDrawable(knobTexture);
        }

        skin.add("default-horizontal", sliderStyle);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();

        if (atlas.findRegion("image_ui_generic_xp_progress_bar") != null) {
            progressBarStyle.background = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_xp_progress_bar"));
        } else {
            progressBarStyle.background = grayDrawable2;
        }

        if (atlas.findRegion("image_ui_generic_xp_progress_bar_fill_green") != null) {
            progressBarStyle.knobBefore = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_xp_progress_bar_fill_green"));
        } else {
            progressBarStyle.knobBefore = grayDrawable2;
        }

        progressBarStyle.knob = grayDrawable2;
        skin.add("default-horizontal", progressBarStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.WHITE;

        if (atlas.findRegion("image_ui_generic_popup_9slice") != null) {
            windowStyle.background = new TextureRegionDrawable(atlas.findRegion("image_ui_generic_popup_9slice"));
        } else {
            windowStyle.background = grayDrawable2;
        }
        skin.add("default", windowStyle);
    }

    private void createSimpleSkin() {
        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fill();
        Texture whiteTexture = new Texture(whitePixmap);
        whitePixmap.dispose();
        Drawable whiteDrawable = new TextureRegionDrawable(whiteTexture);
        skin.add("white_pixel", whiteDrawable);

        Pixmap grayPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        grayPixmap.setColor(Color.LIGHT_GRAY);
        grayPixmap.fill();
        Texture grayTexture = new Texture(grayPixmap);
        grayPixmap.dispose();
        Drawable grayDrawable = new TextureRegionDrawable(grayTexture);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = grayDrawable;
        buttonStyle.down = whiteDrawable;
        skin.add("default", buttonStyle);
        skin.add("green", buttonStyle);
        skin.add("brown", buttonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = whiteDrawable;
        textFieldStyle.cursor = whiteDrawable;
        textFieldStyle.selection = grayDrawable;
        skin.add("default", textFieldStyle);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkboxOn = whiteDrawable;
        checkBoxStyle.checkboxOff = grayDrawable;
        skin.add("default", checkBoxStyle);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = grayDrawable;
        skin.add("default", scrollPaneStyle);

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


        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        Pixmap sliderBgPixmap = new Pixmap(1, 12, Pixmap.Format.RGBA8888);
        sliderBgPixmap.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
        sliderBgPixmap.fill();
        Drawable sliderBgDrawable = new TextureRegionDrawable(new Texture(sliderBgPixmap));
        sliderBgPixmap.dispose();
        sliderStyle.background = sliderBgDrawable;

        Pixmap sliderFilledPixmap = new Pixmap(1, 12, Pixmap.Format.RGBA8888);
        sliderFilledPixmap.setColor(new Color(0.2f, 0.8f, 0.2f, 1));
        sliderFilledPixmap.fill();
        Drawable sliderFilledDrawable = new TextureRegionDrawable(new Texture(sliderFilledPixmap));
        sliderFilledPixmap.dispose();
        sliderStyle.knobBefore = sliderFilledDrawable;

        Pixmap knobPixmap = new Pixmap(22, 22, Pixmap.Format.RGBA8888);
        knobPixmap.setColor(Color.WHITE);
        knobPixmap.fillCircle(11, 11, 10);
        knobPixmap.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
        knobPixmap.drawCircle(11, 11, 10);
        Drawable knobDrawable = new TextureRegionDrawable(new Texture(knobPixmap));
        knobPixmap.dispose();
        sliderStyle.knob = knobDrawable;

        skin.add("default-horizontal", sliderStyle);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = grayDrawable;
        progressBarStyle.knob = whiteDrawable;
        progressBarStyle.knobBefore = whiteDrawable;
        skin.add("default-horizontal", progressBarStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = grayDrawable;
        skin.add("default", windowStyle);

        System.out.println("⚠ Using simple skin (fallback)");
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
        coinsLabel = new Label("Coins: 0", skin);
        diamondsLabel = new Label("Diamonds: 0", skin);
        updateCurrencyDisplay();
        stage.addActor(currencyTable);
    }

    protected void updateCurrencyDisplay() {
        User currentUser = game.getCurrentUser();
        int coins = (currentUser != null) ? currentUser.getProgress().getCoins() : 0;
        int diamonds = (currentUser != null) ? currentUser.getProgress().getDiamonds() : 0;
        currencyTable.clear();

        Drawable bgDrawable = createColorDrawable(new Color(0, 0, 0, 0.5f));

        coinsLabel.setText("Coins: " + coins);
        diamondsLabel.setText("Diamonds: " + diamonds);

        coinsLabel.setFontScale(1.2f);
        diamondsLabel.setFontScale(1.2f);

        coinsLabel.setColor(1, 1, 0.2f, 1);
        diamondsLabel.setColor(0.3f, 0.8f, 1, 1);

        Label.LabelStyle style = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        style.background = bgDrawable;
        coinsLabel.setStyle(style);
        diamondsLabel.setStyle(style);

        currencyTable.add(coinsLabel).padTop(8).padRight(10).padLeft(8);
        currencyTable.add(diamondsLabel).padTop(8).padRight(10);
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
    public void render(float delta) {
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

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
