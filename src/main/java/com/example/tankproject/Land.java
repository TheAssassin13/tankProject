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

    public void terrainGeneration(int seaLevel) {
        int margen = 50;

        //Montañas
        int y = (int) (seaLevel * Math.random());
        int x = (int) (width/4 * Math.random());
        resolutionMatrix[y][x] = 1;
        y = (int) (seaLevel * Math.random());
        x = (int) (width/4 * Math.random()) + width/2;
        resolutionMatrix[y][x] = 1;

        //Cañones
        y = (int) (seaLevel * Math.random()) + seaLevel;
        x = (int) (width/4 * Math.random()) + width/4;
        resolutionMatrix[y][x] = 1;
        y = (int) (seaLevel * Math.random()) + seaLevel;
        x = (int) (width/4 * Math.random()) + 3*width/4 - margen;
        resolutionMatrix[y][x] = 1;
    }
}
