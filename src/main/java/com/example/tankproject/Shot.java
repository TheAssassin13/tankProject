package com.example.tankproject;


import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Shot {
    public Point position;
    private double x;
    private double y;
    public double velocityX, velocityY;
    public double initialVelocity;
    public double angle;
    public ArrayList<Point> trajectory;
    public int damage;
    public int area;

    public Shot(Point position, double initialVelocity, double angle) {
        this.initialVelocity = initialVelocity;
        this.angle = Math.toRadians(angle);
        this.position = position;
        this.x = position.getX();
        this.y = position.getY();
        this.velocityX = initialVelocity * Math.cos(this.angle);
        this.velocityY = -initialVelocity * Math.sin(this.angle);
        trajectory = new ArrayList<>();
    }

    // Updates the position of the shot
    public void shotPosition() {
        x = (x + velocityX * Constants.SHOT_VELOCITY);
        y = (y + velocityY * Constants.SHOT_VELOCITY);
        this.velocityY += Constants.GRAVITY * Constants.SHOT_VELOCITY;
        this.position.setX((int) x);
        this.position.setY((int) y);
    }

    // Checks if a tank gets hit by the shot
    public double tankCollision(Tank tank) {
        double distance = (Math.pow(tank.position.getX() - this.position.getX(),2) + Math.pow(tank.position.getY() - this.position.getY(),2));
        if (distance <= (Math.pow(Constants.TANK_SIZE, 2) - Constants.SHOT_TRAJECTORY_SIZE)) {
            return 1;
        } else return Math.exp(-0.05 * (Math.sqrt(distance) - Constants.TANK_SIZE));
    }

    // Checks if the shot hits the terrain
    public boolean terrainCollision(Terrain terrain) {
        if (this.position.getY() < 0 || this.position.getX() < 0 || this.position.getX() >= Constants.WINDOWS_WIDTH) {
            return false;
        }
        return terrain.resolutionMatrix[this.position.getY()][this.position.getX()] == 1;
    }

    // Checks if a mystery box gets hit by the shot
    public boolean mysteryBoxCollision(MysteryBox box) {
        return (Math.pow(box.position.getX() - this.position.getX(),2) + Math.pow(box.position.getY() - this.position.getY(),2))  <= (Math.pow(Constants.BOX_SIZE, 2) - Constants.SHOT_TRAJECTORY_SIZE);
    }

    // Adds one point to the shot trajectory
    public void addTrajectory() {
        if (!trajectory.isEmpty() && (Math.pow(trajectory.get(trajectory.size()-1).getX() - this.position.getX(),2) + Math.pow(trajectory.get(trajectory.size()-1).getY() - this.position.getY(),2)) < 400) return;
        trajectory.add(new Point(position.getX(), position.getY()));
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void drawShot(GraphicsContext gc) {
    }

    public void drawTrajectory(GraphicsContext gc) {
        gc.setFill(Constants.TRAJECTORY_COLOR);
        for (Point point : this.trajectory) {
            gc.fillOval(point.getX() - Constants.SHOT_TRAJECTORY_SIZE / 2.0, point.getY() - Constants.SHOT_TRAJECTORY_SIZE / 2.0, Constants.SHOT_TRAJECTORY_SIZE / 2.0, Constants.SHOT_TRAJECTORY_SIZE / 2.0);
        }
    }
}
