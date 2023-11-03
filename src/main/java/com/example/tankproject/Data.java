package com.example.tankproject;

import java.util.ArrayList;

public class Data {
    private static Data instance;
    public ArrayList<Player> alivePlayers;
    public ArrayList<Player> deadPlayers;
    public ArrayList<MysteryBox> mysteryBoxes;
    public int gameNumber;
    public Terrain terrain;
    public int playersPlayed;
    public int tanksQuantity;
    public int playableTanksQuantity;
    public int cpuTanksQuantity;

    private Data() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.mysteryBoxes = new ArrayList<>();
        this.gameNumber = 1;
        this.playersPlayed = 0;
        this.playableTanksQuantity = 1;
        this.cpuTanksQuantity = 1;
        this.tanksQuantity = this.playableTanksQuantity + this.cpuTanksQuantity;
    }

    public void reset() {
        instance = new Data();
    }

    public static synchronized Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }


}
