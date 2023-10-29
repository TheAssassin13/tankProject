package com.example.tankproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterludeController implements Initializable {
    public VBox vbox;
    public Label roundTitle;
    public Button playButton;
    public StackPane stackpane;
    public ImageView backgroundImage;
    public Button shopButton;
    public VBox containerVBox;
    public Button scoreboardButton;
    public Button menuButton;
    public ImagesLoader imagesLoader;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.imagesLoader = new ImagesLoader();
        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/interlude_background_image.png")).toExternalForm()));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.containerVBox.getChildren().addAll(ComponentsCreator.createShopVBox(new Tank(Color.WHITE,null)));
        this.containerVBox.setDisable(true);
        this.containerVBox.setVisible(false);
        this.roundTitle.setText("ROUND " + Data.getInstance().roundNumber);
    }

    // Opens game windows
    public void onPlayButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        App.setRoot("game");
    }

    // Opens menu screen
    public void onMenuButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        App.setRoot("menu");
    }

    // When the options button is clicked, the shop appears or disappears
    public void onShopButtonClick(ActionEvent ignoredActionEvent) {
        if (this.containerVBox.isVisible()) {

            this.containerVBox.setDisable(true);
            this.containerVBox.setVisible(false);
            return;
        }
        this.containerVBox.getChildren().clear();
        this.containerVBox.getChildren().addAll(ComponentsCreator.createShopVBox(new Tank(null,null)));
        this.containerVBox.setDisable(false);
        this.containerVBox.setVisible(true);

    }


    public void onScoreboardButtonClick(ActionEvent ignoredEvent) {
    }
}
