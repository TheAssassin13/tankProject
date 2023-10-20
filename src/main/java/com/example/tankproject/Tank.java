package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Tank {
    public Color color;
    public Point position;
    private Double angle;
    public Double power;
    private ToggleButton ammoSelected;
    public ArrayList<Integer> ammunition;
    private int health;

    public Tank(Color color, Point position) {
        this.color = color;
        this.position = position;
        this.angle = null;
        this.power = null;
        this.ammoSelected = null;
        reloadAmmunition();
        this.health= Constants.TANK_HEALTH;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Double getAngle() {
        return angle;
    }

    public int getHealth() {
        return this.health;
    }
    
    public void reduceHealth(int damage) {
        this.health = Math.max(0,this.health - damage);
    }

    public void restoreHealth() { this.health = Constants.TANK_HEALTH; }

    public void reloadAmmunition() {
        this.ammunition = new ArrayList<>();
        this.ammunition.add(Constants.AMMO_QUANTITY[0]);
        this.ammunition.add(Constants.AMMO_QUANTITY[1]);
        this.ammunition.add(Constants.AMMO_QUANTITY[2]);
    }

    public void setAmmoSelected(ToggleButton ammoSelected) {
        this.ammoSelected = ammoSelected;
    }

    public ToggleButton getAmmoSelected() {
        return this.ammoSelected;
    }

    public void drawTank(GraphicsContext gc, ImagesLoader images) {
        Double angle = this.getAngle();
        int wheelSize = Constants.TANK_SIZE/3;

        gc.setFill(this.color);
        // Wheels
        gc.fillOval(this.position.getX() - wheelSize,this.position.getY(),wheelSize,wheelSize);
        gc.fillOval(this.position.getX(),this.position.getY(),wheelSize,wheelSize);
        gc.fillOval(this.position.getX() + wheelSize,this.position.getY(),wheelSize,wheelSize);

        // Body tank
        gc.fillRect(this.position.getX() - wheelSize, this.position.getY() - wheelSize, Constants.TANK_SIZE, wheelSize);
        gc.fillRect(this.position.getX() - wheelSize / 2.0, this.position.getY() - wheelSize * 2, Constants.TANK_SIZE / 2.0,wheelSize * 2);

        // Canon(with rotation added)
        if (angle == null) angle = 0.0;
        Rotate rotation = new Rotate(angle * -1, this.position.getX(), this.position.getY() - wheelSize * 1.8);
        gc.setTransform(rotation.getMxx(), rotation.getMyx(), rotation.getMxy(), rotation.getMyy(), rotation.getTx(), rotation.getTy());
        gc.fillRect(this.position.getX(), this.position.getY() - wheelSize * 1.8, Constants.TANK_SIZE, wheelSize / 2.0);
        gc.setTransform(new Affine());

        //hat
        gc.drawImage(images.witchHatImage, this.position.getX() + 3 - Constants.TANK_SIZE / 2.0, this.position.getY() -32- wheelSize, Constants.TANK_SIZE, Constants.TANK_SIZE);
    }
}
