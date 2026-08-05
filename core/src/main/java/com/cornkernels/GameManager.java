package com.cornkernels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cornkernels.engine.audio.AudioManager;
import com.cornkernels.engine.input.InputManager;
import com.cornkernels.engine.settings.AudioSettings;
import com.cornkernels.engine.settings.GameSettings;
import com.cornkernels.engine.settings.InputSettings;
import com.cornkernels.engine.settings.VideoSettings;
import com.cornkernels.engine.video.VideoManager;
import com.cornkernels.game.GameAttributes;
import com.cornkernels.game.entities.attributes.SeedSlot;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.screens.GameplayScreen;

import java.util.List;

public class GameManager extends Game {

    public SpriteBatch batch;

    private AudioManager audioManager;
    private VideoManager videoManager;
    private InputManager inputManager;

    private GameSettings gameSettings;
    private VideoSettings videoSettings;
    private AudioSettings audioSettings;
    private InputSettings inputSettings;

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameSettings = new GameSettings();
        videoSettings = new VideoSettings(gameSettings);
        audioSettings = new AudioSettings(gameSettings);
        inputSettings = new InputSettings(gameSettings);

        audioManager = AudioManager.getInstance();
        audioManager.init(audioSettings);
        videoManager = VideoManager.getInstance();
        videoManager.init(videoSettings);
        inputManager = InputManager.getInstance();
        inputManager.init(inputSettings);

        setScreen(new GameplayScreen(this, new GameAttributes(
            List.of(new SeedSlot(PlantDef.APPEASE_MINT)), List.of(ZombieDef.ARCADE)))); // Temporary
    }

    @Override
    public void render() {
        super.render();
        inputManager.update();
    }

    @Override
    public void dispose() {
    }
}
