package com.example.tankproject;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    public VBox replayExitButtonsVbox;
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
    public Media backgroundMusic;
    public MediaPlayer music;
    public MediaPlayer sounds;
    public HBox healthRemainingHBox;
    public TankInfoHUD tankInfoHUD;
    public ImageView explosionAnimation;
    public AnimationsCreator animationsCreator;
    public Text currentTankKills;
    public Text currentTankCredits;
    public ImageView windDirectionImageView;
    public Text windVelocityText;
    public VBox windHUDVBox;
    public Shot shot;
    public ImageView lightAmmoImageView;
    public ImageView mediumAmmoImageView;
    public ImageView heavyAmmoImageView;


    // Initializes JavaFX windows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameInitialize();
    }

    // Encapsulation of all methods responsible for initializing the game and rounds
    public void gameInitialize() {
        this.gameCanvasGraphicContext = gameCanvas.getGraphicsContext2D();
        this.random = new Random();
        this.maxHeight = 0;
        this.maxDistance = 0;
        this.lastUpdateTime = 0;
        this.angleTextField.clear();
        this.powerTextField.clear();
        this.mediumAmmoButton.setSelected(true);
        Data.getInstance().terrain = new Terrain(Data.getInstance().canvasHeight, Data.getInstance().windowsWidth);
        Data.getInstance().terrain.terrainGeneration(Data.getInstance().seaLevel, true);
        this.backgroundImage.setImage(Loader.getInstance().currentBackgrounds.get(1));
        this.backgroundMusic = Loader.getInstance().currentBackgroundMusic.get(1);
        this.music = new MediaPlayer(backgroundMusic);
        this.music.setCycleCount(MediaPlayer.INDEFINITE);
        this.music.play();
        this.music.setVolume(Data.getInstance().musicVolume);
        this.tankRadarStackPane.getChildren().clear();
        this.stackPane.getChildren().remove(this.healthRemainingHBox);
        this.tankInfoHUD = new TankInfoHUD();
        this.healthRemainingHBox = this.tankInfoHUD.tankInfoHBox;
        this.stackPane.getChildren().add(this.healthRemainingHBox);
        this.stackPane.getChildren().remove(this.explosionAnimation);
        this.animationsCreator = new AnimationsCreator();
        this.explosionAnimation = this.animationsCreator.explosionImageView;
        this.stackPane.getChildren().add(this.explosionAnimation);
        this.maxDistanceTextField.setText("Max distance = 0 m");
        this.maxHeightTextField.setText("Max height = 0 m");
        this.windHUDVBox.setVisible(Data.getInstance().wind);

        updateWindHUD();
        tanksPlacement();
        shuffleTurn();
        buttonsPanelInitialize();
        tankRadarInitialize();
        componentsSizesInitialize();
        drawingMethods(false);
        buttonsActionInitialize();
        ammunitionPanelControl();
        this.tankInfoHUD.HUDMouseEvents(Data.getInstance().alivePlayers);

        // If CPU plays first
        if (this.turn instanceof CPU)
            ((CPU) this.turn).shoot(shootButton, lightAmmoButton, mediumAmmoButton, heavyAmmoButton, angleTextField, powerTextField);
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
            music.stop();
            Data.getInstance().reset();
            try {
                App.setRoot("interlude");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        this.gameCanvas.setHeight(Data.getInstance().canvasHeight);
        this.gameCanvas.setWidth(Data.getInstance().windowsWidth);
        this.backgroundImage.setFitHeight(Data.getInstance().canvasHeight);
        this.backgroundImage.setFitWidth(Data.getInstance().windowsWidth);
        this.backgroundImage.setPreserveRatio(false);
        this.stackPane.setPrefHeight(Data.getInstance().windowsHeight);
        this.stackPane.setPrefWidth(Data.getInstance().windowsWidth);
        this.stackPane.setMaxSize(Data.getInstance().windowsWidth, Data.getInstance().windowsHeight);
        this.stackPaneCanvas.setPrefHeight(Data.getInstance().canvasHeight);
        this.stackPaneCanvas.setPrefWidth(Data.getInstance().windowsWidth);
        this.stackPaneCanvas.setMaxSize(Data.getInstance().windowsWidth, Data.getInstance().canvasHeight);
        this.gameVBox.setPrefHeight(Data.getInstance().windowsHeight);
        this.gameVBox.setPrefWidth(Data.getInstance().windowsWidth);
        this.buttonsPanel.setPrefHeight(Data.getInstance().buttonsPanelHeight);
        this.buttonsPanel.setMinHeight(Data.getInstance().buttonsPanelHeight);
        this.buttonsPanel.setMaxHeight(Data.getInstance().buttonsPanelHeight);
        this.buttonsPanel.setPrefWidth(Data.getInstance().windowsWidth);
        this.buttonsPanel.setMinWidth(Data.getInstance().windowsWidth);
        this.buttonsPanel.setMaxWidth(Data.getInstance().windowsWidth);
        this.currentPlayerTankStackPane.setPrefWidth(120);
        this.currentPlayerTankStackPane.setPrefHeight(80);
        this.currentPlayerTankImage.setFitWidth(120);
        this.currentPlayerTankImage.setFitHeight(80);
    }

    // All drawing methods that should render every frame
    public void drawingMethods(boolean collision) {
        this.gameCanvasGraphicContext.clearRect(0, 0, Data.getInstance().windowsWidth, Data.getInstance().windowsHeight);
        // If there's a collision it draws the terrain without the optimization
        if (collision) Data.getInstance().terrain.drawTerrain(this.gameCanvasGraphicContext);
        else Data.getInstance().terrain.drawTerrainOptimized(this.gameCanvasGraphicContext);
        for (Player p : Data.getInstance().alivePlayers) {
            p.tank.drawTank(this.gameCanvasGraphicContext);
        }
        for (MysteryBox box : Data.getInstance().mysteryBoxes) {
            box.drawMysteryBox(gameCanvasGraphicContext);
        }
    }

    // Placement of tanks on terrain with random distance
    public void tanksPlacement() {
        int gap = Data.getInstance().windowsWidth / Data.getInstance().tanksQuantity;
        ArrayList<Point> tanksPosition = new ArrayList<>();

        // Position of the first tank
        int x = (int) Math.max(Math.random() * 2 * Data.getInstance().windowsWidth / (Data.getInstance().tanksQuantity * 2 + 1), Constants.TANK_SIZE);
        int y = Data.getInstance().canvasHeight - Constants.TANK_SIZE;
        tanksPosition.add(new Point(x, y));

        for (int i = 1; i < Data.getInstance().tanksQuantity; i++) {
            x = (int) (tanksPosition.get(i - 1).getX() + gap + (gap / Data.getInstance().tanksQuantity) * Math.random());
            tanksPosition.add(new Point(x, y));
        }

        // In case the last one gets out of bound
        if (tanksPosition.get(Data.getInstance().tanksQuantity - 1).getX() + Constants.TANK_SIZE / 2 >= Data.getInstance().windowsWidth)
            tanksPosition.get(Data.getInstance().tanksQuantity - 1).setX(Data.getInstance().windowsWidth - (Constants.TANK_SIZE * 2));

        for (int i = 0; i < Data.getInstance().tanksQuantity; i++) {
            Data.getInstance().alivePlayers.get(i).tank.position.setX(tanksPosition.get(i).getX());
        }

        // Positions tanks on terrain
        for (int i = 0; i < Data.getInstance().tanksQuantity; i++) {
            for (int j = 0; j < Data.getInstance().canvasHeight; j++) {
                if (Data.getInstance().terrain.resolutionMatrix[j][tanksPosition.get(i).getX()] == 1) {
                    Data.getInstance().alivePlayers.get(i).tank.position.setY(j - Constants.TANK_SIZE / 3);
                    break;
                }
            }
        }
    }

    /* Checks if a player's tank, is hit and return the hit player.
    As the method from shot tankCollision() returns a value between 0 and 1, we can use it to know
    if the tank was hit or the shot was close. When the shot is nearby we only return the player
    if the method returns a number greater than 0.1
     */
    public Player tanksCollision(Shot shot, boolean nearby) {
        double minimumDistance;
        if (nearby) minimumDistance = 0.1;
        else minimumDistance = 1;
        for (Player p : Data.getInstance().alivePlayers) {
            if (shot.tankCollision(p.tank) >= minimumDistance) {
                return p;
            }
        }
        return null;
    }

    // Removes hit player from alive players and add to dead players. It returns true if there's only one player left
    public boolean deleteDeadPlayer(Player player) {
        this.sounds.stop();
        this.sounds = new MediaPlayer(Loader.getInstance().currentSoundEffects.get(4));
        this.sounds.setVolume(Data.getInstance().SFXVolume);
        this.sounds.play();
        Data.getInstance().deadPlayers.add(player);
        Data.getInstance().alivePlayers.remove(player);
        if (this.shot.shotPlayer != player) {
            Shop.loadCredits(this.shot.shotPlayer, Constants.CREDITS_FOR_DESTROYING_TANKS);
            this.shot.shotPlayer.tank.kills++;
            this.shot.shotPlayer.score += Constants.POINTS_FOR_DESTROYING_TANKS;
        } else {
            Shop.reduceCredits(this.shot.shotPlayer, Constants.CREDITS_FOR_DESTROYING_TANKS);
            this.shot.shotPlayer.score = Math.max(0, this.shot.shotPlayer.score - Constants.POINTS_FOR_DESTROYING_TANKS);
        }

        // Checks if there is only one player left
        if (Data.getInstance().alivePlayers.size() == 1) {
            this.music.stop();

            if (Data.getInstance().gameNumber != Data.getInstance().gamesMax) Data.getInstance().gameNumber++;

            goToInterlude();
            return true;
        }
        return false;
    }

    /* This method manages the shoot button action in the interface. It creates the shot with the angle
    and initial velocity from user input, calling the method with the animation of the shot.
     */
    public void onShootButtonClick(ActionEvent ignoredActionEvent) {
        this.maxHeight = 0;
        this.maxDistance = 0;
        // Checks if the input is not empty
        if (!powerTextField.getText().isEmpty() && !angleTextField.getText().isEmpty()) {
            try {
                this.turn.tank.setAngle(Double.parseDouble(angleTextField.getText()));
                this.turn.tank.power = Double.parseDouble(powerTextField.getText());
            } catch (NumberFormatException e) {
                this.turn.tank.setAngle(null);
                this.turn.tank.power = null;
                return;
            }
            this.turn.tank.setAmmoSelected((ToggleButton) this.ammunitionButtons.getSelectedToggle());
            // There's a 1/5 chance a mystery box appears
            if (random.nextInt(5) == 1) {
                mysteryBoxAppears();
            }

            ToggleButton selectedAmmo = this.turn.tank.getAmmoSelected();
            double shotInitialX = Math.cos(Math.toRadians(this.turn.tank.getAngle())) * Constants.TANK_SIZE;
            double shotInitialY = Math.sin(Math.toRadians(this.turn.tank.getAngle())) * Constants.TANK_SIZE + Constants.TANK_SIZE * 0.6;
            Point shotInitialPosition = new Point((int) (turn.tank.position.getX() + shotInitialX), (int) (turn.tank.position.getY() - shotInitialY));

            if (selectedAmmo == this.lightAmmoButton) {
                this.shot = new LightShot(shotInitialPosition, Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()), this.turn);
                int SubtractionAMMO = this.turn.tank.ammunition.get(0);
                this.turn.tank.ammunition.set(0, SubtractionAMMO - 1);
            } else if (selectedAmmo == this.mediumAmmoButton) {
                this.shot = new MediumShot(shotInitialPosition, Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()), this.turn);
                int SubtractionAMMO = this.turn.tank.ammunition.get(1);
                this.turn.tank.ammunition.set(1, SubtractionAMMO - 1);
            } else {
                this.shot = new HeavyShot(shotInitialPosition, Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()), this.turn);
                int SubtractionAMMO = this.turn.tank.ammunition.get(2);
                this.turn.tank.ammunition.set(2, SubtractionAMMO - 1);
            }
            gameAnimationTimer(this.shot);
        }
        ammunitionPanelControl();
        this.angleTextField.requestFocus();
    }

    // AnimationTimer responsible for the animation of the shots
    public void gameAnimationTimer(Shot shot) {
        shootButton.setDisable(true);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    shot.shotPosition();
                    drawingMethods(false);
                    shot.drawTrajectory(gameCanvasGraphicContext);
                    shot.drawShot(gameCanvasGraphicContext);
                    replayButton.setDisable(true);
                    menuExitButton.setDisable(true);

                    // Update tank radar every frame
                    tankRadarUpdate(shot);
                    // Update max height and distance every frame
                    calculateMax(shot, turn.tank);

                    // Shot is out of the screen
                    if (shot.position.getX() >= Data.getInstance().windowsWidth || shot.position.getX() < 0
                            || shot.position.getY() >= Data.getInstance().canvasHeight) {
                        stop();
                        stopMethods();
                    } else {
                        // Checks all the possible collisions
                        if (shotCollision(shot, false)) stop();
                    }

                    // Trajectory point added
                    shot.addTrajectory();

                    lastUpdateTime = now;
                }
            }
        }.start();

    }

    // AnimationTimer responsible for the terrain falling after being destroyed
    public void terrainFallAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    drawingMethods(true);
                    vibration();
                    // When the terrain stops falling, the animation stops
                    if (!Data.getInstance().terrain.terrainFalling()) stop();
                }
            }
        }.start();
    }

    // AnimationTimer responsible for tank falling after terrain being destroyed below it
    public void tankFallAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    drawingMethods(false);
                    int flag = 0;
                    for (Player p : Data.getInstance().alivePlayers) {
                        if (p.tank.position.getY() + Constants.TANK_SIZE / 3 < Data.getInstance().canvasHeight &&
                                Data.getInstance().terrain.resolutionMatrix[p.tank.position.getY() + Constants.TANK_SIZE / 3][p.tank.position.getX()] == 0) {
                            p.tank.position.setY(p.tank.position.getY() + 1);
                            p.tank.reduceHealth(Data.getInstance().gravity / 9.8);
                            tankInfoHUD.showHUD(p.tank);
                            if (p.tank.getHealth() <= 0) {
                                deleteDeadPlayer(p);
                                flag = 0;
                                break;
                            }
                            flag = 1;
                        }
                    }
                    updateCurrentPlayerInterfaceValues(ComponentsCreator.healthIcon(turn.tank));
                    if (flag == 0) stop();
                }
            }
        }.start();
    }

    // AnimationTimer responsible for mystery boxes falling from the sky
    public void mysteryBoxFallAnimationTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    int flag = 0;
                    drawingMethods(false);
                    replayButton.setDisable(true);
                    menuExitButton.setDisable(true);

                    for (MysteryBox box : Data.getInstance().mysteryBoxes) {
                        // Drags down the box 1 pixel per frame
                        if (box.position.getY() + Constants.BOX_SIZE / 2 + 1 < Data.getInstance().canvasHeight &&
                                Data.getInstance().terrain.resolutionMatrix[box.position.getY() + Constants.BOX_SIZE / 2 + 1][box.position.getX()] == 0) {
                            box.position.setY(box.position.getY() + 1);
                            flag = 1;
                        }
                    }
                    updateCurrentPlayerInterfaceValues(ComponentsCreator.healthIcon(turn.tank));
                    if (flag == 0) {
                        replayButton.setDisable(false);
                        menuExitButton.setDisable(false);
                        stop();
                    }
                }
            }
        }.start();
    }

    // This method make a mystery box appear on the screen
    public void mysteryBoxAppears() {
        int boxPositionX;
        boolean isOnTank = false;
        // Makes sure the box created is not above a tank
        while (true) {
            boxPositionX = random.nextInt(Data.getInstance().windowsWidth);
            for (Player p : Data.getInstance().alivePlayers) {
                if (boxPositionX < p.tank.position.getX() + Constants.TERRAIN_MARGIN &&
                        boxPositionX > p.tank.position.getX() - Constants.TERRAIN_MARGIN) {
                    isOnTank = true;
                    break;
                }
            }
            if (isOnTank) isOnTank = false;
            else break;
        }
        Data.getInstance().mysteryBoxes.add(new MysteryBox(new Point(boxPositionX, 0)));
        mysteryBoxFallAnimationTimer();
    }

    // Gives the power up to the corresponding tank
    public void mysteryBoxPower(MysteryBox box) {
        if (box.powerUp == 0) {
            this.turn.tank.restoreHealth();
            this.tankInfoHUD.showHUD(this.turn.tank);
        } else if (box.powerUp == 1) {
            Shop.loadCredits(this.turn, Constants.CREDITS_FROM_POWER_UP);
        } else if (box.powerUp == 2) {
            bombardmentAnimationTimer();
        }
        updateCurrentPlayerInterfaceValues(ComponentsCreator.healthIcon(this.turn.tank));
    }

    // Power up that creates a bombardment of shots from the sky
    public void bombardmentAnimationTimer() {
        ArrayList<Shot> bombs = new ArrayList<>();
        Point umbrellaPosition = new Point(turn.tank.position.getX() - Constants.TANK_SIZE, turn.tank.position.getY()-30-Constants.TANK_SIZE);

        // Creates the bombs with random parameters
        for (int i = 0; i < Constants.SHOTS_FROM_BOMBARDMENT; i++) {
            bombs.add(new MediumShot(new Point(random.nextInt(Data.getInstance().windowsWidth - 1), random.nextInt(-Data.getInstance().canvasHeight, 0)), random.nextInt(10), 90, this.turn));
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Makes animation fps constant
                if (now - lastUpdateTime >= Constants.FRAME_TIME) {
                    // It draws all bombs
                    drawingMethods(false);
                    for (Shot s : bombs) {
                        s.shotPosition();
                        s.drawTrajectory(gameCanvasGraphicContext);
                        s.drawShot(gameCanvasGraphicContext);
                    }
                    shootButton.setDisable(true);
                    replayButton.setDisable(true);
                    menuExitButton.setDisable(true);

                    // It draws the protective umbrella
                    gameCanvasGraphicContext.drawImage(Loader.getInstance().umbrellaImage, umbrellaPosition.getX(), umbrellaPosition.getY(), 55.6, 61.2);

                    ArrayList<Shot> deletedShots = new ArrayList<>();
                    for (Shot s : bombs) {
                        // Shot is out of the screen
                        if (s.position.getX() >= Data.getInstance().windowsWidth || s.position.getX() < 0
                                || s.position.getY() >= Data.getInstance().canvasHeight) {
                            deletedShots.add(s);
                        } else {
                            // Checks all the possible collisions
                            if (shotCollision(s, true)) deletedShots.add(s);
                        }

                        // Trajectory point added
                        s.addTrajectory();
                    }

                    // It deletes the bombs that have collided
                    for (Shot s : deletedShots) {
                        bombs.remove(s);
                    }

                    // If there's no bombs left it stops
                    if (bombs.isEmpty()) {
                        stop();
                        stopMethods();
                    }

                    if (Data.getInstance().tie || Data.getInstance().alivePlayers.size() < 2) stop();
                    lastUpdateTime = now;
                }
            }
        }.start();
    }

    // Shakes the windows screen
    public void vibration() {
        gameCanvasGraphicContext.translate(random.nextInt(-2, 2), random.nextInt(-2, 2));
    }

    // Checks all the possible collisions with a shot
    public boolean shotCollision(Shot shot, boolean bombardment) {
        Player playerNearby = tanksCollision(shot, true);
        // Checks if during a bombardment there's a player nearby who's the same one that made the shot
        if (bombardment && playerNearby == this.turn) {
            this.sounds = new MediaPlayer(Loader.getInstance().currentSoundEffects.get(0));
            this.sounds.setVolume(Data.getInstance().SFXVolume);
            this.sounds.play();
            return true;
        }

        // Checks if a tank is hit
        Player hitPlayer = tanksCollision(shot, false);
        if (hitPlayer != null) {
            hitPlayer.tank.reduceHealth(shot.getDamage());
            Data.getInstance().terrain.destroyTerrain(shot.position, shot.area);
            terrainFallAnimationTimer();
            tankFallAnimationTimer();
            this.tankInfoHUD.showHUD(hitPlayer.tank);
            this.animationsCreator.startExplosionAnimation(hitPlayer.tank.position);
            this.sounds = new MediaPlayer(Loader.getInstance().currentSoundEffects.get(1));
            this.sounds.setVolume(Data.getInstance().SFXVolume);
            this.sounds.play();
            if (hitPlayer.tank.getHealth() <= 0) {
                if (deleteDeadPlayer(hitPlayer)) return true;
            }
            if (hitPlayer != this.turn) this.turn.score += Constants.POINTS_FOR_HITTING_SOMETHING;
            if (!bombardment) stopMethods();
            return true;
        }
        // Checks if a box is hit
        if (!bombardment) {
            for (MysteryBox box : Data.getInstance().mysteryBoxes) {
                if (shot.mysteryBoxCollision(box)) {
                    Data.getInstance().mysteryBoxes.remove(box);
                    Data.getInstance().terrain.destroyTerrain(shot.position, shot.area);
                    terrainFallAnimationTimer();
                    tankFallAnimationTimer();
                    sounds = new MediaPlayer(Loader.getInstance().currentSoundEffects.get(2));
                    this.sounds.setVolume(Data.getInstance().SFXVolume);
                    this.sounds.play();
                    this.turn.score += Constants.POINTS_FOR_HITTING_SOMETHING;
                    mysteryBoxPower(box);
                    stopMethods();
                    return true;
                }
            }
        }
        // Checks if terrain is hit
        if (shot.terrainCollision(Data.getInstance().terrain)) {
            Data.getInstance().terrain.destroyTerrain(shot.position, shot.area);
            terrainFallAnimationTimer();
            tankFallAnimationTimer();
            this.sounds = new MediaPlayer(Loader.getInstance().currentSoundEffects.get(0));
            this.sounds.setVolume(Data.getInstance().SFXVolume);
            this.sounds.play();
            // Checks if a tank is nearby
            if (playerNearby != null) {
                playerNearby.tank.reduceHealth((int) (shot.getDamage() * shot.tankCollision(playerNearby.tank)));
                this.tankInfoHUD.showHUD(playerNearby.tank);
                if (playerNearby.tank.getHealth() <= 0) {
                    if (deleteDeadPlayer(playerNearby)) return true;
                }
            }

            if (!bombardment) stopMethods();
            return true;
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

    // Encapsulation of methods responsible for turn change mechanics
    public void stopMethods() {
        if (this.turn == Data.getInstance().alivePlayers.get(Data.getInstance().alivePlayers.size() - 1)) {
            if (shuffleTurn()) return;
        } else if (changeTurn()) return;
        drawingMethods(true);
        shootButton.setDisable(false);
        replayButton.setDisable(false);
        menuExitButton.setDisable(false);
        ammunitionPanelControl();
        // Saves last angle and power
        if (this.turn.tank.power != null || this.turn.tank.getAngle() != null) {
            this.angleTextField.setText(String.valueOf(this.turn.tank.getAngle()));
            this.powerTextField.setText(String.valueOf(this.turn.tank.power));
        } else {
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
        if (this.turn instanceof CPU) {
            ((CPU) this.turn).shoot(shootButton, lightAmmoButton, mediumAmmoButton, heavyAmmoButton, angleTextField, powerTextField);
        }
        updateCurrentPlayerInterfaceValues(ComponentsCreator.healthIcon(this.turn.tank));
    }

    // Turn change, if there's no next player comes back to the first one
    public boolean changeTurn() {
        for (int i = 0; i < Data.getInstance().alivePlayers.size(); i++) {
            if (this.turn == Data.getInstance().alivePlayers.get(i)) {
                if (i + 1 < Data.getInstance().alivePlayers.size()) {
                    this.turn = Data.getInstance().alivePlayers.get(i + 1);
                } else {
                    this.turn = Data.getInstance().alivePlayers.get(0);
                }
                break;
            }
        }

        if (tie()) {
            tieMethod();
            return true;
        }
        if (this.turn.tank.getAmmunitionQuantity() <= 0) changeTurn();

        updateWindHUD();
        updateCurrentPlayerInterfaceValues(ComponentsCreator.healthIcon(this.turn.tank));

        return false;
    }

    // This method shuffles the alivePlayers ArrayList, so the turns are random
    public boolean shuffleTurn() {
        Collections.shuffle(Data.getInstance().alivePlayers);
        if (tie()) {
            tieMethod();
            return true;
        }

        // If the chosen player has no ammo it changes turn
        for (int i = 0; i < Data.getInstance().tanksQuantity; i++) {
            this.turn = Data.getInstance().alivePlayers.get(i);
            if (this.turn.tank.getAmmunitionQuantity() > 0) {
                Collections.swap(Data.getInstance().alivePlayers, 0, i);
                break;
            }
        }
        return false;
    }

    // This method verifies all players have ammunition available
    public boolean tie() {
        boolean onePlayerHasAmmo = false;
        for (Player player : Data.getInstance().alivePlayers) {
            onePlayerHasAmmo |= player.tank.getAmmunitionQuantity() > 0;
        }
        return !onePlayerHasAmmo;
    }

    // This method does the necessary things when a tie happens
    public void tieMethod() {
        this.music.stop();
        if (Data.getInstance().gameNumber != Data.getInstance().gamesMax) Data.getInstance().gameNumber++;

        Data.getInstance().tie = true;

        goToInterlude();
    }

    // Initializes the buttons panel of the interface
    public void buttonsPanelInitialize() {
        Image heartIcon = Loader.getInstance().heartIconImages.get(2);
        this.replayExitButtonsVbox.getChildren().clear();
        this.replayButton = ComponentsCreator.createReplayButton(25, 25);
        this.exitButton = ComponentsCreator.createExitButton(25, 25);
        this.menuExitButton = ComponentsCreator.createMenuExitButton(25, 25);

        this.replayExitButtonsVbox.getChildren().add(this.replayButton);
        this.replayExitButtonsVbox.getChildren().add(this.menuExitButton);
        this.replayExitButtonsVbox.getChildren().add(this.exitButton);
        this.currentPlayerTankImage.setImage(Loader.getInstance().currentTankImage);

        this.lightAmmoImageView.setImage(Loader.getInstance().currentShotImages.get(0));
        this.mediumAmmoImageView.setImage(Loader.getInstance().currentShotImages.get(1));
        this.heavyAmmoImageView.setImage(Loader.getInstance().currentShotImages.get(2));

        updateCurrentPlayerInterfaceValues(heartIcon);
        ammunitionPanelControlInitialize();
    }

    // Encapsulation of methods responsible for updating remaining ammo and ammo lights color every turn
    public void ammunitionPanelControl() {
        this.lightAmmoQuantityText.setText(String.valueOf(this.turn.tank.ammunition.get(0)));
        this.mediumAmmoQuantityText.setText(String.valueOf(this.turn.tank.ammunition.get(1)));
        this.heavyAmmoQuantityText.setText(String.valueOf(this.turn.tank.ammunition.get(2)));
        this.lightAmmoButton.setUserData(this.turn.tank.ammunition.get(0));
        this.mediumAmmoButton.setUserData(this.turn.tank.ammunition.get(1));
        this.heavyAmmoButton.setUserData(this.turn.tank.ammunition.get(2));

        // If selected button has no ammo, shoot button is disabled
        if ((int) this.ammunitionButtons.getSelectedToggle().getUserData() <= 0) {
            this.shootButton.setDisable(true);
        }

        // Changes ammo light color based on ammo left
        if (this.turn.tank.ammunition.get(0) >= 1) {
            this.lightAmmoQuantityLight.setFill(Color.GREEN);
        } else {
            this.lightAmmoQuantityLight.setFill(Color.RED);
        }

        if (this.turn.tank.ammunition.get(1) >= 1) {
            this.mediumAmmoQuantityLight.setFill(Color.GREEN);
        } else {
            this.mediumAmmoQuantityLight.setFill(Color.RED);
        }

        if (this.turn.tank.ammunition.get(2) >= 1) {
            this.heavyAmmoQuantityLight.setFill(Color.GREEN);
        } else {
            this.heavyAmmoQuantityLight.setFill(Color.RED);
        }
    }

    // Encapsulation of methods responsible for initializing toggle buttons, data related
    public void ammunitionPanelControlInitialize() {
        // Verifies that is always a button selected
        this.ammunitionButtons.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            this.shootButton.setDisable(false);
            if (newToggle == null) {
                // If no button is selected, selects the last one
                this.ammunitionButtons.selectToggle(oldToggle);
            }
            if (newToggle != null) {
                // If selected button has no left ammo
                if ((int) newToggle.getUserData() <= 0) {
                    this.shootButton.setDisable(true);
                }
            }
        });

    }

    // Initializes the tank radar of the interface
    public void tankRadarInitialize() {
        int height = 45;
        int width = 3;
        Rectangle pointer = ComponentsCreator.createTankRadarPointer(height, width);
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
        double screenXRadar = this.tankRadarStackPane.localToScene(0, 0).getX();
        double screenYRadar = this.tankRadarStackPane.localToScene(0, 0).getY();
        double posXRadar = screenXRadar + this.tankRadarStackPane.getWidth() / 2;
        double posYRadar = Data.getInstance().windowsHeight - (screenYRadar + this.tankRadarStackPane.getHeight() / 2);
        double posXShot = shot.position.getX();
        double posYShot = Data.getInstance().canvasHeight - shot.position.getY();
        double deltaX = posXShot - posXRadar;
        double deltaY = posYShot - posYRadar;
        double angle;

        angle = Math.toDegrees(Math.atan(deltaX / deltaY));

        if (angle >= -75 && angle <= 75) {
            this.tankRadarPointerRotate.setAngle(angle);
        }
    }

    // Updates the direction of the canon
    public void onAngleTextFieldTyped(KeyEvent ignoredActionEvent) {
        try {
            if (angleTextField.getText().isEmpty()) return;
            this.turn.tank.setAngle(Double.parseDouble(angleTextField.getText()));
            drawingMethods(false);
        } catch (NumberFormatException e) {
            this.turn.tank.setAngle(null);
        }
    }

    // Updates all the current player values of the interface
    public void updateCurrentPlayerInterfaceValues(Image heartIcon) {
        this.currentPlayerTankStackPane.setStyle(this.currentPlayerTankStackPane.getStyle() + "-fx-background-color: " + toHexString(this.turn.tank.color) + ";");
        this.currentPlayerText.setText(this.turn.name + " is playing");
        this.currentTankHealth.setText(String.valueOf((int) this.turn.tank.getHealth()));
        this.currentTankKills.setText(String.valueOf(this.turn.tank.getKills()));
        this.currentTankCredits.setText(String.valueOf(this.turn.tank.getCredits()));
        this.currentTankHealthIcon.setImage(heartIcon);
    }

    // Updates the arrow icon and wind text of the wind HUD
    public void updateWindHUD() {
        if (!Data.getInstance().wind) return;

        setRandomWind();

        if (Data.getInstance().windVelocity < 0) {
            this.windDirectionImageView.setImage(Loader.getInstance().iconImages.get(4));
        } else {
            this.windDirectionImageView.setImage(Loader.getInstance().iconImages.get(5));
        }

        this.windVelocityText.setText("Wind velocity = " + Math.abs(Data.getInstance().windVelocity) + " m/s");
    }

    // Sets a random wind between the negative and positive max values
    public void setRandomWind() {
        Random r = new Random();
        int wind;

        do {
            wind = r.nextInt(Constants.WIND_MAX_VELOCITY * -1, Constants.WIND_MAX_VELOCITY + 1);
        } while (wind == 0);

        Data.getInstance().windVelocity = wind;
    }

    // Switches to interlude windows after some delay
    public void goToInterlude() {
        PauseTransition delay = new PauseTransition(Duration.millis(2500));

        delay.setOnFinished(e -> {
            try {
                App.setRoot("interlude");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        delay.play();
    }
}