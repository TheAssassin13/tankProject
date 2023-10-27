package com.example.tankproject;

import java.util.ArrayList;

public class Data {
    private static Data instance;
    public ArrayList<Player> alivePlayers;
    public ArrayList<Player> deadPlayers;
    public int gameNumber;
    public int roundNumber;

    private Data() {
        this.alivePlayers = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.gameNumber = 1;
        this.roundNumber = 1;
    }

    public static synchronized Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }


}
