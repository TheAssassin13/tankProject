package com.example.tankproject;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

import static java.lang.Math.*;

public class Terrain {
    public int height;
    public int width;
    public int[][] resolutionMatrix;
    public int[] maxTerrainHeight;

    public Terrain(int height, int width) {
        this.height = height;
        this.width = width;
        this.resolutionMatrix = new int[height][width];
        this.maxTerrainHeight = new int[width];
    }

    public void terrainGeneration(int seaLevel, boolean randomized) {
        Random randomNumber = new Random();
        int margin = Constants.TERRAIN_MARGIN;
        //Reference points without random
        Point[] reference = new Point[5];
        if (!randomized) {
            reference[0] = new Point(width/5 - margin, seaLevel - margin*3);
            reference[1] = new Point(2*width/5 - margin, seaLevel + margin*2);
            reference[2] = new Point(3*width/5 - margin, reference[0].getY() + margin);
            reference[3] = new Point(4*width/5 - margin, reference[1].getY() - margin);
            reference[4] = new Point(width - margin, reference[2].getY() - margin*2);
        } else {
            // First hill
            reference[0] = new Point((int) round(margin*2 + Math.random()*width/(reference.length*2 + 1)), margin + randomNumber.nextInt(seaLevel - margin*2));
            for (int i = 1; i < reference.length; i++) {
                if (i % 2 == 0) {
                    // Hills
                    reference[i] = new Point(min(width - margin, (int) round(reference[i-1].getX() + margin*3 + Math.random()*width/(reference.length*1.5))), margin + randomNumber.nextInt(seaLevel - margin*2));
                } else {
                    // Craters
                    reference[i] = new Point(min(width - margin, (int) round(reference[i-1].getX() + margin*3 + Math.random()*width/(reference.length*1.5))), seaLevel + margin*2 + randomNumber.nextInt(height - seaLevel - margin*2));
                }
            }
            // In case the last reference point gets out of bound
            if (reference[reference.length-1].getX() > width - margin) reference[reference.length - 1].setX(width - margin*2);
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
            if (referencePoint > reference.length) maxHeight = randomNumber.nextInt(seaLevel + margin, height);

            // Random y-axis
            if (terrainHeight > height || terrainHeight > maxHeight) terrainHeight -= randomNumber.nextInt(2);
            else if (terrainHeight - margin < 0 || terrainHeight < maxHeight) terrainHeight += randomNumber.nextInt(2);
            else {
                referencePoint++;
            }

            // Random x-axis
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

    // This method destroys a circular chunk of the terrain
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

    /* This method draws the terrain generated in the resolution matrix.
    This matrix includes every "pixel" of the screen.
    Because of optimization, in this method we use an array of the highest points,
    but for the functioning code we use the resolution matrix.
    1 means terrain, 0 means air.
     */
    public void drawTerrainOptimized(GraphicsContext gc) {
        for (int i = 0; i < this.width; i++) {
            gc.setFill(Color.DARKORANGE);
            gc.fillRect(i, this.maxTerrainHeight[i], 1, 2);
            gc.setFill(Constants.TERRAIN_COLOR);
            gc.fillRect(i, this.maxTerrainHeight[i] + 2, 1, Data.getInstance().canvasHeight - this.maxTerrainHeight[i]);
        }
    }

    // This method draws the terrain without the optimization
    public void drawTerrain(GraphicsContext gc) {
        gc.setFill(Constants.TERRAIN_COLOR);
        for (int j = 0; j < this.width; j++) {
            int i = this.maxTerrainHeight[j];
            while (i < Data.getInstance().canvasHeight) {
                if (this.resolutionMatrix[i][j] == 1) {
                    if (i > 0 && this.resolutionMatrix[i-1][j] == 0) {
                        // Border is added
                        gc.setFill(Color.DARKORANGE);
                        gc.fillRect(j, i, 1, 1);
                        gc.fillRect(j, i+1, 1, 1);
                        gc.setFill(Constants.TERRAIN_COLOR);
                        i++;
                    } else {
                        gc.fillRect(j, i, 1, 1);
                    }
                }
                i++;
            }
        }
    }
}
