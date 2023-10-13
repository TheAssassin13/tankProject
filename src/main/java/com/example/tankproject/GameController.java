package com.example.tankproject;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import static com.example.tankproject.App.toHexString;

public class GameController implements Initializable {
    @FXML
    public VBox gameVBox;
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
    public Text currentTankHealth;
    public ImageView currentTankHealthIcon;
    public HBox replayExitButtonsHbox;
    public Text lightAmmoQuantityText;
    public Text mediumAmmoQuantityText;
    public Text heavyAmmoQuantityText;
    public ToggleButton lightAmmoButton;
    public ToggleButton mediumAmmoButton;
    public ToggleButton heavyAmmoButton;
    public Button replayButton;
    public Button exitButton;
    public Button menuExitButton;
    public StackPane stackPaneCanvas;
    public VBox winScreenVBox;
    public ImageView currentPlayerTankImage;
    public StackPane currentPlayerTankStackPane;
    public ToggleGroup ammunitionButtons;
    public Circle lightAmmoQuantityLight;
    public Circle mediumAmmoQuantityLight;
    public Circle heavyAmmoQuantityLight;
    public Rectangle tankRadarPointer;
    public Rotate tankRadarPointerRotate;
    public StackPane tankRadarStackPane;
    public Random random;
    public ArrayList<MysteryBox> boxes;
    public Media backgroundMusic;
    public MediaPlayer music;
    public MediaPlayer sounds;
    public Image umbrella;
    public Point umbrellaPosition;
    public HBox healthRemainingHBox;
    public HealthRemainingHUD healthRemainingHUD;

