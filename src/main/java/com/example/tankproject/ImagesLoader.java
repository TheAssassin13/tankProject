package com.example.tankproject;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Objects;

public class ImagesLoader {
    public ArrayList<Image> shotImages;
    public ArrayList<Image> backgroundImages;
    public Image currentTankImage;
    public Image winnerTankImage;
    public ArrayList<Image> heartIconImages;
    public ArrayList<Image> iconImages;
    public Image umbrellaImage;
    public Image witchHatImage;
    public Image mysteryBoxImage;
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
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/light_shot(halloween).png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/medium_shot(halloween).png")).toExternalForm()));
        this.shotImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/other_heavy_shot(halloween).png")).toExternalForm()));
    }

    // This method loads and saves the background images in an ArrayList
    public void backgroundImagesLoader() {
        this.backgroundImages = new ArrayList<>();
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/menu_background_image.png")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/game_background_image.jpg")).toExternalForm()));
        this.backgroundImages.add(new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/background_halloween.jpg")).toExternalForm()));
    }

    // This method loads and saves all the images related to the interface
    public void interfaceImagesLoader() {
        this.currentTankImage = new Image(Objects.requireNonNull(getClass().getResource("images/current_tank_image.png")).toExternalForm());
        this.winnerTankImage = new Image(Objects.requireNonNull(getClass().getResource("images/winner_tank_image.png")).toExternalForm());
        this.heartIconImages = new ArrayList<>();
        this.heartIconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/empty_heart_icon.png")).toExternalForm()));
        this.heartIconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/half_heart_icon.png")).toExternalForm()));
        this.heartIconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm()));
    }

    // This method loads and saves the icon images in an ArrayList
    public void iconImagesLoader() {
        this.iconImages = new ArrayList<>();
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/replay_icon_white.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/menu_exit_icon_white.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/exit_icon_white.png")).toExternalForm()));
        this.iconImages.add(new Image(Objects.requireNonNull(getClass().getResource("icons/windows_icon.png")).toExternalForm()));
    }

    // This method loads and saves all the game related images
    public void gameImagesLoader() {
        this.umbrellaImage = new Image(Objects.requireNonNull(getClass().getResource("images/umbrella.png")).toExternalForm());
        this.witchHatImage = new Image(Objects.requireNonNull(getClass().getResource("images/halloween_additions/hat.png")).toExternalForm());
        this.mysteryBoxImage = new Image(Objects.requireNonNull(getClass().getResource("images/mystery_box.png")).toExternalForm());
    }
}
