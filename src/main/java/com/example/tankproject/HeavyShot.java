package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class HeavyShot extends Shot {
    public HeavyShot(Point position, double initialVelocity, double angle){
        super(position, initialVelocity, angle);
        this.damage = Constants.AMMO_DAMAGE[2];
        this.area = 500;
    }

    @Override
    public void drawShot(GraphicsContext gc) {
        gc.drawImage(ImagesLoader.getInstance().shotImages.get(2), this.position.getX() - Constants.SHOT_SIZE[2]/2.0, this.position.getY() - Constants.SHOT_SIZE[2]/2.0, Constants.SHOT_SIZE[2], Constants.SHOT_SIZE[2]);
    }
}
