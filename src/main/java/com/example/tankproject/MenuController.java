package com.example.tankproject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public VBox vbox;
    public Label titleMenu;
    public Button playButton;
    public Button playCPUButton;
    public Button exitButton;
    public StackPane stackpane;
    public ImageView backgroundImage;
    public Button optionsButton;
    public VBox optionsMenu;
    public Media backgroundMusic;
    public MediaPlayer mediaPlayer;
    public HashMap<String, Integer> resolutionsHashMap;
    public ArrayList<String> resolutionsString;
    public Spinner<String> resolutionSpinner;
    public Spinner<Integer> tanksQuantitySpinner;
    public Slider sfxVolumeSlider;
    public Slider musicVolumeSlider;
    public static int selectedResolution = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/menu_background_image.png")).toExternalForm()));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.optionsMenu.setDisable(true);
        this.optionsMenu.setVisible(false);
        this.backgroundMusic = new Media(Objects.requireNonNull(getClass().getResource("music/menuMusic.mp3")).toExternalForm());
        this.mediaPlayer = new MediaPlayer(backgroundMusic);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.mediaPlayer.play();
        this.mediaPlayer.setVolume(0.5 * Constants.MUSIC_VOLUME);
        resolutionSpinnerInitialize();
    }

    public void onPlayButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        this.mediaPlayer.stop();
        App.setRoot("game");
    }

    public void onPlayCPUButtonClick(ActionEvent ignoredEvent) throws IOException {
        Constants.CPU = true;
        this.mediaPlayer.stop();
        App.setRoot("game");
    }

    public void onExitButtonClick(ActionEvent ignoredActionEvent) {
        Platform.exit();
    }

    // When the options button is clicked, the options menu appears or disappears
    public void onOptionsButtonClick(ActionEvent ignoredActionEvent) {
        if (this.optionsMenu.isVisible()) {
            this.optionsMenu.setDisable(true);
            this.optionsMenu.setVisible(false);
        } else {
            this.optionsMenu.setDisable(false);
            this.optionsMenu.setVisible(true);
            SpinnerValueFactory<Integer> valueFactory = tanksQuantitySpinner.getValueFactory();
            valueFactory.setValue(Constants.TANKS_QUANTITY);
            this.musicVolumeSlider.adjustValue(Constants.MUSIC_VOLUME * 100);
            this.sfxVolumeSlider.adjustValue(Constants.SFX_VOLUME * 100);
        }
    }

    public void onSaveButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        this.optionsMenu.setDisable(true);
        this.optionsMenu.setVisible(false);

        // User changed screen resolution
        if (Constants.RESOLUTION_HEIGHT[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())] != Constants.WINDOWS_HEIGHT && Constants.RESOLUTION_WIDTH[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())] != Constants.WINDOWS_WIDTH) {
            Constants.WINDOWS_HEIGHT = Constants.RESOLUTION_HEIGHT[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())];
            Constants.WINDOWS_WIDTH = Constants.RESOLUTION_WIDTH[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())];
            selectedResolution = this.resolutionsHashMap.get(this.resolutionSpinner.getValue());
            // Closes the actual windows and opens a new one with the resolution selected by user
            this.mediaPlayer.stop();
            App.restartWindow();
            App.updateScreenResolutionConstants();
        }

        Constants.TANKS_QUANTITY = this.tanksQuantitySpinner.getValue();
    }

    // Initializes the resolution spinner, ArrayList and HashMap
    public void resolutionSpinnerInitialize() {
        this.resolutionsHashMap = new HashMap<>();
        this.resolutionsString = new ArrayList<>();
        this.resolutionsHashMap.put("1000 x 700",0);
        this.resolutionsHashMap.put("1280 x 720",1);
        this.resolutionsHashMap.put("1366 x 768",2);
        this.resolutionsString.add("1000 x 700");
        this.resolutionsString.add("1280 x 720");
        this.resolutionsString.add("1366 x 768");

        ObservableList<String> observableArrayList = FXCollections.observableArrayList(this.resolutionsString);
        this.resolutionSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(observableArrayList));
        this.resolutionSpinner.getValueFactory().setValue(this.resolutionsString.get(selectedResolution));
    }

    public void onMusicVolumeDrag(MouseEvent mouseEvent) {
        Constants.MUSIC_VOLUME = musicVolumeSlider.getValue()/100;
        mediaPlayer.setVolume(0.5 * Constants.MUSIC_VOLUME);
    }

    public void onSFXVolumeDrag(MouseEvent mouseEvent) {
        Constants.SFX_VOLUME = sfxVolumeSlider.getValue()/100;
        MediaPlayer sound = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/powerup.mp3")).toExternalForm()));
        sound.setVolume(Constants.SFX_VOLUME);
        sound.play();
    }
}
