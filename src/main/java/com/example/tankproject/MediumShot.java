package com.example.tankproject;

public class MediumShot extends Shot {
    public MediumShot(Point position, double initialVelocity, double angle){
        super(position, initialVelocity, angle);
        this.damage = Constants.AMMO_DAMAGE[1];
    }
}
