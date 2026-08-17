package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raeleus.tenpatch.TenPatch;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.PlayerProgress;

public class MainMenuScreen implements BaseScreen{
    private Main game;
    private Stage stage;
    private Skin skin;
    private User user;

    public MainMenuScreen(Main game,User user){
        this.game=game;
        this.user=user;
        stage=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        TenPatch.setDefaultDrawable("tenpatch");
        skin=new Skin(Gdx.files.internal("skin/pvz2_skin.json"));
        buildUI();
    }
    private void buildUI(){
        PlayerProgress progress=user.getProgress();
        Table table=new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label(" Main Menu", skin);
        table.add(titleLabel).padBottom(20).row();

        table.add(new Label( user.getNickname(), skin)).row();
        table.add(new Label(" Coins: " + progress.getCoins(), skin)).row();
        table.add(new Label(" Diamonds: " + progress.getDiamonds(), skin)).padBottom(30).row();

        TextButton playBtn = new TextButton(" Play", skin);
        TextButton settingsBtn = new TextButton("⚙ Settings", skin);
        TextButton newsBtn = new TextButton(" News", skin);
        TextButton profileBtn = new TextButton(" Profile", skin);
        TextButton logoutBtn = new TextButton(" Logout", skin);

        table.add(playBtn).width(200).height(50).padBottom(10).row();
        table.add(settingsBtn).width(200).height(50).padBottom(10).row();
        table.add(newsBtn).width(200).height(50).padBottom(10).row();
        table.add(profileBtn).width(200).height(50).padBottom(10).row();
        table.add(logoutBtn).width(200).height(50).row();

        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new GameMenuScreen(game, user));
            }
        });

        logoutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                user.setLoggedIn(false);
                game.getStorageService().saveUsers();
                game.setScreen(new LoginScreen(game));
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
    @Override
    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
    @Override public void show(){}
    @Override public void hide(){}
    @Override public void pause(){}
    @Override public void resume(){}

}
