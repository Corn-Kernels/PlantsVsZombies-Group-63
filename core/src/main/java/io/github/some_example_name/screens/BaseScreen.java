package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.raeleus.tenpatch.TenPatch;
import io.github.some_example_name.Main;

public abstract class BaseScreen implements Screen{
    protected Main game;
    protected Stage stage;
    protected Skin skin;
    protected Image backgroundImage;
    public BaseScreen(Main game){
        this.game=game;
        stage=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TenPatch.setDefaultDrawable("tenpatch");
        skin = new Skin(Gdx.files.internal("skin/pvz2_skin.json"));
        loadBackground();
    }
    private void loadBackground() {
        try {
            Texture bgTexture = new Texture(Gdx.files.internal("IMAGES/background.jpg"));
            backgroundImage = new Image(bgTexture);
            backgroundImage.setFillParent(true);
            backgroundImage.setZIndex(0);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println(" Background not found! Using default color.");
        }
    }
        @Override
            public void render(float delta){

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
