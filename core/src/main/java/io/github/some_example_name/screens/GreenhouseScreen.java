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

import java.util.prefs.BackingStoreException;

public class GreenhouseScreen implements BaseScreen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;
    public GreenhouseScreen(Main game,User user){
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

        Label titleLabel=new Label("GREENHOUSE",skin);
        mainTable.add(titleLabel).padBottom(10).row();
        Table infoTable=new Table();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin));
        infoTable.add(new Label(" Pots: 1/20", skin)).padLeft(20);
        mainTable.add(infoTable).padBottom(20).row();

        Table potsTable=new Table();
        potsTable.setBackground(skin.getDrawable("white_pixel"));
        potsTable.setColor(0.3f,0.2f,0.1f,1);
        String[] statuses = {"⬜ EMPTY", " LOCKED", " LOCKED", " LOCKED", " LOCKED"};

        for(int row=0;row<4;row++){
            for(int col=0;col<5;col++){
                String status=(row==0&&col==0)? " EMPTY" : " LOCKED";
                Label potLabel=new Label(status,skin);
                potLabel.setFontScale(0.7f);
                potsTable.add(potLabel).width(70).height(40).pad(2);
            }
            potsTable.row();
        }
        mainTable.add(potsTable).padBottom(20).row();
        TextButton plantBtn = new TextButton(" Plant (1,1)", skin);
        TextButton collectBtn = new TextButton(" Collect (1,1)", skin);
        TextButton growBtn = new TextButton(" Grow (1,1)", skin);
        TextButton backBtn = new TextButton(" Back", skin);

        Table buttonTable=new Table();
        buttonTable.add(plantBtn).width(140).height(40).padRight(10);
        buttonTable.add(collectBtn).width(140).height(40).padRight(10);
        buttonTable.add(growBtn).width(140).height(40).padRight(10);
        buttonTable.add(backBtn).width(100).height(40);
        mainTable.add(buttonTable).row();

        backBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                game.setScreen(new GameMenuScreen(game,user));
            }
        });
    }
    @Override
    public void render(float delta){
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
