package com.cornkernels.engine.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class GameSettings {

    public float masterVolume = 1.0f;
    public float soundVolume = 1.0f;
    public float musicVolume = 1.0f;

    public float brightness = 1.0f;
    public boolean fullScreen = false;
    public boolean frameCapOn = false;
    public boolean vSync = true;
    public int targetFrameRate = 60;
    public int screenWidth = Gdx.graphics.getWidth();
    public int screenHeight = Gdx.graphics.getHeight();

    public static GameSettings load(FileHandle file) {
        GameSettings settings = new GameSettings();
        if (!file.exists()) return settings;
        try (DataInputStream in = new DataInputStream(file.read())) {
            settings.masterVolume = in.readFloat();
            settings.soundVolume = in.readFloat();
            settings.musicVolume = in.readFloat();
            settings.brightness = in.readFloat();
            settings.fullScreen = in.readBoolean();
            settings.frameCapOn = in.readBoolean();
            settings.vSync = in.readBoolean();
            settings.targetFrameRate = in.readInt();
            settings.screenWidth = in.readInt();
            settings.screenHeight = in.readInt();
        } catch (IOException e) {
            Gdx.app.error("GameSettings", "Failed to load settings, using defaults", e);
            return new GameSettings();
        }
        return settings;
    }

    public void save(FileHandle file) {
        try (DataOutputStream out = new DataOutputStream(file.write(false))) {
            out.writeFloat(masterVolume);
            out.writeFloat(soundVolume);
            out.writeFloat(musicVolume);
            out.writeFloat(brightness);
            out.writeBoolean(fullScreen);
            out.writeBoolean(frameCapOn);
            out.writeBoolean(vSync);
            out.writeInt(targetFrameRate);
            out.writeInt(screenWidth);
            out.writeInt(screenHeight);
        } catch (IOException e) {
            Gdx.app.error("GameSettings", "Failed to save settings", e);
        }
    }

    public void resetToDefaults() {
        GameSettings defaults = new GameSettings();
        defaults.masterVolume = masterVolume;
        defaults.soundVolume = soundVolume;
        defaults.musicVolume = musicVolume;
        defaults.brightness = brightness;
        defaults.fullScreen = fullScreen;
        defaults.frameCapOn = frameCapOn;
        defaults.vSync = vSync;
        defaults.targetFrameRate = targetFrameRate;
        defaults.screenWidth = screenWidth;
        defaults.screenHeight = screenHeight;
    }
}
