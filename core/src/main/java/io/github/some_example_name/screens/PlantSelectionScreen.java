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


import java.util.ArrayList;
import java.util.List;
public class PlantSelectionScreen implements BaseScreen{
    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;
    private String chapterName;
    private List<String>selectedPlants;
    private static final int MAX_PLANTS=8;

    private Table plantListTable;
    private Table selectedTable;
    private Label selectedCountLabel;
    private Label errorLabel;

    public PlantSelectionScreen(Main game,User user,String chapterName){
        this.game=game;
        this.user=user;
        this.chapterName=chapterName;
        this.selectedPlants=new ArrayList<>();

        stage=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        TenPatch.setDefaultDrawable("tenpatch");
        skin=new Skin(Gdx.files.internal("skin/pvz2_skin.json"));
        buildUI();
    }
    private void buildUI(){
        PlayerProgress progress=user.getProgress();
        Table mainTable=new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel=new Label(" SELECT PLANTS - " + chapterName, skin);
        mainTable.add(titleLabel).padBottom(10).row();

        Table infoTable=new Table();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin)).padRight(20);
        selectedCountLabel=new Label("Selected: 0/" + MAX_PLANTS, skin);
        infoTable.add(selectedCountLabel);
        mainTable.add(infoTable).padBottom(10).row();

        errorLabel=new Label("",skin);
        errorLabel.setColor(1,0,0,1);
        mainTable.add(errorLabel).padBottom(10).row();
        Table splitTable=new Table();

        plantListTable=new Table();
        ScrollPane plantScroll=new ScrollPane(plantListTable,skin);
        plantScroll.setScrollingDisabled(true,false);
        plantScroll.setHeight(350);

        selectedTable=new Table();
        ScrollPane selectedScroll=new ScrollPane(selectedTable,skin);
        selectedScroll.setScrollingDisabled(true,false);
        selectedScroll.setHeight(350);

        splitTable.add(plantScroll).width(250).padRight(10);
        splitTable.add(selectedScroll).width(250);
        mainTable.add(splitTable).padBottom(10).row();
        TextButton startBtn=new TextButton(" START BATTLE", skin);
        TextButton backBtn=new TextButton(" Back", skin);

        Table buttonTable=new Table();
        buttonTable.add(startBtn).width(180).height(50).padRight(10);
        buttonTable.add(backBtn).width(150).height(50);
        mainTable.add(buttonTable).row();
        buildPlantList(progress);
        updateSelectedList();
        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleStart();
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AdventureMenuScreen(game, user));
            }
        });
    }
    private void buildPlantList(PlayerProgress progress){
        plantListTable.clear();
        List<String>ownedPlants=progress.getOwnedPlants();

        if(owned.isEmpty()){
            plantListTable.add(new Label("⚠ No plants owned!", skin)).padTop(20).row();
        }
        for(String plant:owned){
            TextButton btn=new TextButton(plant,skin);
            btn.setWidth(220);
            if(selectedPlants.contains(plant))
                btn.setDisabled(true);
            plantListTable.add(btn).width(220).height(40).padBottom(5).row();
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!selectedPlants.contains(plant) && selectedPlants.size() < MAX_PLANTS) {
                        selectedPlants.add(plant);
                        updateSelectedList();
                        buildPlantList(progress);
                        errorLabel.setText("");
                    } else if (selectedPlants.size() >= MAX_PLANTS) {
                        errorLabel.setText(" Max " + MAX_PLANTS + " plants selected!");
                    }
                }
            });
        }
    }
    private void updateSelectedList(){
        selectedTable.clear();
        if(selectedPlants.isEmpty()){
            selectedTable.add(new Label(" No plants selected", skin)).padTop(20).row();
        }else{
            for(String plant:selectedPlants){
                selectedTable.add(new Label( plant, skin)).padBottom(5).row();
            }
        }
        selectedCountLabel.setText("Selected: " + selectedPlants.size() + "/" + MAX_PLANTS);
    }
    private void handleStart(){
        if(selectedPlants.isEmpty()){
            errorLabel.setText(" Select at least one plant!");
            return;
        }
        System.out.println(" Starting battle with: " + selectedPlants);
    }
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resize(int width,int height){
        stage.getViewport().update(width,height,true);
    }
    @Override
    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
