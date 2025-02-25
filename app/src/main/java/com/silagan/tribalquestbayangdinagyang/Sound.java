package com.silagan.tribalquestbayangdinagyang;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Sound {
    private static final String TAG = "SoundManager";

    // Sound constants
    private static final String BG_MUSIC = "bgMusic";
    private static final String MINIGAME_MUSIC = "minigameMusic";
    private static final String MAIN_APP_MUSIC = "mainAppMusic";
    private static final String BUTTON_CLICK = "buttonClick";
    private static final String CONFETTI = "confetti";
    private static final String MAINGAME_GAME_OVER = "maingameGameOver";
    private static final String MINIGAME_GAME_OVER = "minigameGameOver";

    // MediaPlayer for background music (supports looping)
    private MediaPlayer bgMusicPlayer;
    private MediaPlayer minigameMusicPlayer;
    private MediaPlayer mainAppMusicPlayer;

    // SoundPool for short sound effects
    private SoundPool soundPool;
    private Map<String, Integer> soundMap;

    private Context context;
    private boolean initialized = false;

    private static Sound instance;
    private boolean bgMusicWasPlaying = false;
    private boolean minigameMusicWasPlaying = false;
    private boolean mainAppMusicWasPlaying = false;

    public static synchronized Sound getInstance(Context context) {
        if (instance == null) {
            instance = new Sound(context.getApplicationContext());
        }
        return instance;
    }

    public Sound(Context context) {
        this.context = context;
        initialize();
    }

    private void initialize() {
        try {
            // Initialize SoundPool for sound effects
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();

            soundMap = new HashMap<>();

            // Load sound effects
            loadSoundEffects();

            // Initialize MediaPlayers for music
            initializeMediaPlayers();

            initialized = true;
        } catch (Exception e) {
            Log.e(TAG, "Error initializing sound manager: " + e.getMessage());
        }
    }

    private void loadSoundEffects() {
        // Load sound effects into SoundPool
        // Note: Replace R.raw.xxx with your actual resource IDs
        soundMap.put(BUTTON_CLICK, soundPool.load(context, R.raw.button_click, 1));
        soundMap.put(CONFETTI, soundPool.load(context, R.raw.confetti, 1));

        soundMap.put(MAINGAME_GAME_OVER, soundPool.load(context, R.raw.maingame_gameover, 1));
        soundMap.put(MINIGAME_GAME_OVER, soundPool.load(context, R.raw.minigame_gameover, 1));
    }

    private void initializeMediaPlayers() {
        // Initialize MediaPlayers for background music
        // Note: Replace R.raw.xxx with your actual resource IDs
        bgMusicPlayer = MediaPlayer.create(context, R.raw.maingame_music);
        bgMusicPlayer.setLooping(true);

        minigameMusicPlayer = MediaPlayer.create(context, R.raw.minigame_music);
        minigameMusicPlayer.setLooping(true);

        mainAppMusicPlayer = MediaPlayer.create(context, R.raw.main_app_music);
        mainAppMusicPlayer.setLooping(true);
    }

    // Play background music
    public void playBackgroundMusic() {
        if (!initialized) return;

        stopAllMusic();
        if (bgMusicPlayer != null) {
            bgMusicPlayer.start();
        }
    }

    // Play minigame music
    public void playMinigameMusic() {
        if (!initialized) return;

        stopAllMusic();
        if (minigameMusicPlayer != null) {
            minigameMusicPlayer.start();
        }
    }

    // Play main app music
    public void playMainAppMusic() {
        if (!initialized) return;

        stopAllMusic();
        if (mainAppMusicPlayer != null) {
            mainAppMusicPlayer.start();
        }
    }

    // Stop all music tracks
    public void stopAllMusic() {
        if (!initialized) return;

        if (bgMusicPlayer != null && bgMusicPlayer.isPlaying()) {
            bgMusicPlayer.pause();
            bgMusicPlayer.seekTo(0);
        }

        if (minigameMusicPlayer != null && minigameMusicPlayer.isPlaying()) {
            minigameMusicPlayer.pause();
            minigameMusicPlayer.seekTo(0);
        }

        if (mainAppMusicPlayer != null && mainAppMusicPlayer.isPlaying()) {
            mainAppMusicPlayer.pause();
            mainAppMusicPlayer.seekTo(0);
        }
    }

    // Play button click sound
    public void playButtonClickSound() {
        playSound(BUTTON_CLICK);
    }

    // Generic method to play any sound effect
    private void playSound(String soundName) {
        if (!initialized) return;

        Integer soundId = soundMap.get(soundName);
        if (soundId != null) {
            soundPool.play(soundId, 10.0f, 10.0f, 1, 0, 1.0f);
        }
    }

    // Clean up resources
    public void cleanup() {
        if (!initialized) return;

        // Release MediaPlayers
        if (bgMusicPlayer != null) {
            if (bgMusicPlayer.isPlaying()) {
                bgMusicPlayer.stop();
            }
            bgMusicPlayer.release();
            bgMusicPlayer = null;
        }

        if (minigameMusicPlayer != null) {
            if (minigameMusicPlayer.isPlaying()) {
                minigameMusicPlayer.stop();
            }
            minigameMusicPlayer.release();
            minigameMusicPlayer = null;
        }

        if (mainAppMusicPlayer != null) {
            if (mainAppMusicPlayer.isPlaying()) {
                mainAppMusicPlayer.stop();
            }
            mainAppMusicPlayer.release();
            mainAppMusicPlayer = null;
        }

        // Release SoundPool
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        initialized = false;
    }

    // Pause all music (for app lifecycle events)
// Update the pauseAll method to track state
    public void pauseAll() {
        if (!initialized) return;

        if (bgMusicPlayer != null) {
            bgMusicWasPlaying = bgMusicPlayer.isPlaying();
            if (bgMusicWasPlaying) {
                bgMusicPlayer.pause();
            }
        }

        if (minigameMusicPlayer != null) {
            minigameMusicWasPlaying = minigameMusicPlayer.isPlaying();
            if (minigameMusicWasPlaying) {
                minigameMusicPlayer.pause();
            }
        }

        if (mainAppMusicPlayer != null) {
            mainAppMusicWasPlaying = mainAppMusicPlayer.isPlaying();
            if (mainAppMusicWasPlaying) {
                mainAppMusicPlayer.pause();
            }
        }
    }

    // Only resume tracks that were playing before
    public void resumeAll() {
        if (!initialized) return;

        if (bgMusicPlayer != null && bgMusicWasPlaying) {
            bgMusicPlayer.start();
            bgMusicWasPlaying = false; // Reset flag after resuming
        }

        if (minigameMusicPlayer != null && minigameMusicWasPlaying) {
            minigameMusicPlayer.start();
            minigameMusicWasPlaying = false; // Reset flag after resuming
        }

        if (mainAppMusicPlayer != null && mainAppMusicWasPlaying) {
            mainAppMusicPlayer.start();
            mainAppMusicWasPlaying = false; // Reset flag after resuming
        }
    }

    public void playMainGameGOSounds(){
        playSound(MAINGAME_GAME_OVER);

        stopAllMusic();
    }

    public void playMiniGameGOSounds(){
        playSound(MINIGAME_GAME_OVER);

        stopAllMusic();
    }
}