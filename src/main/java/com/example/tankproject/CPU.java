package com.example.tankproject;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

import java.util.Random;

public class CPU extends Player{

    Point target;

    public CPU(String name, Color color, Tank tank) {
        super(name, color, tank);
    }

    public CPU(Player player) {
        super(player);
    }

    // The CPU makes its shot with calculations and random variables
    public void shoot(Button shootButton, ToggleButton lightShot, ToggleButton mediumShot, ToggleButton heavyShot, TextField angle, TextField power) {
        if (this.tank.getHealth() == 0) return; // In case the tank is dead
        Random random = new Random();
        chooseTarget(); // It chooses where it wants to shoot

        double angleShoot;
        double powerShoot;
        double initialPosX = this.tank.position.getX();
        double initialPosY = Constants.CANVAS_HEIGHT - this.tank.position.getY();
        double finalPosX = this.target.getX();
        double finalPosY = Constants.CANVAS_HEIGHT - this.target.getY();

        // Important variables calculation
        double maxHeight = random.nextInt(Constants.CANVAS_HEIGHT, Constants.CANVAS_HEIGHT*2);
        double verticalVelocity = Math.sqrt(2.0 * Constants.GRAVITY * (maxHeight - initialPosY));
        double time = verticalVelocity / Constants.GRAVITY + Math.sqrt((2 / Constants.GRAVITY) * (maxHeight - finalPosY));
        double horizontalVelocity = (finalPosX - initialPosX) / time;

        // Angle and power calculations
        angleShoot = Math.toDegrees(Math.atan(verticalVelocity / horizontalVelocity));
        if (angleShoot < 0.0) angleShoot += 180.0;
        powerShoot = Math.sqrt(Math.pow(verticalVelocity, 2) + Math.pow(horizontalVelocity, 2));

        // Depending on the difficulty the power is randomized
        if (Constants.CPU_DIFFICULTY == 1) {
            powerShoot = random.nextInt((int) powerShoot - 20, (int) powerShoot + 20);
        }
        else if (Constants.CPU_DIFFICULTY == 2) {
            powerShoot = random.nextInt((int) powerShoot - 10, (int) powerShoot + 10);
        }

        // Shot type random selection
        while (true) {
            int shotType = random.nextInt(3);
            if (shotType == 0 && this.tank.ammunition.get(0) > 0) {
                lightShot.fire();
                break;
            }
            else if (shotType == 1 && this.tank.ammunition.get(1) > 0) {
                mediumShot.fire();
                break;
            }
            else if (this.tank.ammunition.get(2) > 0) {
                heavyShot.fire();
                break;
            }
        }

        // It fires the shot with the angle and power previously calculated
        angle.setText(String.valueOf(angleShoot));
        power.setText(String.valueOf(powerShoot));
        shootButton.fire();
    }

    // This method chooses a target for the CPU. It could be a player or a mystery box
    public void chooseTarget() {
        Random random = new Random();
        // There's a 1/3 chance that chooses a mystery box as a target
        if (!Data.getInstance().mysteryBoxes.isEmpty() && random.nextInt(3) == 1) {
            this.target = Data.getInstance().mysteryBoxes.get(random.nextInt(Data.getInstance().mysteryBoxes.size())).position;
            return;
        }

        // It chooses a random target that isn't itself
        do {
            this.target = Data.getInstance().alivePlayers.get(random.nextInt(Data.getInstance().tanksQuantity)).tank.position;
        } while(this.target == this.tank.position);
    }
}
