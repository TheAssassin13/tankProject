package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class MysteryBox {
    Point position;
    public int powerUp;
    public MysteryBox(Point position) {
        this.position = position;
        this.powerUp = new Random().nextInt(3);
    }

    public void drawMysteryBox(GraphicsContext gc) {
        gc.drawImage(Loader.getInstance().mysteryBoxImage, this.position.getX() - Constants.BOX_SIZE/2.0, this.position.getY() - Constants.BOX_SIZE/2.0, Constants.BOX_SIZE, Constants.BOX_SIZE);
    }
}
