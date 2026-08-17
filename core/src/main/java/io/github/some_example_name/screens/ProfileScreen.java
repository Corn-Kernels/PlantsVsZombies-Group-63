package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.raeleus.tenpatch.TenPatch;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.PlayerProgress;

public class ProfileScreen implements BaseScreen{
    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;
    public ProfileScreen(Main game,User user){
        this.game=game;
        this.user=user;
        stage=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TenPatch.setDefaultDrawable("tenpatch");
        skin = new Skin(Gdx.files.internal("skin/pvz2_skin.json"));
        buildUI();
    }
    private void buildUI(){
        PlayerProgress progress=user.getProgress();
        Table mainTable=new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        Label titleLabel=new Label("PROFILE",skin);
        mainTable.add(titleLabel).padBottom(20).row();

        Table infoTable=new Table();
        infoTable.add(new Label("Username: " + user.getUsername(), skin)).left().padBottom(5).row();
        infoTable.add(new Label("Nickname: " + user.getNickname(), skin)).left().padBottom(5).row();
        infoTable.add(new Label("Email: " + user.getEmail(), skin)).left().padBottom(5).row();
        infoTable.add(new Label("Gender: " + user.getGender(), skin)).left().padBottom(10).row();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).left().padBottom(5).row();
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin)).left().padBottom(5).row();
        infoTable.add(new Label(" Unlocked: " + progress.getUnlockedChapters(), skin)).left().padBottom(5).row();

        mainTable.add(infoTable).padBottom(20).row();
        TextButton backBtn=new TextButton("Back",skin);
        mainTable.add(backBtn).width(150).height(50).row();
        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                game.setScreen(new MainMenuScreen(game,user));
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
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
