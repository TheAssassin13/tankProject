package com.example.tankproject;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Objects;

public class ComponentsCreator {

    static Font primaryFont = Font.font("Arial", FontWeight.BOLD,50);
    static Font secondaryFont = Font.font("Arial",FontWeight.NORMAL,30);


    // Creates replay button
    public static Button createReplayButton(int height, int width) {
        Image replayIcon = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/replay_icon_white.png")).toExternalForm());
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
        Image exitIcon = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/exit_icon_white.png")).toExternalForm());
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
        Image menuExitIcon = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/menu_exit_icon_white.png")).toExternalForm());
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
        Color backgroundColor = Color.rgb((int) (Color.GRAY.getRed() * 255), (int) (Color.GRAY.getGreen() * 255), (int) (Color.GRAY.getBlue() * 255), 0.5);
        StackPane winnerTankBackground = new StackPane();
        Image winnerTankImage = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("images/winner_tank_image.png")).toExternalForm());
        ImageView winnerTankImageView = new ImageView(winnerTankImage);

        VBox backgroundVbox = new VBox();
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Text victoryText = new Text("Victory!");
        Text winnerNameText = new Text(winnerPlayer.name);
        HBox healthRemainingHBox = createHealthRemainingHBox(winnerPlayer,30,35, "Health remaining:",25, Color.WHITE);

        winnerTankBackground.setBackground(new Background(new BackgroundFill(winnerPlayer.color,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        winnerTankBackground.setMaxWidth(150);
        winnerTankBackground.setPrefWidth(150);
        winnerTankBackground.getChildren().add(winnerTankImageView);

        victoryText.setFont(primaryFont);
        victoryText.setFill(Color.WHITE);

        winnerNameText.setFont(secondaryFont);
        winnerNameText.setFill(Color.WHITE);

        hbox.setId("winScreenButtonHBox");
        hbox.alignmentProperty().set(Pos.CENTER_RIGHT);
        hbox.setSpacing(30);
        hbox.getChildren().add(replayButton);
        hbox.getChildren().add(exitButton);

        vbox.setId("winnerTankBackground");
        vbox.alignmentProperty().set(Pos.CENTER);
        vbox.setSpacing(15);
        vbox.maxWidthProperty().bind(winnerTankBackground.widthProperty());
        vbox.getChildren().add(winnerTankBackground);
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

    public static HBox createHealthRemainingHBox(Player player, int fontSize, int iconSize ,String text, int spacing, Color fontColor) {
        HBox healthRemainingHbox = new HBox();
        Text healthRemainingText = new Text(text + " " + player.getHealth() + " / " + Constants.TANK_HEALTH);
        Image healthIconImage = healthIcon(player);
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


    public static Image healthIcon(Player player) {
        Image healthIconImage = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());

        if (player.getHealth() == Constants.TANK_HEALTH) healthIconImage = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());
        if (player.getHealth() <= Constants.TANK_HEALTH / 2) healthIconImage = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/hearts_icons/half_heart_icon.png")).toExternalForm());
        if (player.getHealth() == 0) healthIconImage = new Image(Objects.requireNonNull(ComponentsCreator.class.getResource("icons/hearts_icons/empty_heart_icon.png")).toExternalForm());

        return healthIconImage;
    }

    public static double transformX(double posX) {
        return posX - Constants.WINDOWS_WIDTH / 2.0;
    }

    public static double transformY(double posY) {
        return posY - Constants.WINDOWS_HEIGHT / 2.0;
    }
}
