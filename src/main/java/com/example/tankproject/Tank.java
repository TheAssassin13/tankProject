package com.example.tankproject;

import javafx.scene.paint.Color;

public class Tank {
    public Color color;
    public Point position;
    public double angle = -1.23;
    public double power = -1.23;

    public Tank(Color color, Point position) {
        this.color = color;
        this.position = position;
    }
}
