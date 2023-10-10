package com.example.tankproject;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Random;

public class CPU extends Player{

    public CPU(String name, Color color, Tank tank) {
        super(name, color, tank);
    }

    public void shoot(Button button, TextField angle, TextField power, Point positionEnemy) {
        if (this.getHealth() == 0) return;
        Random random = new Random();
        angle.setText(String.valueOf(random.nextDouble(100, 180)));
        power.setText(String.valueOf(random.nextDouble(30, 110)));
        button.fire();
    }
}
