package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;
import java.util.Random;

public class MysteryBox {
    Point position;
    public int powerUp;
    public MysteryBox(Point position) {
        this.position = position;
        this.powerUp = new Random().nextInt(2);
    }

    public void drawMysteryBox(GraphicsContext gc, ImagesLoader images) {
        gc.drawImage(images.mysteryBoxImage, this.position.getX() - Constants.BOX_SIZE/2.0, this.position.getY() - Constants.BOX_SIZE/2.0, Constants.BOX_SIZE, Constants.BOX_SIZE);
    }
}
