package com.example.tankproject;

import java.util.ArrayList;

public class Land {
    public int[][] resolutionMatrix;
    int height;
    int width;

    public Land(int height, int width) {
        this.height = height;
        this.width = width;
        resolutionMatrix = new int[height][width];
    }

    public void terrainGeneration(int SEA_LEVEL) {
        int margen = 50;

        //Montañas
        int y = (int) (SEA_LEVEL * Math.random());
        int x = (int) (width/4 * Math.random());
        resolutionMatrix[y][x] = 1;
        y = (int) (SEA_LEVEL * Math.random());
        x = (int) (width/4 * Math.random()) + width/2;
        resolutionMatrix[y][x] = 1;

        //Cañones
        y = (int) (SEA_LEVEL * Math.random()) + SEA_LEVEL;
        x = (int) (width/4 * Math.random()) + width/4;
        resolutionMatrix[y][x] = 1;
        y = (int) (SEA_LEVEL * Math.random()) + SEA_LEVEL;
        x = (int) (width/4 * Math.random()) + 3*width/4 - margen;
        resolutionMatrix[y][x] = 1;
    }
}
