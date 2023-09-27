package com.example.tankproject;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.tankproject.App.restartGame;
import static com.example.tankproject.App.toHexString;

public class GameController implements Initializable {
    @FXML
    public VBox vbox;
    public Canvas grid;
    public GraphicsContext gc;
    public Player turn;
    public Text currentPlayerText;
    public GridPane buttonsPanel;
    public TextField angleTextField;
    public TextField powerTextField;
    public Terrain terrain;
    public ArrayList<Player> alivePlayers;
    public ArrayList<Player> deadPlayers;
    public Text maxHeightTextField;
    public int maxHeight;
    public Text maxDistanceTextField;
    public int maxDistance;
    public StackPane currentPlayerPanel;
    public StackPane stackPane;
    public Button shootButton;
    public ImageView backgroundImage;
    public long lastUpdateTime;

    // Game interface, players and terrain initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gc = grid.getGraphicsContext2D();
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        for (int i = 0; i < Constants.TANKS_QUANTITY; i++) {
            this.alivePlayers.add(new Player("Player " + (i+1), Constants.TANK_COLORS[i], new Tank(Constants.TANK_COLORS[i], new Point(0, 0))));
        }
        this.turn = alivePlayers.get((int) Math.round(Math.random()));
        this.maxHeight = 0;
        this.maxDistance = 0;
        this.maxHeightTextField.setText("Max height = 0 pixels");
        this.maxDistanceTextField.setText("Max distance = 0 pixels");
        this.terrain = new Terrain(Constants.CANVAS_HEIGHT, Constants.WINDOWS_WIDTH);
        this.terrain.terrainGeneration(Constants.SEA_LEVEL, true);
        this.currentPlayerPanel.setStyle(currentPlayerPanel.getStyle() + "-fx-background-color:" + toHexString(this.turn.color) + ";");
        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/background.jpg")).toExternalForm()));
        this.backgroundImage.setFitHeight(Constants.CANVAS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.lastUpdateTime = 0;

        // Updates the direction of the barrel when the user types an angle
        angleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    double newAngle = Double.parseDouble(newValue);
                    turn.tank.setAngle(newAngle);
                    Illustrator.drawTank(gc, turn.tank);
                } catch (NumberFormatException e) {
                    // Invalid element
                }
                drawingMethods();
            }
        });
        buttonsPanelInitialize();
        tanksPlacement();
        drawingMethods();
    }

    // Initializes the buttons panel of the interface
    public void buttonsPanelInitialize() {
        currentPlayerText.setText(turn.name + " is playing");
        buttonsPanel.setPrefHeight(Constants.BUTTONS_PANEL_HEIGHT);
        buttonsPanel.setMinHeight(Constants.BUTTONS_PANEL_HEIGHT);
        buttonsPanel.setMaxHeight(Constants.BUTTONS_PANEL_HEIGHT);
        grid.setHeight(Constants.CANVAS_HEIGHT);
        grid.setWidth(Constants.WINDOWS_WIDTH);
    }

    // All drawing methods that should render every frame
    public void drawingMethods() {
        gc.clearRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        Illustrator.drawTerrain(this.gc, this.terrain);
        for (Player p : this.alivePlayers) {
            Illustrator.drawTank(this.gc, p.tank);
        }
    }

    // Placement of tanks on terrain with random distance
    public void tanksPlacement() {
        int gap = Constants.WINDOWS_WIDTH / Constants.TANKS_QUANTITY;
        ArrayList<Point> tanksPosition = new ArrayList<>();

        // Position of the first tank
        int x = (int) (Math.random() * 2 * Constants.WINDOWS_WIDTH / (Constants.TANKS_QUANTITY * 2 + 1) + Constants.TANK_SIZE);
        int y = Constants.CANVAS_HEIGHT - Constants.TANK_SIZE;
        tanksPosition.add(new Point(x, y));

        for (int i = 1; i < Constants.TANKS_QUANTITY; i++) {
            x = (int) (tanksPosition.get(i-1).getX() + gap + (gap/Constants.TANKS_QUANTITY) * Math.random());
            tanksPosition.add(new Point(x, y));
        }

        // In case the last one gets out of bound
        if (tanksPosition.get(Constants.TANKS_QUANTITY-1).getX() + Constants.TANK_SIZE / 2 >= Constants.WINDOWS_WIDTH)
            tanksPosition.get(Constants.TANKS_QUANTITY-1).setX(Constants.WINDOWS_WIDTH - (Constants.TANK_SIZE * 2));

        for (int i = 0; i < Constants.TANKS_QUANTITY; i++) {
            this.alivePlayers.get(i).tank.position.setX(tanksPosition.get(i).getX());
        }

        // Positions tanks on terrain
        for (int i = 0; i < Constants.TANKS_QUANTITY; i++) {
            for (int j = 0; j < Constants.CANVAS_HEIGHT; j++) {
                if (terrain.resolutionMatrix[j][tanksPosition.get(i).getX()] == 1) {
                    this.alivePlayers.get(i).tank.position.setY(j - Constants.TANK_SIZE/3);
                    break;
                }
            }
        }
    }

    // Checks if a player's tank, that is not the current player, is hit and return the hit player
    public Player tanksCollision(Shot shot) {
        for (Player p : this.alivePlayers) {
            if (this.turn != p && shot.tankCollision(p.tank)) {
                return p;
            }
        }
        return null;
    }

    // Removes hit player from alive players and add to dead players
    public void deleteDeadPlayer(Player player) {
        this.deadPlayers.add(player);
        this.alivePlayers.remove(player);
    }

    // Manages the shoot button action in the interface. Create the shot from the angle and initial velocity from user input, check for collision and turn changes.
    public void onShootButtonClick(ActionEvent ignoredActionEvent) {
        maxHeight = 0;
        maxDistance = 0;
        // Checks if the input is not empty
        if (!powerTextField.getText().isEmpty() && !angleTextField.getText().isEmpty()) {
            turn.tank.angle = Double.parseDouble(angleTextField.getText());
            turn.tank.power = Double.parseDouble(powerTextField.getText());
            // Creates shot from user input
            Shot shot = new Shot(new Point(turn.tank.position.getX(), turn.tank.position.getY()), Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()));

            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    // Makes animation fps constant
                    if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                        shot.shotPosition();
                        drawingMethods();
                        Illustrator.drawTrajectory(gc, shot);
                        Illustrator.drawShot(gc, shot);
                        shootButton.setDisable(true);
                        // Update max height and distance every frame
                        calculateMax(shot, turn.tank);

                        // Shot is out of the screen in the X-axis
                        if (shot.position.getX() >= Constants.WINDOWS_WIDTH || shot.position.getX() < 0) {
                            stop();
                            stopMethods(shootButton);
                        }
                        // Checks if a tank is hit
                        if (tanksCollision(shot) != null) {
                            deleteDeadPlayer(tanksCollision(shot));
                        }
                        // Checks if terrain is hit
                        if (shot.terrainCollision(terrain)) {
                            stop();
                            stopMethods(shootButton);
                        }
                        // Checks if there is only one player left
                        if (alivePlayers.size() == 1) {
                            winScreen();
                        }
                        // Trajectory point added
                        shot.addTrajectory();

                        lastUpdateTime = now;
                    }
                }
            }.start();
        }
        angleTextField.requestFocus();
    }

    // Calculates max height and distance of the shot and display it to the interface
    public void calculateMax(Shot shot, Tank tank) {
        this.maxHeight = Math.max(this.maxHeight, tank.position.getY() - shot.position.getY());
        this.maxDistance = Math.max(this.maxDistance, Math.abs(tank.position.getX() - shot.position.getX()));

        if (this.maxHeight < 0) this.maxHeight = 0;

        maxDistanceTextField.setText("Max distance = " + maxDistance + " pixels");
        maxHeightTextField.setText("Max height = " + maxHeight + " pixels");
    }


    // Encapsulation of methods in charge of turn change mechanic
    public void stopMethods(Button shootButton) {
        changeTurn();
        drawingMethods();
        shootButton.setDisable(false);
        // Saves last angle and power
        if (turn.tank.power != null || turn.tank.angle != null) {
            angleTextField.setText(String.valueOf(turn.tank.angle));
            powerTextField.setText(String.valueOf(turn.tank.power));
        }  else {
            angleTextField.clear();
            powerTextField.clear();
        }
    }

    // Turn change, if there's no next player comes back to the first one
    public void changeTurn() {
        for (int i = 0; i < this.alivePlayers.size(); i++) {
            if (this.turn == this.alivePlayers.get(i)) {
                if (i + 1 < this.alivePlayers.size()) {
                    this.turn = this.alivePlayers.get(i + 1);
                } else {
                    this.turn = this.alivePlayers.get(0);
                }
                break;
            }
        }
        this.currentPlayerText.setText(this.turn.name + " is playing");
        this.currentPlayerPanel.setStyle(currentPlayerPanel.getStyle() + "-fx-background-color:" + toHexString(this.turn.color) + ";");
    }

    // Creates the game win screen
    public void winScreen() {
        if (stackPane.getChildren().size() != 1) return;
        Color color = Color.rgb((int) (Constants.WIN_SCREEN_BACKGROUND_COLOR.getRed() * 255), (int) (Constants.WIN_SCREEN_BACKGROUND_COLOR.getGreen() * 255), (int) (Constants.WIN_SCREEN_BACKGROUND_COLOR.getBlue() * 255), 0.5);
        BackgroundFill backgroundFill = new BackgroundFill(color,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);
        Background background = new Background(backgroundFill);
        VBox vbox = new VBox();
        VBox replayVbox = new VBox();
        VBox exitVbox = new VBox();
        HBox hbox = new HBox();
        Font font = Font.font("Arial",FontWeight.BOLD,50);
        Font labelFont = Font.font("Arial",20);
        Text winnerText = new Text(this.alivePlayers.get(0).name + " won!");
        Label replayLabel = new Label("Replay");
        Label exitLabel = new Label("Exit");
        Image replayIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/replay_icon.png")).toExternalForm());
        Image exitIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/exit_icon.png")).toExternalForm());
        ImageView replayIconView = new ImageView(replayIcon);
        ImageView exitIconView = new ImageView(exitIcon);

        Button replayButton = new Button("",replayIconView);
        Button exitButton = new Button("",exitIconView);

        // Disables buttons of the interface game
        angleTextField.setDisable(true);
        powerTextField.setDisable(true);
        shootButton.setDisable(true);

        // Replay button action
        replayButton.setOnAction(event -> {
            try {
                restartGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Exit button action
        exitButton.setOnAction(event -> Platform.exit());

        replayLabel.setTextFill(Color.BLACK);
        exitLabel.setTextFill(Color.BLACK);
        replayIconView.setFitHeight(45);
        replayIconView.setFitWidth(45);
        exitIconView.setFitHeight(45);
        exitIconView.setFitWidth(45);
        replayLabel.setFont(labelFont);
        exitLabel.setFont(labelFont);
        replayButton.getStyleClass().add("winScreenButton");
        exitButton.getStyleClass().add("winScreenButton");
        replayButton.setId("replayWinButton");
        exitButton.setId("exitWinButton");
        winnerText.setFont(font);
        replayVbox.getChildren().add(replayButton);
        replayVbox.getChildren().add(replayLabel);
        exitVbox.getChildren().add(exitButton);
        exitVbox.getChildren().add(exitLabel);
        replayVbox.alignmentProperty().set(Pos.CENTER);
        exitVbox.alignmentProperty().set(Pos.CENTER);
        replayVbox.setSpacing(5);
        exitVbox.setSpacing(5);
        hbox.getChildren().add(replayVbox);
        hbox.getChildren().add(exitVbox);
        hbox.alignmentProperty().set(Pos.CENTER);
        hbox.setSpacing(80);
        vbox.getChildren().add(winnerText);
        vbox.getChildren().add(hbox);
        vbox.alignmentProperty().set(Pos.CENTER);
        vbox.setSpacing(30);
        vbox.setBackground(background);
        stackPane.getChildren().add(vbox);
    }
}