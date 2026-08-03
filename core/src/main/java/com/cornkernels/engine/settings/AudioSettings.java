package com.cornkernels.engine.settings;

public final class AudioSettings {

    private final GameSettings settings;

    public AudioSettings(GameSettings gameSettings) {
        this.settings = gameSettings;
    }

    public void setMasterVolume(float masterVolume) {
        settings.masterVolume = masterVolume;
    }

    public void setSoundVolume(float soundVolume) {
        settings.soundVolume = soundVolume;
    }

    public void setMusicVolume(float musicVolume) {
        settings.musicVolume = musicVolume;
    }

    public float getRawMasterVolume() {
        return settings.masterVolume;
    }

    public float getRawSoundVolume() {
        return settings.soundVolume;
    }

    public float getRawMusicVolume() {
        return settings.musicVolume;
    }
}
