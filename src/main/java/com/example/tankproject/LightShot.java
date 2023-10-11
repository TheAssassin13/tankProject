package com.example.tankproject;

public class LightShot extends Shot {
    public LightShot(Point position, double initialVelocity, double angle){
        super(position, initialVelocity, angle);
        this.damage = Constants.AMMO_DAMAGE[0];
    }
}
