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
import com.cornkernels.game.menus.model.User;
import com.cornkernels.game.menus.screens.LoginScreen;
import com.cornkernels.game.menus.screens.MainMenuScreen;
import com.cornkernels.game.menus.service.StorageService;

public class GameManager extends Game {

    public SpriteBatch batch;

    private AudioManager audioManager;
    private VideoManager videoManager;
    private InputManager inputManager;

    private GameSettings gameSettings;
    private VideoSettings videoSettings;
    private AudioSettings audioSettings;
    private InputSettings inputSettings;

    private StorageService storageService;
    private User currentUser;


    @Override
    public void create() {
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

        storageService = new StorageService();
        storageService.loadUsers();

        batch = new SpriteBatch();

        currentUser = storageService.getUser("test");
        if (currentUser == null) {
            currentUser = new User("test", "123", "Tester", "test@test.com", "male");
            storageService.addUser(currentUser);
            storageService.saveUsers();
        }

        if (currentUser != null && currentUser.isLoggedIn() && currentUser.isStayLoggedIn()) {
            System.out.println(" Auto-login: Welcome back " + currentUser.getNickname() + "!");
            setScreen(new MainMenuScreen(this, currentUser));
        } else {
            setScreen(new LoginScreen(this));
        }
    }

    @Override
    public void render() {
        super.render();
        inputManager.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public StorageService getStorageService() {
        return storageService;
    }
}
