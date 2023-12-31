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

    // Creates replay button
    public static Button createReplayButton(int height, int width) {
        Image replayIcon = Loader.getInstance().iconImages.get(0);
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
        Image exitIcon = Loader.getInstance().iconImages.get(2);
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
        Image menuExitIcon = Loader.getInstance().iconImages.get(1);
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
        killsIconImageView = new ImageView(Loader.getInstance().currentTankKillsImage);
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
        Image healthIconImage = Loader.getInstance().heartIconImages.get(2);

        if (tank.getHealth() == Constants.TANK_HEALTH) healthIconImage = Loader.getInstance().heartIconImages.get(2);
        if (tank.getHealth() <= Constants.TANK_HEALTH / 2.0) healthIconImage = Loader.getInstance().heartIconImages.get(1);
        if (tank.getHealth() == 0) healthIconImage = Loader.getInstance().heartIconImages.get(0);

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
    public static StackPane createPlayerTankImage(Image image, Color color, int size) {
        StackPane currentPlayerTankBackgroundStackPane = new StackPane();
        ImageView currentPlayerTankImageView = new ImageView(image);

        currentPlayerTankBackgroundStackPane.setBackground(new Background(new BackgroundFill(color,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        currentPlayerTankBackgroundStackPane.setAlignment(Pos.CENTER);
        currentPlayerTankImageView.setPreserveRatio(true);
        currentPlayerTankBackgroundStackPane.setMinSize(size,size);
        currentPlayerTankBackgroundStackPane.setPrefSize(size,size);
        currentPlayerTankImageView.fitWidthProperty().bind(currentPlayerTankBackgroundStackPane.widthProperty());
        currentPlayerTankImageView.fitHeightProperty().bind(currentPlayerTankBackgroundStackPane.widthProperty());

        currentPlayerTankBackgroundStackPane.getChildren().add(currentPlayerTankImageView);

        return currentPlayerTankBackgroundStackPane;
    }
}
