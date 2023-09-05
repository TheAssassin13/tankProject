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
        int height1 = (int) (seaLevel * Math.random());
        if (height1 - margen * 2 > 0) height1 -= margen*2;
        int width1 = (int) (width/4 * Math.random());
        resolutionMatrix[height1][width1] = 1;
        int height3 = (int) (seaLevel * Math.random());
        if (height3 - margen * 2 > 0) height3 -= margen*2;
        int width3 = (int) (width/4 * Math.random()) + width/2;
        resolutionMatrix[height3][width3] = 1;

        //Cañones
        int height2 = (int) (seaLevel * Math.random()) + seaLevel;
        if (height2 + margen * 2 < height) height2 += margen*2;
        int width2 = (int) (width/4 * Math.random()) + width/4;
        resolutionMatrix[height2][width2] = 1;
        int height4 = (int) (seaLevel * Math.random()) + seaLevel;
        if (height4 + margen * 2 < height) height4 += margen*2;
        int width4 = (int) (width/4 * Math.random()) + 3*width/4 - margen;
        resolutionMatrix[height4][width4] = 1;

        int i = 0;
        while (i < width) {
            int maxHeight = seaLevel;
            resolutionMatrix[seaLevel][i] = 1;
            if (seaLevel > 1 && i < width1) {
                maxHeight = height1;
                seaLevel -= Math.round(Math.random());
            } else if (seaLevel < height - 1 && i < width2) {
                maxHeight = height2;
                seaLevel += Math.round(Math.random());
            } else if (seaLevel > 1 && i < width3) {
                maxHeight = height3;
                seaLevel -= Math.round(Math.random());
            } else if (seaLevel < height - 1 && i < width4) {
                maxHeight = height4;
                seaLevel += Math.round(Math.random());
            } else {
                seaLevel += Math.round(Math.random()) * -(Math.round(Math.random()));
            }

            if (Math.abs(maxHeight - seaLevel) > margen) {
                int rand1 = (int) Math.round(Math.random());
                int rand2 = (int) Math.round(Math.random());
                if (rand1 == 0 || rand2 == 0) {
                    i += Math.round(Math.random());
                }
            } else {
                i++;
            }
        }

        resolutionMatrix[height1][width1] = 0;
        resolutionMatrix[height2][width2] = 0;
        resolutionMatrix[height3][width3] = 0;
        resolutionMatrix[height4][width4] = 0;
    }
}
