package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
//import com.raeleus.tenpatch.TenPatch;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.PlayerProgress;

import java.util.List;

public class AdventureMenuScreen implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Table mainTable;
    private Table chaptersTable;
    private ScrollPane scrollPane;
    private boolean showingLevels = false;
    private String currentChapter = "";

    private Label coinsLabel;
    private Label diamondsLabel;
    private Table currencyTable;

    public AdventureMenuScreen(Main game, User user) {
        this.game = game;
        this.user = user;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //TenPatch.setDefaultDrawable("tenpatch");
        //skin = new Skin(Gdx.files.internal("skin/pvz2_skin.json"));
        skin = new Skin();
        BitmapFont font = new BitmapFont();  // ← این دیگه قرمز نیست
        skin.add("default-font", font);



        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // ===== TextButtonStyle =====
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        skin.add("default", buttonStyle);
        skin.add("green", buttonStyle);


        loadBackground();
        setupCurrencyDisplay();
        buildUI();
    }
    private void loadBackground() {
        try {
            backgroundTexture = new Texture(Gdx.files.internal("IMAGES/adventure_background.jpg"));
            backgroundImage = new Image(backgroundTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println(" Adventure background not found! Using default color.");
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

        coinsLabel = new Label("🪙 " + coins, skin);
        diamondsLabel = new Label("💎 " + diamonds, skin);
        coinsLabel.setFontScale(1.2f);
        diamondsLabel.setFontScale(1.2f);

        currencyTable.add(coinsLabel).padTop(10).padRight(10);
        currencyTable.add(diamondsLabel).padTop(10).padRight(20);
        currencyTable.row();
    }

    private void buildUI() {
        PlayerProgress progress = user.getProgress();
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" ADVENTURE", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        chaptersTable = new Table();
        scrollPane = new ScrollPane(chaptersTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(350);

        buildChapterList(progress);

        mainTable.add(scrollPane).width(450).height(350).padBottom(20).row();

        TextButton backBtn = new TextButton(" Back", skin, "default");
        mainTable.add(backBtn).width(150).height(50).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameMenuScreen(game, user));
            }
        });
    }
    private void buildChapterList(PlayerProgress progress) {
        chaptersTable.clear();
        showingLevels = false;

        List<String> unlockedChapters = progress.getUnlockedChapters();
        String[] allChapters = {"Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4"};
        int totalLevels = 4;
        for (String chapter : allChapters) {
            boolean isUnlocked = unlockedChapters.contains(chapter);
            int completedLevels = getCompletedLevelsForChapter(chapter);

            String statusText = isUnlocked ? "✅" : "🔒";
            String chapterText = statusText + " " + chapter + " (" + completedLevels + "/" + totalLevels + ")";

            TextButton chapterBtn = new TextButton(chapterText, skin);

            if (!isUnlocked) {
                chapterBtn.setDisabled(true);
            }
            chaptersTable.add(chapterBtn).width(400).height(45).padBottom(5).row();

            chapterBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isUnlocked) {
                        showLevelsForChapter(chapter);
                    }
                }
            });
        }
    }
    private void showLevelsForChapter(String chapterName) {
        chaptersTable.clear();
        showingLevels = true;
        currentChapter = chapterName;

        Label titleLabel = new Label( chapterName + " - Levels", skin);
        chaptersTable.add(titleLabel).padBottom(10).row();

        String[] levels = {"Level 1", "Level 2", "Level 3", "Level 4"};
        boolean[] isUnlocked = {true, true, false, false};

        for (int i = 0; i < levels.length; i++) {
            final int index = i;
            final String levelName = levels[i];
            String status = isUnlocked[i] ? "▶️ " : "🔒 ";
            TextButton levelBtn = new TextButton(status + levelName, skin);

            if (!isUnlocked[i]) {
                levelBtn.setDisabled(true);
            }

            chaptersTable.add(levelBtn).width(350).height(40).padBottom(5).row();
            levelBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isUnlocked[index]) {
                        System.out.println("▶ Starting " + levelName);
                    }
                }
            });
        }
        TextButton backBtn = new TextButton(" Back to Chapters", skin, "default");
        chaptersTable.add(backBtn).width(350).height(40).padTop(10).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayerProgress progress = user.getProgress();
                buildChapterList(progress);
            }
        });
        scrollPane.setScrollY(0);
    }

    private int getCompletedLevelsForChapter(String chapter) {
        return 2;
    }

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
    public void dispose() {
        stage.dispose();
        skin.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
