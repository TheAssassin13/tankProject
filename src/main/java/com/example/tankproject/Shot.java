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

        // Verifica si la bala ha alcanzado la altura del canvas
        if (this.position.getY() >= Constants.WINDOWS_HEIGHT) {
            // Restablece la posición de la bala
            this.position.setX(50);
            this.position.setY(300);
            this.balavelocidadY = -initialVelocity * Math.sin(angle);
        }

    }



}
