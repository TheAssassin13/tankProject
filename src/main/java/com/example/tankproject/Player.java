package com.example.tankproject;

import javafx.scene.paint.Color;

public class Player {
    public String name;
    public Color color;
    public Tank tank;

    public Player(String name, Color color, Tank tank) {
        this.name = name;
        this.color = color;
        this.tank = tank;
    }
}
