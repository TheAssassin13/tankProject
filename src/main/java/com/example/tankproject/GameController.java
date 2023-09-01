package com.example.tankproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    public Canvas grid;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setFill(Color.RED);
        gc.fillRect(50,50,100,250);
        gc.setFill(Color.BLUE);

        gc.fillOval(100,0,300,300);


    }
}