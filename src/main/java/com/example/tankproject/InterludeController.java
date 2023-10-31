package com.example.tankproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.tankproject.App.toHexString;

public class InterludeController implements Initializable {
    public VBox vbox;
    public Text gameNumberText;
    public Button playButton;
    public StackPane stackpane;
    public ImageView backgroundImage;
    public Button shopButton;
    public Button scoreboardButton;
    public Button menuButton;
    public VBox shopVBox;
    public Spinner<Player> currentShopPlayerSpinner;
    public StackPane currentShopPlayerSpinnerImageStackPane;
    public Text currentShopPlayerNameText;
    public Text currentShopPlayerCreditsText;
    public Text currentShopPlayerLightAmmoText;
    public Text currentShopPlayerMediumAmmoText;
    public Text currentShopPlayerHeavyAmmoText;
    public Text heavyAmmoCostText;
    public Text mediumAmmoCostText;
    public Text lightAmmoCostText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Data.getInstance().gameNumber == 1) Data.getInstance().reset();
        createPlayers();
        this.backgroundImage.setImage(ImagesLoader.getInstance().backgroundImages.get(2));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.shopVBox.setDisable(true);
        this.shopVBox.setVisible(false);
        this.gameNumberText.setText("Game " + Data.getInstance().gameNumber);
        this.lightAmmoCostText.setText(String.valueOf(Constants.AMMO_PRICE[0]));
        this.mediumAmmoCostText.setText(String.valueOf(Constants.AMMO_PRICE[1]));
        this.heavyAmmoCostText.setText(String.valueOf(Constants.AMMO_PRICE[2]));
        initializeCurrentPlayerShopSpinner();
    }

    // Opens game windows
    public void onPlayButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        Data.getInstance().gameNumber++;
        App.setRoot("game");
    }

    // Opens menu screen
    public void onMenuButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        App.setRoot("menu");
    }

    // When the options button is clicked, the shop appears or disappears
    public void onShopButtonClick(ActionEvent ignoredActionEvent) {
        if (this.shopVBox.isVisible()) {
            this.shopVBox.setDisable(true);
            this.shopVBox.setVisible(false);
            return;
        }
        this.shopVBox.setDisable(false);
        this.shopVBox.setVisible(true);
    }

    public void onScoreboardButtonClick(ActionEvent ignoredEvent) {
    }

    // This method loads the players and saves them into the alivePlayers arrayList
    public void createPlayers() {
        Data.getInstance().alivePlayers = new ArrayList<>();
        Data.getInstance().deadPlayers = new ArrayList<>();
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
    }

    public void initializeCurrentPlayerShopSpinner() {
        ObservableList<Player> observableArrayList;
        ArrayList<Player> players = Data.getInstance().alivePlayers;

        observableArrayList = FXCollections.observableArrayList(players);
        this.currentShopPlayerSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(observableArrayList));

        this.currentShopPlayerSpinner.getValueFactory().setValue(players.get(0));
        this.currentShopPlayerSpinner.setEditable(false);

        this.currentShopPlayerSpinnerImageStackPane.setStyle(this.currentShopPlayerSpinnerImageStackPane.getStyle() + "-fx-background-color: " + toHexString(players.get(0).tank.color) + ";");
        this.currentShopPlayerNameText.setText(players.get(0).name);
        this.currentShopPlayerCreditsText.setText(String.valueOf(players.get(0).tank.credits));
        this.currentShopPlayerLightAmmoText.setText(String.valueOf(players.get(0).tank.ammunition.get(0)));
        this.currentShopPlayerMediumAmmoText.setText(String.valueOf(players.get(0).tank.ammunition.get(1)));
        this.currentShopPlayerHeavyAmmoText.setText(String.valueOf(players.get(0).tank.ammunition.get(2)));

        this.currentShopPlayerSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.currentShopPlayerSpinnerImageStackPane.setStyle(this.currentShopPlayerSpinnerImageStackPane.getStyle() + "-fx-background-color: " + toHexString(newValue.tank.color) + ";");
            this.currentShopPlayerNameText.setText(newValue.name);
            this.currentShopPlayerCreditsText.setText(String.valueOf(newValue.tank.credits));
            this.currentShopPlayerLightAmmoText.setText(String.valueOf(newValue.tank.ammunition.get(0)));
            this.currentShopPlayerMediumAmmoText.setText(String.valueOf(newValue.tank.ammunition.get(1)));
            this.currentShopPlayerHeavyAmmoText.setText(String.valueOf(newValue.tank.ammunition.get(2)));
        });



    }
}
