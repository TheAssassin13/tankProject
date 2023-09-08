package com.example.tankproject;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

    //Game interface, tanks and terrain initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gc = grid.getGraphicsContext2D();
        //Test player
        this.players = new ArrayList<>();
        this.players.add(new Player("Player1",Color.GREENYELLOW,new Tank(Color.GREENYELLOW, new Point(0,0))));
        this.players.add(new Player("Player2",Color.BLUE,new Tank(Color.BLUE, new Point(0,0))));
        this.turn = players.get(0);

        this.terrain = new Terrain(Constants.WINDOWS_HEIGHT, Constants.WINDOWS_WIDTH);
        this.terrain.terrainGeneration(Constants.SEA_LEVEL,false);
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
        Illustrator.drawBackground(this.gc);
        Illustrator.drawTerrain(this.gc,this.terrain);
        for (Player p: this.players) {
            Illustrator.drawTank(this.gc, p.tank);
        }
    }


    public void tanksPlacement() {
        int gap = Constants.WINDOWS_WIDTH / 2;
        int posXFirstTank = (int) (Math.random() * 2 * Constants.WINDOWS_WIDTH/5 + Constants.TANK_SIZE);
        int posXSecondTank = (int) (posXFirstTank + gap + gap * Math.random());
        if (posXSecondTank >= Constants.WINDOWS_WIDTH) posXSecondTank = Constants.WINDOWS_WIDTH - (Constants.TANK_SIZE * 2);
        int posYSecondTank = Constants.CANVAS_HEIGHT - Constants.TANK_SIZE;
        int posYFirstTank = Constants.CANVAS_HEIGHT - Constants.TANK_SIZE;
        this.players.get(0).tank.position.setX(posXFirstTank);
        this.players.get(1).tank.position.setX(posXSecondTank);

        //Position tank 1 on terrain
        for (int i = 0; i < Constants.CANVAS_HEIGHT; i++) {
            if (terrain.resolutionMatrix[i][posXFirstTank + Constants.TANK_SIZE/2] == 1) {
                posYFirstTank = i - Constants.TANK_SIZE;
                break;
            }
        }
        this.players.get(0).tank.position.setY(posYFirstTank);

        //Position tank 2 on terrain
        for (int i = 0; i < Constants.CANVAS_HEIGHT; i++) {
            if (terrain.resolutionMatrix[i][posXSecondTank + Constants.TANK_SIZE/2] == 1) {
                posYSecondTank = i - Constants.TANK_SIZE;
                break;
            }
        }
        this.players.get(1).tank.position.setY(posYSecondTank);
    }

    public void onShootButtonClick(ActionEvent actionEvent) {
        if (!powerTextField.getText().isEmpty() && !angleTextField.getText().isEmpty()) {
            Shot s = new Shot(new Point(turn.tank.position.getX(),turn.tank.position.getY()),Double.parseDouble(powerTextField.getText()),Double.parseDouble(angleTextField.getText()));
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    gc.clearRect(0,0,Constants.WINDOWS_WIDTH,Constants.WINDOWS_HEIGHT);
                    s.shotPosition();
                    drawingMethods();
                    Illustrator.drawShot(gc,s);
                    if (s.position.getX() > Constants.WINDOWS_WIDTH || s.position.getY() > Constants.CANVAS_HEIGHT) {
                        stop();
                    }
                    if (s.tankColission(players.get(1).tank)) System.out.println("Impacto");

                }
            }.start();
        }

        angleTextField.clear();
        powerTextField.clear();


    }


}