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

public class GameMenuScreen extends BaseScreen {

    private User user;

    public GameMenuScreen(GameManager game, User user) {
        super(game);
        this.user = user;
        buildUI();
    }

    private void buildUI() {
        PlayerProgress progress = user.getProgress();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label(" GAME MENU", skin);
        table.add(titleLabel).padBottom(20).row();

        table.add(new Label(user.getNickname(), skin)).row();
        table.add(new Label(" Coins: " + progress.getCoins(), skin)).row();
        table.add(new Label(" Diamonds: " + progress.getDiamonds(), skin)).padBottom(30).row();

        TextButton adventureBtn = new TextButton("🗺 Adventure", skin);
        TextButton collectionBtn = new TextButton(" Collection", skin);
        TextButton greenhouseBtn = new TextButton(" Greenhouse", skin);
        TextButton shopBtn = new TextButton(" Shop", skin);
        TextButton backBtn = new TextButton(" Back", skin);

        table.add(adventureBtn).width(200).height(50).padBottom(10).row();
        table.add(collectionBtn).width(200).height(50).padBottom(10).row();
        table.add(greenhouseBtn).width(200).height(50).padBottom(10).row();
        table.add(shopBtn).width(200).height(50).padBottom(10).row();
        table.add(backBtn).width(200).height(50).row();

        adventureBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AdventureMenuScreen(game, user));
            }
        });

        collectionBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CollectionScreen(game, user));
            }
        });

        greenhouseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GreenhouseScreen(game, user));
            }
        });

        shopBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game, user));
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

        Gdx.gl.glClearColor(0.2f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }
}
