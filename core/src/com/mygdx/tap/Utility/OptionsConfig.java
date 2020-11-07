package com.mygdx.tap.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class OptionsConfig {
    private static final String OptionNames = "tap";
    private static final String OptionMusicVolume = "volume";
    private static final String OptionSoundOn = "sound.enabled";
    private static final String OptionMusicOn = "music.enabled";
    private Preferences prefs;



    protected Preferences getOptions() {
        if (prefs == null)
            prefs = Gdx.app.getPreferences(OptionNames);
        return prefs;
    }


    public boolean musicOn() {
        return  getOptions().getBoolean(OptionMusicOn, true);
    }

    public void setMusicOn(boolean musicEnabled) {
        getOptions().putBoolean(OptionMusicOn, musicEnabled);
        getOptions().flush();
    }


    public boolean SoundOn() {
        return getOptions().getBoolean(OptionSoundOn, true);
    }

    public void setSoundOn(boolean soundEffectsEnabled) {
        getOptions().putBoolean(OptionSoundOn, soundEffectsEnabled);
        getOptions().flush();
    }



    public void setMusicVolume(float volume) {
        getOptions().putFloat(OptionMusicVolume, volume);
         getOptions().flush();
    }

    public float getMusicVolume() {
        return  getOptions().getFloat(OptionMusicVolume, 0.5f);
    }


}
