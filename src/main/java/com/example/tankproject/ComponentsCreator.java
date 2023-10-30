package com.example.tankproject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Objects;

public class ComponentsCreator {

    static Font primaryFont = Font.font("Arial", FontWeight.BOLD,50);
    static Font subHeaderFont = Font.font("Arial", FontWeight.NORMAL,36);
    static Font secondaryFont = Font.font("Arial", FontWeight.NORMAL,30);
    static Font shopButtonFont = Font.font("Arial", FontWeight.NORMAL,24);


    // Creates replay button
    public static Button createReplayButton(int height, int width) {
        Image replayIcon = ImagesLoader.getInstance().iconImages.get(0);
        ImageView replayIconView = new ImageView(replayIcon);
        Button replayButton = new Button("",replayIconView);

        replayButton.getStyleClass().add("winScreenButton");
        replayButton.setId("replayWinButton");
        replayButton.setPrefHeight(height);
        replayButton.setPrefWidth(width);
        replayButton.setMaxSize(width,height);
        replayIconView.setFitHeight(height);
        replayIconView.setFitWidth(width);

        return replayButton;
    }

    // Creates exit button
    public static Button createExitButton(int height, int width) {
        Image exitIcon = ImagesLoader.getInstance().iconImages.get(2);
        ImageView exitIconView = new ImageView(exitIcon);
        Button exitButton = new Button("",exitIconView);

        exitButton.getStyleClass().add("winScreenButton");
        exitButton.setId("exitWinButton");
        exitButton.setPrefHeight(height);
        exitButton.setPrefWidth(width);
        exitButton.setMaxSize(width,height);
        exitIconView.setFitHeight(height);
        exitIconView.setFitWidth(width);

        return exitButton;
    }

    // Creates menu exit button
    public static Button createMenuExitButton(int height, int width) {
        Image menuExitIcon = ImagesLoader.getInstance().iconImages.get(1);
        ImageView menuExitIconView = new ImageView(menuExitIcon);
        Button menuExitButton = new Button("",menuExitIconView);

        menuExitButton.getStyleClass().add("winScreenButton");
        menuExitButton.setId("menuExitButton");
        menuExitButton.setPrefHeight(height);
        menuExitButton.setPrefWidth(width);
        menuExitButton.setMaxSize(width,height);
        menuExitIconView.setFitHeight(height);
        menuExitIconView.setFitWidth(width);

        return menuExitButton;
    }

