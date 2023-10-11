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
    public void drawShot(GraphicsContext gc) {
        gc.drawImage(new Image(Objects.requireNonNull(getClass().getResource("images/light_shot.png")).toExternalForm()), this.position.getX() - Constants.SHOT_WIDTH[1]/2.0, this.position.getY() - Constants.SHOT_HEIGHT[1]/2.0, Constants.SHOT_WIDTH[1], Constants.SHOT_HEIGHT[1]);
    }
}
