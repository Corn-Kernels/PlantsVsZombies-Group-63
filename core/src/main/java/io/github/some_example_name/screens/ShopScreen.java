package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.raeleus.tenpatch.TenPatch;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.PlayerProgress;



public class ShopScreen implements BaseScreen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;
    public ShopScreen(Main game,User user){
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
        Label titleLabel=new Label("SHOP",skin);
        mainTable.add(titleLabel).padBottom(10).row();

        Table infoTable=new Table();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin));
        mainTable.add(infoTable).padBottom(20).row();
        Table itemsTable=new Table();

        String[][] items={
            {"POT", "2000", "Coins"},
            {"PLANT FOOD", "3", "Diamonds"},
            {"RANDOM SEED", "1000", "Coins"},
            {"CHOSEN SEED", "5", "Diamonds"},
            {"DAILY OFFER", "1600", "Coins", "⭐ 20% OFF!"}
        };
        for(String[] item:items){
            String name=item[0];
            String price=item[1];
            String currency=item[2];
            String extra=item.length> 3 ? " " + item[3] : "";
            Label itemLabel = new Label(name + " - " + price + " " + currency + extra, skin);
            itemsTable.add(itemLabel).left().padBottom(5).row();
        }
        ScrollPane scrollPane=new ScrollPane(itemsTable,skin);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setHeight(250);
        mainTable.add(scrollPane).width(350).padBottom(20).row();

        TextButton buyBtn = new TextButton(" Buy Selected", skin);
        TextButton backBtn = new TextButton(" Back", skin);

        Table buttonTable=new Table();
        buttonTable.add(buyBtn).width(150).height(50).padRight(10);
        buttonTable.add(backBtn).width(100).height(50);
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
