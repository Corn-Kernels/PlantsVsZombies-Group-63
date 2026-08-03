package com.cornkernels.engine.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.cornkernels.engine.settings.AudioSettings;
import org.jspecify.annotations.NonNull;

public class AudioManager {

    private static AudioManager INSTANCE = new AudioManager();

    private final Array<ActiveSound> activeLoopingSounds = new Array<>();
    private final Array<Music> activeMusics = new Array<>();

    private AudioSettings settings;

    private AudioManager() {
    }

    public static AudioManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AudioManager();
        }
        return INSTANCE;
    }

    public void init(AudioSettings settings) {
        this.settings = settings;
    }

    public void setVolumes(float master, float sound, float music) {
        settings.setMasterVolume(master);
        settings.setSoundVolume(sound);
        settings.setMusicVolume(music);

        applyMusicVolumeToAll();
        applySoundVolumeToAll();
    }

    public void setMasterVolume(float masterVolume) {
        settings.setMasterVolume(masterVolume);

        applyMusicVolumeToAll();
        applySoundVolumeToAll();
    }

    public float getMusicVolume() {
        return settings.getRawMusicVolume() * settings.getRawMasterVolume();
    }

    public void setMusicVolume(float musicVolume) {
        settings.setMusicVolume(musicVolume);

        applyMusicVolumeToAll();
    }

    public float getSoundVolume() {
        return settings.getRawSoundVolume() * settings.getRawMasterVolume();
    }

    public void setSoundVolume(float soundVolume) {
        settings.setSoundVolume(soundVolume);
        applySoundVolumeToAll();
    }

    public float getRawMasterVolume() {
        return settings.getRawMasterVolume();
    }

    public float getRawSoundVolume() {
        return settings.getRawSoundVolume();
    }

    public float getRawMusicVolume() {
        return settings.getRawMusicVolume();
    }

    public Music playMusic(@NonNull Music music, boolean loop) {
        music.setLooping(loop);
        music.setVolume(getMusicVolume());
        music.play();
        activeMusics.add(music);
        return music;
    }

    public Music playMusicExclusive(@NonNull Music music, boolean loop) {
        stopAllMusic();
        return playMusic(music, loop);
    }

    public void stopMusic(@NonNull Music music) {
        music.stop();
        activeMusics.removeValue(music, true);
    }

    public void stopAllMusic() {
        for (Music m : activeMusics) {
            m.stop();
        }
        activeMusics.clear();
    }

    public void stopMusic() {
        stopAllMusic();
    }

    public void pauseAllMusic() {
        for (Music m : activeMusics) {
            if (m.isPlaying()) m.pause();
        }
    }

    public void resumeAllMusic() {
        for (Music m : activeMusics) {
            if (!m.isPlaying()) m.play();
        }
    }

    public void pauseMusic() {
        pauseAllMusic();
    }

    public void resumeMusic() {
        resumeAllMusic();
    }

    private void applyMusicVolumeToAll() {
        for (Music m : activeMusics) {
            m.setVolume(getMusicVolume());
        }
    }

    public long playSound(@NonNull Sound sound, boolean loop) {
        long id = sound.play(getSoundVolume());
        sound.setLooping(id, loop);

        if (loop) {
            activeLoopingSounds.add(new ActiveSound(sound, id));
        }

        return id;
    }

    public void stopSound(@NonNull Sound sound, long id) {
        sound.stop(id);
        removeActiveLoopingSound(sound, id);
    }

    public void stopAllSounds() {
        for (ActiveSound active : activeLoopingSounds) {
            active.sound.stop(active.id);
        }
        activeLoopingSounds.clear();
    }

    public void stopSound() {
        stopAllSounds();
    }

    private void applySoundVolumeToAll() {
        float volume = getSoundVolume();
        for (ActiveSound active : activeLoopingSounds) {
            active.sound.setVolume(active.id, volume);
        }
    }

    private void removeActiveLoopingSound(Sound sound, long id) {
        for (int i = activeLoopingSounds.size - 1; i >= 0; i--) {
            ActiveSound active = activeLoopingSounds.get(i);
            if (active.sound == sound && active.id == id) {
                activeLoopingSounds.removeIndex(i);
                break;
            }
        }
    }

    private record ActiveSound(Sound sound, long id) {
    }
}
