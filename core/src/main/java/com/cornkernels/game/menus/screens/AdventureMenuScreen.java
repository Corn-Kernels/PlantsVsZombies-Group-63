package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.PlayerProgress;

import java.util.List;

public class AdventureMenuScreen extends BaseScreen {

    private User user;
    private Image backgroundImage;
    private Table mainTable;
    private Table chaptersTable;
    private ScrollPane scrollPane;
    private boolean showingLevels = false;
    private String currentChapter = "";

    public AdventureMenuScreen(Main game, User user) {
        super(game);
        this.user = user;

        loadBackground();
        // ❌ حذف: setupCurrencyDisplay(); ← خود BaseScreen این رو توی سازنده صدا میزنه
        buildUI();
    }

    private void loadBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/adventure_background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("Adventure background not found! Using default color.");
        }
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

            final String finalChapter = chapter;
            final boolean finalIsUnlocked = isUnlocked;
            chapterBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (finalIsUnlocked) {
                        showLevelsForChapter(finalChapter);
                    }
                }
            });
        }
    }

    private void showLevelsForChapter(String chapterName) {
        chaptersTable.clear();
        showingLevels = true;
        currentChapter = chapterName;

        Label titleLabel = new Label(" " + chapterName + " - Levels", skin);
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

            final boolean isUnlockedFinal = isUnlocked[i];
            levelBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isUnlockedFinal) {
                        System.out.println("▶ Starting " + levelName);
                        game.setScreen(new PlantSelectionScreen(game, user, chapterName));
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
    public void dispose() {
        super.dispose();
    }
}
