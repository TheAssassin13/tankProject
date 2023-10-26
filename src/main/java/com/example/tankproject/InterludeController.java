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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterludeController implements Initializable {
    public VBox vbox;
    public Label titleMenu;
    public Button playButton;
    public StackPane stackpane;
    public ImageView backgroundImage;
    public Button shopButton;
    public VBox shopVBox;
    public HashMap<String, Integer> resolutionsHashMap;
    public ArrayList<String> resolutionsString;
    public Spinner<String> resolutionSpinner;
    public Button scoreboardButton;
    public Button menuButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/interlude_background_image.png")).toExternalForm()));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.shopVBox.setDisable(true);
        this.shopVBox.setVisible(false);
    }

    // Opens game windows
    public void onPlayButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        App.setRoot("game");
    }

    // Opens menu screen
    public void onMenuButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        App.setRoot("menu");

    }

    // When the options button is clicked, the options menu appears or disappears
    public void onShopButtonClick(ActionEvent ignoredActionEvent) {
        if (this.shopVBox.isVisible()) {
            this.shopVBox.setDisable(true);
            this.shopVBox.setVisible(false);
            return;
        }
        this.shopVBox.setDisable(false);
        this.shopVBox.setVisible(true);

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

    public void onScoreboardButtonClick(ActionEvent ignoredEvent) {
    }
}
