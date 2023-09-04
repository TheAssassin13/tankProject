package com.example.tankproject;

import java.util.ArrayList;

public class Land {
    public ArrayList<ArrayList<Integer>> resolutionMatrix;
    int height;
    int width;

    public Land(int height, int width) {
        this.height = height;
        this.width = width;
        resolutionMatrix = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            resolutionMatrix.add(new ArrayList<>(width));
        }
    }

    public void terrainGeneration(int seaLevel) {
        int rand = (int) (seaLevel * Math.random());
        System.out.println(rand);
    }
}
