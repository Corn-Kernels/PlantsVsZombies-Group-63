package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;

import java.util.ArrayList;
import java.util.List;

public class NewsScreen extends BaseScreen{
    private User user;
    private List<NewsItem> allNews;
    private List<NewsItem> unreadNews;

    public NewsScreen(Main game,User user){
        super(game);
        this.user=user;

        allNews = new ArrayList<>();
        unreadNews = new ArrayList<>();

        allNews.add(new NewsItem(" Welcome to the game!", "2024-01-01"));
        allNews.add(new NewsItem(" New zombie: Buckethead!", "2024-01-05"));
        allNews.add(new NewsItem(" Plant of the week: Sunflower", "2024-01-10"));
        allNews.add(new NewsItem(" New update: Speed mode added!", "2024-01-15"));
        unreadNews.addAll(allNews);
        buildUI();
    }
    private void buildUI(){
        Table mainTable=new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabl=new Label("NEWS",skin);
        mainTable.add(titleLabl).padBottom(10).row();

        Label countLabel=new Label("Unread:"+unreadNews.size(),skin);
        mainTable.add(countLabel).padBottom(20).row();
        Table newsTable=new Table();

        for (NewsItem news : allNews) {
            String status = unreadNews.contains(news) ? "🔴 " : "✅ ";
            Label newsLabel = new Label(status + news.getTitle() + " (" + news.getDate() + ")", skin);
            newsLabel.setFontScale(0.8f);
            newsTable.add(newsLabel).left().padBottom(5).row();
        }
        ScrollPane scrollPane=new ScrollPane(newsTable,skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setHeight(350);
        mainTable.add(scrollPane).width(400).padBottom(20).row();

        TextButton unreadBtn=new TextButton("Show Unread",skin);
        TextButton allBtn=new TextButton("Show All",skin);
        TextButton backBtn=new TextButton("Back",skin);

        Table buttonTable=new Table();
        buttonTable.add(unreadBtn).width(130).height(40).padRight(10);
        buttonTable.add(allBtn).width(130).height(40).padRight(10);
        buttonTable.add(backBtn).width(100).height(40);
        mainTable.add(buttonTable).row();

        unreadBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                System.out.println(" Showing unread news...");
                showUnreadNews();
            }
        });
        allBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                System.out.println(" Showing all news...");
                showAllNews();
            }
        });
        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                game.setScreen(new MainMenuScreen(game,user));
            }
        });
    }
    private void showUnreadNews() {
        if (unreadNews.isEmpty()) {
            System.out.println(" No unread news!");
            return;
        }
        System.out.println(" Showing unread news:");
        for (NewsItem news : unreadNews) {
            System.out.println("  🔴 " + news.getTitle() + " (" + news.getDate() + ")");
        }
    }

    private void showAllNews() {
        if (allNews.isEmpty()) {
            System.out.println(" No news!");
            return;
        }
        System.out.println(" Showing all news:");
        for (NewsItem news : allNews) {
            String status = unreadNews.contains(news) ? "🔴" : "✅";
            System.out.println("  " + status + " " + news.getTitle() + " (" + news.getDate() + ")");
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
    public void resize(int width, int height) {
        super.resize(width, height);
    }
    @Override
    public void dispose() {
        super.dispose();
    }
    @Override public void show() { super.show(); }
    @Override public void hide() { super.hide(); }
    @Override public void pause() { super.pause(); }
    @Override public void resume() { super.resume(); }
    private static class NewsItem {
        private String title;
        private String date;
        public NewsItem(String title, String date) {
            this.title = title;
            this.date = date;
        }
        public String getTitle() { return title; }
        public String getDate() { return date; }
    }
}
