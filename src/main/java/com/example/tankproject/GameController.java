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

    //Game interface, tanks and terrain initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gc = grid.getGraphicsContext2D();
        //Test player
        this.turn = new Player("Player1",Color.GREENYELLOW,new Tank(Color.GREENYELLOW, new Point(0,0)));

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
        Illustrator.drawTank(this.gc,this.turn.tank);

    }


    public void tanksPlacement() {
        int gap = Constants.WINDOWS_WIDTH / 2;
        int posXFirstTank = (int) (Math.random() * 2 * Constants.WINDOWS_WIDTH/5 + Constants.TANK_SIZE);
        int posXSecondTank = (int) (posXFirstTank + gap + gap * Math.random());
        if (posXSecondTank > Constants.WINDOWS_WIDTH) posXSecondTank = Constants.WINDOWS_WIDTH - Constants.TANK_SIZE * 2;
        int posYSecondTank = Constants.WINDOWS_HEIGHT - Constants.TANK_SIZE;
        turn.tank.position.setX(posXFirstTank);

        //Position tank 1 on terrain
        for (int i = 0; i < Constants.WINDOWS_HEIGHT; i++) {
            if (terrain.resolutionMatrix[i][posXFirstTank + Constants.TANK_SIZE/2] == 1) {
                turn.tank.position.setY(i - Constants.TANK_SIZE);
                break;
            }
        }
//        Illustrator.drawTank(gc,turn.tank);

        //Position tank 2 on terrain
        for (int i = 0; i < Constants.WINDOWS_HEIGHT; i++) {
            if (terrain.resolutionMatrix[i][posXSecondTank + Constants.TANK_SIZE/2] == 1) {
                posYSecondTank = i - Constants.TANK_SIZE;
                break;
            }
        }
  //      Illustrator.drawTank(gc,new Tank(Color.BLUE,new Point(posXSecondTank, posYSecondTank)));
    }

    public void onShootButtonClick(ActionEvent actionEvent) {
        //TEST
        GraphicsContext gc = this.gc;

        Shot s = new Shot(new Point(turn.tank.position.getX(),turn.tank.position.getY()),Integer.parseInt(powerTextField.getText()),Integer.parseInt(angleTextField.getText()));
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0,0,Constants.WINDOWS_WIDTH,Constants.WINDOWS_HEIGHT);
                s.shotPosition();
                drawingMethods();
                Illustrator.drawShot(gc,s);
                if (s.position.getX() > Constants.WINDOWS_WIDTH || s.position.getY() > Constants.CANVAS_HEIGHT) {
                    stop();
                    System.out.println("salio");
                }

            }
        }.start();

        angleTextField.clear();
        powerTextField.clear();


    }


}