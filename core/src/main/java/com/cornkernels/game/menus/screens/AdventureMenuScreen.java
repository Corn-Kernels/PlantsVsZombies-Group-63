package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cornkernels.GameManager;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.User;
import com.cornkernels.game.screens.GameplayScreen;
import com.cornkernels.game.systems.controller.plants.SeedSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventureMenuScreen extends BaseScreen {

    // Placeholder roster until levels carry their own zombie eligibility data.
    private static final List<ZombieDef> DEFAULT_ELIGIBLE_ZOMBIES = List.of(
        ZombieDef.DEFAULT, ZombieDef.ARCADE, ZombieDef.PIANO, ZombieDef.PROSPECTOR, ZombieDef.GARGANTUAR);

    private User user;
    private Image backgroundImage;
    private Table mainTable;
    private Table chaptersTable;
    private ScrollPane scrollPane;
    private boolean showingLevels = false;
    private String currentChapter = "";

    public AdventureMenuScreen(GameManager game, User user) {
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
        // Sequential unlock: you can play up to one level past however many you've completed.
        // PlayerProgress only tracks a single global completedLevels counter (not per-chapter),
        // so this is the best available signal until real per-chapter tracking exists.
        int completedInChapter = getCompletedLevelsForChapter(chapterName);

        for (int i = 0; i < levels.length; i++) {
            final String levelName = levels[i];
            boolean isUnlocked = i <= completedInChapter;
            String status = isUnlocked ? "▶️ " : "🔒 ";
            TextButton levelBtn = new TextButton(status + levelName, skin);

            if (!isUnlocked) {
                levelBtn.setDisabled(true);
            }

            chaptersTable.add(levelBtn).width(350).height(40).padBottom(5).row();

            final boolean isUnlockedFinal = isUnlocked;
            levelBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isUnlockedFinal) {
                        startLevel(levelName);
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
        return Math.min(user.getProgress().getCompletedLevels(), 4);
    }

    // Plant selection happens inside GameplayScreen's own seed-chooser, not a separate menu
    // screen — GameAttributes is seeded with every plant the player owns, and the in-game UI
    // is what narrows that down to the loadout actually brought into the level.
    private void startLevel(String levelName) {
        System.out.println("▶ Starting " + levelName);
        game.setScreen(new GameplayScreen(game, buildGameAttributes()));
    }

    private GameAttributes buildGameAttributes() {
        Map<String, PlantDef> plantsByNormalizedName = new HashMap<>();
        for (PlantDef def : PlantDef.values()) {
            plantsByNormalizedName.putIfAbsent(normalizePlantKey(def.getPlantName()), def);
        }

        List<SeedSlot> seedSlots = new ArrayList<>();
        for (String ownedPlant : user.getProgress().getOwnedPlants()) {
            PlantDef def = plantsByNormalizedName.get(normalizePlantKey(ownedPlant));
            if (def != null) {
                seedSlots.add(new SeedSlot(def));
            }
        }

        return new GameAttributes(seedSlots, DEFAULT_ELIGIBLE_ZOMBIES);
    }

    // Plant names are written inconsistently across the app ("Wall-nut", "WALL_NUT", ...);
    // matches PlayerProgress's own normalization so ownership lines up with PlantDef regardless
    // of which spelling a given screen used to grant the plant.
    private static String normalizePlantKey(String name) {
        return name == null ? "" : name.toUpperCase().replaceAll("[^A-Z0-9]", "");
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
