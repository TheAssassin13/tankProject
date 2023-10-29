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
    public ToggleButton easyButton;
    public ToggleButton mediumButton;
    public ToggleButton hardButton;
    public ToggleGroup CPUDifficultyButtons;

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
        musicVolumeDragInitialize();
        difficultyButtonsAlwaysSelected();
    }

    // Opens game windows
    public void onPlayButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        this.mediaPlayer.stop();
        App.setRoot("interlude");
    }

    // Close windows
    public void onExitButtonClick(ActionEvent ignoredActionEvent) {
        Platform.exit();
    }

    // When the options button is clicked, the options menu appears or disappears
    public void onOptionsButtonClick(ActionEvent ignoredActionEvent) {
        if (this.optionsMenu.isVisible()) {
            this.optionsMenu.setDisable(true);
            this.optionsMenu.setVisible(false);
            return;
        }
        this.optionsMenu.setDisable(false);
        this.optionsMenu.setVisible(true);
        SpinnerValueFactory<Integer> valueFactory = tanksQuantitySpinner.getValueFactory();
        valueFactory.setValue(Constants.TANKS_QUANTITY);
        this.musicVolumeSlider.adjustValue(Constants.MUSIC_VOLUME * 100);
        this.sfxVolumeSlider.adjustValue(Constants.SFX_VOLUME * 100);
        this.resolutionSpinner.getValueFactory().setValue(Constants.WINDOWS_WIDTH + " x " + Constants.WINDOWS_HEIGHT);
        if (Constants.CPU_DIFFICULTY == 1) {
            this.CPUDifficultyButtons.selectToggle(easyButton);
        } else if (Constants.CPU_DIFFICULTY == 2) {
            this.CPUDifficultyButtons.selectToggle(mediumButton);
        } else if (Constants.CPU_DIFFICULTY == 3) {
            this.CPUDifficultyButtons.selectToggle(hardButton);
        }
    }

    // Saves user changes in the options menu and applies them
    public void onSaveButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        this.optionsMenu.setDisable(true);
        this.optionsMenu.setVisible(false);

        // User changed screen resolution
        if (Constants.RESOLUTION_HEIGHT[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())] != Constants.WINDOWS_HEIGHT || Constants.RESOLUTION_WIDTH[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())] != Constants.WINDOWS_WIDTH) {
            Constants.WINDOWS_HEIGHT = Constants.RESOLUTION_HEIGHT[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())];
            Constants.WINDOWS_WIDTH = Constants.RESOLUTION_WIDTH[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())];
            // Closes the actual windows and opens a new one with the resolution selected by user
            this.mediaPlayer.stop();
            App.restartWindow();
            App.updateScreenResolutionConstants();
        }
        Constants.TANKS_QUANTITY = this.tanksQuantitySpinner.getValue();
        if (easyButton.isSelected()) Constants.CPU_DIFFICULTY = 1;
        else if (mediumButton.isSelected()) Constants.CPU_DIFFICULTY = 2;
        else if (hardButton.isSelected()) Constants.CPU_DIFFICULTY = 3;
    }

    // Initializes the resolution spinner, ArrayList and HashMap
    public void resolutionSpinnerInitialize() {
        this.resolutionsHashMap = new HashMap<>();
        this.resolutionsString = new ArrayList<>();

        for (int i = 0; i < Constants.RESOLUTION_WIDTH.length; i++) {
            this.resolutionsString.add(Constants.RESOLUTION_WIDTH[i] + " x " + Constants.RESOLUTION_HEIGHT[i]);
            this.resolutionsHashMap.put(Constants.RESOLUTION_WIDTH[i] + " x " + Constants.RESOLUTION_HEIGHT[i],i);
        }

        ObservableList<String> observableArrayList = FXCollections.observableArrayList(this.resolutionsString);
        this.resolutionSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(observableArrayList));
    }

    // Sets the sfx game volume and play a sound for reference
    public void onSFXVolumeDrag(MouseEvent ignoredMouseEvent) {
        Constants.SFX_VOLUME = sfxVolumeSlider.getValue()/100;
        MediaPlayer sound = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/powerup.mp3")).toExternalForm()));
        sound.setVolume(Constants.SFX_VOLUME);
        sound.play();
    }

    // Sets the music game volume
    public void musicVolumeDragInitialize() {
        this.musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Constants.MUSIC_VOLUME = newValue.doubleValue() / 100;
            mediaPlayer.setVolume(0.5 * Constants.MUSIC_VOLUME);
        });
    }

    // Verifies that is always a difficulty button selected in the options menu
    public void difficultyButtonsAlwaysSelected() {
        this.CPUDifficultyButtons.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == null) {
                // If no button is selected, selects the last one
                this.CPUDifficultyButtons.selectToggle(oldToggle);
            }
        });
    }
}
