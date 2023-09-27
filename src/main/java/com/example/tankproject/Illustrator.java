package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Illustrator {

    /* This method draws the terrain generated in the resolution matrix.
    This matrix includes every "pixel" of the screen.
    To optimize in this method we use an array of the highest points,
    but for the functioning code we use the resolution matrix.
    1 means terrain, 0 means air.
     */
    public static void drawTerrain(GraphicsContext gc, Terrain terrain) {
        for (int i = 0; i < Constants.WINDOWS_WIDTH; i++) {
            gc.setFill(Color.DARKORANGE);
            gc.fillRect(i, terrain.maxTerrainHeight[i], 1, 2);
            gc.setFill(Constants.TERRAIN_COLOR);
            gc.fillRect(i, terrain.maxTerrainHeight[i] + 2, 1, Constants.CANVAS_HEIGHT - terrain.maxTerrainHeight[i]);
        }
    }

    public static void drawTank(GraphicsContext gc, Tank tank) {
        Double angle = tank.angle;
        int wheelSize = Constants.TANK_SIZE/3;

        gc.setFill(tank.color);
        // Wheels
        gc.fillOval(tank.position.getX() - wheelSize,tank.position.getY(),wheelSize,wheelSize);
        gc.fillOval(tank.position.getX(),tank.position.getY(),wheelSize,wheelSize);
        gc.fillOval(tank.position.getX() + wheelSize,tank.position.getY(),wheelSize,wheelSize);

        // Body tank
        gc.fillRect(tank.position.getX() - wheelSize, tank.position.getY() - wheelSize, Constants.TANK_SIZE, wheelSize);
        gc.fillRect(tank.position.getX() - wheelSize/2, tank.position.getY() - wheelSize * 2, Constants.TANK_SIZE/2,wheelSize*2);

        // Canon(with rotation added)
        if (angle == null) angle = 0.0;
        Rotate rotation = new Rotate(angle * -1, tank.position.getX(), tank.position.getY() - wheelSize * 1.8);
        gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
        gc.fillRect(tank.position.getX(), tank.position.getY() - wheelSize * 1.8, Constants.TANK_SIZE, wheelSize / 2);
        gc.setTransform(new Affine());

    }

    public static void drawShot(GraphicsContext gc, Shot shot) {
        gc.setFill(Constants.SHOT_COLOR);
        gc.fillOval(shot.position.getX() - Constants.SHOT_SIZE / 2,shot.position.getY() - Constants.SHOT_SIZE / 2,Constants.SHOT_SIZE,Constants.SHOT_SIZE);
    }

    public static void drawTrajectory(GraphicsContext gc, Shot shot) {
        gc.setFill(Constants.TRAJECTORY_COLOR);
        for (int i = 0; i < shot.trajectory.size(); i++) {
            gc.fillOval(shot.trajectory.get(i).getX() - Constants.SHOT_SIZE / 2,shot.trajectory.get(i).getY() - Constants.SHOT_SIZE / 2, Constants.SHOT_SIZE/2, Constants.SHOT_SIZE/2);
        }
    }
}
