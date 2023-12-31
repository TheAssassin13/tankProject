package com.example.tankproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    public HBox finalScreenHBox;
    public Text winnerPlayerNameText;
    public Text winnerPlayerKillsText;
    public VBox tieScreenVBox;
    public VBox winScreenVBox;
    public VBox drawScreenVBox;
    public StackPane drawFirstPlayerImageStackPane;
    public StackPane drawSecondPlayerImageStackPane;
    public ImageView winnerPlayerImageView;
    public Text winnerPlayerCreditsText;
    public ImageView drawFirstPlayerImageView;
    public ImageView drawSecondPlayerImageView;
    public ImageView heavyAmmoImageView;
    public ImageView mediumAmmoImageView;
    public ImageView lightAmmoImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.shop = new Shop();
        this.backgroundImage.setImage(Loader.getInstance().currentBackgrounds.get(2));
        this.backgroundImage.setFitHeight(Data.getInstance().windowsHeight);
        this.backgroundImage.setFitWidth(Data.getInstance().windowsWidth);
        this.music = new MediaPlayer(Loader.getInstance().currentBackgroundMusic.get(2));
        this.music.setVolume(Data.getInstance().musicVolume);
        this.music.setCycleCount(MediaPlayer.INDEFINITE);
        this.music.play();

        // If there's a tie, it shows a message
        if (Data.getInstance().gameNumber < Data.getInstance().gamesMax && Data.getInstance().tie)
            showNodeTimeline(this.tieScreenVBox, 4);

        // If it's the first game it creates the players, otherwise it just loads them and reset some data
        if (Data.getInstance().gameNumber == 0) createPlayers();
        else {
            Data.getInstance().restart();
            loadPlayers();
        }

        // When all games finished it shows the final screen
        if (Data.getInstance().gameNumber == Data.getInstance().gamesMax) {
            showFinalScreen(getWinnerPlayers().size() != 1);
            return;
        }

        this.gameNumberText.setText("Game " + (Data.getInstance().gameNumber + 1));
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

        initializeAmmunitionImageView();
        scoreboardTableViewInitialize(this.menuScoreboardTableView);
    }

    // Opens game windows
    public void onPlayButtonClick(ActionEvent ignoredActionEvent) throws IOException {
        boolean needToBuyAmmo = true;

        setDefinitiveAmmunition();

        // Checks if there's at least one player with ammo
        for (Player player : Data.getInstance().alivePlayers) {
            if (player.tank.getAmmunitionQuantity() > 0) needToBuyAmmo = false;
        }

        if (needToBuyAmmo) {
            showNodeTimeline(this.warningHBox, 3);
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

    // Closes game application
    public void onExitButtonClick(ActionEvent ignoredEvent) {
        Platform.exit();
    }

    // When the shop button is clicked, the shop appears or disappears
    public void onShopButtonClick(ActionEvent ignoredActionEvent) {
        this.scoreboardVBox.setVisible(false);
        if (Data.getInstance().playableTanksQuantity < 1)
            return; // if there's no playable tanks the shop cannot be open
        if (this.shopVBox.isVisible()) {
            this.shopVBox.setDisable(true);
            this.shopVBox.setVisible(false);
            return;
        }
        this.shopVBox.setDisable(false);
        this.shopVBox.setVisible(true);
    }

    // When the scoreboard button is clicked, the scoreboard table appears or disappears
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
        Random random = new Random();
        Data.getInstance().alivePlayers = new ArrayList<>();
        Data.getInstance().deadPlayers = new ArrayList<>();
        int playersQuantity = Data.getInstance().playableTanksQuantity;
        int cpuQuantity = Data.getInstance().cpuTanksQuantity;

        // It creates the playable players
        for (int i = 0; i < playersQuantity; i++) {
            Data.getInstance().alivePlayers.add(new Player("Player " + (i + 1), Constants.TANK_COLORS[i], new Tank(Constants.TANK_COLORS[i], new Point(0, 0))));
        }

        // It creates the CPU players
        for (int i = 0; i < cpuQuantity; i++) {
            Data.getInstance().alivePlayers.add(new CPU("CPU " + (i + 1), Constants.TANK_COLORS[playersQuantity + i], new Tank(Constants.TANK_COLORS[playersQuantity + i], new Point(0, 0)), random.nextInt(1, 4)));
            // The CPU buys the ammo needed
            CPUBuysAmmo(((CPU) Data.getInstance().alivePlayers.get(Data.getInstance().alivePlayers.size() - 1)));
        }
    }

    // This method loads the players, and brings the dead players to the alivePlayers arrayList
    public void loadPlayers() {
        for (Player player : Data.getInstance().alivePlayers) {
            player.score += (int) player.tank.getHealth();
            remainingAmmoPoints(player);
        }

        for (Player player : Data.getInstance().deadPlayers) {
            Data.getInstance().alivePlayers.add(player);
        }

        for (Player player : Data.getInstance().alivePlayers) {
            player.tank.restoreHealth();
            player.tank.initializeTemporaryAmmunition();
            Shop.loadCredits(player, Constants.INITIAL_CREDITS);
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
        shop.buyAmmo(cpu, Constants.AMMO_PRICE[0], ammo[0], false);
        shop.buyAmmo(cpu, Constants.AMMO_PRICE[1], ammo[1], false);
        shop.buyAmmo(cpu, Constants.AMMO_PRICE[2], ammo[2], false);
    }

    // It gives the points to a player according to their remaining ammo
    public void remainingAmmoPoints(Player player) {
        player.score += Constants.POINTS_FOR_REMAINING_AMMO[0] * player.tank.ammunition.get(0);
        player.score += Constants.POINTS_FOR_REMAINING_AMMO[1] * player.tank.ammunition.get(1);
        player.score += Constants.POINTS_FOR_REMAINING_AMMO[2] * player.tank.ammunition.get(2);
    }

    // Initializes the current shop player spinner
    public void initializeCurrentPlayerShopSpinner() {
        ObservableList<Player> observableArrayList;
        ArrayList<Player> players = new ArrayList<>();

        for (Player p : Data.getInstance().alivePlayers) {
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
        this.currentShopPlayerLightAmmoText.setText(String.valueOf(p.tank.ammunition.get(0) + p.tank.temporaryAmmunition.get(0)));
        this.currentShopPlayerMediumAmmoText.setText(String.valueOf(p.tank.ammunition.get(1) + p.tank.temporaryAmmunition.get(1)));
        this.currentShopPlayerHeavyAmmoText.setText(String.valueOf(p.tank.ammunition.get(2) + p.tank.temporaryAmmunition.get(2)));
    }

    // Buys one unit of selected ammo when user clicks the button and updates shop text values related
    public void onShopAmmoButtonClick(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this.shopLightAmmoHBox) {
            this.shop.buyAmmo(this.currentShopPlayerSpinner.getValueFactory().getValue(), Constants.AMMO_PRICE[0], 1, true);
            this.currentShopPlayerLightAmmoText.setText(String.valueOf(this.currentShopPlayerSpinner.getValueFactory().getValue().tank.ammunition.get(0) + this.currentShopPlayerSpinner.getValueFactory().getValue().tank.temporaryAmmunition.get(0)));
        }

        if (mouseEvent.getSource() == this.shopMediumAmmoHBox) {
            this.shop.buyAmmo(this.currentShopPlayerSpinner.getValueFactory().getValue(), Constants.AMMO_PRICE[1], 1, true);
            this.currentShopPlayerMediumAmmoText.setText(String.valueOf(this.currentShopPlayerSpinner.getValueFactory().getValue().tank.ammunition.get(1) + this.currentShopPlayerSpinner.getValueFactory().getValue().tank.temporaryAmmunition.get(1)));
        }

        if (mouseEvent.getSource() == this.shopHeavyAmmoHBox) {
            this.shop.buyAmmo(this.currentShopPlayerSpinner.getValueFactory().getValue(), Constants.AMMO_PRICE[2], 1, true);
            this.currentShopPlayerHeavyAmmoText.setText(String.valueOf(this.currentShopPlayerSpinner.getValueFactory().getValue().tank.ammunition.get(2) + this.currentShopPlayerSpinner.getValueFactory().getValue().tank.temporaryAmmunition.get(2)));
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
        TableColumn<Player, String> playerScoreColumn = new TableColumn<>("Score");

        playerPositionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().position)));
        playerTankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().color)));
        playerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name));
        playerKillsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().tank.kills)));
        playerScoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().score)));

        playerTankColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String color, boolean empty) {
                super.updateItem(color, empty);

                if (color != null && !empty) {
                    StackPane tankImageStackPane = ComponentsCreator.createPlayerTankImage(Loader.getInstance().currenkTankScoreboardImage, Color.valueOf(color), 35);
                    setGraphic(tankImageStackPane);
                }
            }
        });

        playerPositionColumn.setPrefWidth(85);
        playerTankColumn.setPrefWidth(115);
        playerNameColumn.setPrefWidth(115);
        playerKillsColumn.setPrefWidth(85);
        playerScoreColumn.setPrefWidth(115);

        playerPositionColumn.setReorderable(false);
        playerTankColumn.setReorderable(false);
        playerNameColumn.setReorderable(false);
        playerKillsColumn.setReorderable(false);
        playerScoreColumn.setReorderable(false);

        playerPositionColumn.setResizable(false);
        playerTankColumn.setResizable(false);
        playerNameColumn.setResizable(false);
        playerKillsColumn.setResizable(false);
        playerScoreColumn.setResizable(false);

        playerPositionColumn.getStyleClass().add("table-cell-centered");
        playerTankColumn.getStyleClass().add("table-cell-centered");
        playerNameColumn.getStyleClass().add("table-cell-centered");
        playerKillsColumn.getStyleClass().add("table-cell-centered");
        playerScoreColumn.getStyleClass().add("table-cell-centered");

        tableView.getColumns().clear();
        tableView.getColumns().addAll(playerPositionColumn, playerTankColumn, playerNameColumn, playerKillsColumn, playerScoreColumn);
        tableView.setItems(FXCollections.observableArrayList(Data.getInstance().alivePlayers));
        tableView.getSortOrder().add(playerPositionColumn);
        playerPositionColumn.setSortType(TableColumn.SortType.ASCENDING);
    }

    // Calculates the player's position based on the score
    public void calculatePlayersPosition() {
        ArrayList<Player> players = new ArrayList<>(Data.getInstance().alivePlayers);
        int position = 1;
        int winnerScore;

        players.addAll(Data.getInstance().deadPlayers);
        players.sort(Comparator.comparing(player -> player.score));
        Collections.reverse(players);

        winnerScore = players.get(0).score;

        // The players with the same score as the winner take first position
        for (Player p : players) {
            if (p.score == winnerScore) p.position = position;
            else p.position = ++position;
        }
    }

    // Makes final screen visible
    public void showFinalScreen(boolean draw) {
        if (draw) {
            ArrayList<Player> winnerPlayers = getWinnerPlayers();

            if (Data.getInstance().windowsWidth == Constants.RESOLUTION_WIDTH[0] && Data.getInstance().windowsHeight == Constants.RESOLUTION_HEIGHT[0]) {
                this.drawFirstPlayerImageView.setFitWidth(150);
                this.drawSecondPlayerImageView.setFitWidth(150);
            }

            this.winScreenVBox.setVisible(false);
            this.winScreenVBox.setDisable(true);
            this.winScreenVBox.setManaged(false);
            this.drawScreenVBox.setVisible(true);
            this.drawScreenVBox.setDisable(false);
            this.drawScreenVBox.setManaged(true);
            this.drawFirstPlayerImageStackPane.setStyle(this.drawFirstPlayerImageStackPane.getStyle() + "-fx-background-color: " + toHexString(winnerPlayers.get(0).tank.color) + ";");
            this.drawSecondPlayerImageStackPane.setStyle(this.drawSecondPlayerImageStackPane.getStyle() + "-fx-background-color: " + toHexString(winnerPlayers.get(1).tank.color) + ";");

        } else {
            Player winnerPlayer = getWinnerPlayers().get(0);

            if (Data.getInstance().windowsWidth == Constants.RESOLUTION_WIDTH[0] && Data.getInstance().windowsHeight == Constants.RESOLUTION_HEIGHT[0])
                this.winnerPlayerImageView.setFitWidth(250);

            this.winScreenVBox.setVisible(true);
            this.winScreenVBox.setDisable(false);
            this.winScreenVBox.setManaged(true);
            this.drawScreenVBox.setVisible(false);
            this.drawScreenVBox.setDisable(true);
            this.drawScreenVBox.setManaged(false);
            this.winnerPlayerImageStackPane.setStyle(this.winnerPlayerImageStackPane.getStyle() + "-fx-background-color: " + toHexString(winnerPlayer.tank.color) + ";");
            this.winnerPlayerNameText.setText(winnerPlayer.name);
            this.winnerPlayerKillsText.setText(String.valueOf(winnerPlayer.tank.kills));
            this.winnerPlayerCreditsText.setText(String.valueOf(winnerPlayer.tank.credits));
        }

        this.finalScreenHBox.setVisible(true);
        this.finalScreenHBox.setDisable(false);
        this.shopVBox.setVisible(false);

        this.music.stop();
        this.music = new MediaPlayer(Loader.getInstance().currentSoundEffects.get(3));
        this.music.setVolume(Data.getInstance().SFXVolume);
        this.music.play();

        scoreboardTableViewInitialize(this.winScreenScoreboardTableView);
    }

    // Gets players with the highest score
    public ArrayList<Player> getWinnerPlayers() {
        ArrayList<Player> winnerPlayers = new ArrayList<>();

        calculatePlayersPosition();

        for (Player player : Data.getInstance().alivePlayers) {
            if (player.position == 1) winnerPlayers.add(player);
        }

        return winnerPlayers;
    }

    // Makes node visible for a few seconds
    public void showNodeTimeline(Node node, int seconds) {
        Timeline timeline = new Timeline();
        // Makes node invisible after some time
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> node.setVisible(true)),
                new KeyFrame(Duration.seconds(seconds), e -> node.setVisible(false))
        );

        timeline.setOnFinished(event -> timeline.stop());
        timeline.play();
    }

    // Resets the ammo purchase, return the credits to the user and the ammo to the shop
    public void onResetButtonClick(ActionEvent ignoredActionEvent) {
        Shop.loadCredits(this.currentShopPlayerSpinner.getValueFactory().getValue(), this.currentShopPlayerSpinner.getValueFactory().getValue().tank.temporaryAmmunition.get(0) * Constants.AMMO_PRICE[0]);
        Shop.loadCredits(this.currentShopPlayerSpinner.getValueFactory().getValue(), this.currentShopPlayerSpinner.getValueFactory().getValue().tank.temporaryAmmunition.get(1) * Constants.AMMO_PRICE[1]);
        Shop.loadCredits(this.currentShopPlayerSpinner.getValueFactory().getValue(), this.currentShopPlayerSpinner.getValueFactory().getValue().tank.temporaryAmmunition.get(2) * Constants.AMMO_PRICE[2]);

        this.currentShopPlayerSpinner.getValueFactory().getValue().tank.initializeTemporaryAmmunition();

        updateShopValues(this.currentShopPlayerSpinner.getValueFactory().getValue());
    }

    // Changes temporary ammunition to definitive ammunition
    public void setDefinitiveAmmunition() {
        for (Player player : Data.getInstance().alivePlayers) {
            if (!(player instanceof CPU)) {
                player.tank.ammunition.set(0, player.tank.ammunition.get(0) + player.tank.temporaryAmmunition.get(0));
                player.tank.ammunition.set(1, player.tank.ammunition.get(1) + player.tank.temporaryAmmunition.get(1));
                player.tank.ammunition.set(2, player.tank.ammunition.get(2) + player.tank.temporaryAmmunition.get(2));
            }
        }
    }

    // Initializes ammunition image view
    public void initializeAmmunitionImageView() {
        this.lightAmmoImageView.setImage(Loader.getInstance().currentShotImages.get(0));
        this.mediumAmmoImageView.setImage(Loader.getInstance().currentShotImages.get(1));
        this.heavyAmmoImageView.setImage(Loader.getInstance().currentShotImages.get(2));
    }
}
