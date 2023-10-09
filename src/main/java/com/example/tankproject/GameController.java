package com.example.tankproject;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    public Text currentPlayerHealth;
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
    public VBox winScreenVbox;
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
    public Button menuExitButton;
    public Media backgroundMusic;
    public MediaPlayer music;
    public MediaPlayer sounds;
    public Image umbrella;
    public Point umbrellaPosition;

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
            this.alivePlayers.add(new Player("Player " + (i+1), Constants.TANK_COLORS[i], new Tank(Constants.TANK_COLORS[i], new Point(0, 0))));
        }
        this.turn = this.alivePlayers.get((int) Math.round(Math.random()));
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
        calculateMax(new Shot(new Point(0,0),0,0,0),this.turn.tank);
        tanksPlacement();
        componentsSizesInitialize();
        drawingMethods(false);

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
        this.vbox.setPrefHeight(Constants.WINDOWS_HEIGHT);
        this.vbox.setPrefWidth(Constants.WINDOWS_WIDTH);
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

    // Initializes the buttons panel of the interface
    public void buttonsPanelInitialize() {
        Image heartIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());
        this.replayExitButtonsHbox.getChildren().clear();
        this.replayButton = createReplayButton(25,25);
        this.exitButton = createExitButton(25,25);
        this.menuExitButton = createMenuExitButton(25,25);
        this.currentPlayerText.setText(turn.name + " is playing");
        this.currentPlayerHealth.setText("Health : " + this.turn.getHealth() + " / 100");
        this.replayExitButtonsHbox.getChildren().add(this.replayButton);
        this.replayExitButtonsHbox.getChildren().add(this.menuExitButton);
        this.replayExitButtonsHbox.getChildren().add(this.exitButton);
        this.currentPlayerLifeIcon.setImage(heartIcon);
        this.currentPlayerTankImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/current_tank_image.png")).toExternalForm()));
        this.currentPlayerTankStackPane.setStyle(this.currentPlayerTankStackPane.getStyle() + "-fx-background-color: " + toHexString(this.turn.tank.color) + ";");
        ammunitionPanelControlInitialize();
    }

    // All drawing methods that should render every frame
    public void drawingMethods(boolean collision) {
        this.gameCanvasGraphicContext.clearRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        if (umbrella != null) gameCanvasGraphicContext.drawImage(umbrella, umbrellaPosition.getX(), umbrellaPosition.getY());
        // If there's a collision it draws the terrain without the optimization
        if (collision) Illustrator.drawTerrain(this.gameCanvasGraphicContext, this.terrain);
        else Illustrator.drawTerrainOptimized(this.gameCanvasGraphicContext, this.terrain);
        for (Player p : this.alivePlayers) {
            Illustrator.drawTank(this.gameCanvasGraphicContext, p.tank);
        }
        for (MysteryBox box : this.boxes) {
            Illustrator.drawMysteryBox(gameCanvasGraphicContext, box);
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
        this.maxHeight = 0;
        this.maxDistance = 0;
        // Checks if the input is not empty
        if (!powerTextField.getText().isEmpty() && !angleTextField.getText().isEmpty()) {
            this.turn.tank.setAngle(Double.parseDouble(angleTextField.getText()));
            this.turn.tank.power = Double.parseDouble(powerTextField.getText());
            this.turn.tank.setAmmoSelected((ToggleButton) this.ammunitionButtons.getSelectedToggle());
            // There's a 1/5 chance a mystery box appears
            if (random.nextInt(5) == 4) {
                boxes.add(new MysteryBox(new Point(random.nextInt(Constants.WINDOWS_WIDTH), 0)));
                mysteryBoxFalling();
            }

            // Creates shot from user input
            Shot shot = new Shot(new Point(turn.tank.position.getX(), turn.tank.position.getY()), Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()), 0);
            // Sets damage from selected ammunition
            shot.setDamage((Integer) this.turn.tank.getAmmoSelected().getUserData());

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
                   replayButton.setDisable(true);
                   tankRadarUpdate(shot);
                   // Update max height and distance every frame
                   calculateMax(shot, turn.tank);

                   // Shot is out of the screen in the X-axis
                   if (shot.position.getX() >= Constants.WINDOWS_WIDTH || shot.position.getX() < 0
                        || shot.position.getY() >= Constants.CANVAS_HEIGHT) {
                       stop();
                       stopMethods();
                   }

                   // Checks all the possible collisions
                   if (shotCollision(shot)) stop();

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
                            p.reduceHealth(1);
                            if (p.getHealth() <= 0) {
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

    public void mysteryBoxFalling() {
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

    public void mysteryBoxPower(MysteryBox box) {
        if (box.powerUp == 0) {
            turn.restoreHealth();
        } else if (box.powerUp == 1) {
            this.umbrella = new Image(Objects.requireNonNull(getClass().getResource("images/umbrella.png")).toExternalForm());
            this.umbrellaPosition = new Point(turn.tank.position.getX(), turn.tank.position.getY()-10);
            bombardment();
            this.umbrella = null;
        }
    }

    public void bombardment() {
        for (int i = 0; i < 10; i++) {
            gameAnimationTimer(new Shot(new Point(random.nextInt(Constants.WINDOWS_WIDTH - 1), 0), 10, -90, 10));
        }
    }

    public void vibration() {
        gameCanvasGraphicContext.translate(random.nextInt(-2,2), random.nextInt(-2,2));
    }

    // Checks all the possible collisions with a shot
    public boolean shotCollision(Shot shot) {
        // Checks if a tank is hit
        if (tanksCollision(shot) != null) {
            Player hitPlayer = tanksCollision(shot);
            hitPlayer.reduceHealth(shot.getDamage());
            terrain.destroyTerrain(shot.position, shot.area);
            terrainFallAnimationTimer();
            tankFallAnimationTimer();
            stopMethods();
            if (hitPlayer.getHealth() <= 0) {
                deleteDeadPlayer(hitPlayer);
            }
            return true;
        }
        // Checks if terrain is hit
        else if (shot.terrainCollision(terrain)) {
            terrain.destroyTerrain(shot.position, shot.area);
            terrainFallAnimationTimer();
            tankFallAnimationTimer();
            stopMethods();
            return true;
        }
        // Checks if a box is hit
        else {
            for (MysteryBox box : boxes) {
                if (shot.mysteryBoxCollision(box)) {
                    boxes.remove(box);
                    terrain.destroyTerrain(shot.position, shot.area);
                    terrainFallAnimationTimer();
                    tankFallAnimationTimer();
                    stopMethods();
                    sounds.stop();
                    sounds = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/powerup.mp3")).toExternalForm()));
                    sounds.play();
                    mysteryBoxPower(box);
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
        Image heartIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());
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
        this.currentPlayerHealth.setText("Health : " +this.turn.getHealth() + " / 100");
        if (this.turn.getHealth() == Constants.TANK_HEALTH) heartIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());
        if (this.turn.getHealth() <= Constants.TANK_HEALTH / 2) heartIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/half_heart_icon.png")).toExternalForm());
        if (this.turn.getHealth() == 0) heartIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/empty_heart_icon.png")).toExternalForm());
        this.currentPlayerLifeIcon.setImage(heartIcon);
        this.currentPlayerTankStackPane.setStyle(this.currentPlayerTankStackPane.getStyle() + "-fx-background-color: " + toHexString(this.turn.tank.color) + ";");
        this.sounds = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/boom.mp3")).toExternalForm()));
        sounds.play();
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
        Color backgroundColor = Color.rgb((int) (Color.GRAY.getRed() * 255), (int) (Color.GRAY.getGreen() * 255), (int) (Color.GRAY.getBlue() * 255), 0.5);
        StackPane winnerTankBackground = new StackPane();
        Image winnerTankImage = new Image(Objects.requireNonNull(getClass().getResource("images/winner_tank_image.png")).toExternalForm());
        ImageView winnerTankImageView = new ImageView(winnerTankImage);
        Image healthIconImage = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());
        ImageView healthIconImageView;
        VBox backgroundVbox = new VBox();
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        HBox healthRemainingHbox = new HBox();
        Font primaryFont = Font.font("Arial",FontWeight.BOLD,50);
        Font secondaryFont = Font.font("Arial",FontWeight.NORMAL,30);
        Text victoryText = new Text("Victory!");
        Text winnerNameText = new Text(this.alivePlayers.get(0).name);
        Text healthRemainingText = new Text("Health remaining: " + this.alivePlayers.get(0).getHealth());
        Button replayButton = createReplayButton(40,40);
        Button exitButton = createExitButton(40,40);

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

        if (this.alivePlayers.get(0).getHealth() == Constants.TANK_HEALTH) healthIconImage = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/full_heart_icon.png")).toExternalForm());
        if (this.alivePlayers.get(0).getHealth() <= Constants.TANK_HEALTH / 2) healthIconImage = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/half_heart_icon.png")).toExternalForm());
        if (this.alivePlayers.get(0).getHealth() == 0) healthIconImage = new Image(Objects.requireNonNull(getClass().getResource("icons/hearts_icons/empty_heart_icon.png")).toExternalForm());

        winnerTankBackground.setBackground(new Background(new BackgroundFill(this.alivePlayers.get(0).color,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        winnerTankBackground.setMaxWidth(150);
        winnerTankBackground.setPrefWidth(150);
        winnerTankBackground.getChildren().add(winnerTankImageView);

        victoryText.setFont(primaryFont);
        victoryText.setFill(Color.WHITE);

        healthRemainingText.setFont(secondaryFont);
        healthRemainingText.setFill(Color.WHITE);

        winnerNameText.setFont(secondaryFont);
        winnerNameText.setFill(Color.WHITE);

        healthIconImageView = new ImageView(healthIconImage);
        healthIconImageView.setFitWidth(35);
        healthIconImageView.setFitHeight(35);

        healthRemainingHbox.setAlignment(Pos.CENTER);
        healthRemainingHbox.setSpacing(15);
        healthRemainingHbox.getChildren().add(healthRemainingText);
        healthRemainingHbox.getChildren().add(healthIconImageView);

        hbox.setId("winScreenButtonHbox");
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
        vbox.getChildren().add(healthRemainingHbox);
        vbox.getChildren().add(hbox);

        backgroundVbox.setBackground(new Background(new BackgroundFill(backgroundColor,CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        backgroundVbox.setAlignment(Pos.CENTER);
        backgroundVbox.getChildren().add(vbox);

        this.winScreenVbox = backgroundVbox;
        this.stackPane.getChildren().add(this.winScreenVbox);
        music.stop();
        music = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("sounds/victory.mp3")).toExternalForm()));
        music.play();
    }

    // Creates replay button
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
            // WinScreen is in screen
            if (this.stackPane.getChildren().size() != 1) {
                disableWinScreen();
            }
            this.music.stop();
            gameInitialize();
        });

        return replayButton;
    }

    // Creates exit button
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

    // Creates menu exit button
    public Button createMenuExitButton(int height, int width) {
        Image menuExitIcon = new Image(Objects.requireNonNull(getClass().getResource("icons/menu_exit_icon_white.png")).toExternalForm());
        ImageView menuExitIconView = new ImageView(menuExitIcon);
        Button menuExitButton = new Button("",menuExitIconView);

        menuExitButton.getStyleClass().add("winScreenButton");
        menuExitButton.setId("menuExitButton");
        menuExitButton.setPrefHeight(height);
        menuExitButton.setPrefWidth(width);
        menuExitButton.setMaxSize(width,height);
        menuExitIconView.setFitHeight(height);
        menuExitIconView.setFitWidth(width);

        // Menu exit button action
        menuExitButton.setOnAction(event -> {
            try {
                this.music.stop();
                App.setRoot("menu");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return menuExitButton;
    }

    // Disables win screen to keep playing
    public void disableWinScreen() {
        this.stackPane.getChildren().remove(this.winScreenVbox);
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

    // Encapsulation of methods responsible for updating remaining ammo and ammo lights color every turn
    public void ammunitionPanelControl() {
        this.lightAmmoQuantityText.setText(this.turn.tank.ammunition.get("Bullet30") + " / 3");
        this.mediumAmmoQuantityText.setText(this.turn.tank.ammunition.get("Bullet40") + " / 10");
        this.heavyAmmoQuantityText.setText(this.turn.tank.ammunition.get("Bullet50") + " / 3");
        if (this.turn.tank.ammunition.get("Bullet30") == Constants.AMMO_QUANTITY[0]) this.lightAmmoQuantityLight.setFill(Color.GREEN);
        if (this.turn.tank.ammunition.get("Bullet30") <= Constants.AMMO_QUANTITY[0] / 2) this.lightAmmoQuantityLight.setFill(Color.YELLOW);
        if (this.turn.tank.ammunition.get("Bullet30") == 0) this.lightAmmoQuantityLight.setFill(Color.RED);
        if (this.turn.tank.ammunition.get("Bullet40") == Constants.AMMO_QUANTITY[1]) this.mediumAmmoQuantityLight.setFill(Color.GREEN);
        if (this.turn.tank.ammunition.get("Bullet40") <= Constants.AMMO_QUANTITY[1] / 2) this.mediumAmmoQuantityLight.setFill(Color.YELLOW);
        if (this.turn.tank.ammunition.get("Bullet40") == 0) this.mediumAmmoQuantityLight.setFill(Color.RED);
        if (this.turn.tank.ammunition.get("Bullet50") == Constants.AMMO_QUANTITY[2]) this.heavyAmmoQuantityLight.setFill(Color.GREEN);
        if (this.turn.tank.ammunition.get("Bullet50") <= Constants.AMMO_QUANTITY[2] / 2) this.heavyAmmoQuantityLight.setFill(Color.YELLOW);
        if (this.turn.tank.ammunition.get("Bullet50") == 0) this.heavyAmmoQuantityLight.setFill(Color.RED);
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

    // Changes the angle of the radar pointer every frame
    public void tankRadarUpdate(Shot shot) {
        boolean rightDirection = true;
        double posX = this.turn.tank.position.getX();
        double posY = this.turn.tank.position.getY();
        double deltaX = shot.position.getX() - posX;
        double deltaY = posY - shot.position.getY();
        double angle;

        if (deltaX < 0) {
            rightDirection = false;
            deltaX = deltaX * -1;
        }
        angle = Math.toDegrees(Math.atan(deltaX / deltaY));

        if (!rightDirection) {
            angle = angle * -1;
        }

        if (angle >= -75 && angle <= 75) {
            this.tankRadarPointerRotate.setAngle(angle);
        }
    }

    // Initializes the tank radar of the interface
    public void tankRadarInitialize() {
        Circle circle = new Circle(45);
        Rectangle pointer = new Rectangle();
        double height = 45;
        double width = 3;

        this.tankRadarPointerRotate = new Rotate();
        this.tankRadarPointerRotate.setPivotX(width);
        this.tankRadarPointerRotate.setPivotY(height);
        this.tankRadarPointerRotate.setAngle(0);
        this.tankRadarStackPane.getChildren().add(circle);
        this.tankRadarStackPane.getChildren().add(pointer);
        this.tankRadarPointer = pointer;

        circle.setId("tankRadarCircle");
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);
        pointer.setId("tankRadarPointer");
        pointer.setWidth(width);
        pointer.setHeight(height);
        pointer.setArcHeight(4);
        pointer.setArcWidth(4);
        pointer.setFill(Color.WHITE);
        pointer.getTransforms().addAll(this.tankRadarPointerRotate);
    }

}

