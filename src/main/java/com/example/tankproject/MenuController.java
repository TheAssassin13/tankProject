package com.example.tankproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public VBox vbox;
    public Label titleMenu;
    public Button startMenuButton;
    public Button exitMenuButton;
    public StackPane stackpane;
    public ImageView backgroundImage;
    public Button optionsButton;
    public VBox optionsMenu;
    public Label resolutionOption;
    public TextField tanksQuantityField;
    public MusicPlayer backgroundMusic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/menu_background_image.png")).toExternalForm()));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.resolutionOption.setText(Constants.WINDOWS_WIDTH + " X " + Constants.WINDOWS_HEIGHT);
        this.tanksQuantityField.setText(String.valueOf(Constants.TANKS_QUANTITY));
        this.optionsMenu.setDisable(true);
        this.optionsMenu.setVisible(false);
        this.backgroundMusic = new MusicPlayer("music/menuMusic.wav");
        backgroundMusic.play();
    }

    public void onStartButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        this.backgroundMusic.stop();
        this.backgroundMusic = new MusicPlayer("music/gameMusic.wav");
        this.backgroundMusic.play();
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
        }
    }

    public void onSaveButtonClick(ActionEvent ignoredActionEvent) {
        this.optionsMenu.setDisable(true);
        this.optionsMenu.setVisible(false);
    }
}
