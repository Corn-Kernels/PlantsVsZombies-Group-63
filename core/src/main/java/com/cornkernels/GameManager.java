package com.cornkernels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.engine.audio.AudioManager;
import com.cornkernels.engine.input.InputManager;
import com.cornkernels.engine.settings.AudioSettings;
import com.cornkernels.engine.settings.GameSettings;
import com.cornkernels.engine.settings.VideoSettings;
import com.cornkernels.engine.video.VideoManager;
import com.cornkernels.game.screens.MenuScreen;

public class GameManager extends Game {

    public SpriteBatch batch;

    private AudioManager audioManager;
    private VideoManager videoManager;
    private InputManager inputManager;

    private GameSettings gameSettings;
    private VideoSettings videoSettings;
    private AudioSettings audioSettings;

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameSettings = new GameSettings();
        videoSettings = new VideoSettings(gameSettings);
        audioSettings = new AudioSettings(gameSettings);

        audioManager = AudioManager.getInstance();
        audioManager.init(audioSettings);
        videoManager = VideoManager.getInstance();
        videoManager.init(videoSettings);
        inputManager = InputManager.getInstance();
        inputManager.init(gameSettings);

        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {
    }
}
