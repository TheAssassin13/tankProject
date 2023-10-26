package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class MediumShot extends Shot {
    public MediumShot(Point position, double initialVelocity, double angle){
        super(position, initialVelocity, angle);
        this.damage = Constants.AMMO_DAMAGE[1];
        this.area = 300;
    }

    @Override
    public void drawShot(GraphicsContext gc, ImagesLoader images) {
        gc.drawImage(images.shotImages.get(1), this.position.getX() - Constants.SHOT_SIZE[1]/2.0, this.position.getY() - Constants.SHOT_SIZE[1]/2.0, Constants.SHOT_SIZE[1], Constants.SHOT_SIZE[1]);
    }
}