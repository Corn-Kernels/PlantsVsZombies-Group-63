package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cornkernels.GameManager;
import com.cornkernels.game.menus.model.PlayerProgress;
import com.cornkernels.game.menus.model.User;

public class ProfileScreen extends BaseScreen {

    private User user;

    public ProfileScreen(GameManager game, User user) {
        super(game);
        this.user = user;
        buildUI();
    }

    private void buildUI() {
        PlayerProgress progress = user.getProgress();
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" PROFILE", skin);
        mainTable.add(titleLabel).padBottom(20).row();
        Table infoTable = new Table();

        infoTable.add(new Label("Username: " + user.getUsername(), skin)).left().padBottom(5).row();
        infoTable.add(new Label("Nickname: " + user.getNickname(), skin)).left().padBottom(5).row();
        infoTable.add(new Label("Email: " + user.getEmail(), skin)).left().padBottom(5).row();
        infoTable.add(new Label("Gender: " + user.getGender(), skin)).left().padBottom(10).row();

        infoTable.add(new Label(" Games Played: " + progress.getGamesPlayed(), skin)).left().padBottom(5).row();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).left().padBottom(5).row();
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin)).left().padBottom(5).row();
        infoTable.add(new Label(" Unlocked Chapters: " + progress.getUnlockedChapters(), skin)).left().padBottom(5).row();
        infoTable.add(new Label(" Completed Levels: " + progress.getCompletedLevels(), skin)).left().padBottom(5).row();
        infoTable.add(new Label(" High Score: " + progress.getHighScore(), skin)).left().padBottom(10).row();

        mainTable.add(infoTable).padBottom(20).row();

        TextButton editBtn = new TextButton("✏ Edit Profile", skin, "default");
        TextButton backBtn = new TextButton(" Back", skin, "default");

        Table buttonTable = new Table();
        buttonTable.add(editBtn).width(150).height(50).padRight(10);
        buttonTable.add(backBtn).width(150).height(50);
        mainTable.add(buttonTable).row();

        editBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new EditProfileScreen(game, user));
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, user));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
