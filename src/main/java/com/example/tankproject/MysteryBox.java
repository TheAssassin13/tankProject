package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class MysteryBox {
    Point position;
    public int powerUp;
    public MysteryBox(Point position) {
        this.position = position;
        this.powerUp = new Random().nextInt(2);
    }

    public void drawMysteryBox(GraphicsContext gc) {
        gc.setFill(Constants.BOX_COLOR);
        gc.fillRect(this.position.getX() - Constants.BOX_SIZE/2, this.position.getY() - Constants.BOX_SIZE/2, Constants.BOX_SIZE, Constants.BOX_SIZE);
    }
}
