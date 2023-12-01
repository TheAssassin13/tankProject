package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;

public class LightShot extends Shot {
    public LightShot(Point position, double initialVelocity, double angle, Player shotPlayer){
        super(position, initialVelocity, angle, shotPlayer);
        this.damage = Constants.AMMO_DAMAGE[0];
        this.area = 100;
    }

    @Override
    public void drawShot(GraphicsContext gc) {
        gc.drawImage(ImagesLoader.getInstance().currentShotImages.get(0), this.position.getX() - Constants.SHOT_SIZE[0]/2.0, this.position.getY() - Constants.SHOT_SIZE[0]/2.0, Constants.SHOT_SIZE[0], Constants.SHOT_SIZE[0]);
    }
}
