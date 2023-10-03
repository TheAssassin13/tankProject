package com.example.tankproject;

import java.util.Random;

public class MysteryBox {
    Point position;
    public int powerUp;
    public MysteryBox(Point position) {
        this.position = position;
        this.powerUp = new Random().nextInt(2);
    }

    public void obtainPowerUp(Player player) {
        if (powerUp == 0) {
            player.restoreHealth();
        } else if (powerUp == 1) {
            // Lots of destruction
        }
    }
}
