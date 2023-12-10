package com.example.tankproject;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.util.ArrayList;
import java.util.Objects;

public class Loader {
    public static Loader instance;
    private ArrayList<Image> shotImages;
    private ArrayList<Image> backgroundImages;
    public Image currentTankImage;
    public Image currentTankKillsImage;
    public Image currenkTankScoreboardImage;
    public ArrayList<Image> heartIconImages;
    public ArrayList<Image> iconImages;
    public Image umbrellaImage;
    public Image hatImage;
    public Image mysteryBoxImage;
    public ArrayList<Image> currentBackgrounds;
    public ArrayList<Image> currentShotImages;
    private ArrayList<Media> backgroundMusic;
    private ArrayList<Media> soundEffects;
    public ArrayList<Media> currentBackgroundMusic;
    public ArrayList<Media> currentSoundEffects;

    public Loader() {
        shotImagesLoader();
        backgroundImagesLoader();
        interfaceImagesLoader();
        iconImagesLoader();
        gameImagesLoader();
        backgroundMusicLoader();
        SFXLoader();
    }

    // This method loads and saves the shot images in an ArrayList
    public void shotImagesLoader() {
        this.shotImages = new ArrayList<>();
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/light_shot.png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/medium_shot.png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/heavy_shot.png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/light_shot(halloween).png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/medium_shot(halloween).png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/other_heavy_shot(halloween).png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/christmas_additions/light_shot_christmas.png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/christmas_additions/medium_shot_christmas.png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/christmas_additions/heavy_shot_christmas.png")).toExternalForm()));

        this.currentShotImages = new ArrayList<>();
        this.currentShotImages.add(this.shotImages.get(0));
        this.currentShotImages.add(this.shotImages.get(1));
        this.currentShotImages.add(this.shotImages.get(2));
    }

