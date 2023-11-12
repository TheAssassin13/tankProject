package com.example.tankproject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public Button exitButton;
    public StackPane stackpane;
    public ImageView backgroundImage;
    public Button optionsButton;
    public VBox appOptionsVBox;
    public Media backgroundMusic;
    public MediaPlayer mediaPlayer;
    public HashMap<String, Integer> resolutionsHashMap;
    public ArrayList<String> resolutionsString;
    public Spinner<String> resolutionSpinner;
    public Spinner<Integer> playersQuantitySpinner;
    public Slider sfxVolumeSlider;
    public Slider musicVolumeSlider;
    public ToggleButton easyButton;
    public ToggleButton mediumButton;
    public ToggleButton hardButton;
    public ToggleGroup CPUDifficultyButtons;
    public VBox gameOptionsVBox;
    public VBox optionsVBox;
    public Spinner<Integer> CPUQuantitySpinner;
    public Spinner<Double> gravityAmountSpinner;
    public CheckBox windCheckBox;
    public Spinner<Integer> gamesQuantitySpinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.backgroundImage.setImage(ImagesLoader.getInstance().backgroundImages.get(0));
        this.backgroundImage.setFitHeight(Data.getInstance().windowsHeight);
        this.backgroundImage.setFitWidth(Data.getInstance().windowsWidth);
        this.optionsVBox.setDisable(true);
        this.optionsVBox.setVisible(false);
        this.appOptionsVBox.setDisable(true);
        this.appOptionsVBox.setVisible(false);
        this.gameOptionsVBox.setDisable(false);
        this.gameOptionsVBox.setVisible(true);
        this.backgroundMusic = new Media(Objects.requireNonNull(getClass().getResource("music/menuMusic.mp3")).toExternalForm());
        this.mediaPlayer = new MediaPlayer(backgroundMusic);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.mediaPlayer.play();
        this.mediaPlayer.setVolume(Data.getInstance().musicVolume);
        resolutionSpinnerInitialize();
        musicVolumeDragInitialize();
        difficultyButtonsAlwaysSelected();
        Data.getInstance().reset();
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
        if (this.optionsVBox.isVisible()) {
            this.optionsVBox.setDisable(true);
            this.optionsVBox.setVisible(false);
            return;
        }

        onGameOptionsButtonClick(new ActionEvent());
        this.optionsVBox.setDisable(false);
        this.optionsVBox.setVisible(true);
    }

    // Saves user changes in the options menu and applies them
    public void onSaveButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        this.optionsVBox.setDisable(true);
        this.optionsVBox.setVisible(false);

        // User changed screen resolution
        if (Constants.RESOLUTION_HEIGHT[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())] != Data.getInstance().windowsHeight || Constants.RESOLUTION_WIDTH[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())] != Data.getInstance().windowsWidth) {
            Data.getInstance().windowsHeight = Constants.RESOLUTION_HEIGHT[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())];
            Data.getInstance().windowsWidth = Constants.RESOLUTION_WIDTH[this.resolutionsHashMap.get(this.resolutionSpinner.getValue())];
            // Closes the actual windows and opens a new one with the resolution selected by user
            this.mediaPlayer.stop();
            App.restartWindow();
            App.updateScreenResolutionVariables();
        }
        if (easyButton.isSelected()) Data.getInstance().CPUDifficulty = 1;
        else if (mediumButton.isSelected()) Data.getInstance().CPUDifficulty = 2;
        else if (hardButton.isSelected()) Data.getInstance().CPUDifficulty = 3;

        Data.getInstance().updatesTanksQuantity(playersQuantitySpinner.getValue(), CPUQuantitySpinner.getValue());
        Data.getInstance().gamesMax = this.gamesQuantitySpinner.getValue();
        Data.getInstance().gravity = this.gravityAmountSpinner.getValue();
        Data.getInstance().wind = this.windCheckBox.isSelected();
    }

    // Initializes the resolution spinner, ArrayList and HashMap
    public void resolutionSpinnerInitialize() {
        this.resolutionsHashMap = new HashMap<>();
        this.resolutionsString = new ArrayList<>();

        for (int i = 0; i < Constants.RESOLUTION_WIDTH.length; i++) {
            this.resolutionsString.add(Constants.RESOLUTION_WIDTH[i] + " x " + Constants.RESOLUTION_HEIGHT[i]);
            this.resolutionsHashMap.put(Constants.RESOLUTION_WIDTH[i] + " x " + Constants.RESOLUTION_HEIGHT[i], i);
        }

        ObservableList<String> observableArrayList = FXCollections.observableArrayList(this.resolutionsString);
        this.resolutionSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(observableArrayList));
        this.resolutionSpinner.getValueFactory().setValue(Data.getInstance().windowsWidth + " x " + Data.getInstance().windowsHeight);
    }

    // Sets the sfx game volume and play a sound for reference
    public void onSFXVolumeDrag(MouseEvent ignoredMouseEvent) {
        Data.getInstance().SFXVolume = sfxVolumeSlider.getValue() / 100 * Constants.MAX_VOLUME;
        MediaPlayer sound = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/powerup.mp3")).toExternalForm()));
        sound.setVolume(Data.getInstance().SFXVolume);
        sound.play();
    }

    // Sets the music game volume
    public void musicVolumeDragInitialize() {
        this.musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Data.getInstance().musicVolume = newValue.doubleValue() / 100 * Constants.MAX_VOLUME;
            mediaPlayer.setVolume(Data.getInstance().musicVolume);
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

    // When the game options button is clicked, the game options are displayed
    public void onGameOptionsButtonClick(ActionEvent ignoredEvent) {
        this.gameOptionsVBox.setDisable(false);
        this.gameOptionsVBox.setVisible(true);
        this.appOptionsVBox.setDisable(true);
        this.appOptionsVBox.setVisible(false);

        this.playersQuantitySpinner.getValueFactory().setValue(Data.getInstance().playableTanksQuantity);
        this.CPUQuantitySpinner.getValueFactory().setValue(Data.getInstance().cpuTanksQuantity);
        this.gamesQuantitySpinner.getValueFactory().setValue(Data.getInstance().gamesMax);
        this.gravityAmountSpinner.getValueFactory().setValue(Data.getInstance().gravity);
        this.windCheckBox.setSelected(Data.getInstance().wind);

        if (Data.getInstance().CPUDifficulty == 1) {
            this.CPUDifficultyButtons.selectToggle(easyButton);
        } else if (Data.getInstance().CPUDifficulty == 2) {
            this.CPUDifficultyButtons.selectToggle(mediumButton);
        } else if (Data.getInstance().CPUDifficulty == 3) {
            this.CPUDifficultyButtons.selectToggle(hardButton);
        }
    }

    // When the app options button is clicked, the app options are displayed
    public void onAppOptionsButtonClick(ActionEvent ignoredEvent) {
        this.appOptionsVBox.setDisable(false);
        this.appOptionsVBox.setVisible(true);
        this.gameOptionsVBox.setDisable(true);
        this.gameOptionsVBox.setVisible(false);

        this.musicVolumeSlider.adjustValue(Data.getInstance().musicVolume * 100);
        this.sfxVolumeSlider.adjustValue(Data.getInstance().SFXVolume * 100);
        this.resolutionSpinner.getValueFactory().setValue(Data.getInstance().windowsWidth + " x " + Data.getInstance().windowsHeight);

    }

    public void onPlayerSpinnerClick(MouseEvent ignoredMouseEvent) {
        int min = Math.max(0, 2 - playersQuantitySpinner.getValue());
        int max = 10 - playersQuantitySpinner.getValue();
        CPUQuantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, Math.min(max, CPUQuantitySpinner.getValue())));
    }

    public void onCPUSpinnerClick(MouseEvent ignoredMouseEvent) {
        int min = Math.max(0, 2 - CPUQuantitySpinner.getValue());
        int max = 10 - CPUQuantitySpinner.getValue();
        playersQuantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, Math.min(max, playersQuantitySpinner.getValue())));
    }

}
