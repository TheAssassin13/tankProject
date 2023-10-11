package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class LightShot extends Shot {
    public LightShot(Point position, double initialVelocity, double angle){
        super(position, initialVelocity, angle);
        this.damage = Constants.AMMO_DAMAGE[0];
        this.area = 100;
    }

    @Override
    public void drawShot(GraphicsContext gc) {
        gc.drawImage(new Image(Objects.requireNonNull(getClass().getResource("images/light_shot.png")).toExternalForm()), this.position.getX() - Constants.SHOT_WIDTH[0]/2.0, this.position.getY() - Constants.SHOT_HEIGHT[0]/2.0, Constants.SHOT_WIDTH[0], Constants.SHOT_HEIGHT[0]);
    }
}
