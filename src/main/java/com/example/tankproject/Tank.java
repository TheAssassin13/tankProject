package com.example.tankproject;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class Tank {
    public Color color;
    public Point position;
    public Double angle;
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

    public double getAngle() {
        return angle;
    }



    public HashMap<String, Integer> reloadAmmunition() {
        HashMap<String, Integer> temp = new HashMap<>();
        temp.put("Bullet50", 3);
        temp.put("Bullet40", 10);
        temp.put("Bullet30", 3);
        return temp;
    }
}
