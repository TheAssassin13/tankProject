package com.example.tankproject;


public class Shot {
    public Point position;
    private double velocityX, velocityY; // Velocidad de la bala
    private double initialVelocity; // Velocidad inicial de la bala
    private double angle; // Ángulo de lanzamiento

    public Shot(Point position, double initialVelocity, double angle) {
        this.initialVelocity = initialVelocity;
        this.angle = Math.toRadians(angle); // Convierte el ángulo a radianes
        this.position = position;
        this.velocityX = initialVelocity * Math.cos(this.angle);
        this.velocityY = -initialVelocity * Math.sin(this.angle);
    }

    public void shotPosition() {
        // Esto va actualizando la posición de la bala
        this.position.setX((int) (this.position.getX() + velocityX));
        this.position.setY((int) (this.position.getY() + velocityY));
        this.velocityY += Constants.GRAVITY;

    }

    public boolean tankColission(Tank tank) {
        return (Math.pow(tank.position.getX() - this.position.getX(),2) + Math.pow(tank.position.getY() - this.position.getY(),2))  <= (Math.pow(Constants.TANK_SIZE, 2) + Constants.SHOT_SIZE);
    }

    public boolean TerrainColission(Terrain terrain) {
        /*if () {

        }*/
        return true;
    }
}
