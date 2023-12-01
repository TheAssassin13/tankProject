package com.example.tankproject;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class ImagesLoader {
    public static ImagesLoader instance;
    private ArrayList<Image> shotImages;
    private ArrayList<Image> backgroundImages;
    public Image currentTankImage;
    public Image currentTankKillsImage;
    public Image currenkTankScoreboardImage;
    public ArrayList<Image> heartIconImages;
    public ArrayList<Image> iconImages;
    public Image umbrellaImage;
    public Image witchHatImage;
    public Image mysteryBoxImage;
    public ArrayList<Image> currentBackgrounds;
    public ArrayList<Image> currentShotImages;

    public ImagesLoader() {
        shotImagesLoader();
        backgroundImagesLoader();
        interfaceImagesLoader();
        iconImagesLoader();
        gameImagesLoader();
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
        this.witchHatImage = new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/hat.png")).toExternalForm());
        this.mysteryBoxImage = new Image(Objects.requireNonNull(getClass().getResource("images/mystery_box.png")).toExternalForm());
    }

    // This method returns the theme icon corresponding to the current selected theme
    public Image getThemeIcon() {
        if (Data.getInstance().themeSelected == 0) return ImagesLoader.getInstance().iconImages.get(6);
        if (Data.getInstance().themeSelected == 1) return ImagesLoader.getInstance().iconImages.get(7);
        if (Data.getInstance().themeSelected == 2) return ImagesLoader.getInstance().iconImages.get(8);
        return null;
    }

    // This method changes between normal, Halloween and Christmas themes
    public void changeTheme() {
        // Normal
        if (Data.getInstance().themeSelected == 0) {
            this.currentBackgrounds.set(0, this.backgroundImages.get(0));
            this.currentBackgrounds.set(1, this.backgroundImages.get(1));
            this.currentShotImages.set(0, this.shotImages.get(0));
            this.currentShotImages.set(1, this.shotImages.get(1));
            this.currentShotImages.set(2, this.shotImages.get(2));
            return;
        }
        // Halloween
        if (Data.getInstance().themeSelected == 1) {
            this.currentBackgrounds.set(0, this.backgroundImages.get(3));
            this.currentBackgrounds.set(1, this.backgroundImages.get(4));
            this.currentShotImages.set(0, this.shotImages.get(3));
            this.currentShotImages.set(1, this.shotImages.get(4));
            this.currentShotImages.set(2, this.shotImages.get(5));
            return;
        }
        // Christmas
        if (Data.getInstance().themeSelected == 2) {

        }
    }

    public static synchronized ImagesLoader getInstance() {
        if (instance == null) {
            instance = new ImagesLoader();
        }
        return instance;
    }
}
