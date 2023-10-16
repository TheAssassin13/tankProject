package com.example.tankproject;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

import java.util.Random;

public class CPU extends Player{

    public CPU(String name, Color color, Tank tank) {
        super(name, color, tank);
    }

    public void shoot(Button shootButton, ToggleButton lightShot, ToggleButton mediumShot, ToggleButton heavyShot, TextField angle, TextField power, Point positionEnemy) {
        if (this.tank.getHealth() == 0) return;
        Random random = new Random();
        double x1 = this.tank.position.getX();
        double y1 = Constants.CANVAS_HEIGHT - this.tank.position.getY();
        double x2 = positionEnemy.getX();
        double y2 = Constants.CANVAS_HEIGHT - positionEnemy.getY();

        double maxHeight = random.nextInt(Constants.CANVAS_HEIGHT, Constants.CANVAS_HEIGHT*2);
        double verticalVelocity = Math.sqrt(2.0 * Constants.GRAVITY * (maxHeight - y1));
        double time = verticalVelocity / Constants.GRAVITY + Math.sqrt((2 / Constants.GRAVITY) * (maxHeight - y2));
        double horizontalVelocity = (x2 - x1) / time;

        double angleShoot = Math.toDegrees(Math.atan(verticalVelocity / horizontalVelocity));
        if (angleShoot < 0.0) angleShoot += 180.0;
        double powerShoot = Math.sqrt(Math.pow(verticalVelocity, 2) + Math.pow(horizontalVelocity, 2));

        int shotType = random.nextInt(3);
        if (shotType == 0 && this.tank.ammunition.get(0) > 0) lightShot.fire();
        else if (shotType == 1 && this.tank.ammunition.get(1) > 0) mediumShot.fire();
        else if (this.tank.ammunition.get(2) > 0) heavyShot.fire();

        angle.setText(String.valueOf(angleShoot));
        power.setText(String.valueOf(powerShoot));
        shootButton.fire();
    }
}
