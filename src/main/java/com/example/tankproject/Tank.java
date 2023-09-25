package com.example.tankproject;

import javafx.scene.paint.Color;

public class Tank {
    public Color color;
    public Point position;
    public Double angle;
    public Double power;

    public Tank(Color color, Point position) {
        this.color = color;
        this.position = position;
        this.angle = null;
        this.power = null;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
