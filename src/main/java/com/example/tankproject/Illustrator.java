package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Illustrator {


    public static void drawBackground(GraphicsContext gc) {
        gc.setFill(Constants.SKY_COLOR);
        gc.fillRect(0, 0, Constants.WINDOWS_WIDTH, Constants.WINDOWS_HEIGHT);
        gc.setFill(Constants.SUN_COLOR);
        gc.fillOval(100, 50, 300, 300);
        gc.setFill(Constants.SEA_LEVEL_COLOR);
        gc.fillRect(0,Constants.SEA_LEVEL,Constants.WINDOWS_WIDTH, Constants.CANVAS_HEIGHT - Constants.SEA_LEVEL);
    }

    public static void drawTerrain(GraphicsContext gc, Terrain terrain) {
        gc.setFill(Constants.TERRAIN_COLOR);
        for (int i = 0; i < Constants.CANVAS_HEIGHT; i++) {
            for (int j = 0; j < Constants.WINDOWS_WIDTH; j++) {
                if (terrain.resolutionMatrix[i][j] == 1) {
                    gc.fillRect(j, i,Constants.PIXEL_SIZE,Constants.PIXEL_SIZE);
                }
            }
        }
    }

    public static void drawTank(GraphicsContext gc, Tank tank) {
        int wheelSize = Constants.TANK_SIZE/3;
        gc.setFill(tank.color);
        //Wheels
        gc.fillOval(tank.position.getX() - wheelSize,tank.position.getY(),wheelSize,wheelSize);
        gc.fillOval(tank.position.getX(),tank.position.getY(),wheelSize,wheelSize);
        gc.fillOval(tank.position.getX() + wheelSize,tank.position.getY(),wheelSize,wheelSize);

        gc.fillRect(tank.position.getX() - wheelSize, tank.position.getY() - wheelSize, Constants.TANK_SIZE, wheelSize);
        gc.fillRect(tank.position.getX() - wheelSize/2, tank.position.getY() - wheelSize * 2, Constants.TANK_SIZE/2,wheelSize*2);

        //Canon
        gc.fillRect(tank.position.getX(), tank.position.getY() - wheelSize * 1.8, Constants.TANK_SIZE,wheelSize/2);
    }

    public static void drawShot(GraphicsContext gc, Shot shot) {
        gc.setFill(Constants.SHOT_COLOR);
        gc.fillOval(shot.position.getX() - Constants.SHOT_SIZE / 2,shot.position.getY() - Constants.SHOT_SIZE / 2,Constants.SHOT_SIZE,Constants.SHOT_SIZE);
    }

    public static void drawTrajectory(GraphicsContext gc, Shot shot) {
        gc.setFill(Color.GRAY);
        for (int i = 0; i < shot.trajectory.size(); i+=3) {
            gc.fillOval(shot.trajectory.get(i).getX() - Constants.SHOT_SIZE / 2,shot.trajectory.get(i).getY() - Constants.SHOT_SIZE / 2, Constants.SHOT_SIZE, Constants.SHOT_SIZE);
        }
    }
}
