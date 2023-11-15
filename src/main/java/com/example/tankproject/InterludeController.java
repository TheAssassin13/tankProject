package com.example.tankproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
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
    public MediaPlayer music;
    public VBox scoreboardVBox;
    public TableView<Player> menuScoreboardTableView;
    public TableView<Player> winScreenScoreboardTableView;
    public HBox warningHBox;
    public StackPane winnerPlayerImageStackPane;
    public HBox winScreenHBox;
    public Text winnerPlayerNameText;
    public Text winnerPlayerHealthText;
    public Text winnerPlayerKillsText;
    public ImageView winnerPlayerHealthImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.shop = new Shop();
        this.backgroundImage.setImage(ImagesLoader.getInstance().backgroundImages.get(2));
        this.backgroundImage.setFitHeight(Data.getInstance().windowsHeight);
        this.backgroundImage.setFitWidth(Data.getInstance().windowsWidth);

        if (Data.getInstance().gameNumber == Data.getInstance().gamesMax && !Data.getInstance().tie) {
            showWinScreen();
            return;
        }
        if (Data.getInstance().gameNumber == 0) createPlayers();
        else  {
            Data.getInstance().restart();
            loadPlayers();
        }


        this.gameNumberText.setText("Game " + (Data.getInstance().gameNumber+1));
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
        this.music = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("music/interludeMusic.mp3")).toExternalForm()));
        this.music.setVolume(Data.getInstance().musicVolume);
        this.music.setCycleCount(MediaPlayer.INDEFINITE);
        this.music.play();

        scoreboardTableViewInitialize(this.menuScoreboardTableView);
    }

    // Opens game windows
    public void onPlayButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        boolean needToBuyAmmo = true;
        for (Player player : Data.getInstance().alivePlayers) {
            if (player.tank.getAmmunitionQuantity() > 0) needToBuyAmmo = false;
        }
        if (needToBuyAmmo) {
            showWarningHBox();
            return;
        }
        this.music.stop();
        App.setRoot("game");
    }

    // Opens menu screen
    public void onMenuButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        this.music.stop();
        App.setRoot("menu");
    }

    // When the options button is clicked, the shop appears or disappears
    public void onShopButtonClick(ActionEvent ignoredActionEvent) {
        this.scoreboardVBox.setVisible(false);
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
        this.shopVBox.setVisible(false);
        if (this.scoreboardVBox.isVisible()) {
            this.scoreboardVBox.setDisable(true);
            this.scoreboardVBox.setVisible(false);
            return;
        }
        this.scoreboardVBox.setDisable(false);
        this.scoreboardVBox.setVisible(true);
        scoreboardTableViewInitialize(this.menuScoreboardTableView);
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
        }

        for (Player player : Data.getInstance().alivePlayers) {
            player.tank.restoreHealth();
            this.shop.LoadCredits(player,Constants.INITIAL_CREDITS);
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
        players.sort(Comparator.comparing(player -> player.name));

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

    // Initializes the scoreboard table
    public void scoreboardTableViewInitialize(TableView<Player> tableView) {
        calculatePlayersPosition();
        TableColumn<Player, String> playerPositionColumn = new TableColumn<>("Pos");
        TableColumn<Player, String> playerTankColumn = new TableColumn<>("Tank");
        TableColumn<Player, String> playerNameColumn = new TableColumn<>("Name");
        TableColumn<Player, String> playerKillsColumn = new TableColumn<>("Kills");
        TableColumn<Player, String> playerCreditsColumn = new TableColumn<>("Credits");

        playerPositionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().position)));
        playerTankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().color)));
        playerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name));
        playerKillsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().tank.kills)));
        playerCreditsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().tank.credits)));

        playerTankColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String color, boolean empty) {
                super.updateItem(color, empty);

                if (color != null && !empty) {
                    StackPane tankImageStackPane = ComponentsCreator.createPlayerTankImage(ImagesLoader.getInstance().currenkTankScoreboardImage, Color.valueOf(color), 35);
                    setGraphic(tankImageStackPane);
                }
            }
        });


        playerPositionColumn.setPrefWidth(85);
        playerTankColumn.setPrefWidth(115);
        playerNameColumn.setPrefWidth(115);
        playerKillsColumn.setPrefWidth(85);
        playerCreditsColumn.setPrefWidth(115);

        playerPositionColumn.getStyleClass().add("table-cell-centered");
        playerTankColumn.getStyleClass().add("table-cell-centered");
        playerNameColumn.getStyleClass().add("table-cell-centered");
        playerKillsColumn.getStyleClass().add("table-cell-centered");
        playerCreditsColumn.getStyleClass().add("table-cell-centered");

        tableView.getColumns().clear();
        tableView.getColumns().addAll(playerPositionColumn,playerTankColumn,playerNameColumn,playerKillsColumn,playerCreditsColumn);
        tableView.setItems(FXCollections.observableArrayList(Data.getInstance().alivePlayers));
        tableView.getSortOrder().add(playerPositionColumn);
        playerPositionColumn.setSortType(TableColumn.SortType.ASCENDING);
    }

    // Calculates the player's position based on the number of kills
    public void calculatePlayersPosition() {
        ArrayList<Player> players = new ArrayList<>(Data.getInstance().alivePlayers);
        int position = 1;
        int kills;

        players.sort(Comparator.comparing(player -> player.tank.kills));
        Collections.reverse(players);
        kills = players.get(0).tank.kills;

        for (Player p: players) {
            if (p.tank.kills == kills) {
                p.position = position;
            } else {
                p.position = ++position;
                kills = p.tank.kills;
            }
        }
    }

    // Makes warning HBox visible for a few seconds
    public void showWarningHBox() {
        Timeline timeline = new Timeline();
        int showingSeconds = 3;
        // Makes warning invisible after some time
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> warningHBox.setVisible(true)),
                new KeyFrame(Duration.seconds(showingSeconds), e -> warningHBox.setVisible(false))
        );

        timeline.setOnFinished(event -> timeline.stop());
        timeline.play();
    }

    // Makes win screen visible
    public void showWinScreen() {
        Player winnerPlayer = getWinnerPlayer();

        this.winScreenHBox.setVisible(true);
        this.winScreenHBox.setDisable(false);
        this.winnerPlayerImageStackPane.setStyle(this.winnerPlayerImageStackPane.getStyle() + "-fx-background-color: " + toHexString(winnerPlayer.tank.color) + ";");
        this.winnerPlayerNameText.setText(winnerPlayer.name);
        this.winnerPlayerHealthText.setText(String.valueOf((int)winnerPlayer.tank.getHealth()));
        this.winnerPlayerKillsText.setText(String.valueOf(winnerPlayer.tank.kills));
        this.winnerPlayerHealthImage.setImage(ComponentsCreator.healthIcon(winnerPlayer.tank));
        this.shopVBox.setVisible(false);

        //TODO add victory SFX here

        scoreboardTableViewInitialize(this.winScreenScoreboardTableView);
    }

    // Gets player with the most kills
    public Player getWinnerPlayer() {
        Player winnerPlayer = Data.getInstance().alivePlayers.get(0);

        for (Player player : Data.getInstance().deadPlayers) {
            Data.getInstance().alivePlayers.add(player);
        }

        calculatePlayersPosition();

        for (Player player : Data.getInstance().alivePlayers) {
            if (player.position == 1) winnerPlayer = player;
        }

        return winnerPlayer;
    }
}
