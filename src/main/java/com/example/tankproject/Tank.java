package com.example.tankproject;

import javafx.scene.paint.Color;

public class Tank {
    public Color color;
    public Point position;

    public Tank(Color color, Point position) {
        this.color = color;
        this.position = position;
    }
}
