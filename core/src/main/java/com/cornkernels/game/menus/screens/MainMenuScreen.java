package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cornkernels.GameManager;
import com.cornkernels.game.menus.model.User;

public class MainMenuScreen extends BaseScreen {

    private User user;

    public MainMenuScreen(GameManager game, User user) {
        super(game);
        this.user = user;
        buildUI();
    }

    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label(" Main Menu", skin);
        titleLabel.setFontScale(1.5f);
        table.add(titleLabel).padBottom(20).row();


        table.add(new Label(user.getNickname(), skin)).padBottom(5).row();

        TextButton playBtn = new TextButton(" Play", skin, "default");
        TextButton settingsBtn = new TextButton(" Settings", skin, "default");

        int unreadCount = getUnreadNewsCount();
        TextButton newsBtn;
        if (unreadCount > 0) {
            newsBtn = new TextButton("* News *", skin, "default");
        } else {
            newsBtn = new TextButton(" News", skin, "default");
        }
        TextButton profileBtn = new TextButton(" Profile", skin, "default");
        TextButton leaderboardBtn = new TextButton(" Leaderboard", skin, "default");
        TextButton logoutBtn = new TextButton(" Logout", skin, "default");

        table.add(playBtn).width(200).height(50).padBottom(10).row();
        table.add(settingsBtn).width(200).height(50).padBottom(10).row();
        table.add(newsBtn).width(200).height(50).padBottom(10).row();
        table.add(profileBtn).width(200).height(50).padBottom(10).row();
        table.add(leaderboardBtn).width(200).height(50).padBottom(10).row();
        table.add(logoutBtn).width(200).height(50).row();


        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameMenuScreen(game, user));
            }
        });
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, user));
            }
        });
        newsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new NewsScreen(game, user));
            }
        });
        profileBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ProfileScreen(game, user));
            }
        });
        leaderboardBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game, user));
            }
        });
        logoutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                user.setLoggedIn(false);
                game.getStorageService().saveUsers();
                game.setScreen(new LoginScreen(game));
            }
        });
    }

    private int getUnreadNewsCount() {
        return user.getProgress().getUnreadNewsCount();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
