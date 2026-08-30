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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventureMenuScreen extends BaseScreen {

    private User user;
    private Image backgroundImage;
    private Table mainTable;
    private Table chaptersTable;
    private ScrollPane scrollPane;
    private boolean showingLevels = false;
    private String currentChapter = "";
    private Map<String, Integer> completedLevelsPerChapter;
    public AdventureMenuScreen(Main game, User user) {
        super(game);
        this.user = user;
        completedLevelsPerChapter = new HashMap<>();

        completedLevelsPerChapter.put("Chapter 1", 0);
        completedLevelsPerChapter.put("Chapter 2", 0);
        completedLevelsPerChapter.put("Chapter 3", 0);
        completedLevelsPerChapter.put("Chapter 4", 0);
        loadBackground();

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
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(10).row();

        chaptersTable = new Table();
        scrollPane = new ScrollPane(chaptersTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(350);

        buildChapterList(progress);

        mainTable.add(scrollPane).width(450).height(350).padBottom(20).row();

        TextButton backBtn = new TextButton(" Back", skin, "default");
        backBtn.getLabel().setFontScale(1.2f);
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

            String statusText = isUnlocked ? " (Unlocked)" : " (Locked)";
            String chapterText = chapter + statusText + " [" + completedLevels + "/" + totalLevels + "]";

            TextButton chapterBtn = new TextButton(chapterText, skin);
            chapterBtn.getLabel().setFontScale(1.1f);

            if (!isUnlocked) {
                chapterBtn.setDisabled(true);
                chapterBtn.setColor(0.5f, 0.5f, 0.5f, 1);// خاکستری برای لاک
            }else {
                chapterBtn.setColor(1, 1, 1, 1);// سفید برای آنلاک
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
        titleLabel.setFontScale(1.3f);
        chaptersTable.add(titleLabel).padBottom(10).row();

        String[] levels = {"Level 1", "Level 2", "Level 3", "Level 4"};
        int completedCount = getCompletedLevelsForChapter(chapterName);

        for (int i = 0; i < levels.length; i++) {
            final int index = i;
            final String levelName = levels[i];

            boolean isUnlocked = (i <= completedCount);
            String statusText = isUnlocked ? " (Unlocked)" : " (Locked)";
            String displayText = "▶ " + levelName + statusText;

            TextButton levelBtn = new TextButton(displayText, skin);
            levelBtn.getLabel().setFontScale(1f);
            if (!isUnlocked) {
                levelBtn.setDisabled(true);
                levelBtn.setColor(0.5f, 0.5f, 0.5f, 1);
            } else {
                levelBtn.setColor(1, 1, 1, 1);
            }

            chaptersTable.add(levelBtn).width(350).height(40).padBottom(5).row();

            final boolean isUnlockedFinal = isUnlocked;
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
        backBtn.getLabel().setFontScale(1.1f);
        chaptersTable.add(backBtn).width(350).height(40).padTop(10).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayerProgress progress = user.getProgress();
                updateCompletedLevels(progress);
                buildChapterList(progress);
            }
        });
        scrollPane.setScrollY(0);
    }
    private void updateCompletedLevels(PlayerProgress progress) {
        // در اینجا باید از داده‌های واقعی استفاده کنی
        int totalCompleted = progress.getCompletedLevels();
        int levelsPerChapter = 4;
        for (String chapter : completedLevelsPerChapter.keySet()) {
            if (totalCompleted >= levelsPerChapter) {
                completedLevelsPerChapter.put(chapter, levelsPerChapter);
                totalCompleted -= levelsPerChapter;
            } else {
                completedLevelsPerChapter.put(chapter, totalCompleted);
                break;
            }
        }
    }

    private int getCompletedLevelsForChapter(String chapter) {
        PlayerProgress progress = user.getProgress();
        int totalCompleted = progress.getCompletedLevels();

        if (totalCompleted == 0) return 0;

        int levelsPerChapter = 4;
        int chapterIndex = getChapterIndex(chapter);
        int completedBefore = chapterIndex * levelsPerChapter;

        if (totalCompleted <= completedBefore) return 0;
        return Math.min(totalCompleted - completedBefore, levelsPerChapter);
    }
    private int getChapterIndex(String chapter) {
        switch (chapter) {
            case "Chapter 1": return 0;
            case "Chapter 2": return 1;
            case "Chapter 3": return 2;
            case "Chapter 4": return 3;
            default: return 0;
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
