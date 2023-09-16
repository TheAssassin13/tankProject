package com.example.tankproject;


public class Terrain {
    public int[][] resolutionMatrix;
    public int height;
    public int width;

    public Terrain(int height, int width) {
        this.height = height;
        this.width = width;
        this.resolutionMatrix = new int[height][width];
    }

    public void terrainGeneration(int seaLevel, boolean random) {
        int margin = 50;
        //Reference points without random
        int height1 = seaLevel - margin*3;
        int height2 = seaLevel + margin*2;
        int height3 = height1 + margin;
        int height4 = height2 - margin;
        int width1 = width/4 - margin;
        int width2 = width/2 - margin;
        int width3 = 3*width/4 - margin;
        int width4 = width - margin;

        if (random) {
            //Reference points for mountains
            height1 = (int) (seaLevel * Math.random());
            if (height1 - margin * 2 > 0) height1 -= margin * 2;
            width1 = (int) (width / 4 * Math.random()) + margin;
            height3 = (int) (seaLevel * Math.random());
            if (height3 - margin * 2 > 0) height3 -= margin * 2;
            width3 = (int) (width / 4 * Math.random()) + width / 2;

            //Reference points for craters
            height2 = (int) (seaLevel * Math.random()) + seaLevel;
            if (height2 + margin * 2 < height) height2 += margin * 2;
            width2 = (int) (width / 4 * Math.random()) + width / 4;
            height4 = (int) (seaLevel * Math.random()) + seaLevel;
            if (height4 + margin * 2 < height) height4 += margin * 2;
            width4 = (int) (width / 4 * Math.random()) + 3 * width / 4 - margin;
        }

        int i = 0;
        while (i < width) {
            int maxHeight = seaLevel;
            for (int j = seaLevel; j < height; j++) {
                resolutionMatrix[j][i] = 1;
                if (j + 1 < height && resolutionMatrix[j+1][i] == 1) {
                    break;
                }
            }

            //Random probability y-axis increments or decrements
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

            } else if (seaLevel < height - 1 && seaLevel > 1){
                seaLevel += Math.round(Math.random() * -Math.round(Math.random()));
            }

            //Random probability x-axis increments
            if (Math.abs(maxHeight - seaLevel) > margin) {
                int rand1 = (int) Math.round(Math.random());
                int rand2 = (int) Math.round(Math.random());
                if (rand1 == 1 || rand2 == 1) {
                    i += (int) Math.round(Math.random());
                }
            } else {
                i++;
            }
        }
    }
}
