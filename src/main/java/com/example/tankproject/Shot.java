package com.example.tankproject;


public class Shot {
    public Point position;
    private double balavelocidadX, balavelocidadY; // Velocidad de la bala
    private double initialVelocity; // Velocidad inicial de la bala
    private double angle; // Ángulo de lanzamiento

    public Shot(Point position, double initialVelocity, double angle) {
        this.initialVelocity = initialVelocity;
        this.angle = Math.toRadians(angle); // Convierte el ángulo a radianes
        this.position = position;
        this.balavelocidadX = initialVelocity * Math.cos(this.angle);
        this.balavelocidadY = -initialVelocity * Math.sin(this.angle);
    }

    public void shotPosition() {
        // Esto va actualizando la posición de la bala
        this.position.setX((int) (this.position.getX() + balavelocidadX));
        this.position.setY((int) (this.position.getY() + balavelocidadY));
        this.balavelocidadY += Constants.GRAVITY;

    }

    public boolean tankColission(Tank tank) {
        //
        return true;
    }

    public boolean TerrainColission(Terrain terrain) {
        if (this.position.getY() < 0 || this.position.getX() < 0 || this.position.getX() > Constants.WINDOWS_WIDTH) {
            return false;
        }
        if (terrain.resolutionMatrix[this.position.getY()][this.position.getX()] == 1) {
            return true;
        }
        return false;
    }
}
