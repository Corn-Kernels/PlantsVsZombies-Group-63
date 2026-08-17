package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;

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

public class CollectionScreen implements BaseScreen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;

    public CollectionScreen(Main game, User user) {
        this.game = game;
        this.user = user;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TenPatch.setDefaultDrawable("tenpatch");
        skin = new Skin(Gdx.files.internal("skin/pvz2_skin.json"));

        buildUI();
    }

    private void buildUI() {
        PlayerProgress progress = user.getProgress();
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        Label titleLabel = new Label("COLLECTION", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        Table infoTable = new Table();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin));
        mainTable.add(infoTable).padBottom(20).row();
        Table contentTable = new Table();

        Table plantsTable = new Table();
        ScrollPane plantScroll = new ScrollPane(plantsTable, skin);
        plantScroll.setScrollingDisabled(true, false);
        plantScroll.setHeight(350);

        String[] allPlants = {"SUNFLOWER", "PEASHOOTER", "WALL_NUT", "POTATO_MINE", "CHERRY_BOMB"};
        for (String plant : allPlants) {
            boolean owned = progress.hasPlant(plant);
            Label plantLabel = new Label((owned ? "✅ " : "🔒 ") + plant, skin);
            plantsTable.add(plantLabel).left().padBottom(5).row();
        }
        Table zombiesTable = new Table();
        ScrollPane zombieScroll = new ScrollPane(zombiesTable, skin);
        zombieScroll.setScrollingDisabled(true, false);
        zombieScroll.setHeight(350);

        String[] allZombies = {"BASIC", "CONEHEAD", "BUCKETHEAD", "GARGANTUAR"};
        for (String zombie : allZombies) {
            boolean seen = Math.random() > 0.5; // نمونه
            Label zombieLabel = new Label((seen ? "🧟 " : "❓ ") + zombie, skin);
            zombiesTable.add(zombieLabel).left().padBottom(5).row();
        }
        contentTable.add(plantScroll).width(300).padRight(10);
        contentTable.add(zombieScroll).width(300);
        mainTable.add(contentTable).padBottom(20).row();

        TextButton backBtn=new TextButton("Back",skin);
        mainTable.add(backBtn).width(150).height(50).row();
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
