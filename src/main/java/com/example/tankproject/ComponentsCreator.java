package com.example.tankproject;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ComponentsCreator {

    static Font primaryFont = Font.font("Arial", FontWeight.BOLD,50);
    static Font secondaryFont = Font.font("Arial", FontWeight.NORMAL,30);

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
        StackPane winnerTankBackgroundStackPane = createPlayerTankImage(ImagesLoader.getInstance().winnerTankImage);
        VBox backgroundVbox = new VBox();
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Text victoryText = new Text("Victory!");
        Text winnerNameText = new Text(winnerPlayer.name);
        HBox healthRemainingHBox = createTankInfoHBox(winnerPlayer.tank,30,35,25, Color.WHITE,false);

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

    // Creates tank information HBox
    public static HBox createTankInfoHBox(Tank tank, int fontSize, int iconSize, int spacing, Color fontColor, boolean inverse) {
        HBox healthRemainingHbox = new HBox();
        Text healthRemainingText = new Text(String.valueOf( (int) tank.getHealth()));
        Text killsText = new Text(String.valueOf(tank.kills));
        Image healthIconImage = healthIcon(tank);
        ImageView healthIconImageView;
        ImageView killsIconImageView;
        Font font = Font.font("Arial",FontWeight.NORMAL,fontSize);

        healthRemainingText.setFont(font);
        healthRemainingText.setFill(fontColor);
        killsText.setFont(font);
        killsText.setFill(fontColor);

        healthIconImageView = new ImageView(healthIconImage);
        healthIconImageView.setFitWidth(iconSize);
        healthIconImageView.setFitHeight(iconSize);
        killsIconImageView = new ImageView(ImagesLoader.getInstance().currentTankKillsImage);
        killsIconImageView.setFitWidth(iconSize);
        killsIconImageView.setFitHeight(iconSize);
        if (inverse) killsIconImageView.setEffect(new ColorAdjust(1,1,-1,1));

        healthRemainingHbox.setAlignment(Pos.CENTER);
        healthRemainingHbox.setSpacing(spacing);
        healthRemainingHbox.getChildren().add(healthRemainingText);
        healthRemainingHbox.getChildren().add(healthIconImageView);
        healthRemainingHbox.getChildren().add(new Text("  "));
        healthRemainingHbox.getChildren().add(killsText);
        healthRemainingHbox.getChildren().add(killsIconImageView);

        return healthRemainingHbox;
    }

    // Choose the health icon image according to tank health
    public static Image healthIcon(Tank tank) {
        Image healthIconImage = ImagesLoader.getInstance().heartIconImages.get(2);

        if (tank.getHealth() == Constants.TANK_HEALTH) healthIconImage = ImagesLoader.getInstance().heartIconImages.get(2);
        if (tank.getHealth() <= Constants.TANK_HEALTH / 2.0) healthIconImage = ImagesLoader.getInstance().heartIconImages.get(1);
        if (tank.getHealth() == 0) healthIconImage = ImagesLoader.getInstance().heartIconImages.get(0);

        return healthIconImage;
    }

    // Transforms x-coordinates to a translation x-distance from the center of the screen, used in StackPane translations
    public static double transformX(double posX) {
        return posX - Data.getInstance().windowsWidth / 2.0;
    }

    // Transforms y-coordinates to a translation y-distance from the center of the screen, used in StackPane translations
    public static double transformY(double posY) {
        return posY - Data.getInstance().windowsHeight / 2.0;
    }

    // Creates a tank image StackPane
    public static StackPane createPlayerTankImage(Image image) {
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
