package com.cornkernels.engine.settings;

public final class VideoSettings {

    private final GameSettings settings;

    public VideoSettings(GameSettings settings) {
        this.settings = settings;
    }

    public float getBrightness() {
        return settings.brightness;
    }

    public void setBrightness(float value) {
        settings.brightness = Math.clamp(value, 0.2f, 1f);
    }

    public void setScreenResolution(int width, int height) {
        settings.screenWidth = width;
        settings.screenHeight = height;

    }

    public int getWindowWidth() {
        return settings.screenWidth;
    }

    public int getWindowHeight() {
        return settings.screenHeight;
    }

    public boolean isFullScreen() {
        return settings.fullScreen;
    }

    public void setFullScreen(boolean value) {
        settings.fullScreen = value;
    }

    public boolean isVSync() {
        return settings.vSync;
    }

    public void setVSync(boolean value) {
        settings.vSync = value;
    }
}
