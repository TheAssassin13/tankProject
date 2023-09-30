package com.example.tankproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/MenuBackground.png")).toExternalForm()));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
    }

    public void onStartButtonClick(ActionEvent actionEvent) throws IOException {
        App.setRoot("game");
    }

    public void onExitButtonClick(ActionEvent actionEvent) {
        Platform.exit();
    }
}
