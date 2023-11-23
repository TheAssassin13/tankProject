package com.example.tankproject;

import javafx.scene.paint.Color;

public class Player {
    public String name;
    public Color color;
    public Tank tank;
    public int position;
    public int score;

    public Player(String name, Color color, Tank tank) {
        this.name = name;
        this.color = color;
        this.tank = tank;
        this.position = 1;
        this.score = 0;
    }

    public Player(Player player) {
        this.name = player.name;
        this.color = player.color;
        this.tank = player.tank;
        this.position = player.position;
        this.score = player.score;
    }
}