    // This method loads and saves the background images in an ArrayList
    public void backgroundImagesLoader() {
        this.backgroundImages = new ArrayList<>();
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/menu_background_image.png")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/game_background_image.jpg")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/interlude_background_image.png")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/menu_background_image.png")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/background_halloween.jpg")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/interlude_background_image.png")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/christmas_additions/menu_background_image.png")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/christmas_additions/game_background_image.png")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/christmas_additions/interlude_background_image.png")).toExternalForm()));

        this.currentBackgrounds = new ArrayList<>();
        this.currentBackgrounds.add(this.backgroundImages.get(0));
        this.currentBackgrounds.add(this.backgroundImages.get(1));
        this.currentBackgrounds.add(this.backgroundImages.get(2));
    }

    // This method loads and saves all the images related to the interface
    public void interfaceImagesLoader() {
        this.currentTankImage = new Image(Objects.requireNonNull(getClass().getResource("images/current_tank_image.png")).toExternalForm());
        this.currenkTankScoreboardImage = new Image(Objects.requireNonNull(getClass().getResource("images/current_tank_image_scoreboard.png")).toExternalForm());
        this.heartIconImages = new ArrayList<>();
        this.heartIconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/empty_heart_icon.png")).toExternalForm()));
        this.heartIconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/half_heart_icon.png")).toExternalForm()));
        this.heartIconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm()));
        this.currentTankKillsImage = new Image(Objects.requireNonNull(getClass().getResource("icons/kills_icon.png")).toExternalForm());
    }

    // This method loads and saves the icon images in an ArrayList
    public void iconImagesLoader() {
        this.iconImages = new ArrayList<>();
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/replay_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/menu_exit_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/exit_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/windows_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/arrow2_left_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/arrow2_right_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/normal_theme_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/halloween_theme_icon.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/christmas_theme_icon.png")).toExternalForm()));
    }

    // This method loads and saves all the game related images
    public void gameImagesLoader() {
        this.umbrellaImage = new Image(Objects.requireNonNull(getClass().getResource("images/umbrella.png")).toExternalForm());
        this.hatImage = new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/hat.png")).toExternalForm());
        this.mysteryBoxImage = new Image(Objects.requireNonNull(getClass().getResource("images/mystery_box.png")).toExternalForm());
    }

    // This method loads and saves all the background music
    public void backgroundMusicLoader() {
        this.backgroundMusic = new ArrayList<>();
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/menuMusic.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/gameMusic.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/interludeMusic.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/menuMusicHalloween.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/gameMusicHalloween.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/interludeMusicHalloween.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/menuMusicChristmas.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/gameMusicChristmas.mp3")).toExternalForm()));
        this.backgroundMusic.add(new Media(Objects.requireNonNull(getClass().getResource("music/interludeMusicChristmas.mp3")).toExternalForm()));

        this.currentBackgroundMusic = new ArrayList<>();
        this.currentBackgroundMusic.add(this.backgroundMusic.get(0));
        this.currentBackgroundMusic.add(this.backgroundMusic.get(1));
        this.currentBackgroundMusic.add(this.backgroundMusic.get(2));
    }

    // This method loads and saves all the sound effects
    public void SFXLoader() {
        this.soundEffects = new ArrayList<>();
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/boom.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/explosionTank.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/powerup.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/victory.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/explosionTankDeath.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/halloween_sounds/victory_laugh.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/halloween_sounds/ghost_collision.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/christmas_sounds/victory_hohoho.mp3")).toExternalForm()));
        this.soundEffects.add(new Media(Objects.requireNonNull(getClass().getResource("sounds/christmas_sounds/sparky_death.mp3")).toExternalForm()));

        this.currentSoundEffects = new ArrayList<>();
        this.currentSoundEffects.add(this.soundEffects.get(0));
        this.currentSoundEffects.add(this.soundEffects.get(1));
        this.currentSoundEffects.add(this.soundEffects.get(2));
        this.currentSoundEffects.add(this.soundEffects.get(3));
        this.currentSoundEffects.add(this.soundEffects.get(4));
    }

    // This method returns the theme icon corresponding to the current selected theme
    public Image getThemeIcon() {
        if (Data.getInstance().themeSelected == 0) return Loader.getInstance().iconImages.get(6);
        if (Data.getInstance().themeSelected == 1) return Loader.getInstance().iconImages.get(7);
        if (Data.getInstance().themeSelected == 2) return Loader.getInstance().iconImages.get(8);
        return null;
    }

    // This method changes between normal, Halloween and Christmas themes
    public void changeTheme() {
        // Normal
        if (Data.getInstance().themeSelected == 0) {
            this.currentBackgrounds.set(0, this.backgroundImages.get(0));
            this.currentBackgrounds.set(1, this.backgroundImages.get(1));
            this.currentBackgrounds.set(2, this.backgroundImages.get(2));
            this.currentShotImages.set(0, this.shotImages.get(0));
            this.currentShotImages.set(1, this.shotImages.get(1));
            this.currentShotImages.set(2, this.shotImages.get(2));

            this.currentBackgroundMusic.set(0, this.backgroundMusic.get(0));
            this.currentBackgroundMusic.set(1, this.backgroundMusic.get(1));
            this.currentBackgroundMusic.set(2, this.backgroundMusic.get(2));
            this.currentSoundEffects.set(3, this.soundEffects.get(3));
            this.currentSoundEffects.set(4, this.soundEffects.get(4));
            return;
        }
        // Halloween
        if (Data.getInstance().themeSelected == 1) {
            this.currentBackgrounds.set(0, this.backgroundImages.get(3));
            this.currentBackgrounds.set(1, this.backgroundImages.get(4));
            this.currentBackgrounds.set(2, this.backgroundImages.get(5));
            this.currentShotImages.set(0, this.shotImages.get(3));
            this.currentShotImages.set(1, this.shotImages.get(4));
            this.currentShotImages.set(2, this.shotImages.get(5));

            this.currentBackgroundMusic.set(0, this.backgroundMusic.get(3));
            this.currentBackgroundMusic.set(1, this.backgroundMusic.get(4));
            this.currentBackgroundMusic.set(2, this.backgroundMusic.get(5));
            this.currentSoundEffects.set(3, this.soundEffects.get(5));
            this.currentSoundEffects.set(4, this.soundEffects.get(6));
            this.hatImage = new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/hat.png")).toExternalForm());
            return;
        }
        // Christmas
        if (Data.getInstance().themeSelected == 2) {
            this.currentBackgrounds.set(0, this.backgroundImages.get(6));
            this.currentBackgrounds.set(1, this.backgroundImages.get(7));
            this.currentBackgrounds.set(2, this.backgroundImages.get(8));
            this.currentShotImages.set(0, this.shotImages.get(6));
            this.currentShotImages.set(1, this.shotImages.get(7));
            this.currentShotImages.set(2, this.shotImages.get(8));

            this.currentBackgroundMusic.set(0, this.backgroundMusic.get(6));
            this.currentBackgroundMusic.set(1, this.backgroundMusic.get(7));
            this.currentBackgroundMusic.set(2, this.backgroundMusic.get(8));
            this.currentSoundEffects.set(3, this.soundEffects.get(7));
            this.currentSoundEffects.set(4, this.soundEffects.get(8));
            this.hatImage = new Image(Objects.requireNonNull(getClass().getResource("images/christmas_additions/hat.png")).toExternalForm());
            return;
        }
    }

    public static synchronized Loader getInstance() {
        if (instance == null) {
            instance = new Loader();
        }
        return instance;
    }
}
