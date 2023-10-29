package com.example.tankproject;

import java.util.ArrayList;

public class Data {
    private static Data instance;
    public ArrayList<Player> alivePlayers;
    public ArrayList<Player> deadPlayers;
    public ArrayList<MysteryBox> mysteryBoxes;
    public int gameNumber;
    public int roundNumber;
    public int playableTanksQuantity;
    public int cpuTanksQuantity;

    private Data() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.mysteryBoxes = new ArrayList<>();
        this.gameNumber = 1;
        this.roundNumber = 1;
        this.playableTanksQuantity = Constants.TANKS_QUANTITY;
        this.cpuTanksQuantity = 0;
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
