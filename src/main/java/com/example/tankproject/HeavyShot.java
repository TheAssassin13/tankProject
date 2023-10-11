package com.example.tankproject;

public class HeavyShot extends Shot {
    public HeavyShot(Point position, double initialVelocity, double angle){
        super(position, initialVelocity, angle);
        this.damage = Constants.AMMO_DAMAGE[2];
    }
}
