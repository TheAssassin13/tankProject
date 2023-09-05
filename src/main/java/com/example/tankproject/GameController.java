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
        grid.setHeight(Constants.WINDOWHEIGHT);
        grid.setWidth(Constants.WINDOWWIDTH);
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0,0,Constants.WINDOWWIDTH, Constants.WINDOWHEIGHT);
        gc.setFill(Color.ORANGE);

        gc.fillOval(100,0,300,300);


        //Dibujo terreno
        gc.setFill(Color.BLACK);
        Land terrain = new Land(Constants.WINDOWHEIGHT, Constants.WINDOWWIDTH);
        terrain.terrainGeneration(Constants.SEALEVEL);
        for (int i = 0; i < Constants.WINDOWHEIGHT; i++) {
            for (int j = 0; j < Constants.WINDOWWIDTH; j++) {
                if (terrain.resolutionMatrix[i][j] == 1) {
                    gc.fillRect(j, i,1,1000);
                }
            }
        }
    }
}