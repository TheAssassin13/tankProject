package com.example.tankproject;

import javafx.scene.paint.Color;

public class Player {
    public String name;
    public Color color;
    public Tank tank;
    private int health;

    public Player(String name, Color color, Tank tank) {
        this.name = name;
        this.color = color;
        this.tank = tank;
        this.health= Constants.TANK_HEALTH;
    }
    public int getHealth() {
        return this.health;
    }
    public void reduceHealth(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public void restoreHealth() { this.health = Constants.TANK_HEALTH; }
}
