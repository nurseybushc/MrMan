package com.oscarboking.mrman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

/**
 * Created by boking on 2016-09-04.
 */
public class Settings {

    public static boolean soundEnabled;
    public static boolean musicEnabled;

    private static Preferences prefs;

    private static Music menuMusic;


    public static void update(){
        prefs = Gdx.app.getPreferences("My Preferences");
        setSoundEnabled(prefs.getBoolean("soundEnabled",true));
        setMusicEnabled(prefs.getBoolean("musicEnabled",true));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/awesomeness.ogg"));
        menuMusic.setVolume(0.7f);
        menuMusic.setLooping(true);
    }

    public static void playMenuMusic(){
        menuMusic.play();
    }
    public static void stopMenuMusic(){
        menuMusic.stop();
    }
    public static void pauseMenuMusic(){
        menuMusic.pause();
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static void setSoundEnabled(boolean soundEnabled) {
        Settings.soundEnabled = soundEnabled;
        prefs.putBoolean("soundEnabled",soundEnabled);
        prefs.flush();
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        Settings.musicEnabled = musicEnabled;
        prefs.putBoolean("musicEnabled",musicEnabled);
        prefs.flush();
    }
}