    // Creates win screen
    public static VBox createWinScreenVBox(Player winnerPlayer, Button replayButton, Button exitButton) {
        Color backgroundColor = Color.rgb((int) (Constants.WIN_SCREEN_BACKGROUND_COLOR.getRed() * 255), (int) (Constants.WIN_SCREEN_BACKGROUND_COLOR.getGreen() * 255), (int) (Constants.WIN_SCREEN_BACKGROUND_COLOR.getBlue() * 255), 0.5);
        StackPane winnerTankBackgroundStackPane = createCurrentPlayerTankImage(ImagesLoader.getInstance().winnerTankImage);
        VBox backgroundVbox = new VBox();
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Text victoryText = new Text("Victory!");
        Text winnerNameText = new Text(winnerPlayer.name);
        HBox healthRemainingHBox = createHealthRemainingHBox(winnerPlayer.tank,30,35, "Health:",25, Color.WHITE);

        winnerTankBackgroundStackPane.setBackground(new Background(new BackgroundFill(winnerPlayer.color,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

        victoryText.setFont(primaryFont);
        victoryText.setFill(Color.WHITE);

        winnerNameText.setFont(secondaryFont);
        winnerNameText.setFill(Color.WHITE);

        hbox.setId("winScreenButtonHBox");
        hbox.alignmentProperty().set(Pos.CENTER);
        hbox.setSpacing(30);
        hbox.getChildren().add(replayButton);
        hbox.getChildren().add(exitButton);

        vbox.setId("winnerTankBackground");
        vbox.alignmentProperty().set(Pos.CENTER);
        vbox.setSpacing(15);
        vbox.maxWidthProperty().bind(winnerTankBackgroundStackPane.widthProperty());
        vbox.getChildren().add(winnerTankBackgroundStackPane);
        vbox.getChildren().add(victoryText);
        vbox.getChildren().add(winnerNameText);
        vbox.getChildren().add(healthRemainingHBox);
        vbox.getChildren().add(hbox);

        backgroundVbox.setBackground(new Background(new BackgroundFill(backgroundColor,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        backgroundVbox.setAlignment(Pos.CENTER);
        backgroundVbox.getChildren().add(vbox);

        return backgroundVbox;
    }

    // Creates tank radar background circle
    public static Circle createTankRadarCircle(int radius) {
        Circle circle = new Circle(radius);

        circle.setId("tankRadarCircle");
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        return circle;
    }

    // Creates tank radar angle pointer
    public static Rectangle createTankRadarPointer(int height, int width) {
        Rectangle pointer = new Rectangle();

        pointer.setId("tankRadarPointer");
        pointer.setWidth(width);
        pointer.setHeight(height);
        pointer.setArcHeight(4);
        pointer.setArcWidth(4);
        pointer.setFill(Color.WHITE);

        return pointer;
    }

    // Creates tank health remaining HBox
    public static HBox createHealthRemainingHBox(Tank tank, int fontSize, int iconSize ,String text, int spacing, Color fontColor) {
        HBox healthRemainingHbox = new HBox();
        Text healthRemainingText = new Text(text + " " + tank.getHealth() + " / " + Constants.TANK_HEALTH);
        Image healthIconImage = healthIcon(tank);
        ImageView healthIconImageView;
        Font font = Font.font("Arial",FontWeight.NORMAL,fontSize);

        healthRemainingText.setFont(font);
        healthRemainingText.setFill(fontColor);

        healthIconImageView = new ImageView(healthIconImage);
        healthIconImageView.setFitWidth(iconSize);
        healthIconImageView.setFitHeight(iconSize);

        healthRemainingHbox.setAlignment(Pos.CENTER);
        healthRemainingHbox.setSpacing(spacing);
        healthRemainingHbox.getChildren().add(healthRemainingText);
        healthRemainingHbox.getChildren().add(healthIconImageView);

        return healthRemainingHbox;
    }

    // Choose the health icon image according to tank health
    public static Image healthIcon(Tank tank) {
        Image healthIconImage = ImagesLoader.getInstance().heartIconImages.get(2);

        if (tank.getHealth() == Constants.TANK_HEALTH) healthIconImage = ImagesLoader.getInstance().heartIconImages.get(2);
        if (tank.getHealth() <= Constants.TANK_HEALTH / 2) healthIconImage = ImagesLoader.getInstance().heartIconImages.get(1);
        if (tank.getHealth() == 0) healthIconImage = ImagesLoader.getInstance().heartIconImages.get(0);

        return healthIconImage;
    }

    // Transforms x-coordinates to a translation x-distance from the center of the screen, used in StackPane translations
    public static double transformX(double posX) {
        return posX - Constants.WINDOWS_WIDTH / 2.0;
    }

    // Transforms y-coordinates to a translation y-distance from the center of the screen, used in StackPane translations
    public static double transformY(double posY) {
        return posY - Constants.WINDOWS_HEIGHT / 2.0;
    }

    public static VBox createShopVBox() {
        VBox shopVBox = new VBox();
        HBox currentTankHBox = new HBox();
        Spinner<Player> playerSpinner = createCurrentPlayerShopSpinner();
        HBox lightShotButton = createShotShopButton(ImagesLoader.getInstance().shotImages.get(0),"60mm", Constants.AMMO_PRICE[0], Data.getInstance().currentPlayerShop.tank.ammunition.get(0), Constants.AMMO_QUANTITY[0]);
        HBox mediumShotButton = createShotShopButton(ImagesLoader.getInstance().shotImages.get(1),"80mm", Constants.AMMO_PRICE[1], Data.getInstance().currentPlayerShop.tank.ammunition.get(1), Constants.AMMO_QUANTITY[1]);
        HBox heavyShotButton = createShotShopButton(ImagesLoader.getInstance().shotImages.get(2),"105mm", Constants.AMMO_PRICE[2], Data.getInstance().currentPlayerShop.tank.ammunition.get(2), Constants.AMMO_QUANTITY[2]);
        Text shopText = new Text("Shop");
        Text currentPlayerNameText = new Text(Data.getInstance().currentPlayerShop.name);
        Button buyAllButton = new Button("Buy all");
        Text currentTankCreditsText = new Text(String.valueOf(Data.getInstance().currentPlayerShop.tank.credits));


        shopText.setFont(subHeaderFont);
        shopText.setFill(Color.WHITE);
        currentTankCreditsText.setFont(shopButtonFont);
        currentTankCreditsText.setFill(Color.WHITE);


        currentTankHBox.setAlignment(Pos.CENTER);

        lightShotButton.getStyleClass().add("buyShotShopButton");
        mediumShotButton.getStyleClass().add("buyShotShopButton");
        heavyShotButton.getStyleClass().add("buyShotShopButton");
        buyAllButton.setId("buyAllShopButton");



        currentTankHBox.getChildren().add(playerSpinner);
        currentTankHBox.getChildren().add(currentTankCreditsText);

        shopVBox.setAlignment(Pos.CENTER);
        shopVBox.setSpacing(15);

        shopVBox.getChildren().add(shopText);
        shopVBox.getChildren().add(currentTankHBox);
        shopVBox.getChildren().add(lightShotButton);
        shopVBox.getChildren().add(mediumShotButton);
        shopVBox.getChildren().add(heavyShotButton);
        shopVBox.getChildren().add(buyAllButton);

        return shopVBox;
    }

    public static HBox createShotShopButton(Image image, String shotType, int priceCredits, int currentAmount, int maxAmount) {
        HBox shotButtonHBox = new HBox();
        Image shotImage;
        Image creditImage;
        ImageView shotImageView;
        ImageView creditImageView;
        Text shotTypeText;
        Text priceCreditsText;
        Text amountText;

        shotImage = image;
        creditImage = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/shot_money.png")).toExternalForm());
        shotImageView = new ImageView(shotImage);
        creditImageView = new ImageView(creditImage);

        shotTypeText = new Text(shotType);
        priceCreditsText = new Text(String.valueOf(priceCredits));
        amountText = new Text(currentAmount + " / " + maxAmount);


        shotImageView.setPreserveRatio(true);
        creditImageView.setPreserveRatio(true);
        shotImageView.setFitWidth(65);
        creditImageView.setFitWidth(50);
        shotTypeText.setFont(shopButtonFont);
        shotTypeText.setFill(Color.WHITE);
        priceCreditsText.setFont(shopButtonFont);
        priceCreditsText.setFill(Color.WHITE);
        amountText.setFont(shopButtonFont);
        amountText.setFill(Color.WHITE);

        shotButtonHBox.setAlignment(Pos.CENTER_LEFT);
        shotButtonHBox.setSpacing(25);

        shotButtonHBox.getChildren().add(shotImageView);
        shotButtonHBox.getChildren().add(shotTypeText);
        shotButtonHBox.getChildren().add(creditImageView);
        shotButtonHBox.getChildren().add(priceCreditsText);
        shotButtonHBox.getChildren().add(amountText);


        return shotButtonHBox;
    }

    public static Spinner<Player> createCurrentPlayerShopSpinner() {
        ObservableList<Player> observableArrayList;
        ArrayList<Player> players = Data.getInstance().alivePlayers;
        Spinner<Player> playerSpinner = new Spinner<>();

        observableArrayList = FXCollections.observableArrayList(players);
        playerSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(observableArrayList));

        playerSpinner.getValueFactory().setValue(players.get(0));
        playerSpinner.getStyleClass().add("split-arrows-horizontal");
        playerSpinner.setId("currentPlayerSpinnerShop");
        playerSpinner.setEditable(false);

        Data.getInstance().currentPlayerShop = players.get(0);

        playerSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.name);
            Data.getInstance().currentPlayerShop = newValue;


        });


        return playerSpinner;

    }

    public static StackPane createCurrentPlayerTankImage(Image image) {
        StackPane currentPlayerTankBackgroundStackPane = new StackPane();
        ImageView currentPlayerTankImageView = new ImageView(image);

        currentPlayerTankBackgroundStackPane.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        currentPlayerTankBackgroundStackPane.setAlignment(Pos.CENTER);
        currentPlayerTankImageView.fitWidthProperty().bind(currentPlayerTankBackgroundStackPane.widthProperty());
        currentPlayerTankImageView.setPreserveRatio(true);

        currentPlayerTankBackgroundStackPane.getChildren().add(currentPlayerTankImageView);

        return currentPlayerTankBackgroundStackPane;
    }
}
