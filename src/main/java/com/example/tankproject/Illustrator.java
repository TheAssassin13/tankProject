package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

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
        for (int j = 0; j < Constants.WINDOWS_WIDTH; j++) {
            int i = 0;
            while (i < Constants.CANVAS_HEIGHT) {
                if (terrain.resolutionMatrix[i][j] == 1) {
                    if (i > 0 && terrain.resolutionMatrix[i-1][j] == 0) {
                        gc.setFill(Color.DARKORANGE);
                        gc.fillRect(j, i,Constants.PIXEL_SIZE,Constants.PIXEL_SIZE);
                        gc.fillRect(j, i+1,Constants.PIXEL_SIZE,Constants.PIXEL_SIZE);
                        gc.setFill(Constants.TERRAIN_COLOR);
                        i++;
                    } else {
                        gc.fillRect(j, i,Constants.PIXEL_SIZE,Constants.PIXEL_SIZE);
                    }
                }
                i++;
            }
        }
    }

    public static void drawTank(GraphicsContext gc, Tank tank, double currentAngle) {
        int wheelSize = Constants.TANK_SIZE/3;
        gc.setFill(tank.color);
        //Wheels
        gc.fillOval(tank.position.getX() - wheelSize,tank.position.getY(),wheelSize,wheelSize);
        gc.fillOval(tank.position.getX(),tank.position.getY(),wheelSize,wheelSize);
        gc.fillOval(tank.position.getX() + wheelSize,tank.position.getY(),wheelSize,wheelSize);

        gc.fillRect(tank.position.getX() - wheelSize, tank.position.getY() - wheelSize, Constants.TANK_SIZE, wheelSize);
        gc.fillRect(tank.position.getX() - wheelSize/2, tank.position.getY() - wheelSize * 2, Constants.TANK_SIZE/2,wheelSize*2);

        //Canon(with rotation added)
        Rotate rotation = new Rotate(tank.angle*-1, tank.position.getX(), tank.position.getY() - wheelSize * 1.8);
        gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
        gc.fillRect(tank.position.getX(), tank.position.getY() - wheelSize * 1.8, Constants.TANK_SIZE, wheelSize / 2);
        gc.setTransform(new Affine());

    }

    public static void drawShot(GraphicsContext gc, Shot shot) {
        gc.setFill(Constants.SHOT_COLOR);
        gc.fillOval(shot.position.getX() - Constants.SHOT_SIZE / 2,shot.position.getY() - Constants.SHOT_SIZE / 2,Constants.SHOT_SIZE,Constants.SHOT_SIZE);
    }

    public static void drawTrajectory(GraphicsContext gc, Shot shot) {
        gc.setFill(Color.GRAY);
        for (int i = 0; i < shot.trajectory.size(); i+=2) {
            gc.fillOval(shot.trajectory.get(i).getX() - Constants.SHOT_SIZE / 2,shot.trajectory.get(i).getY() - Constants.SHOT_SIZE / 2, Constants.SHOT_SIZE/2, Constants.SHOT_SIZE/2);
        }
    }
}
