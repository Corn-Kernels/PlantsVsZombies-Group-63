package com.cornkernels.engine.video;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.cornkernels.engine.settings.VideoSettings;

public class VideoManager {
    private static final VideoManager INSTANCE = new VideoManager();

    private VideoSettings settings;

    private VideoManager() {
    }

    public static VideoManager getInstance() {
        return INSTANCE;
    }

    public void init(VideoSettings settings) {
        this.settings = settings;
        applyAll();
    }

    public void applyAll() {
        applyFullScreen();
        applyVSync();
    }


    public float getBrightness() {
        return settings.getBrightness();
    }

    public void setBrightness(float brightness) {
        settings.setBrightness(brightness);
    }


    public boolean isFullScreen() {
        return settings.isFullScreen();
    }

    public void setFullScreen(boolean fullScreen) {
        settings.setFullScreen(fullScreen);
        applyFullScreen();
    }

    private void applyFullScreen() {
        if (settings.isFullScreen()) {
            Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(mode);
        } else {
            Gdx.graphics.setWindowedMode(settings.getWindowWidth(), settings.getWindowHeight());
        }
    }

    public boolean isVSync() {
        return settings.isVSync();
    }

    public void setVSync(boolean vSync) {
        settings.setVSync(vSync);
        applyVSync();
    }

    private void applyVSync() {
        Gdx.graphics.setVSync(settings.isVSync());
    }


    public void setScreenResolution(int width, int height) {
        settings.setScreenResolution(width, height);
        if (!settings.isFullScreen()) {
            Gdx.graphics.setWindowedMode(width, height);
        }
    }

    public void setFullScreenDisplayMode(Graphics.DisplayMode mode) {
        settings.setFullScreen(true);
        Gdx.graphics.setFullscreenMode(mode);
    }

    public Graphics.DisplayMode[] getAvailableDisplayModes() {
        return Gdx.graphics.getDisplayModes();
    }

    public Graphics.DisplayMode getCurrentDisplayMode() {
        return Gdx.graphics.getDisplayMode();
    }
}
