package com.example.tankproject;


import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class Terrain {
    public int[][] resolutionMatrix;
    public int height;
    public int width;
    public int[] maxTerrainHeight;

    public Terrain(int height, int width) {
        this.height = height;
        this.width = width;
        this.resolutionMatrix = new int[height][width];
        this.maxTerrainHeight = new int[width];
    }

    public void terrainGeneration(int seaLevel, boolean random) {
        int margin = 50;
        //Reference points without random
        Point[] reference = new Point[5];
        if (!random) {
            reference[0] = new Point(width/5 - margin, seaLevel - margin*3);
            reference[1] = new Point(2*width/5 - margin, seaLevel + margin*2);
            reference[2] = new Point(3*width/5 - margin, reference[0].getY() + margin);
            reference[3] = new Point(4*width/5 - margin, reference[1].getY() - margin);
            reference[4] = new Point(width - margin, reference[2].getY() - margin*2);
        } else {
            // First hill
            reference[0] = new Point((int) round(margin*2 + Math.random()*width/(reference.length*2 + 1)), (int) round(seaLevel - margin*2 - Math.random()*(seaLevel - margin*2)));
            for (int i = 1; i < reference.length; i++) {
                if (i % 2 == 0) {
                    // Hills
                    reference[i] = new Point((int) round(reference[i-1].getX() + margin*3 + Math.random()*width/(reference.length*1.5)), seaLevel - margin*2 - new Random().nextInt(seaLevel - margin*2));
                } else {
                    // Craters
                    reference[i] = new Point((int) round(reference[i-1].getX() + margin*3 + Math.random()*width/(reference.length*1.5)), seaLevel + margin*2 + new Random().nextInt(height - seaLevel - margin*2));
                }
            }
            // In case the last reference point gets out of bound
            if (reference[reference.length-1].getX() > width) reference[reference.length - 1].setX(width - margin);
        }

        int x = 0;
        int maxHeight = seaLevel;
        int terrainHeight = seaLevel;
        int referencePoint = 0;
        while (x < width) {
            maxTerrainHeight[x] = terrainHeight; // For the drawing method

            // Fills the resolution matrix with 1's all the way to the bottom
            for (int y = terrainHeight; y < height; y++) {
                if (resolutionMatrix[y][x] == 1) break; // Optimization
                resolutionMatrix[y][x] = 1;
            }

            // Checks which reference point it needs to follow
            while (referencePoint < reference.length) {
                if (x < reference[referencePoint].getX()){
                    maxHeight = reference[referencePoint].getY();
                    break;
                }
                referencePoint++;
            }
            if (referencePoint > reference.length) maxHeight = height;

            // Random y-axis
            if (terrainHeight > height || terrainHeight > maxHeight) terrainHeight -= new Random().nextInt(2);
            else if (terrainHeight - margin < 0 || terrainHeight < maxHeight) terrainHeight += new Random().nextInt(2);
            else referencePoint++;

            // random x-axis
            x += new Random().nextInt(2);
        }
    }

    public void calculateMaxHeight() {
        for (int i = 0; i < this.width; i++) {
            for (int j = maxTerrainHeight[i]; j < this.height; j++) {
                if (resolutionMatrix[j][i] == 1) {
                    maxTerrainHeight[i] = j;
                    break;
                }
            }
        }
    }

    public void destroyTerrain(Point center, int radius) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (Math.pow(center.getX() - j,2) + Math.pow(center.getY() - i,2) <= radius) {
                    resolutionMatrix[i][j] = 0;
                }
            }
        }
        calculateMaxHeight();
    }

    public boolean terrainFalling() {
        boolean change = false;

        // Iterates terrain from bottom to top
        for (int j = 0; j < this.width; j++) {
            for (int i = this.height - 1; i > 1; i--) {
                // Corner case
                if (this.resolutionMatrix[i][j] == 1 && this.resolutionMatrix[i-1][j] == 0 && this.resolutionMatrix[i-2][j] == 1) {
                    this.resolutionMatrix[i-1][j] = 1;
                    this.resolutionMatrix[i-2][j] = 0;
                    change = true;
                }
                // If there's air and terrain above it, drag terrain down
                if (this.resolutionMatrix[i][j] == 0 && this.resolutionMatrix[i-1][j] == 1) {
                    this.resolutionMatrix[i][j] = 1;
                    this.resolutionMatrix[i-1][j] = 0;
                    this.maxTerrainHeight[j] = i;
                    change = true;
                }

            }
        }
     return change;
    }
}
