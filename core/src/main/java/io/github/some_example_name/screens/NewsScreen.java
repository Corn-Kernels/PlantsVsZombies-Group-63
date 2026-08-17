package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsScreen extends BaseScreen {

    private User user;
    private List<NewsItem> allNews;
    private List<NewsItem> unreadNews;
    private Label detailLabel;
    private Label countLabel;

    public NewsScreen(Main game, User user) {
        super(game);
        this.user = user;


        allNews = new ArrayList<>();
        unreadNews = new ArrayList<>();

        allNews.add(new NewsItem(
            " Welcome to the game!",
            "2024-01-01",
            "Welcome to Plants vs. Zombies! Start your adventure by playing Chapter 1.",
            "GENERAL"
        ));
        allNews.add(new NewsItem(
            " New zombie: Buckethead!",
            "2024-01-05",
            "The Buckethead zombie has high armor. Use explosive plants to defeat it.",
            "ZOMBIE"
        ));
        allNews.add(new NewsItem(
            " New plant: Snow Pea!",
            "2024-01-10",
            "Snow Pea slows down zombies with its icy peas.",
            "PLANT"
        ));
        allNews.add(new NewsItem(
            " Chapter 2 Unlocked!",
            "2024-01-12",
            "You have unlocked Chapter 2: Ice Caves. New zombies and plants await!",
            "LEVEL"
        ));
        allNews.add(new NewsItem(
            " New Minigame: Vasebreaker!",
            "2024-01-15",
            "Try the new Vasebreaker minigame. Break vases to find plants and defeat zombies.",
            "MINIGAME"
        ));
        unreadNews.addAll(allNews);

        buildUI();
    }
    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" NEWS", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        Label countLabel = new Label(" Unread: " + unreadNews.size(), skin);
        mainTable.add(countLabel).padBottom(20).row();

        Table newsTable = new Table();

        for (NewsItem news : allNews) {
            String status = unreadNews.contains(news) ? "🔴 " : "✅ ";
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
                    if (unreadNews.contains(news)) {
                        unreadNews.remove(news);
                        countLabel.setText(" Unread: " + unreadNews.size());
                    }
                    showNewsDetail(news);
                }
            });
        }
        ScrollPane scrollPane = new ScrollPane(newsTable, skin);
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
                game.setScreen(new MainMenuScreen(game, user));
            }
        });
    }
    private String getTypeIcon(String type) {
        switch (type) {
            case "ZOMBIE": return "🧟";
            case "PLANT": return "🌱";
            case "LEVEL": return "📖";
            case "MINIGAME": return "🎮";
            default: return "📌";
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
        if (unreadNews.isEmpty()) {
            detailLabel.setText(" No unread news!");
            return;
        }
        StringBuilder sb = new StringBuilder(" Unread News:\n\n");
        for (NewsItem news : unreadNews) {
            sb.append("🔴 ").append(news.getTitle()).append("\n");
        }
        detailLabel.setText(sb.toString());
    }
    private void showAllNews() {
        if (allNews.isEmpty()) {
            detailLabel.setText(" No news!");
            return;
        }
        StringBuilder sb = new StringBuilder("📚 All News:\n\n");
        for (NewsItem news : allNews) {
            String status = unreadNews.contains(news) ? "🔴" : "✅";
            String icon = getTypeIcon(news.getType());
            sb.append(status).append(" ").append(icon).append(" ").append(news.getTitle()).append("\n");
        }
        detailLabel.setText(sb.toString());
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    private static class NewsItem {
        private String title;
        private String date;
        private String body;
        private String type;

        public NewsItem(String title, String date, String body, String type) {
            this.title = title;
            this.date = date;
            this.body = body;
            this.type = type;
        }
        public String getTitle() { return title; }
        public String getDate() { return date; }
        public String getBody() { return body; }
        public String getType() { return type; }
    }
}
