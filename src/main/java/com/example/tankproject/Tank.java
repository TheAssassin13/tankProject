package com.example.tankproject;

import javafx.scene.paint.Color;
import java.util.HashMap;

public class Tank {
    public Color color;
    public Point position;
    private Double angle;
    public Double power;
    public HashMap<String,Integer> ammunition;

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

    public Double getAngle() {
        return angle;
    }

    public HashMap<String, Integer> reloadAmmunition() {
        HashMap<String, Integer> temp = new HashMap<>();
        temp.put("Bullet30", Constants.AMMO_QUANTITY[0]);
        temp.put("Bullet40", Constants.AMMO_QUANTITY[1]);
        temp.put("Bullet50", Constants.AMMO_QUANTITY[2]);
        return temp;
    }
}
