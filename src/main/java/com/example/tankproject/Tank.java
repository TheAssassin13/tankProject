package com.example.tankproject;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Tank {
    public Color color;
    public Point position;
    public Double angle;
    public Double power;
    public ArrayList<Integer> ammunition;

    public Tank(Color color, Point position) {
        this.color = color;
        this.position = position;
        this.angle = null;
        this.power = null;
        this.ammunition = reloadAmmunition();
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public ArrayList<Integer> reloadAmmunition() {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(3);
        temp.add(10);
        temp.add(3);
        return temp;
    }
}
