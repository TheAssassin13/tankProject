package com.example.tankproject;


import java.util.ArrayList;

public class Shot {
    public Point position;
    public double velocityX, velocityY;
    public double initialVelocity;
    public double angle;
    public ArrayList<Point> trajectory;

    public Shot(Point position, double initialVelocity, double angle) {
        this.initialVelocity = initialVelocity;
        this.angle = Math.toRadians(angle);
        this.position = position;
        this.velocityX = initialVelocity * Math.cos(this.angle);
        this.velocityY = -initialVelocity * Math.sin(this.angle);
        trajectory = new ArrayList<>();
    }

    // Updates the position of the shot
    public void shotPosition() {
        this.position.setX((int) (this.position.getX() + velocityX * Constants.SHOT_VELOCITY));
        this.position.setY((int) (this.position.getY() + velocityY * Constants.SHOT_VELOCITY));
        this.velocityY += Constants.GRAVITY;
    }

    // Checks if a tank gets hit by the shot
    public boolean tankCollision(Tank tank) {
        return (Math.pow(tank.position.getX() - this.position.getX(),2) + Math.pow(tank.position.getY() - this.position.getY(),2))  <= (Math.pow(Constants.TANK_SIZE, 2) - Constants.SHOT_SIZE);
    }

    // Checks if the shot hits the terrain
    public boolean terrainCollision(Terrain terrain) {
        if (this.position.getY() < 0 || this.position.getX() < 0 || this.position.getX() >= Constants.WINDOWS_WIDTH) {
            return false;
        }
        if (terrain.resolutionMatrix[this.position.getY()][this.position.getX()] == 1) {
            return true;
        }
        return false;
    }

    // Adds one point to the shot trajectory
    public void addTrajectory() {
        if (trajectory.size() != 0 && (Math.pow(trajectory.get(trajectory.size()-1).getX() - this.position.getX(),2) + Math.pow(trajectory.get(trajectory.size()-1).getY() - this.position.getY(),2)) < 400) return;
        trajectory.add(new Point(position.getX(), position.getY()));
    }
}
