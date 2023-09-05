package com.example.tankproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    public VBox vbox;
    public Canvas grid;

    public Player turn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid.setHeight(Constants.WINDOWS_HEIGHT-150);
        grid.setWidth(Constants.WINDOWS_WIDTH);
        GraphicsContext gc = grid.getGraphicsContext2D();
        //Illustrator.drawTest(gc);
        Illustrator.drawTank(gc,new Tank(new Point(50,60)));

    }

}