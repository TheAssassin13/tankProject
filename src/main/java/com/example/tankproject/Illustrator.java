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

    public static void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        gc.setFill(Color.ORANGE);
        gc.fillOval(100, 50, 300, 300);
    }

    public static void drawTerrain(GraphicsContext gc, Land terrain) {
        gc.setFill(Color.BLACK);
        for (int i = 0; i < Constants.WINDOWS_HEIGHT; i++) {
            for (int j = 0; j < Constants.WINDOWS_WIDTH; j++) {
                if (terrain.resolutionMatrix[i][j] == 1) {
                    gc.fillRect(j, i,1,1000);
                }
            }
        }
    }

    public static void drawTank(GraphicsContext gc, Tank tank) {
        gc.setFill(tank.color);
        gc.fillOval(tank.position.getX(),tank.position.getY(),Constants.TANK_SIZE,Constants.TANK_SIZE);
    }

    public static void drawShot(GraphicsContext gc, Shot shot) {

    }
}
