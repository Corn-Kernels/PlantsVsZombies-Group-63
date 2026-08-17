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

import java.util.List;
public class AdventureMenuScreen implements BaseScreen{
    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;

    public AdventureMenuScreen(Main game, User user){
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
        Label titleLabel=new Label(" ADVENTURE", skin);
        mainTable.add(titleLabel).padBottom(10).row();

        Table infoTable=new Table();
        infoTable.add(new Label(" Coins: " + progress.getCoins(), skin)).padRight(20);
        infoTable.add(new Label(" Diamonds: " + progress.getDiamonds(), skin));
        mainTable.add(infoTable).padBottom(20).row();

        // لیست فصل‌ها
        Table chaptersTable = new Table();
        ScrollPane scrollPane = new ScrollPane(chaptersTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setHeight(300);

        List<String> unlocked = progress.getUnlockedChapters();
        String[] allChapters = {"Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4"};

        for (String chapter : allChapters) {
            boolean isUnlocked = unlocked.contains(chapter);
            TextButton btn = new TextButton((isUnlocked ? "✅ " : "🔒 ") + chapter, skin);
            if (!isUnlocked) btn.setDisabled(true);
            chaptersTable.add(btn).width(350).height(40).padBottom(5).row();

            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (isUnlocked) {
                        System.out.println(" Entering " + chapter);
                        game.setScreen(new PlantSelectionScreen(game, user, chapter));
                    }
                }
            });
        }

        mainTable.add(scrollPane).width(400).height(350).padBottom(20).row();

        TextButton backBtn = new TextButton(" Back", skin);
        mainTable.add(backBtn).width(150).height(50).row();

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameMenuScreen(game, user));
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
    @Override
    public void resize(int width,int height){
        stage.getViewport().update(width,height,true);
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
