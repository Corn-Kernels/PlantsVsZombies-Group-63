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
import com.cornkernels.game.menus.model.NewsItem;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.User;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class NewsScreen extends BaseScreen {

    private User user;
    private List<NewsItem> allNews;
    private Label detailLabel;
    private Label countLabel;
    private Table newsTable;
    private ScrollPane scrollPane;

    public NewsScreen(GameManager game, @NonNull User user) {
        super(game);
        this.user = user;

        PlayerProgress progress = user.getProgress();
        allNews = progress.getNewsList();

        if (allNews.isEmpty()) {
            initializeDefaultNews(progress);
        }

        buildUI();
        updateNewsList();
    }

    private void initializeDefaultNews(@NonNull PlayerProgress progress) {
        progress.addNews(new NewsItem("1", "Welcome to the game!", "2024-01-01",
            "Welcome to Plants vs. Zombies! Start your adventure by playing Chapter 1.", "GENERAL"));
        progress.addNews(new NewsItem("2", "New zombie: Buckethead!", "2024-01-05",
            "The Buckethead zombie has high armor. Use explosive plants to defeat it.", "ZOMBIE"));
        progress.addNews(new NewsItem("3", "New plant: Snow Pea!", "2024-01-10",
            "Snow Pea slows down zombies with its icy peas.", "PLANT"));
        progress.addNews(new NewsItem("4", "Chapter 2 Unlocked!", "2024-01-12",
            "You have unlocked Chapter 2: Ice Caves. New zombies and plants await!", "LEVEL"));
        progress.addNews(new NewsItem("5", "New Minigame: Vasebreaker!", "2024-01-15",
            "Try the new Vasebreaker minigame. Break vases to find plants and defeat zombies.", "MINIGAME"));

        game.getStorageService().saveUsers();
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" NEWS", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        countLabel = new Label("", skin);
        mainTable.add(countLabel).padBottom(20).row();

        newsTable = new Table();
        scrollPane = new ScrollPane(newsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(300);
        mainTable.add(scrollPane).width(500).padBottom(20).row();

        detailLabel = new Label("", skin);
        detailLabel.setWrap(true);
        detailLabel.setWidth(450);
        detailLabel.setColor(1, 1, 0.8f, 1);
        mainTable.add(detailLabel).width(450).padBottom(10).row();

        TextButton unreadBtn = new TextButton(" Show Unread", skin, "default");
        TextButton allBtn = new TextButton(" Show All", skin, "default");
        TextButton backBtn = new TextButton(" Back", skin, "default");

        Table buttonTable = new Table();
        buttonTable.add(unreadBtn).width(130).height(40).padRight(10);
        buttonTable.add(allBtn).width(130).height(40).padRight(10);
        buttonTable.add(backBtn).width(100).height(40);
        mainTable.add(buttonTable).row();

        unreadBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showUnreadNews();
            }
        });
        allBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAllNews();
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getStorageService().saveUsers();
                game.setScreen(new MainMenuScreen(game, user));
            }
        });
    }

    private void updateNewsList() {
        newsTable.clear();
        PlayerProgress progress = user.getProgress();
        int unreadCount = progress.getUnreadNewsCount();
        countLabel.setText(" Unread: " + unreadCount);

        for (NewsItem news : allNews) {
            boolean isRead = news.isRead();
            String status = isRead ? "✅ " : "🔴 ";
            String typeIcon = getTypeIcon(news.getType());

            TextButton newsBtn = new TextButton(
                status + typeIcon + " " + news.getTitle() + " (" + news.getDate() + ")",
                skin, "default"
            );
            newsBtn.setWidth(400);
            newsBtn.setHeight(35);
            newsTable.add(newsBtn).left().padBottom(5).row();

            newsBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!news.isRead()) {
                        news.setRead(true);
                        game.getStorageService().saveUsers();
                        updateNewsList();
                    }
                    showNewsDetail(news);
                }
            });
        }
    }

    private void showNewsDetail(NewsItem news) {
        String typeIcon = getTypeIcon(news.getType());
        detailLabel.setText(
            typeIcon + " " + news.getTitle() + "\n" +
                "📅 " + news.getDate() + "\n" +
                "📂 " + news.getType() + "\n\n" +
                news.getBody()
        );
    }

    private void showUnreadNews() {
        PlayerProgress progress = user.getProgress();
        List<NewsItem> unread = progress.getUnreadNews();

        if (unread.isEmpty()) {
            detailLabel.setText(" No unread news!");
            return;
        }

        StringBuilder sb = new StringBuilder(" 🔴 Unread News:\n\n");
        for (NewsItem news : unread) {
            sb.append("🔴 ").append(news.getTitle()).append("\n");
        }
        detailLabel.setText(sb.toString());
    }

    private void showAllNews() {
        if (allNews.isEmpty()) {
            detailLabel.setText(" No news!");
            return;
        }

        StringBuilder sb = new StringBuilder(" All News:\n\n");
        for (NewsItem news : allNews) {
            String status = news.isRead() ? "✅" : "🔴";
            String icon = getTypeIcon(news.getType());
            sb.append(status).append(" ").append(icon).append(" ").append(news.getTitle()).append("\n");
        }
        detailLabel.setText(sb.toString());
    }

    private String getTypeIcon(String type) {
        switch (type) {
            case "ZOMBIE":
                return "🧟";
            case "PLANT":
                return "🌱";
            case "LEVEL":
                return "📖";
            case "MINIGAME":
                return "🎮";
            default:
                return "📌";
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
