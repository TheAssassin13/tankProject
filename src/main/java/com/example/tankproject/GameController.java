package com.example.tankproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    public Player turn;
    public Text currentPlayerText;
    public GridPane buttonsPanel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Player pTest = new Player("Player1",Color.GREENYELLOW,new Tank(Color.GREENYELLOW, new Point(0,0)));
        turn = pTest;
        GraphicsContext gc = grid.getGraphicsContext2D();
        currentPlayerText.setText("Current Player: " + turn.name);
        buttonsPanel.setPrefHeight(Constants.BUTTONS_PANEL_HEIGHT);
        Land terrain = new Land(Constants.WINDOWS_HEIGHT, Constants.WINDOWS_WIDTH);
        terrain.terrainGeneration(Constants.SEA_LEVEL,false);
        grid.setHeight(Constants.WINDOWS_HEIGHT-Constants.BUTTONS_PANEL_HEIGHT);
        grid.setWidth(Constants.WINDOWS_WIDTH);
        Illustrator.drawBackground(gc);
        Illustrator.drawTerrain(gc,terrain);
        tanksPlacement(gc);

    }


    public void tanksPlacement(GraphicsContext gc) {
        int gap = Constants.WINDOWS_WIDTH / 2;
        int posXFirstTank = (int) (Math.random() * (gap/2 - 25) + 25);
        int posxSecondTank = posXFirstTank + gap;
        turn.tank.position.setX(posXFirstTank);
        turn.tank.position.setY(Constants.SEA_LEVEL);
        Illustrator.drawTank(gc,turn.tank);
        Illustrator.drawTank(gc,new Tank(Color.BLUE,new Point(posxSecondTank,Constants.SEA_LEVEL)));
    }
}