package com.example.tankproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    public Shop shop;
    public HBox shopLightAmmoHBox;
    public HBox shopMediumAmmoHBox;
    public HBox shopHeavyAmmoHBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.shop = new Shop();
        if (Data.getInstance().gameNumber == 1) {
            Data.getInstance().reset();
            createPlayers();
        } else loadPlayers();

        this.backgroundImage.setImage(ImagesLoader.getInstance().backgroundImages.get(2));
        this.backgroundImage.setFitHeight(Constants.WINDOWS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.gameNumberText.setText("Game " + Data.getInstance().gameNumber);
        this.lightAmmoCostText.setText(String.valueOf(Constants.AMMO_PRICE[0]));
        this.mediumAmmoCostText.setText(String.valueOf(Constants.AMMO_PRICE[1]));
        this.heavyAmmoCostText.setText(String.valueOf(Constants.AMMO_PRICE[2]));
        if (Data.getInstance().playableTanksQuantity > 0) {
            this.shopVBox.setDisable(false);
            this.shopVBox.setVisible(true);
            initializeCurrentPlayerShopSpinner();
        } else {
            this.shopVBox.setDisable(true);
            this.shopVBox.setVisible(false);
        }
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
        if (Data.getInstance().playableTanksQuantity < 1) return;
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

    // This method creates the players and saves them into the alivePlayers arrayList
    public void createPlayers() {
        Data.getInstance().alivePlayers = new ArrayList<>();
        Data.getInstance().deadPlayers = new ArrayList<>();
        int playersQuantity = Data.getInstance().playableTanksQuantity;
        int cpuQuantity = Data.getInstance().cpuTanksQuantity;

        // It creates the playable players
        for (int i = 0; i < playersQuantity; i++) {
            Data.getInstance().alivePlayers.add(new Player("Player " + (i+1), Constants.TANK_COLORS[i], new Tank(Constants.TANK_COLORS[i], new Point(0, 0))));
        }

        // It creates the CPU players
        for (int i = 0; i < cpuQuantity; i++) {
            Data.getInstance().alivePlayers.add(new CPU("CPU " + (i+1), Constants.TANK_COLORS[playersQuantity + i], new Tank(Constants.TANK_COLORS[playersQuantity + i], new Point(0, 0))));
            // The CPU buys the ammo needed
            CPUBuysAmmo(((CPU) Data.getInstance().alivePlayers.get(Data.getInstance().alivePlayers.size()-1)));
        }
    }

    // This method loads the players, and brings the dead players to the alivePlayers arrayList
    public void loadPlayers() {
        for (Player player : Data.getInstance().deadPlayers) {
            Data.getInstance().alivePlayers.add(player);
            player.tank.restoreHealth();
        }
        Data.getInstance().deadPlayers = new ArrayList<>();

        // It looks for all the CPU, so they can buy the ammo needed
        for (Player player : Data.getInstance().alivePlayers) {
            if (player instanceof CPU) CPUBuysAmmo((CPU) player);
        }
    }

    // It buys the ammo for the CPU
    public void CPUBuysAmmo(CPU cpu) {
        int[] ammo = cpu.getAmmoToBuy();
        shop.BuyBullet(cpu, Constants.AMMO_PRICE[0], ammo[0]);
        shop.BuyBullet(cpu, Constants.AMMO_PRICE[1], ammo[1]);
        shop.BuyBullet(cpu, Constants.AMMO_PRICE[2], ammo[2]);
    }

    // Initializes the current shop player spinner
    public void initializeCurrentPlayerShopSpinner() {
        ObservableList<Player> observableArrayList;
        ArrayList<Player> players = new ArrayList<>();

        for (Player p: Data.getInstance().alivePlayers) {
            if (!(p instanceof CPU)) {
                players.add(p);
            }
        }
        
        for (Player p: Data.getInstance().deadPlayers) {
            if (!(p instanceof CPU)) {
                players.add(p);
            }
        }

        observableArrayList = FXCollections.observableArrayList(players);
        this.currentShopPlayerSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(observableArrayList));
        this.currentShopPlayerSpinner.getValueFactory().setValue(players.get(0));
        this.currentShopPlayerSpinner.setEditable(false);

        updateShopValues(players.get(0));

        // Updates shop text values when another player is selected in spinner
        this.currentShopPlayerSpinner.valueProperty().addListener((observable, oldPlayer, newPlayer) -> updateShopValues(newPlayer));
    }

    // Updates all shop text values with player values
    public void updateShopValues(Player p) {
        this.currentShopPlayerSpinnerImageStackPane.setStyle(this.currentShopPlayerSpinnerImageStackPane.getStyle() + "-fx-background-color: " + toHexString(p.tank.color) + ";");
        this.currentShopPlayerNameText.setText(p.name);
        this.currentShopPlayerCreditsText.setText(String.valueOf(p.tank.credits));
        this.currentShopPlayerLightAmmoText.setText(String.valueOf(p.tank.ammunition.get(0)));
        this.currentShopPlayerMediumAmmoText.setText(String.valueOf(p.tank.ammunition.get(1)));
        this.currentShopPlayerHeavyAmmoText.setText(String.valueOf(p.tank.ammunition.get(2)));
    }

    // Buys one unit of selected ammo when user clicks the button and updates shop text values related
    public void onShopAmmoButtonClick(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this.shopLightAmmoHBox) {
            this.shop.BuyBullet(this.currentShopPlayerSpinner.getValueFactory().getValue(),Constants.AMMO_PRICE[0], 1);
            this.currentShopPlayerLightAmmoText.setText(String.valueOf(this.currentShopPlayerSpinner.getValueFactory().getValue().tank.ammunition.get(0)));
        }
        
        if (mouseEvent.getSource() == this.shopMediumAmmoHBox) {
            this.shop.BuyBullet(this.currentShopPlayerSpinner.getValueFactory().getValue(),Constants.AMMO_PRICE[1], 1);
            this.currentShopPlayerMediumAmmoText.setText(String.valueOf(this.currentShopPlayerSpinner.getValueFactory().getValue().tank.ammunition.get(1)));
        }

        if (mouseEvent.getSource() == this.shopHeavyAmmoHBox) {
            this.shop.BuyBullet(this.currentShopPlayerSpinner.getValueFactory().getValue(),Constants.AMMO_PRICE[2], 1);
            this.currentShopPlayerHeavyAmmoText.setText(String.valueOf(this.currentShopPlayerSpinner.getValueFactory().getValue().tank.ammunition.get(2)));
        }
        this.currentShopPlayerCreditsText.setText(String.valueOf(this.currentShopPlayerSpinner.getValueFactory().getValue().tank.credits));
    }
}
