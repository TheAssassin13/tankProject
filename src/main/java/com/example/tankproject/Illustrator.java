package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Illustrator {

    public static void drawTest(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(0,Constants.SEA_LEVEL,Constants.WINDOWS_WIDTH, 10);
        gc.setFill(Color.BLUE);
        gc.fillOval(100,0,300,300);

    }

    public static void drawTerrain(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        Land terrain = new Land(Constants.WINDOWS_HEIGHT, Constants.WINDOWS_WIDTH);
        terrain.terrainGeneration(Constants.SEA_LEVEL);
        for (int i = 0; i < Constants.WINDOWS_HEIGHT; i++) {
            for (int j = 0; j < Constants.WINDOWS_WIDTH; j++) {
                if (terrain.resolutionMatrix[i][j] == 1) {
                    gc.fillRect(j, i,10,10);
                }
            }
        }
    }

    public static void drawTank(GraphicsContext gc, Tank tank) {
        gc.setFill(Color.GREEN);
        gc.fillOval(tank.position.getX(),tank.position.getY(),Constants.TANK_SIZE,Constants.TANK_SIZE);
    }

    public static void drawShot(GraphicsContext gc, Shot shot) {

    }
}
