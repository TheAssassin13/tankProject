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
import javafx.scene.control.ToggleButton;
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

public class GameController implements Initializable {
    @FXML
    public VBox vbox;
    public Canvas gameCanvas;
    public GraphicsContext gameCanvasGraphicContext;
    public Player turn;
    public Text currentPlayerText;
    public HBox buttonsPanel;
    public TextField angleTextField;
    public TextField powerTextField;
    public Terrain terrain;
    public ArrayList<Player> alivePlayers;
    public ArrayList<Player> deadPlayers;
    public Text maxHeightTextField;
    public int maxHeight;
    public Text maxDistanceTextField;
    public int maxDistance;
    public StackPane stackPane;
    public Button shootButton;
    public ImageView backgroundImage;
    public long lastUpdateTime;
    public Text currentPlayerLife;
    public ImageView currentPlayerLifeIcon;
    public HBox replayExitButtonsHbox;
    public Text lightAmmoQuantityText;
    public Text mediumAmmoQuantityText;
    public Text heavyAmmoQuantityText;
    public ToggleButton lightAmmoButton;
    public ToggleButton mediumAmmoButton;
    public ToggleButton heavyAmmoButton;
    public Button replayButton;
    public Button exitButton;
    public StackPane stackPaneCanvas;

    // Game interface, players and terrain initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gameCanvasGraphicContext = gameCanvas.getGraphicsContext2D();
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        for (int i = 0; i < Constants.TANKS_QUANTITY; i++) {
            this.alivePlayers.add(new Player("Player " + (i+1), Constants.TANK_COLORS[i], new Tank(Constants.TANK_COLORS[i], new Point(0, 0))));
        }
        this.turn = alivePlayers.get((int) Math.round(Math.random()));
        this.maxHeight = 0;
        this.maxDistance = 0;
        this.gameCanvas.setHeight(Constants.CANVAS_HEIGHT);
        this.gameCanvas.setWidth(Constants.WINDOWS_WIDTH);
        this.terrain = new Terrain(Constants.CANVAS_HEIGHT, Constants.WINDOWS_WIDTH);
        this.terrain.terrainGeneration(Constants.SEA_LEVEL, true);
        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/background.jpg")).toExternalForm()));
        this.backgroundImage.setFitHeight(Constants.CANVAS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.backgroundImage.setPreserveRatio(false);
        this.lastUpdateTime = 0;
        this.stackPane.setPrefHeight(Constants.WINDOWS_HEIGHT);
        this.stackPane.setPrefWidth(Constants.WINDOWS_WIDTH);
        this.stackPane.setMaxSize(Constants.WINDOWS_WIDTH,Constants.WINDOWS_HEIGHT);
        this.stackPaneCanvas.setPrefHeight(Constants.CANVAS_HEIGHT);
        this.stackPaneCanvas.setPrefWidth(Constants.WINDOWS_WIDTH);
        this.stackPaneCanvas.setMaxSize(Constants.WINDOWS_WIDTH,Constants.CANVAS_HEIGHT);
        this.vbox.setPrefHeight(Constants.WINDOWS_HEIGHT);
        this.vbox.setPrefWidth(Constants.WINDOWS_WIDTH);

        // Updates the direction of the barrel when the user types an angle
        angleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    double newAngle = Double.parseDouble(newValue);
                    this.turn.tank.setAngle(newAngle);
                } catch (NumberFormatException e) {
                    // Invalid element
                }
                drawingMethods(false);
            }
        });

        calculateMax(new Shot(new Point(0,0),0,0,0),this.turn.tank);
        tanksPlacement();
        buttonsPanelInitialize();
        drawingMethods(false);
    }

    // Initializes the buttons panel of the interface
    public void buttonsPanelInitialize() {
        this.replayButton = createReplayButton(25,25);
        this.exitButton = createExitButton(25,25);
        this.currentPlayerText.setText(turn.name + " is playing");
        this.currentPlayerLife.setText("Life : " + this.turn.getHealth() + " / 100");
        this.currentPlayerLifeIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("icons/half_heart_icon.png")).toExternalForm()));
        this.buttonsPanel.setPrefHeight(Constants.BUTTONS_PANEL_HEIGHT);
        this.buttonsPanel.setMinHeight(Constants.BUTTONS_PANEL_HEIGHT);
        this.buttonsPanel.setMaxHeight(Constants.BUTTONS_PANEL_HEIGHT);
        this.buttonsPanel.setPrefWidth(Constants.WINDOWS_WIDTH);
        this.buttonsPanel.setMinWidth(Constants.WINDOWS_WIDTH);
        this.buttonsPanel.setMaxWidth(Constants.WINDOWS_WIDTH);
        this.replayExitButtonsHbox.getChildren().add(this.replayButton);
        this.replayExitButtonsHbox.getChildren().add(this.exitButton);
        ammunitionPanelControl();
    }

    // All drawing methods that should render every frame
    public void drawingMethods(boolean collision) {
        this.gameCanvasGraphicContext.clearRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        // If there's a collision it draws the terrain without the optimization
        if (collision) Illustrator.drawTerrain(this.gameCanvasGraphicContext, this.terrain);
        else Illustrator.drawTerrainOptimized(this.gameCanvasGraphicContext, this.terrain);
        for (Player p : this.alivePlayers) {
            Illustrator.drawTank(this.gameCanvasGraphicContext, p.tank);
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
        this.maxHeight = 0;
        this.maxDistance = 0;
        // Checks if the input is not empty
        if (!powerTextField.getText().isEmpty() && !angleTextField.getText().isEmpty()) {
            this.turn.tank.setAngle(Double.parseDouble(angleTextField.getText()));
            this.turn.tank.power = Double.parseDouble(powerTextField.getText());
            // Creates shot from user input
            Shot shot = new Shot(new Point(turn.tank.position.getX(), turn.tank.position.getY()), Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()), 0);
            if (lightAmmoButton.isSelected()) shot.setDamage(Constants.AMMO_DAMAGE[0]);
            if (mediumAmmoButton.isSelected()) shot.setDamage(Constants.AMMO_DAMAGE[1]);
            if (heavyAmmoButton.isSelected()) shot.setDamage(Constants.AMMO_DAMAGE[2]);

            gameAnimationTimer(shot);
        }

        this.angleTextField.requestFocus();
   }

   public void gameAnimationTimer(Shot shot) {
       new AnimationTimer() {
           @Override
           public void handle(long now) {
               // Makes animation fps constant
               if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                   shot.shotPosition();
                   drawingMethods(false);
                   Illustrator.drawTrajectory(gameCanvasGraphicContext, shot);
                   Illustrator.drawShot(gameCanvasGraphicContext, shot);
                   shootButton.setDisable(true);
                   // Update max height and distance every frame
                   calculateMax(shot, turn.tank);

                        // Shot is out of the screen in the X-axis
                        if (shot.position.getX() >= Constants.WINDOWS_WIDTH || shot.position.getX() < 0) {
                            stop();
                            stopMethods();
                        }
                        // Checks if a tank is hit
                        if (tanksCollision(shot) != null) {
                            Player hitPlayer = tanksCollision(shot);
                            hitPlayer.reduceHealth(shot.getDamage());
                            stop();
                            terrain.destroyTerrain(shot.position, shot.area);
                            terrainFallAnimationTimer();
                            stopMethods();
                            if (hitPlayer.getHealth() <= 0) {
                                deleteDeadPlayer(hitPlayer);
                            }
                        }
                        // Checks if terrain is hit
                        if (shot.terrainCollision(terrain)) {
                            stop();
                            terrain.destroyTerrain(shot.position, shot.area);
                            terrainFallAnimationTimer();
                            stopMethods();
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

    public void terrainFallAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    drawingMethods(true);
                    // When the terrain stops falling, the animation stops
                    if (!terrain.terrainFalling()) stop();
                }
            }
        }.start();
    }

    // Calculates max height and distance of the shot and display it to the interface
    public void calculateMax(Shot shot, Tank tank) {
        this.maxHeight = Math.max(this.maxHeight, tank.position.getY() - shot.position.getY());
        this.maxDistance = Math.max(this.maxDistance, Math.abs(tank.position.getX() - shot.position.getX()));

        if (this.maxHeight < 0) this.maxHeight = 0;

        this.maxDistanceTextField.setText("Max distance = " + this.maxDistance + " m");
        this.maxHeightTextField.setText("Max height = " + this.maxHeight + " m");
    }

    // Encapsulation of methods in charge of turn change mechanic
    public void stopMethods() {
        changeTurn();
        drawingMethods(true);
        shootButton.setDisable(false);
        ammunitionPanelControl();

        // Saves last angle and power
        if (this.turn.tank.power != null || this.turn.tank.getAngle() != null) {
            this.angleTextField.setText(String.valueOf(this.turn.tank.getAngle()));
            this.powerTextField.setText(String.valueOf(this.turn.tank.power));
        }  else {
            this.angleTextField.clear();
            this.powerTextField.clear();
        }
        this.currentPlayerText.setText(this.turn.name + " is playing");
        this.currentPlayerLife.setText("Life : " +this.turn.getHealth() + " / 100");

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
    }

    // Creates the game win screen
    public void winScreen() {
        if (this.stackPane.getChildren().size() != 1) return;
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

        Button replayButton = createReplayButton(45,45);
        Button exitButton = createExitButton(45,45);

        // Disables buttons of the interface game
        this.angleTextField.setDisable(true);
        this.powerTextField.setDisable(true);
        this.shootButton.setDisable(true);
        this.replayButton.setDisable(true);
        this.exitButton.setDisable(true);
        this.lightAmmoButton.setDisable(true);
        this.mediumAmmoButton.setDisable(true);
        this.heavyAmmoButton.setDisable(true);

        replayLabel.setTextFill(Color.BLACK);
        exitLabel.setTextFill(Color.BLACK);
        replayLabel.setFont(labelFont);
        exitLabel.setFont(labelFont);
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
        this.stackPane.getChildren().add(vbox);
    }

    public Button createReplayButton(int height, int width) {
        Image replayIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/replay_icon_white.png")).toExternalForm());
        ImageView replayIconView = new ImageView(replayIcon);
        Button replayButton = new Button("",replayIconView);

        replayButton.getStyleClass().add("winScreenButton");
        replayButton.setId("replayWinButton");
        replayButton.setPrefHeight(height);
        replayButton.setPrefWidth(width);
        replayButton.setMaxSize(width,height);
        replayIconView.setFitHeight(height);
        replayIconView.setFitWidth(width);

        // Replay button action
        replayButton.setOnAction(event -> {
            try {
                restartGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return replayButton;
    }

    public Button createExitButton(int height, int width) {
        Image exitIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/exit_icon_white.png")).toExternalForm());
        ImageView exitIconView = new ImageView(exitIcon);
        Button exitButton = new Button("",exitIconView);

        exitButton.getStyleClass().add("winScreenButton");
        exitButton.setId("exitWinButton");
        exitButton.setPrefHeight(height);
        exitButton.setPrefWidth(width);
        exitButton.setMaxSize(width,height);
        exitIconView.setFitHeight(height);
        exitIconView.setFitWidth(width);

        // Exit button action
        exitButton.setOnAction(event -> Platform.exit());

        return exitButton;
    }

    public void ammunitionPanelControl() {
        this.lightAmmoQuantityText.setText(this.turn.tank.ammunition.get("Bullet30") + " / 3");
        this.mediumAmmoQuantityText.setText(this.turn.tank.ammunition.get("Bullet40") + " / 10");
        this.heavyAmmoQuantityText.setText(this.turn.tank.ammunition.get("Bullet50") + " / 3");

    }
}

