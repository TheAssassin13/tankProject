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
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class InterludeController implements Initializable {
    public VBox vbox;
    public Text roundTitle;
    public Button playButton;
    public StackPane stackpane;
    public ImageView backgroundImage;
    public Button shopButton;
    public VBox containerVBox;
    public Button scoreboardButton;
    public Button menuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Data.getInstance().roundNumber == 1) {
            Data.getInstance().reset();
            createPlayers();
        }
        this.backgroundImage.setImage(ImagesLoader.getInstance().backgroundImages.get(2));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.containerVBox.getChildren().addAll(ComponentsCreator.createShopVBox());
        this.containerVBox.setDisable(true);
        this.containerVBox.setVisible(false);
        this.roundTitle.setText("Round " + Data.getInstance().roundNumber);
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
        this.containerVBox.getChildren().addAll(ComponentsCreator.createShopVBox());
        this.containerVBox.setDisable(false);
        this.containerVBox.setVisible(true);

    }


    public void onScoreboardButtonClick(ActionEvent ignoredEvent) {
    }

    // This method loads the players and saves them into the alivePlayers arrayList
    public void createPlayers() {
        Random random = new Random();
        int playersQuantity = Data.getInstance().playableTanksQuantity;
        int cpuQuantity = Data.getInstance().cpuTanksQuantity;

        // It loads the playable players
        for (int i = 0; i < playersQuantity; i++) {
            Data.getInstance().alivePlayers.add(new Player("Player " + (i+1), Constants.TANK_COLORS[i], new Tank(Constants.TANK_COLORS[i], new Point(0, 0))));
        }

        // It loads the CPU players
        for (int i = 0; i < cpuQuantity; i++) {
            Data.getInstance().alivePlayers.add(new CPU("CPU " + (i+1), Constants.TANK_COLORS[playersQuantity + i], new Tank(Constants.TANK_COLORS[playersQuantity + i], new Point(0, 0))));
        }

        // It shuffles the order
        for (int i = 0; i < Data.getInstance().alivePlayers.size(); i++) {
            int r = random.nextInt(i+1);
            Collections.swap(Data.getInstance().alivePlayers, i, r);
        }
    }
}
