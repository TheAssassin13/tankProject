package com.example.tankproject;

import java.util.ArrayList;

public class Data {
    private static Data instance;
    public ArrayList<Player> alivePlayers;
    public ArrayList<Player> deadPlayers;
    public ArrayList<MysteryBox> mysteryBoxes;
    public int gameNumber;
    public int gamesMax;
    public Terrain terrain;
    public int tanksQuantity;
    public int playableTanksQuantity;
    public int cpuTanksQuantity;
    public int windowsHeight;
    public int windowsWidth;
    public int buttonsPanelHeight;
    public int canvasHeight;
    public int seaLevel;
    public double musicVolume;
    public double SFXVolume;
    public double gravity;
    public boolean wind;
    public int windVelocity;
    public boolean tie;
    public int themeSelected;  // 0 normal, 1 Halloween, 2 Christmas

    private Data() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.mysteryBoxes = new ArrayList<>();
        this.gameNumber = 0;
        this.gamesMax = 1;
        this.playableTanksQuantity = 2;
        this.cpuTanksQuantity = 0;
        this.tanksQuantity = this.playableTanksQuantity + this.cpuTanksQuantity;
        this.windowsHeight = Constants.RESOLUTION_HEIGHT[0];
        this.windowsWidth  = Constants.RESOLUTION_WIDTH[0];
        this.buttonsPanelHeight = 185;
        this.canvasHeight = windowsHeight - buttonsPanelHeight;
        this.seaLevel = (int) (canvasHeight - canvasHeight * 0.4);
        this.musicVolume = 1;
        this.SFXVolume = 1;
        this.gravity = 9.8;
        this.wind = false;
        this.windVelocity = 0;
        this.tie = false;
        this.themeSelected = 0;
    }

    public void updatesTanksQuantity(int playableTanks, int cpuTanks) {
        this.playableTanksQuantity = playableTanks;
        this.cpuTanksQuantity = cpuTanks;
        this.tanksQuantity = playableTanks + cpuTanks;
    }

    public void reset() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.mysteryBoxes = new ArrayList<>();
        this.gameNumber = 0;
        this.tie = false;
    }

    public void restart() {
        this.mysteryBoxes = new ArrayList<>();
    }

    public static synchronized Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }


}
