package com.example.tankproject;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicPlayer {
    private Clip clip;

    public MusicPlayer(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            } else {
                System.err.println("It wasn't possible to load the music.");
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Reproduces in a loop
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}

