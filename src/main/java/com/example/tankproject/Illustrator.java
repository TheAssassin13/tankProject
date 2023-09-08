package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Illustrator {


    public static void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        gc.setFill(Color.ORANGE);
        gc.fillOval(100, 50, 300, 300);
        gc.setFill(Color.web("#332E30"));
        gc.fillRect(0,Constants.SEA_LEVEL,Constants.WINDOWS_WIDTH, Constants.CANVAS_HEIGHT - Constants.SEA_LEVEL);
    }

    public static void drawTerrain(GraphicsContext gc, Terrain terrain) {
        gc.setFill(Color.BLACK);
        for (int i = 0; i < Constants.WINDOWS_HEIGHT; i++) {
            for (int j = 0; j < Constants.WINDOWS_WIDTH; j++) {
                if (terrain.resolutionMatrix[i][j] == 1) {
                    gc.fillRect(j, i,1,2);
                }
            }
        }
    }

    public static void drawTank(GraphicsContext gc, Tank tank) {
        gc.setFill(tank.color);
        gc.fillOval(tank.position.getX(),tank.position.getY(),Constants.TANK_SIZE,Constants.TANK_SIZE);
        gc.fillRect(tank.position.getX() + Constants.TANK_SIZE/2 - 2, tank.position.getY() - 8, 5, 10);
    }

    public static void drawShot(GraphicsContext gc, Shot shot) {
        gc.setFill(Constants.SHOT_COLOR);
        gc.fillOval(shot.position.getX() - Constants.SHOT_SIZE / 2,shot.position.getY() - Constants.SHOT_SIZE / 2,Constants.SHOT_SIZE,Constants.SHOT_SIZE);
    }
}
