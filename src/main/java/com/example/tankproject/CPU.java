package com.example.tankproject;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

import java.util.Random;

public class CPU extends Player{

    Point target;
    int difficulty;

    public CPU(String name, Color color, Tank tank, int difficulty) {
        super(name, color, tank);
        this.difficulty = difficulty;
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
        double initialPosY = Data.getInstance().canvasHeight - this.tank.position.getY();
        double finalPosX = this.target.getX();
        double finalPosY = Data.getInstance().canvasHeight - this.target.getY();

        // Important variables calculation
        double maxHeight = random.nextInt(Data.getInstance().canvasHeight, Data.getInstance().canvasHeight *2);
        double verticalVelocity = Math.sqrt(2.0 * Data.getInstance().gravity * (maxHeight - initialPosY));
        double time = verticalVelocity / Data.getInstance().gravity + Math.sqrt((2 / Data.getInstance().gravity) * (maxHeight - finalPosY));
        double horizontalVelocity = (finalPosX - initialPosX) / time - Data.getInstance().windVelocity;

        // Angle and power calculations
        angleShoot = Math.toDegrees(Math.atan(verticalVelocity / horizontalVelocity));
        if (angleShoot < 0.0) angleShoot += 180.0;
        powerShoot = Math.sqrt(Math.pow(verticalVelocity, 2) + Math.pow(horizontalVelocity, 2));

        // Depending on the difficulty the power is randomized
        if (this.difficulty== 1) {
            powerShoot = random.nextDouble(powerShoot - 20, powerShoot + 20);
        }
        else if (this.difficulty== 2) {
            powerShoot = random.nextDouble(powerShoot - 10, powerShoot + 10);
        }

        // Shot type random selection
        while (this.tank.getAmmunitionQuantity() > 0) {
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
            this.target = Data.getInstance().alivePlayers.get(random.nextInt(Data.getInstance().alivePlayers.size())).tank.position;
        } while(this.target == this.tank.position);
    }

    // This method returns an array with the amount of ammo it wants
    public int[] getAmmoToBuy() {
        Random random = new Random();
        int creditsToUse = this.tank.credits; // Credits to simulate the purchase
        int[] ammo = {0, 0, 0};

        while (creditsToUse >= Constants.AMMO_PRICE[0]) {
            int shotType = random.nextInt(3);
            if (creditsToUse >= Constants.AMMO_PRICE[shotType]) {
                creditsToUse -= Constants.AMMO_PRICE[shotType];
                ammo[shotType]++;
            }
        }
        return ammo;
    }
}
