package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cornkernels.GameManager;
import com.cornkernels.game.menus.model.LeaderboardEntry;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.User;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class LeaderboardScreen extends BaseScreen {

    private GameManager game;
    private User currentUser;
    private Table leaderboardTable;
    private ScrollPane scrollPane;
    private List<LeaderboardEntry> entries;
    private String sortBy = "highScore";
    private boolean ascending = false;

    public LeaderboardScreen(GameManager game, User user) {
        super(game);
        this.game = game;
        this.currentUser = user;
        buildUI();
        loadLeaderboardData();
        displayLeaderboard();
    }

    private void loadLeaderboardData() {
        entries = new ArrayList<>();
        Map<String, User> allUsers = game.getStorageService().getAllUsers();

        for (User user : allUsers.values()) {
            PlayerProgress progress = user.getProgress();

            // آخرین مرحله
            String lastLevel = getLastLevel(progress);

            LeaderboardEntry entry = new LeaderboardEntry(
                user.getUsername(),
                lastLevel,
                progress.getMinigamesCompleted(),
                progress.getDailyQuestsCompleted(),
                progress.getNonDailyQuestsCompleted(),
                progress.getHighScore()
            );
            entries.add(entry);
        }
    }

    private @NonNull String getLastLevel(@NonNull PlayerProgress progress) {
        List<String> unlocked = progress.getUnlockedChapters();
        if (unlocked.isEmpty()) return "None";
        String lastChapter = unlocked.get(unlocked.size() - 1);
        int levels = progress.getCompletedLevels();
        return lastChapter + " - Level " + (levels % 4 + 1);
    }

    private void displayLeaderboard() {
        leaderboardTable.clear();

        Label headerLabel = new Label(" LEADERBOARD", skin);
        headerLabel.setFontScale(1.5f);
        leaderboardTable.add(headerLabel).colspan(6).padBottom(15).row();

        String[] columns = {"Username", "Last Level", "MiniGames", "Daily Quests", "Quests", "High Score"};
        String[] sortKeys = {"username", "lastLevel", "minigamesCompleted", "dailyQuestsCompleted", "nonDailyQuestsCompleted", "highScore"};

        for (int i = 0; i < columns.length; i++) {
            final String key = sortKeys[i];
            String display = columns[i];
            if (sortBy.equals(key)) {
                display += ascending ? " ▲" : " ▼";
            }
            TextButton headerBtn = new TextButton(display, skin, "default");
            leaderboardTable.add(headerBtn).width(120).height(40).padBottom(5);

            headerBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (sortBy.equals(key)) {
                        ascending = !ascending;
                    } else {
                        sortBy = key;
                        ascending = false;
                    }
                    sortAndDisplay();
                }
            });
        }
        leaderboardTable.row();

        Label line = new Label("------------------------------------------------------------", skin);
        leaderboardTable.add(line).colspan(6).padBottom(5).row();

        for (LeaderboardEntry entry : entries) {
            boolean isCurrentUser = entry.getUsername().equals(currentUser.getUsername());
            String color = isCurrentUser ? "⭐ " : "";

            leaderboardTable.add(new Label(color + entry.getUsername(), skin)).width(120).padBottom(3);
            leaderboardTable.add(new Label(entry.getLastLevel(), skin)).width(120).padBottom(3);
            leaderboardTable.add(new Label(String.valueOf(entry.getMinigamesCompleted()), skin)).width(120).padBottom(3);
            leaderboardTable.add(new Label(String.valueOf(entry.getDailyQuestsCompleted()), skin)).width(120).padBottom(3);
            leaderboardTable.add(new Label(String.valueOf(entry.getNonDailyQuestsCompleted()), skin)).width(120).padBottom(3);
            leaderboardTable.add(new Label(String.valueOf(entry.getHighScore()), skin)).width(120).padBottom(3);
            leaderboardTable.row();
        }

        TextButton backBtn = new TextButton(" Back", skin, "default");
        leaderboardTable.add(backBtn).colspan(6).width(150).height(50).padTop(20).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, currentUser));
            }
        });
    }

    private void sortAndDisplay() {
        Comparator<LeaderboardEntry> comparator = switch (sortBy) {
            case "username" -> Comparator.comparing(LeaderboardEntry::getUsername);
            case "lastLevel" -> Comparator.comparing(LeaderboardEntry::getLastLevel);
            case "minigamesCompleted" -> Comparator.comparingInt(LeaderboardEntry::getMinigamesCompleted);
            case "dailyQuestsCompleted" -> Comparator.comparingInt(LeaderboardEntry::getDailyQuestsCompleted);
            case "nonDailyQuestsCompleted" -> Comparator.comparingInt(LeaderboardEntry::getNonDailyQuestsCompleted);
            default -> Comparator.comparingInt(LeaderboardEntry::getHighScore);
        };

        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(entries, comparator);
        displayLeaderboard();
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        leaderboardTable = new Table();
        scrollPane = new ScrollPane(leaderboardTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(450);

        mainTable.add(scrollPane).width(800).height(500).padBottom(20).row();
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