    // Initializes JavaFX windows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       gameInitialize();
    }

    // Encapsulation of all methods responsible for initializing the game
    public void gameInitialize() {
        this.gameCanvasGraphicContext = gameCanvas.getGraphicsContext2D();
        this.maxHeight = 0;
        this.maxDistance = 0;
        this.lastUpdateTime = 0;
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.angleTextField.clear();
        this.powerTextField.clear();
        this.mediumAmmoButton.setSelected(true);
        for (int i = 0; i < Constants.TANKS_QUANTITY; i++) {
            //this.alivePlayers.add(new Player("Player " + (1), Constants.TANK_COLORS[0], new Tank(Constants.TANK_COLORS[0], new Point(0, 0))));
            //this.alivePlayers.add(new CPU("CPU " + (1), Constants.TANK_COLORS[1], new Tank(Constants.TANK_COLORS[1], new Point(0, 0))));
            this.alivePlayers.add(new Player("Player " + (i+1), Constants.TANK_COLORS[i], new Tank(Constants.TANK_COLORS[i], new Point(0, 0))));

        }

        for (Player p: this.alivePlayers) {
            if (p instanceof CPU) {
                this.turn = this.alivePlayers.get(0);
                break;
            }
        }
        if (this.turn == null) this.turn = this.alivePlayers.get((int) Math.round(Math.random()));
        this.terrain = new Terrain(Constants.CANVAS_HEIGHT, Constants.WINDOWS_WIDTH);
        this.terrain.terrainGeneration(Constants.SEA_LEVEL, true);
        this.backgroundImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/background_image.jpg")).toExternalForm()));
        this.backgroundMusic = new Media(Objects.requireNonNull(getClass().getResource("music/gameMusic.mp3")).toExternalForm());
        this.music = new MediaPlayer(backgroundMusic);
        this.music.setCycleCount(MediaPlayer.INDEFINITE);
        this.music.play();
        this.tankRadarStackPane.getChildren().clear();
        random = new Random();
        boxes = new ArrayList<>();
        this.stackPane.getChildren().remove(this.healthRemainingHBox);
        this.healthRemainingHUD = new HealthRemainingHUD();
        this.healthRemainingHBox = this.healthRemainingHUD.healthRemainingHBox;

        this.stackPane.getChildren().add(this.healthRemainingHBox);
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

        buttonsPanelInitialize();
        tankRadarInitialize();
        calculateMax(new Shot(new Point(0,0),0,0),this.turn.tank);
        tanksPlacement();
        componentsSizesInitialize();
        drawingMethods(false);
        buttonsActionInitialize();
        ammunitionPanelControl();
        this.healthRemainingHUD.healthRemainingBoxMouseEvents(this.alivePlayers);
    }

    // Encapsulation of all methods responsible for initializing buttons of the interface game actions
    public void buttonsActionInitialize() {
        setReplayButtonAction(this.replayButton);
        setExitButtonAction(this.exitButton);
        setMenuExitButtonAction(this.menuExitButton);
    }

    // Sets replay button action
    public void setReplayButtonAction(Button replayButton) {
        replayButton.setOnAction(event -> {
            // WinScreen is in screen
            if (this.stackPane.getChildren().size() != 1) {
                disableWinScreen();
            }
            music.stop();
            gameInitialize();
        });
    }

    // Sets exit button action
    public void setExitButtonAction(Button exitButton) {
        exitButton.setOnAction(event -> Platform.exit());
    }

    // Sets menu exit button action
    public void setMenuExitButtonAction(Button menuExitButton) {
        menuExitButton.setOnAction(event -> {
            try {
                this.music.stop();
                App.setRoot("menu");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Encapsulation of methods responsible for changing the size of components
    public void componentsSizesInitialize() {
        this.gameCanvas.setHeight(Constants.CANVAS_HEIGHT);
        this.gameCanvas.setWidth(Constants.WINDOWS_WIDTH);
        this.backgroundImage.setFitHeight(Constants.CANVAS_HEIGHT);
        this.backgroundImage.setFitWidth(Constants.WINDOWS_WIDTH);
        this.backgroundImage.setPreserveRatio(false);
        this.stackPane.setPrefHeight(Constants.WINDOWS_HEIGHT);
        this.stackPane.setPrefWidth(Constants.WINDOWS_WIDTH);
        this.stackPane.setMaxSize(Constants.WINDOWS_WIDTH,Constants.WINDOWS_HEIGHT);
        this.stackPaneCanvas.setPrefHeight(Constants.CANVAS_HEIGHT);
        this.stackPaneCanvas.setPrefWidth(Constants.WINDOWS_WIDTH);
        this.stackPaneCanvas.setMaxSize(Constants.WINDOWS_WIDTH,Constants.CANVAS_HEIGHT);
        this.gameVBox.setPrefHeight(Constants.WINDOWS_HEIGHT);
        this.gameVBox.setPrefWidth(Constants.WINDOWS_WIDTH);
        this.buttonsPanel.setPrefHeight(Constants.BUTTONS_PANEL_HEIGHT);
        this.buttonsPanel.setMinHeight(Constants.BUTTONS_PANEL_HEIGHT);
        this.buttonsPanel.setMaxHeight(Constants.BUTTONS_PANEL_HEIGHT);
        this.buttonsPanel.setPrefWidth(Constants.WINDOWS_WIDTH);
        this.buttonsPanel.setMinWidth(Constants.WINDOWS_WIDTH);
        this.buttonsPanel.setMaxWidth(Constants.WINDOWS_WIDTH);
        this.currentPlayerTankStackPane.setPrefWidth(120);
        this.currentPlayerTankStackPane.setPrefHeight(80);
        this.currentPlayerTankImage.setFitWidth(120);
        this.currentPlayerTankImage.setFitHeight(80);
    }

    // All drawing methods that should render every frame
    public void drawingMethods(boolean collision) {
        this.gameCanvasGraphicContext.clearRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        if (umbrella != null) gameCanvasGraphicContext.drawImage(umbrella, umbrellaPosition.getX(), umbrellaPosition.getY(), 55.6, 61.2);
        // If there's a collision it draws the terrain without the optimization
        if (collision) this.terrain.drawTerrain(this.gameCanvasGraphicContext);
        else this.terrain.drawTerrainOptimized(this.gameCanvasGraphicContext);
        for (Player p : this.alivePlayers) {
            p.tank.drawTank(this.gameCanvasGraphicContext);
        }
        for (MysteryBox box : this.boxes) {
            box.drawMysteryBox(gameCanvasGraphicContext);
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
        // Checks if there is only one player left
        if (alivePlayers.size() == 1) {
            winScreen();
        }
    }

    // Manages the shoot button action in the interface. Create the shot from the angle and initial velocity from user input, check for collision and turn changes.
    public void onShootButtonClick(ActionEvent ignoredActionEvent) {
        this.umbrella = null;
        this.maxHeight = 0;
        this.maxDistance = 0;
        // Checks if the input is not empty
        if (!powerTextField.getText().isEmpty() && !angleTextField.getText().isEmpty()) {
            this.turn.tank.setAngle(Double.parseDouble(angleTextField.getText()));
            this.turn.tank.power = Double.parseDouble(powerTextField.getText());
            this.turn.tank.setAmmoSelected((ToggleButton) this.ammunitionButtons.getSelectedToggle());
            // There's a 1/5 chance a mystery box appears
            if (random.nextInt(5) == 1) {
                mysteryBoxAppears();
            }

            ToggleButton selectedAmmo = this.turn.tank.getAmmoSelected();
            Shot shot;

            if (selectedAmmo == this.lightAmmoButton){
                shot = new LightShot(new Point(turn.tank.position.getX(), turn.tank.position.getY()), Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()));
                int SubtractionAMMO = this.turn.tank.ammunition.get(0); 
                this.turn.tank.ammunition.set(0, SubtractionAMMO - 1);
            }
            else if(selectedAmmo == this.mediumAmmoButton){
                shot = new MediumShot(new Point(turn.tank.position.getX(), turn.tank.position.getY()), Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()));
                int SubtractionAMMO = this.turn.tank.ammunition.get(1); 
                this.turn.tank.ammunition.set(1, SubtractionAMMO - 1);
            }
                else{
                shot = new HeavyShot(new Point(turn.tank.position.getX(), turn.tank.position.getY()), Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()));
                int SubtractionAMMO = this.turn.tank.ammunition.get(2); 
                this.turn.tank.ammunition.set(2, SubtractionAMMO - 1);
            }
            gameAnimationTimer(shot, true);
        }
        this.angleTextField.requestFocus();
   }

    public void gameAnimationTimer(Shot shot, boolean fromTank) {
       new AnimationTimer() {
           @Override
           public void handle(long now) {
               // Makes animation fps constant
               if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                   shot.shotPosition();
                   drawingMethods(!fromTank);
                   shot.drawTrajectory(gameCanvasGraphicContext);
                   shot.drawShot(gameCanvasGraphicContext);
                   shootButton.setDisable(true);
                   replayButton.setDisable(true);

                   // Update tank radar every frame
                   tankRadarUpdate(shot);
                   // Update max height and distance every frame
                   calculateMax(shot, turn.tank);

                   // Shot is out of the screen
                   if (shot.position.getX() >= Constants.WINDOWS_WIDTH || shot.position.getX() < 0
                        || shot.position.getY() >= Constants.CANVAS_HEIGHT) {
                       stop();
                       if (fromTank) stopMethods();
                   }

                   // Checks all the possible collisions
                   if (shotCollision(shot, fromTank)) stop();

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
                    vibration();
                    // When the terrain stops falling, the animation stops
                    if (!terrain.terrainFalling()) stop();
                }
            }
        }.start();
    }

    public void tankFallAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    drawingMethods(false);
                    int flag = 0;
                    for (Player p : alivePlayers) {
                        if (p.tank.position.getY() + Constants.TANK_SIZE/3 < Constants.CANVAS_HEIGHT &&
                                terrain.resolutionMatrix[p.tank.position.getY() + Constants.TANK_SIZE/3][p.tank.position.getX()] == 0) {
                            p.tank.position.setY(p.tank.position.getY()+1);
                            p.tank.reduceHealth(1);
                            if (p.tank.getHealth() <= 0) {
                                deleteDeadPlayer(p);
                            }
                            flag = 1;
                        }
                    }
                    if (flag == 0) stop();
                }
            }
        }.start();
    }

    public void mysteryBoxFallAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    drawingMethods(false);
                    int flag = 0;
                    for (MysteryBox box : boxes) {
                        if (box.position.getY() + Constants.BOX_SIZE/2 + 1 < Constants.CANVAS_HEIGHT &&
                                terrain.resolutionMatrix[box.position.getY() + Constants.BOX_SIZE/2 + 1][box.position.getX()] == 0) {
                            box.position.setY(box.position.getY()+1);
                            flag = 1;
                        }
                    }
                    if (flag == 0) stop();
                }
            }
        }.start();
    }

    public void mysteryBoxAppears() {
        int boxPositionX;
        boolean isOnTank = false;
        while(true) {
            boxPositionX = random.nextInt(Constants.WINDOWS_WIDTH);
            for (Player p : alivePlayers) {
                if (boxPositionX < p.tank.position.getX() + Constants.TERRAIN_MARGIN &&
                        boxPositionX > p.tank.position.getX() - Constants.TERRAIN_MARGIN){
                    isOnTank = true;
                    break;
                }
            }
            if (isOnTank) isOnTank = false;
            else break;
        }
        boxes.add(new MysteryBox(new Point(boxPositionX, 0)));
        mysteryBoxFallAnimationTimer();
    }

    public void mysteryBoxPower(MysteryBox box) {
        if (box.powerUp == 0) {
            turn.tank.restoreHealth();
        } else if (box.powerUp == 1) {
            this.umbrella = new Image(Objects.requireNonNull(getClass().getResource("images/umbrella.png")).toExternalForm());
            this.umbrellaPosition = new Point(turn.tank.position.getX() - Constants.TANK_SIZE, turn.tank.position.getY()-30-Constants.TANK_SIZE);
            bombardment();
        }
    }

    public void bombardment() {
        for (int i = 0; i < 10; i++) {
            gameAnimationTimer(new LightShot(new Point(random.nextInt(Constants.WINDOWS_WIDTH - 1), 0), 10, -90), false);
        }
    }

    public void vibration() {
        gameCanvasGraphicContext.translate(random.nextInt(-2,2), random.nextInt(-2,2));
    }

    // Checks all the possible collisions with a shot
    public boolean shotCollision(Shot shot, boolean fromTank) {
        // Checks if a tank is hit
        if (tanksCollision(shot) != null) {
            Player hitPlayer = tanksCollision(shot);
            hitPlayer.tank.reduceHealth(shot.getDamage());
            terrain.destroyTerrain(shot.position, shot.area);
            terrainFallAnimationTimer();
            tankFallAnimationTimer();
            healthRemainingHUD.healthRemainingBox(hitPlayer.tank);
            this.sounds = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/boom.mp3")).toExternalForm()));
            sounds.play();
            if (fromTank) stopMethods();
            if (hitPlayer.tank.getHealth() <= 0) {
                deleteDeadPlayer(hitPlayer);
            }
            return true;
        }
        // Checks if terrain is hit
        else if (shot.terrainCollision(terrain)) {
            terrain.destroyTerrain(shot.position, shot.area);
            terrainFallAnimationTimer();
            tankFallAnimationTimer();
            this.sounds = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/boom.mp3")).toExternalForm()));
            sounds.play();
            if (fromTank) stopMethods();
            return true;
        }
        // Checks if a box is hit
        else if (fromTank) {
            for (MysteryBox box : boxes) {
                if (shot.mysteryBoxCollision(box)) {
                    boxes.remove(box);
                    terrain.destroyTerrain(shot.position, shot.area);
                    terrainFallAnimationTimer();
                    tankFallAnimationTimer();
                    sounds = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/powerup.mp3")).toExternalForm()));
                    sounds.play();
                    mysteryBoxPower(box);
                    stopMethods();
                    return true;
                }
            }
        }
        return false;
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
        replayButton.setDisable(false);
        ammunitionPanelControl();
        // Saves last angle and power
        if (this.turn.tank.power != null || this.turn.tank.getAngle() != null) {
            this.angleTextField.setText(String.valueOf(this.turn.tank.getAngle()));
            this.powerTextField.setText(String.valueOf(this.turn.tank.power));
        }  else {
            this.angleTextField.clear();
            this.powerTextField.clear();
        }

        // Saves last selected ammunition
        if (this.turn.tank.getAmmoSelected() != null) {
            this.ammunitionButtons.selectToggle(this.turn.tank.getAmmoSelected());
        } else {
            this.ammunitionButtons.selectToggle(this.mediumAmmoButton);
        }

        this.tankRadarPointerRotate.setAngle(0);
        this.currentPlayerText.setText(this.turn.name + " is playing");
        this.currentTankHealth.setText("Health : " + this.turn.tank.getHealth() + " / 100");
        this.currentTankHealthIcon.setImage(ComponentsCreator.healthIcon(this.turn.tank));
        this.currentPlayerTankStackPane.setStyle(this.currentPlayerTankStackPane.getStyle() + "-fx-background-color: " + toHexString(this.turn.tank.color) + ";");
        if (this.turn instanceof CPU) ((CPU) this.turn).shoot(shootButton, angleTextField, powerTextField, this.alivePlayers.get(0).tank.position);
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
        if (this.healthRemainingHBox != null) this.stackPane.getChildren().remove(this.healthRemainingHBox);
        if (this.stackPane.getChildren().size() != 1) return;
        Button replayButton = ComponentsCreator.createReplayButton(40,40);
        Button exitButton = ComponentsCreator.createExitButton(40,40);
        VBox backgroundVbox = ComponentsCreator.createWinScreenVBox(this.alivePlayers.get(0),replayButton,exitButton);

        setReplayButtonAction(replayButton);
        setExitButtonAction(exitButton);

        // Disables buttons of the interface game
        this.angleTextField.setDisable(true);
        this.powerTextField.setDisable(true);
        this.shootButton.setDisable(true);
        this.replayButton.setDisable(true);
        this.exitButton.setDisable(true);
        this.menuExitButton.setDisable(true);
        this.lightAmmoButton.setDisable(true);
        this.mediumAmmoButton.setDisable(true);
        this.heavyAmmoButton.setDisable(true);

        this.winScreenVBox = backgroundVbox;
        this.stackPane.getChildren().add(this.winScreenVBox);

        music.stop();
        music = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/victory.mp3")).toExternalForm()));
        music.play();
    }

    // Disables win screen to keep playing
    public void disableWinScreen() {
        this.stackPane.getChildren().remove(this.winScreenVBox);
        this.angleTextField.setDisable(false);
        this.powerTextField.setDisable(false);
        this.shootButton.setDisable(false);
        this.replayButton.setDisable(false);
        this.exitButton.setDisable(false);
        this.menuExitButton.setDisable(false);
        this.lightAmmoButton.setDisable(false);
        this.mediumAmmoButton.setDisable(false);
        this.heavyAmmoButton.setDisable(false);
    }

    // Initializes the buttons panel of the interface
    public void buttonsPanelInitialize() {
        Image heartIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());
        this.replayExitButtonsHbox.getChildren().clear();
        this.replayButton = ComponentsCreator.createReplayButton(25,25);
        this.exitButton = ComponentsCreator.createExitButton(25,25);
        this.menuExitButton = ComponentsCreator.createMenuExitButton(25,25);
        this.currentPlayerText.setText(turn.name + " is playing");
        this.currentTankHealth.setText("Health : " + this.turn.tank.getHealth() + " / 100");
        this.replayExitButtonsHbox.getChildren().add(this.replayButton);
        this.replayExitButtonsHbox.getChildren().add(this.menuExitButton);
        this.replayExitButtonsHbox.getChildren().add(this.exitButton);
        this.currentTankHealthIcon.setImage(heartIcon);
        this.currentPlayerTankImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/current_tank_image.png")).toExternalForm()));
        this.currentPlayerTankStackPane.setStyle(this.currentPlayerTankStackPane.getStyle() + "-fx-background-color: " + toHexString(this.turn.tank.color) + ";");
        ammunitionPanelControlInitialize();
    }

    // Encapsulation of methods responsible for updating remaining ammo and ammo lights color every turn
    public void ammunitionPanelControl() {
        this.lightAmmoQuantityText.setText(this.turn.tank.ammunition.get(0) + " / 3");
        this.mediumAmmoQuantityText.setText(this.turn.tank.ammunition.get(1) + " / 10");
        this.heavyAmmoQuantityText.setText(this.turn.tank.ammunition.get(2) + " / 3");
        if (this.turn.tank.ammunition.get(0) == Constants.AMMO_QUANTITY[0]) this.lightAmmoQuantityLight.setFill(Color.GREEN);
        if (this.turn.tank.ammunition.get(0) <= Constants.AMMO_QUANTITY[0] / 2) this.lightAmmoQuantityLight.setFill(Color.YELLOW);
        if (this.turn.tank.ammunition.get(0) == 0) this.lightAmmoQuantityLight.setFill(Color.RED);
        if (this.turn.tank.ammunition.get(1) == Constants.AMMO_QUANTITY[1]) this.mediumAmmoQuantityLight.setFill(Color.GREEN);
        if (this.turn.tank.ammunition.get(1) <= Constants.AMMO_QUANTITY[1] / 2) this.mediumAmmoQuantityLight.setFill(Color.YELLOW);
        if (this.turn.tank.ammunition.get(1) == 0) this.mediumAmmoQuantityLight.setFill(Color.RED);
        if (this.turn.tank.ammunition.get(2) == Constants.AMMO_QUANTITY[2]) this.heavyAmmoQuantityLight.setFill(Color.GREEN);
        if (this.turn.tank.ammunition.get(2) <= Constants.AMMO_QUANTITY[2] / 2) this.heavyAmmoQuantityLight.setFill(Color.YELLOW);
        if (this.turn.tank.ammunition.get(2) == 0) this.heavyAmmoQuantityLight.setFill(Color.RED);
    }

    // Encapsulation of methods responsible for initializing toggle buttons, data related
    public void ammunitionPanelControlInitialize() {
        this.lightAmmoButton.setUserData(Constants.AMMO_DAMAGE[0]);
        this.mediumAmmoButton.setUserData(Constants.AMMO_DAMAGE[1]);
        this.heavyAmmoButton.setUserData(Constants.AMMO_DAMAGE[2]);
        // Verifies that is always a button selected
        this.ammunitionButtons.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == null) {
                // If no button is selected, selects the last one
                this.ammunitionButtons.selectToggle(oldToggle);
            }
        });
    }

    // Initializes the tank radar of the interface
    public void tankRadarInitialize() {
        int height = 45;
        int width = 3;
        Rectangle pointer = ComponentsCreator.createTankRadarPointer(height,width);
        Circle background = ComponentsCreator.createTankRadarCircle(height);

        this.tankRadarPointerRotate = new Rotate();
        this.tankRadarPointerRotate.setPivotX(width);
        this.tankRadarPointerRotate.setPivotY(height);
        this.tankRadarPointerRotate.setAngle(0);
        this.tankRadarStackPane.getChildren().add(background);
        this.tankRadarStackPane.getChildren().add(pointer);
        this.tankRadarPointer = pointer;

        pointer.getTransforms().addAll(this.tankRadarPointerRotate);
    }

    // Changes the angle of the radar pointer every frame
    public void tankRadarUpdate(Shot shot) {
        double screenXRadar = this.tankRadarStackPane.localToScene(0,0).getX();
        double screenYRadar = this.tankRadarStackPane.localToScene(0,0).getY();
        double posXRadar = screenXRadar + this.tankRadarStackPane.getWidth() / 2;
        double posYRadar = Constants.WINDOWS_HEIGHT - (screenYRadar + this.tankRadarStackPane.getHeight() / 2);
        double posXShot = shot.position.getX();
        double posYShot = Constants.CANVAS_HEIGHT - shot.position.getY();
        double deltaX = posXShot - posXRadar;
        double deltaY = posYShot - posYRadar;
        double angle;

        angle = Math.toDegrees(Math.atan(deltaX / deltaY));

        if (angle >= -75 && angle <= 75) {
            this.tankRadarPointerRotate.setAngle(angle);
        }
            }
        
}

