package com.example.tankproject;

import javafx.scene.canvas.GraphicsContext;

public class MediumShot extends Shot {
    public MediumShot(Point position, double initialVelocity, double angle, Player shotPlayer){
        super(position, initialVelocity, angle, shotPlayer);
        this.damage = Constants.AMMO_DAMAGE[1];
        this.area = 300;
    }

    @Override
    public void drawShot(GraphicsContext gc) {
        gc.drawImage(ImagesLoader.getInstance().currentShotImages.get(1), this.position.getX() - Constants.SHOT_SIZE[1]/2.0, this.position.getY() - Constants.SHOT_SIZE[1]/2.0, Constants.SHOT_SIZE[1], Constants.SHOT_SIZE[1]);
    }
}
