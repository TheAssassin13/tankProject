package com.example.tankproject;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.tankproject.MainApp.toHexString;

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
    public ArrayList<Player> players;
    public Text maxHeightTextField;
    public int maxHeight;
    public Text maxDistanceTextField;
    public int maxDistance;
    public StackPane currentPlayerPanel;

    //Game interface, tanks and terrain initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //grid.widthProperty().bind(vbox.widthProperty());
        //grid.heightProperty().bind(vbox.heightProperty());
        this.gc = grid.getGraphicsContext2D();
        //Test player
        this.players = new ArrayList<>();
        this.players.add(new Player("Player1", Color.GREENYELLOW, new Tank(Color.GREENYELLOW, new Point(0, 0))));
        this.players.add(new Player("Player2", Color.BLUE, new Tank(Color.BLUE, new Point(0, 0))));
        this.turn = players.get(0);
        this.maxHeightTextField.setText("Max distance = 0");
        this.maxDistanceTextField.setText("Max height = 0");
        this.terrain = new Terrain(Constants.CANVAS_HEIGHT, Constants.WINDOWS_WIDTH);
        this.terrain.terrainGeneration(Constants.SEA_LEVEL, false);
        buttonsPanelInitialize();
        tanksPlacement();
        drawingMethods();
    }

    public void buttonsPanelInitialize() {
        currentPlayerText.setText("Current player: " + turn.name);
        buttonsPanel.setPrefHeight(Constants.BUTTONS_PANEL_HEIGHT);
        grid.setHeight(Constants.CANVAS_HEIGHT);
        grid.setWidth(Constants.WINDOWS_WIDTH);
    }

    public void drawingMethods() {
        gc.clearRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        Illustrator.drawBackground(this.gc);
        Illustrator.drawTerrain(this.gc, this.terrain);
        for (Player p : this.players) {
            Illustrator.drawTank(this.gc, p.tank);
        }
    }

    public void tanksPlacement() {
        int gap = Constants.WINDOWS_WIDTH / 2;
        int posXFirstTank = (int) (Math.round(Math.random()) * 2 * Constants.WINDOWS_WIDTH / 5 + Constants.TANK_SIZE);
        int posXSecondTank = (int) (posXFirstTank + gap + gap * Math.round(Math.random()));
        if (posXSecondTank + Constants.TANK_SIZE / 2 >= Constants.WINDOWS_WIDTH)
            posXSecondTank = Constants.WINDOWS_WIDTH - (Constants.TANK_SIZE * 2);
        int posYSecondTank = Constants.CANVAS_HEIGHT - Constants.TANK_SIZE;
        int posYFirstTank = Constants.CANVAS_HEIGHT - Constants.TANK_SIZE;
        this.players.get(0).tank.position.setX(posXFirstTank);
        this.players.get(1).tank.position.setX(posXSecondTank);

        //Position tank 1 on terrain
        for (int i = 0; i < Constants.CANVAS_HEIGHT; i++) {
            if (terrain.resolutionMatrix[i][posXFirstTank + Constants.TANK_SIZE / 2] == 1) {
                posYFirstTank = i - Constants.TANK_SIZE;
                break;
            }
        }
        this.players.get(0).tank.position.setY(posYFirstTank);

        //Position tank 2 on terrain
        for (int i = 0; i < Constants.CANVAS_HEIGHT; i++) {
            if (terrain.resolutionMatrix[i][posXSecondTank + Constants.TANK_SIZE / 2] == 1) {
                posYSecondTank = i - Constants.TANK_SIZE;
                break;
            }
        }
        this.players.get(1).tank.position.setY(posYSecondTank);
    }

    public boolean tanksCollision(Shot shot) {
        for (Player p : this.players) {
            if (this.turn != p && shot.tankCollision(p.tank)) {
                return true;
            }
        }
        return false;
    }


    public void onShootButtonClick(ActionEvent actionEvent) {
        maxHeight = 0;
        maxDistance = 0;

        if (!powerTextField.getText().isEmpty() && !angleTextField.getText().isEmpty()) {
            Shot s = new Shot(new Point(turn.tank.position.getX(), turn.tank.position.getY()), Double.parseDouble(powerTextField.getText()), Double.parseDouble(angleTextField.getText()));
            maxHeight = (int) ((Math.pow(s.initialVelocity,2) * Math.pow(Math.sin(s.angle),2)) / (2 * Constants.GRAVITY));
            maxDistance = (int) ((Math.pow(s.initialVelocity,2) * Math.sin(s.angle * 2)) / Constants.GRAVITY);
            maxDistanceTextField.setText("Max height = " + maxDistance);
            maxHeightTextField.setText("Max distance = " + maxHeight);
            new AnimationTimer() {
                @Override
                public void handle(long now) {

                    s.shotPosition();
                    drawingMethods();
                    Illustrator.drawShot(gc, s);
                    if (s.position.getX() > Constants.WINDOWS_WIDTH || s.position.getY() > Constants.CANVAS_HEIGHT) {
                        stop();
                        changeTurn();

                    }
                    if (tanksCollision(s)) System.out.println("Impacto");
                    if (s.terrainCollision(terrain)) {
                        stop();
                        changeTurn();
                        s.shotPosition();
                        drawingMethods();

                    }
                }
            }.start();


        }
        angleTextField.clear();
        powerTextField.clear();

    }

    public void changeTurn() {
        for (int i = 0; i < this.players.size(); i++) {
            if (this.turn == this.players.get(i)) {
                if (i + 1 < this.players.size()) {
                    this.turn = this.players.get(i + 1);
                } else {
                    this.turn = this.players.get(0);
                }
                break;
            }
        }
        this.currentPlayerText.setText("Current player: " + this.turn.name);
        this.currentPlayerPanel.setStyle(currentPlayerPanel.getStyle() + "-fx-background-color:" + toHexString(this.turn.color) + ";");
        maxDistanceTextField.setText("Max height = " + 0);
        maxHeightTextField.setText("Max distance = " + 0);
    }
}